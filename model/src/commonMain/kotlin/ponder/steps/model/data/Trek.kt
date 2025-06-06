package ponder.steps.model.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Trek(
    val id: String,
    val userId: String,
    val intentId: String,
    val rootId: String,
    val stepId: String,
    val stepIndex: Int,
    val stepCount: Int,
    val pathIds: List<String>,
    val breadCrumbs: List<String>,
    val availableAt: Instant,
    val startedAt: Instant?,
    val progressAt: Instant?,
    val finishedAt: Instant?,
    val expectedAt: Instant?,
)