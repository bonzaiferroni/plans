package ponder.steps.io

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import ponder.steps.appDb
import ponder.steps.db.SyncDao
import ponder.steps.db.SyncRecord
import ponder.steps.db.toEntity
import ponder.steps.db.toStep
import ponder.steps.model.data.SyncData

class LocalSyncRepository(
    private val dao: SyncDao = appDb.getSyncDao(),
): SyncRepository {

    suspend fun readSyncStartAt() = dao.readLastSync()?.endSyncAt ?: defaultStartSyncAt

    suspend fun logSync(startSyncAt: Instant, endSyncAt: Instant) {
        dao.deleteAllSyncRecords()
        dao.insert(SyncRecord(startSyncAt = startSyncAt, endSyncAt = endSyncAt))
    }

    override suspend fun readSync(syncStartAt: Instant, syncEndAt: Instant): SyncData {
        val deletions = dao.readAllDeletionsBefore(syncEndAt).toSet()
        val steps = dao.readStepsUpdated(syncStartAt, syncEndAt).map { it.toStep() }
        val pathSteps = dao.readPathStepsUpdated(syncStartAt, syncEndAt)
        val questions = dao.readQuestionsUpdated(syncStartAt, syncEndAt)
        // val questions = questionDao.readQuestionsByStepIds(stepIds)
        return SyncData(
            startSyncAt = syncStartAt,
            endSyncAt = syncEndAt,
            deletions = deletions,
            steps = steps,
            pathSteps = pathSteps,
            questions = questions
        )
    }

    override suspend fun writeSync(data: SyncData): Boolean {
        val deletions = data.deletions.toList()

        // handle updates
        dao.upsert(*data.steps.map { it.toEntity() }.toTypedArray())
        dao.upsert(*data.pathSteps.map { it.toEntity() }.toTypedArray())
        dao.upsert(*data.questions.map { it.toEntity() }.toTypedArray())

        // handle deletions
        dao.deleteStepsInList(deletions)
        dao.deletePathStepsInList(deletions)
        dao.deleteDeletionsInList(deletions)
        dao.deleteQuestionsInList(deletions)
        dao.deleteDeletionsBefore(data.endSyncAt)
        return true
    }
}

private val defaultStartSyncAt = LocalDate.parse("2017-03-20").atStartOfDayIn(TimeZone.UTC)