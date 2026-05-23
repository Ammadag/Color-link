package com.example.colorlink.feature.gameplay

import com.example.colorlink.core.mvi.StateViewModel
import com.example.colorlink.domain.model.BoardPosition
import com.example.colorlink.domain.model.ColorPath
import com.example.colorlink.domain.model.LevelProgress
import com.example.colorlink.domain.model.MoveResult
import com.example.colorlink.domain.repository.LevelRepository
import com.example.colorlink.domain.repository.ProgressRepository
import com.example.colorlink.domain.usecase.*
import java.util.Stack

class GameplayViewModel(
    private val levelRepository: LevelRepository,
    private val progressRepository: ProgressRepository,
    private val startPathUseCase: StartPathUseCase,
    private val validateMoveUseCase: ValidateMoveUseCase,
    private val checkWinConditionUseCase: CheckWinConditionUseCase,
    private val calculateStarsUseCase: CalculateStarsUseCase,
    private val interpolateCellsUseCase: InterpolateCellsUseCase
) : StateViewModel<GameplayState, GameplayIntent, GameplayEvent>(GameplayState()) {

    private val history = Stack<List<ColorPath>>()

    override fun handleIntent(intent: GameplayIntent) {
        when (intent) {
            is GameplayIntent.LoadLevel -> loadLevel(intent.levelId)
            is GameplayIntent.StartPath -> startPath(intent.position)
            is GameplayIntent.DragTo -> dragTo(intent.position)
            GameplayIntent.EndPath -> endPath()
            GameplayIntent.Undo -> undo()
            GameplayIntent.Restart -> restart()
            GameplayIntent.UseHint -> useHint()
            GameplayIntent.PauseClicked -> updateState { copy(isPaused = true) }
            GameplayIntent.ResumeClicked -> updateState { copy(isPaused = false) }
            GameplayIntent.BackClicked -> emitEvent(GameplayEvent.NavigateBack)
            GameplayIntent.LevelCompleteContinueClicked -> emitEvent(GameplayEvent.NavigateToNextLevel)
        }
    }

    private fun loadLevel(levelId: String) {
        launchSafely {
            updateState { copy(isLoading = true) }
            val level = levelRepository.getLevel(levelId)
            updateState { 
                copy(
                    isLoading = false, 
                    level = level,
                    paths = emptyList(),
                    activePath = null,
                    moveCount = 0,
                    isCompleted = false
                ) 
            }
            history.clear()
        }
    }

    private fun startPath(position: BoardPosition) {
        val level = state.value.level ?: return
        if (state.value.isCompleted || state.value.isPaused) return

        val (newPaths, activePath) = startPathUseCase(level, state.value.paths, position)
        
        if (activePath != null) {
            history.push(state.value.paths)
            updateState { 
                copy(
                    paths = newPaths,
                    activePath = activePath,
                    invalidMoveMessage = null
                ) 
            }
        }
    }

    private fun dragTo(position: BoardPosition) {
        val level = state.value.level ?: return
        val activePath = state.value.activePath ?: return
        if (state.value.isCompleted || state.value.isPaused) return

        val lastCell = activePath.cells.last()
        val interpolatedCells = interpolateCellsUseCase(lastCell, position)

        var currentActivePath = activePath
        var updatedPaths = state.value.paths

        for (cell in interpolatedCells) {
            val result = validateMoveUseCase(level, updatedPaths, currentActivePath, cell)
            when (result) {
                is MoveResult.Valid -> {
                    currentActivePath = result.updatedPath
                }
                is MoveResult.Completed -> {
                    updatedPaths = updatedPaths + result.completedPath
                    updateState { 
                        copy(
                            paths = updatedPaths,
                            activePath = null,
                            moveCount = moveCount + 1
                        ) 
                    }
                    checkWin(updatedPaths)
                    return 
                }
                is MoveResult.Backtracked -> {
                    currentActivePath = result.updatedPath
                }
                is MoveResult.Invalid -> break 
            }
        }

        updateState { copy(activePath = currentActivePath) }
    }

    private fun endPath() {
        if (state.value.activePath != null) {
            val updatedPaths = state.value.paths + state.value.activePath!!
            updateState { 
                copy(
                    paths = updatedPaths,
                    activePath = null,
                    moveCount = moveCount + 1
                ) 
            }
            checkWin(updatedPaths)
        }
    }

    private fun checkWin(paths: List<ColorPath>) {
        val level = state.value.level ?: return
        val isWon = checkWinConditionUseCase(level, paths)
        if (isWon) {
            updateState { copy(isCompleted = true) }
            saveProgress()
            emitEvent(GameplayEvent.ShowLevelComplete)
            emitEvent(GameplayEvent.PlayWinAnimation)
        }
    }

    private fun saveProgress() {
        val level = state.value.level ?: return
        launchSafely {
            val stars = calculateStarsUseCase(level, state.value.moveCount)
            progressRepository.saveLevelProgress(
                LevelProgress(
                    levelId = level.id,
                    isCompleted = true,
                    bestMoves = state.value.moveCount,
                    stars = stars,
                    completedAtMillis = System.currentTimeMillis()
                )
            )
        }
    }

    private fun undo() {
        if (history.isNotEmpty() && !state.value.isCompleted) {
            val previousPaths = history.pop()
            updateState { 
                copy(
                    paths = previousPaths,
                    activePath = null,
                    moveCount = maxOf(0, moveCount - 1)
                ) 
            }
        }
    }

    private fun restart() {
        updateState { 
            copy(
                paths = emptyList(),
                activePath = null,
                moveCount = 0,
                isCompleted = false
            ) 
        }
        history.clear()
    }

    private fun useHint() {
        if (state.value.hintCount > 0) {
            updateState { copy(hintCount = hintCount - 1) }
        }
    }
}
