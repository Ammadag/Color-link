package com.example.colorlink.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.colorlink.domain.model.LevelProgress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class ProgressRepositoryImplTest {

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private lateinit var testDataStore: DataStore<Preferences>
    private lateinit var repository: ProgressRepositoryImpl
    private val json = Json { ignoreUnknownKeys = true }
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @Before
    fun setup() {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(tmpFolder.newFolder(), "test.preferences_pb") }
        )
        repository = ProgressRepositoryImpl(testDataStore, json)
    }

    @Test
    fun `save and load level progress`() = runTest(testDispatcher) {
        val progress = LevelProgress(
            levelId = "level_1",
            isCompleted = true,
            bestMoves = 5,
            stars = 3,
            completedAtMillis = 123456789L
        )

        repository.saveLevelProgress(progress)

        val result = repository.getLevelProgress("level_1")
        assertEquals(progress, result)
    }

    @Test
    fun `get all progress returns list of saved items`() = runTest(testDispatcher) {
        val p1 = LevelProgress("l1", true, 5, 3, 100L)
        val p2 = LevelProgress("l2", true, 8, 2, 200L)

        repository.saveLevelProgress(p1)
        repository.saveLevelProgress(p2)

        val all = repository.getAllProgress()
        assertEquals(2, all.size)
        assertTrue(all.any { it.levelId == "l1" })
        assertTrue(all.any { it.levelId == "l2" })
    }

    @Test
    fun `return null for missing progress`() = runTest(testDispatcher) {
        val result = repository.getLevelProgress("non_existent")
        assertEquals(null, result)
    }
}
