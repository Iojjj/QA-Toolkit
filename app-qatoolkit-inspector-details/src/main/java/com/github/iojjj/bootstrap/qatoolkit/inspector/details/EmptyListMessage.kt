package com.github.iojjj.bootstrap.qatoolkit.inspector.details

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun BoxScope.EmptyListMessage(searchQuery: String) {
    val message = when (searchQuery.isEmpty()) {
        true -> {
            stringResource(R.string.qatoolkit_inspector_details_attributes_not_found)
        }
        false -> {
            stringResource(R.string.qatoolkit_inspector_details_attributes_not_found_search_query, searchQuery)
        }
    }
    Text(
        text = message,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center)
            .padding(16.dp)
    )
}