package ponder.steps.io

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ponder.steps.model.Api
import ponder.steps.model.data.Step
import ponder.steps.model.data.NewStep
import ponder.steps.model.data.PathStep
import ponder.steps.model.data.SyncData
import pondui.io.ApiClient
import pondui.io.globalApiClient
import kotlin.time.Duration.Companion.days

class StepServerRepository(private val client: ApiClient = globalApiClient): StepRepository {
    override suspend fun createStep(newStep: NewStep) = client.post(Api.Steps.Create, newStep)

    override suspend fun updateStep(step: Step) = client.update(Api.Steps.Update, step)

    override suspend fun addStepToPath(pathId: String, stepId: String, position: Int?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun removeStepFromPath(pathId: String, stepId: String, position: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun moveStepPosition(pathId: String, stepId: String, delta: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteStep(stepId: String) = client.delete(Api.Steps.Delete, stepId)

    override suspend fun readStep(stepId: String) = client.get(Api.Steps, stepId)

    override suspend fun readPathSteps(pathId: String) = client.get(Api.Steps.Children, pathId)

    override suspend fun readRootSteps() = client.get(Api.Steps.Root)

    override suspend fun searchSteps(query: String) = client.get(Api.Steps.Search, Api.Steps.Search.query.write(query))

    override suspend fun readSync(lastSyncAt: Instant) = client.post(Api.Steps.ReadSync, lastSyncAt)

    override suspend fun writeSync(data: SyncData) = client.post(Api.Steps.WriteSync, data)
}
