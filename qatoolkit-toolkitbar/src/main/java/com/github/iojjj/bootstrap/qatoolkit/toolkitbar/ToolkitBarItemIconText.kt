package com.github.iojjj.bootstrap.qatoolkit.toolkitbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.style.ToolkitBarStyle

@Composable
fun ToolkitBarItemIconText(
    @DrawableRes
    icon: Int,
    text: String,
    modifier: Modifier = Modifier,
    contentColor: Color = ToolkitBarStyle.Colors.contentColor(isChecked = false, isEnabled = true),
) {
    Box(
        Modifier.size(OneItemSize)
            .then(modifier)
    ) {
        ToolkitBarItemIcon(icon, contentColor = contentColor)
        Text(
            text = text,
            fontSize = 10.sp,
            color = contentColor,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(2.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewToolkitBarItemIconText() {
    ToolkitBarItemIconText(
        icon = R.drawable.qatoolkit_toolkitbar_ic_bug_24dp,
        text = "1",
    )
}