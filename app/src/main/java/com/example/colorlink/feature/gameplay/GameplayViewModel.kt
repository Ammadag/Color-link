package com.example.colorlink.feature.gameplay

import com.example.colorlink.core.mvi.StateViewModel
import com.example.colorlink.domain.model.BoardPosition
import com.example.colorlink.domain.model.ColorPath
import com.example.colorlink.domain.model.LevelProgress
import com.example.colorlink.domain.model.MoveResult
import com.example.colorlink.domain.repository.LevelRepository
import com.example.colorlink.domain.repository.ProgressRepository
import com.example.colorlink.domain.usecase.CalculateStarsUseCase
import com.example.colorlink.domain.usecase.CheckWinConditionUseCase
import com.example.colorlink.domain.usecase.GetHintUseCase
import com.example.colorlink.domain.usecase.InterpolateCellsUseCase
import com.example.colorlink.domain.usecase.StartPathUseCase
import com.example.colorlink.domain.usecase.UseHintUseCase
import com.example.colorlink.domain.usecase.ValidateMoveUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.Stack

class GameplayViewModel(
    private val levelRepository: LevelRepository,
    private val progressRepository: ProgressRepository,
    private val startPathUseCase: StartPathUseCase,
    private val validateMoveUseCase: ValidateMoveUseCase,
    private val checkWinConditionUseCase: CheckWinConditionUseCase,
    private val calculateStarsUseCase: CalculateStarsUseCase,
    private val interpolateCellsUseCase: InterpolateCellsUseCase,
    private val useHintUseCase: UseHintUseCase,
    private val getHintUseCase: GetHintUseCase
) : StateViewModel<GameplayState, GameplayIntent, GameplayEvent>(GameplayState()) {

    private val history = Stack<List<ColorPath>>()
    private var timerJob: Job? = null

    override fun handleIntent(intent: GameplayIntent) {
        when (intent) {
            is GameplayIntent.LoadLevel -> loadLevel(intent.levelId)
            is GameplayIntent.StartPath -> startPath(intent.position)
            is GameplayIntent.DragTo -> dragTo(intent.position)
            GameplayIntent.EndPath -> endPath()
            GameplayIntent.Undo -> undo()
            GameplayIntent.Restart -> restart()
            GameplayIntent.UseHint -> useHint()
            GameplayIntent.PauseClicked -> pauseGame()
            GameplayIntent.ResumeClicked -> resumeGame()
            GameplayIntent.BackClicked -> emitEvent(GameplayEvent.NavigateBack)
            GameplayIntent.LevelCompleteNextLevelClicked -> navigateToNextLevel()
            GameplayIntent.LevelCompleteReplayClicked -> restart()
            GameplayIntent.LevelCompleteLevelsClicked -> emitEvent(GameplayEvent.NavigateToLevelSelection)
        }
    }

    private fun loadLevel(levelId: String) {
        launchSafely {
            updateState { copy(isLoading = true, isCompleted = false, errorMessage = null) }
            try {
                val level = levelRepository.getLevel(levelId)
                val progress = progressRepository.getLevelProgress(levelId)

                updateState {
                    copy(
                        isLoading = false,
                        level = level,
                        paths = emptyList(),
                        activePath = null,
                        hintPath = null,
                        moveCount = 0,
                        timeSeconds = 0,
                        bestMoves = progress?.bestMoves,
                        isCompleted = false
                    )
                }
                history.clear()
                stopTimer()
                startTimer()
            } catch (e: Exception) {
                updateState { copy(isLoading = false, errorMessage = e.message) }
                emitEvent(GameplayEvent.ShowError(e.message ?: "Failed to load level"))
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = launchSafely {
            while (isActive) {
                delay(1000)
                if (!state.value.isPaused && !state.value.isCompleted) {
                    updateState { copy(timeSeconds = timeSeconds + 1) }
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun pauseGame() {
        updateState { copy(isPaused = true) }
    }

    private fun resumeGame() {
        updateState { copy(isPaused = false) }
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
                    hintPath = if (hintPath?.color == activePath.color) null else hintPath,
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
                            hintPath = if (hintPath?.color == result.completedPath.color) null else hintPath,
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
            val active = state.value.activePath!!
            val updatedPaths = state.value.paths + active
            updateState {
                copy(
                    paths = updatedPaths,
                    activePath = null,
                    hintPath = if (hintPath?.color == active.color) null else hintPath,
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
            val stars = calculateStarsUseCase(level, state.value.moveCount)
            updateState {
                copy(
                    isCompleted = true,
                    stars = stars
                )
            }
            stopTimer()
            saveProgress(stars)
            emitEvent(GameplayEvent.ShowLevelComplete)
            emitEvent(GameplayEvent.PlayWinAnimation)
        }
    }

    private fun saveProgress(stars: Int) {
        val level = state.value.level ?: return
        launchSafely {
            val currentBest = state.value.bestMoves ?: Int.MAX_VALUE
            val newBest = minOf(currentBest, state.value.moveCount)

            progressRepository.saveLevelProgress(
                LevelProgress(
                    levelId = level.id,
                    isCompleted = true,
                    bestMoves = newBest,
                    stars = stars,
                    completedAtMillis = System.currentTimeMillis()
                )
            )
            updateState { copy(bestMoves = newBest) }
        }
    }

    private fun navigateToNextLevel() {
        val currentLevelNumber = state.value.level?.number ?: return
        val nextLevelId = "level_${currentLevelNumber + 1}"
        emitEvent(GameplayEvent.NavigateToGameplay(nextLevelId))
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
                hintPath = null,
                moveCount = 0,
                timeSeconds = 0,
                isCompleted = false
            )
        }
        history.clear()
        stopTimer()
        startTimer()
    }

    private fun useHint() {
        val level = state.value.level ?: return
        if (state.value.isCompleted) return

        launchSafely {
            val success = useHintUseCase()
            if (success) {
                val suggestion = getHintUseCase(level, state.value.paths)
                updateState {
                    copy(
                        hintPath = suggestion,
                        hintCount = hintCount - 1
                    )
                }
            }
        }
    }
}
