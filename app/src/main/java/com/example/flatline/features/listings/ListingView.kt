package com.example.flatline.features.listings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.flatline.common.ui.swipetodismiss.CustomFlingBehavior
import com.example.flatline.common.ui.swipetodismiss.ListItemDragValue
import com.example.flatline.features.listings.domain.Listing
import com.example.flatline.ui.theme.FlatlineTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// TODO: extract the whole swipeable functionality into separate composable
@Composable
fun ListingView(
    modifier: Modifier = Modifier,
    item: Listing,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val offset = 100.dp
    val density = LocalDensity.current
    val offsetPx = with(density) { offset.toPx() }

    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = ListItemDragValue.Center,
            anchors = DraggableAnchors {
                ListItemDragValue.Center at 0f
                ListItemDragValue.End at offsetPx
            }
        )
    }

    val scope = rememberCoroutineScope()

    val flingBehavior = remember(draggableState) {
        CustomFlingBehavior(
            draggableState = draggableState,
            snapBack = { scope.launch { draggableState.animateTo(ListItemDragValue.Center) } },
            onDismiss = { onDismiss() },
        )
    }

    Column(
        modifier = modifier
            .anchoredDraggable(
                state = draggableState,
                orientation = Orientation.Horizontal,
                flingBehavior = flingBehavior,
            )
            .offset { IntOffset(x = draggableState.offset.roundToInt(), 0) }
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp),
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            modifier = modifier,
            text = item.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        item.address?.let {
            Text(
                modifier = modifier,
                text = it,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Text(
            modifier = modifier,
            text = item.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AdditionalInfo(text = "${item.rooms} Zimmer")
            AdditionalInfo(text = "${item.area} qm")
            AdditionalInfo(text = "${item.rent} €".replace(".", ","))
            if (item.wbs) {
                AdditionalInfo(text = "WBS")
            }
            AdditionalInfo(text = item.provider)
        }
    }
}

@Composable
private fun AdditionalInfo(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Preview(showBackground = true)
@Composable
private fun ListingPreview() {
    FlatlineTheme {
        ListingView(
            item = Listing(
                id = -1,
                title = "Dein neues Zuhause",
                rent = 453.28,
                area = 38.88,
                rooms = 2,
                wbs = true,
                address = "Stiftsweg 19A, 13187 Berlin",
                description = "Some random facts",
                provider = "GESOBAU",
                url = "whatever",
            ),
            onClick = {},
            onDismiss = {},
        )
    }
}