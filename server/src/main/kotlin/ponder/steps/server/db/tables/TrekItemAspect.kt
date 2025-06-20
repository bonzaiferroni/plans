package ponder.steps.server.db.tables

import kabinet.utils.toInstantFromUtc
import klutch.db.Aspect
import klutch.utils.toStringId
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import ponder.steps.model.data.TrekItem

internal object TrekItemAspect: Aspect<TrekItemAspect, TrekItem>(
    TrekTable.join(StepTable, JoinType.LEFT, TrekTable.nextId, StepTable.id)
        .join(IntentTable, JoinType.LEFT, TrekTable.intentId, IntentTable.id),
    ResultRow::toTrekItem
) {
    val trekId = add(TrekTable.id)
    val stepId = add(StepTable.id)
    val stepLabel = add(StepTable.label)
    val stepPathSize = add(StepTable.pathSize)
    val progress = add(TrekTable.progress)
    val stepImgUrl = add(StepTable.imgUrl)
    val stepThumbUrl = add(StepTable.thumbUrl)
    val stepDescription = add(StepTable.description)
    val stepAudioLabelUrl = add(StepTable.audioLabelUrl)
    val stepAudioFullUrl = add(StepTable.audioFullUrl)
    val intentLabel = add(IntentTable.label)
    val intentPriority = add(IntentTable.priority)
    val expectedMinutes = add(IntentTable.expectedMins)
    val availableAt = add(TrekTable.availableAt)
    val startedAt = add(TrekTable.startedAt)
    val finishedAt = add(TrekTable.finishedAt)
}

internal fun ResultRow.toTrekItem() = TrekItem(
    trekId = this[TrekItemAspect.trekId].value.toStringId(),
    stepId = this[TrekItemAspect.stepId].value.toStringId(),
    stepLabel = this[TrekItemAspect.stepLabel],
    pathSize = this[TrekItemAspect.stepPathSize],
    progress = this[TrekItemAspect.progress],
    imgUrl = this[TrekItemAspect.stepImgUrl],
    thumbUrl = this[TrekItemAspect.stepThumbUrl],
    description = this[TrekItemAspect.stepDescription],
    audioLabelUrl = this[TrekItemAspect.stepAudioLabelUrl],
    audioFullUrl = this[TrekItemAspect.stepAudioFullUrl],
    intentLabel = this[TrekItemAspect.intentLabel],
    priority = this[TrekItemAspect.intentPriority],
    expectedMinutes = this[TrekItemAspect.expectedMinutes],
    availableAt = this[TrekItemAspect.availableAt].toInstantFromUtc(),
    startedAt = this[TrekItemAspect.startedAt]?.toInstantFromUtc(),
    finishedAt = this[TrekItemAspect.finishedAt]?.toInstantFromUtc(),
)

// @Serializable
//data class TrekItem(
//    val trekId: Long,
//    val expectedMinutes: Int,
//    val stepLabel: String,
//    val stepIndex: Int,
//    val stepCount: Int,
//    val intentLabel: String,
//    val availableAt: Instant,
//    val startedAt: Instant,
//)