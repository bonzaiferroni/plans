package ponder.steps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Plus
import kotlinx.collections.immutable.toImmutableList
import pondui.LocalWavePlayer
import pondui.ui.behavior.MagicItem
import pondui.ui.controls.BottomBarSpacer
import pondui.ui.controls.Button
import pondui.ui.controls.LazyColumn
import pondui.ui.controls.MenuWheel

@Composable
fun TodoOldView() {
    val viewModel = viewModel { TodoOldModel() }
    val state by viewModel.state.collectAsState()
    val player = LocalWavePlayer.current

    DisposableEffect(Unit) {
        onDispose(viewModel::onDispose)
    }

    LaunchedEffect(Unit) {
        viewModel.onLoad()
    }

    AddStepCloud(
        title = "Add step",
        isVisible = state.isAddingItem,
        createIntent = true,
        pathId = null,
        dismiss = viewModel::toggleAddItem
    )

    LazyColumn(1, horizontalAlignment = Alignment.CenterHorizontally) {
        item(key = "span") {
            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                MenuWheel(state.span, TrekSpan.entries.toImmutableList()) { viewModel::setSpan }
                Button(TablerIcons.Plus, onClick = viewModel::toggleAddItem)
            }
        }
        items(state.items, key = { it.trekId }) { item ->
            val questionSet = state.questionSets.firstOrNull { it.trekId == item.trekId }
            val question = questionSet?.questions?.firstOrNull()

            if (item.finishedAt == null) {
                LaunchedEffect(item) {
                    val audioUrl = item.audioLabelUrl
                    if (audioUrl != null) {
                        player.play(toServerUrl(audioUrl))
                    }
                }
            }

            MagicItem(
                item = question,
                offsetX = 50.dp,
                itemContent = { question ->
                    QuestionRow(item.stepLabel, question) { viewModel.answerQuestion(item.trekId, question, it) }
                },
                isVisibleInit = true,
                modifier = Modifier.height(72.dp)
                    .animateItem()
            ) {
                TrekItemRow(item, viewModel::completeStep)
            }
        }
        item(key = "bottom spacer") {
            BottomBarSpacer()
        }
    }
}

fun toServerUrl(url: String) = "http://localhost:8080/$url"
