package com.github.iojjj.bootstrap.qatoolkit.toolkitbar

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ToolkitBarRotateItem(
    isClickEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ToolkitBarItemIcon(
        icon = R.drawable.qatoolkit_toolkitbar_ic_rotate_24dp,
        modifier = Modifier
            .clickable(
                enabled = isClickEnabled,
                onClick = onClick
            )
            .then(modifier),
    )
}

@Preview
@Composable
private fun PreviewToolkitBarRotateItem() {
    ToolkitBarRotateItem(isClickEnabled = true, onClick = { })
}