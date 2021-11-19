package com.github.iojjj.bootstrap.qatoolkit.toolkitbar

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ToolkitBarDragItem(
    isInteractionEnabled: Boolean,
    dragHandler: suspend PointerInputScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .pointerInput(isInteractionEnabled, dragHandler)
    ) {
        ToolkitBarItemIcon(
            icon = R.drawable.qatoolkit_toolkitbar_ic_bug_24dp,
        )
    }
}

@Preview
@Composable
private fun PreviewToolkitBarDragItem() {
    ToolkitBarDragItem(isInteractionEnabled = true, dragHandler = NoOpPointerInput)
}