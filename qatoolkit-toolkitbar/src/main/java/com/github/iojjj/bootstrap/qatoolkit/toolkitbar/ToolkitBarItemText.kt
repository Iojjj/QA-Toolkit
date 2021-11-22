package com.github.iojjj.bootstrap.qatoolkit.toolkitbar

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.style.ToolkitBarStyle

@Composable
fun ToolkitBarItemText(
    @StringRes
    text: Int,
    modifier: Modifier = Modifier,
    contentColor: Color = ToolkitBarStyle.Colors.contentColor(isChecked = false, isEnabled = true),
) {
    ToolkitBarItemText(
        text = stringResource(text),
        contentColor = contentColor,
        modifier = modifier,
    )
}

@Composable
fun ToolkitBarItemText(
    text: String,
    modifier: Modifier = Modifier,
    contentColor: Color = ToolkitBarStyle.Colors.contentColor(isChecked = false, isEnabled = true),
) {
    Box(
        Modifier.size(OneItemSize)
            .then(modifier)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Preview
@Composable
private fun PreviewToolkitBarItemText() {
    ToolkitBarItemText("DP")
}