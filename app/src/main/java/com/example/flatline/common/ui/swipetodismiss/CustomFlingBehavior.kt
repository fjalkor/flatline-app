package com.example.flatline.common.ui.swipetodismiss

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.TargetedFlingBehavior

class CustomFlingBehavior(
    val draggableState: AnchoredDraggableState<ListItemDragValue>,
    val snapBack: () -> Unit,
    val onDismiss: () -> Unit,
) : TargetedFlingBehavior {
    override suspend fun ScrollScope.performFling(
        initialVelocity: Float,
        onRemainingDistanceUpdated: (Float) -> Unit
    ): Float {
        val currentOffset = draggableState.offset
        val anchorPosition = draggableState.anchors.positionOf(ListItemDragValue.End)

        if (currentOffset / anchorPosition < 0.7f) {
            snapBack()
            return initialVelocity
        }

        onDismiss()
        snapBack()

        return initialVelocity
    }
}