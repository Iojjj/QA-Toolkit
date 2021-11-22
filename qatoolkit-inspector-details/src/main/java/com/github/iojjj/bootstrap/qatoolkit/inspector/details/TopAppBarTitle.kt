package com.github.iojjj.bootstrap.qatoolkit.inspector.details

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
internal fun TopAppBarTitle(
    modifier: Modifier
) {
    Text(
        text = stringResource(R.string.qatoolkit_inspector_details_title),
        style = MaterialTheme.typography.h6,
        modifier = modifier
    )
}