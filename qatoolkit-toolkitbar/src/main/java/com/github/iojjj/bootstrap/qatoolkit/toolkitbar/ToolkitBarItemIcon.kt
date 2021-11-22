package com.github.iojjj.bootstrap.qatoolkit.toolkitbar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.style.ToolkitBarStyle

@Composable
fun ToolkitBarItemIcon(
    @DrawableRes
    icon: Int,
    @StringRes
    contentDescription: Int,
    modifier: Modifier = Modifier,
    contentColor: Color = ToolkitBarStyle.Colors.contentColor(isChecked = false, isEnabled = true),
) {
    ToolkitBarItemIcon(
        icon = icon,
        contentColor = contentColor,
        contentDescription = stringResource(contentDescription),
        modifier = modifier
    )
}

@Composable
fun ToolkitBarItemIcon(
    @DrawableRes
    icon: Int,
    modifier: Modifier = Modifier,
    contentColor: Color = ToolkitBarStyle.Colors.contentColor(isChecked = false, isEnabled = true),
    contentDescription: String? = null,
) {
    val painter = painterResource(icon)
    Image(
        modifier = Modifier
            .size(OneItemSize)
            .then(modifier),
        painter = painter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Inside,
        colorFilter = ColorFilter.tint(contentColor),
    )
}

@Preview
@Composable
private fun PreviewToolkitBarItemIcon() {
    ToolkitBarItemIcon(R.drawable.qatoolkit_toolkitbar_ic_bug_24dp)
}