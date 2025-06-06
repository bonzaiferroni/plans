package ponder.steps.io

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ponder.steps.model.data.SyncData
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class DataSync(
    private val leftRepo: StepRepository,
    private val rightRepo: StepRepository,
    private var lastSyncAt: Instant,
    private val onSync: (Instant) -> Unit,
    private val interval: Duration = 10.seconds
) {
    fun init(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            while (true) {
                val syncAt = lastSyncAt
                lastSyncAt = Clock.System.now()

                val leftResponse = leftRepo.readSync(syncAt)
                val rightResponse = rightRepo.readSync(syncAt)

                val leftCount = syncSteps(leftResponse, rightResponse, rightRepo)
                val rightCount = syncSteps(rightResponse, leftResponse, leftRepo)

                if (leftCount > 0) println("sync'd $leftCount steps left to right")
                if (rightCount > 0) println("sync'd $rightCount steps right to left")

                onSync(syncAt)
                delay(interval)
            }
        }
    }

    private suspend fun syncSteps(origin: SyncData, target: SyncData, targetRepo: StepRepository): Int {
        val steps = origin.steps.filter { origin ->
            target.steps.all { target -> origin.id != target.id || origin.updatedAt > target.updatedAt }
        }
        val pathSteps = origin.pathSteps.filter { pathStep ->
            steps.any { it.id == pathStep.id }
        }
        targetRepo.writeSync(SyncData(steps, pathSteps))
        return steps.size
    }
}