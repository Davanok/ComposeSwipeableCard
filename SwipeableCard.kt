package //TODO

import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> AnchoredDraggableState<T>.OnStateChange(onStateChange: (T) -> Unit) {
    var previousValue by remember { mutableStateOf(currentValue) }
    if (currentValue != previousValue) {
        onStateChange(currentValue)
        previousValue = currentValue
    }
}

private enum class Anchors {
    START,
    CENTER,
    END
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableCard(
    modifier: Modifier = Modifier,
    state: SwipeableCardState = rememberSwipeableCardState(),
    key: () -> Int,
    endContent: @Composable (RowScope.() -> Unit)? = null,
    startContent: @Composable (RowScope.() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val anchors = remember { mutableMapOf(Anchors.CENTER to 0f) }

    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val anchorState = remember {
        AnchoredDraggableState(
            initialValue = Anchors.CENTER,
            positionalThreshold = { it * .5f },
            animationSpec = spring(),
            velocityThreshold = { with(density) { 100.dp.toPx() } },
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    anchors.forEach { anchor ->
                        anchor.key at anchor.value
                    }
                }
            )
        }
    }
    anchorState.OnStateChange {
        if (it != Anchors.CENTER) state.currentAnchored = key()
    }
    if (state.currentAnchored != key() && anchorState.currentValue != Anchors.CENTER)
        LaunchedEffect(key1 = Unit){
            scope.launch {
                anchorState.animateTo(Anchors.CENTER)
            }
        }
    Box (
        modifier = modifier
    ) {
        endContent?.let { eContent ->
            Row (
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .onSizeChanged {
                        scope.launch {
                            anchorState.updateAnchors(
                                DraggableAnchors {
                                    anchors[Anchors.END] = -it.width.toFloat()
                                    anchors.forEach { anchor ->
                                        anchor.key at anchor.value
                                    }
                                }
                            )
                        }
                    },
            ) {
                eContent()
            }
        }
        startContent?.let { sContent ->
            Row (
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .onSizeChanged {
                        scope.launch {
                            anchorState.updateAnchors(
                                DraggableAnchors {
                                    anchors[Anchors.START] = it.width.toFloat()
                                    anchors.forEach { anchor ->
                                        anchor.key at anchor.value
                                    }
                                }
                            )
                        }
                    },
            ) {
                sContent()
            }
        }
        Box (
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = anchorState
                            .requireOffset()
                            .roundToInt(),
                        y = 0
                    )
                }
                .anchoredDraggable(anchorState, Orientation.Horizontal),
        ) {
            content()
        }
    }
}

@Composable
fun rememberSwipeableCardState() = remember { SwipeableCardState() }

class SwipeableCardState {
    var currentAnchored by mutableIntStateOf(-1)
}
