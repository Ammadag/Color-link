package com.example.colorlink.feature.gameplay

import app.cash.turbine.test
import com.example.colorlink.domain.model.*
import com.example.colorlink.domain.repository.LevelRepository
import com.example.colorlink.domain.repository.ProgressRepository
import com.example.colorlink.domain.usecase.CalculateStarsUseCase
import com.example.colorlink.domain.usecase.CheckWinConditionUseCase
import com.example.colorlink.domain.usecase.StartPathUseCase
import com.example.colorlink.domain.usecase.ValidateMoveUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameplayViewModelTest {

    private val levelRepository = mockk<LevelRepository>()
    private val progressRepository = mockk<ProgressRepository>()
    private val startPathUseCase = mockk<StartPathUseCase>()
    private val validateMoveUseCase = mockk<ValidateMoveUseCase>()
    private val checkWinConditionUseCase = mockk<CheckWinConditionUseCase>()
    private val calculateStarsUseCase = mockk<CalculateStarsUseCase>()

    private lateinit var viewModel: GameplayViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    private val mockLevel = Level(
        id = "l1",
        number = 1,
        rows = 5,
        columns = 5,
        dots = emptyList()
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = GameplayViewModel(
            levelRepository,
            progressRepository,
            startPathUseCase,
            validateMoveUseCase,
            checkWinConditionUseCase,
            calculateStarsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `LoadLevel intent updates state with level`() = runTest {
        coEvery { levelRepository.getLevel("l1") } returns mockLevel

        viewModel.handleIntent(GameplayIntent.LoadLevel("l1"))

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(mockLevel, state.level)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `StartPath intent updates activePath`() = runTest {
        // Setup state with level
        coEvery { levelRepository.getLevel("l1") } returns mockLevel
        viewModel.handleIntent(GameplayIntent.LoadLevel("l1"))
        
        val pos = BoardPosition(0, 0)
        val activePath = ColorPath(DotColor.Red, listOf(pos))
        every { startPathUseCase(mockLevel, any(), pos) } returns (emptyList<ColorPath>() to activePath)

        viewModel.handleIntent(GameplayIntent.StartPath(pos))

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(activePath, state.activePath)
        }
    }

    @Test
    fun `DragTo valid move updates activePath`() = runTest {
        // Setup state
        coEvery { levelRepository.getLevel("l1") } returns mockLevel
        viewModel.handleIntent(GameplayIntent.LoadLevel("l1"))
        
        val pos1 = BoardPosition(0, 0)
        val activePath1 = ColorPath(DotColor.Red, listOf(pos1))
        every { startPathUseCase(mockLevel, any(), pos1) } returns (emptyList<ColorPath>() to activePath1)
        viewModel.handleIntent(GameplayIntent.StartPath(pos1))

        val pos2 = BoardPosition(0, 1)
        val updatedPath = ColorPath(DotColor.Red, listOf(pos1, pos2))
        every { validateMoveUseCase(mockLevel, any(), activePath1, pos2) } returns MoveResult.Valid(updatedPath)

        viewModel.handleIntent(GameplayIntent.DragTo(pos2))

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(updatedPath, state.activePath)
        }
    }

    @Test
    fun `Check win condition triggers level completion`() = runTest {
        coEvery { levelRepository.getLevel("l1") } returns mockLevel
        viewModel.handleIntent(GameplayIntent.LoadLevel("l1"))
        
        val pos1 = BoardPosition(0, 0)
        val activePath1 = ColorPath(DotColor.Red, listOf(pos1))
        every { startPathUseCase(mockLevel, any(), pos1) } returns (emptyList<ColorPath>() to activePath1)
        viewModel.handleIntent(GameplayIntent.StartPath(pos1))

        val pos2 = BoardPosition(0, 1)
        val completedPath = ColorPath(DotColor.Red, listOf(pos1, pos2), isCompleted = true)
        every { validateMoveUseCase(mockLevel, any(), activePath1, pos2) } returns MoveResult.Completed(completedPath)
        every { checkWinConditionUseCase(mockLevel, any()) } returns true
        every { calculateStarsUseCase(mockLevel, 1) } returns 3
        coEvery { progressRepository.saveLevelProgress(any()) } returns Unit

        viewModel.handleIntent(GameplayIntent.DragTo(pos2))

        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.isCompleted)
            assertEquals(1, state.moveCount)
        }

        viewModel.events.test {
            assertEquals(GameplayEvent.ShowLevelComplete, awaitItem())
            assertEquals(GameplayEvent.PlayWinAnimation, awaitItem())
        }
    }
}
