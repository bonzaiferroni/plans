package ponder.steps.model.data

import kotlinx.serialization.Serializable

@Serializable
data class Example(
    val id: Long,
    val userId: String,
    val label: String,
)