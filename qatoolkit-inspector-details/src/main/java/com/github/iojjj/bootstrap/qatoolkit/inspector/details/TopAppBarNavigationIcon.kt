package com.github.iojjj.bootstrap.qatoolkit.inspector.details

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable

@Composable
internal fun TopAppBarNavigationIcon(
    onBackIconClicked: () -> Unit,
) {
    IconButton(
        onClick = onBackIconClicked
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null
        )
    }
}