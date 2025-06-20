package ponder.steps

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable
import pondui.ui.nav.AppRoute
import pondui.ui.nav.IdRoute
import pondui.ui.nav.matchLongIdRoute
import pondui.ui.nav.matchStringIdRoute
import pondui.ui.nav.matchStringOrNullIdRoute

@Serializable
object StartRoute : AppRoute("Start")

@Serializable
object HelloRoute : AppRoute("Hello")

@Serializable
object ExampleListRoute : AppRoute("Examples")

@Serializable
object PathsRoute : AppRoute("Paths")

@Serializable
data class StepProfileRoute(val stepId: String) : IdRoute<String>(TITLE, stepId) {
    companion object {
        const val TITLE = "Steps"
        fun matchRoute(path: String) = matchStringIdRoute(path, TITLE) { StepProfileRoute(it) }
    }
}

@Serializable
object GeminiRoute : AppRoute("Gemini")

@Serializable
object JourneyRoute : AppRoute("Journey")

@Serializable
object SettingsRoute : AppRoute("Settings")

@Serializable
data class ExampleProfileRoute(val exampleId: Long) : IdRoute<Long>(TITLE, exampleId) {
    companion object {
        const val TITLE = "Example"
        fun matchRoute(path: String) = matchLongIdRoute(path, TITLE) { ExampleProfileRoute(it) }
    }
}
