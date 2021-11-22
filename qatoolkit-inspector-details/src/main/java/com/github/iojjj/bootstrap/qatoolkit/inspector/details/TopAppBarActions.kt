package com.github.iojjj.bootstrap.qatoolkit.inspector.details

import androidx.annotation.DrawableRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
internal fun TopAppBarActions(
    onSearchIconClicked: () -> Unit,
    @DrawableRes
    infoIcon: Int,
    onInfoIconClicked: () -> Unit,
) {
    IconButton(
        onClick = onSearchIconClicked
    ) {
        Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = null
        )
    }
    IconButton(
        onClick = onInfoIconClicked
    ) {
        Icon(
            painter = painterResource(infoIcon),
            contentDescription = null
        )
    }
}