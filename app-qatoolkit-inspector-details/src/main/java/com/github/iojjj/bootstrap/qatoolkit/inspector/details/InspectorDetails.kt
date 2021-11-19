package com.github.iojjj.bootstrap.qatoolkit.inspector.details

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun InspectorDetails(
    state: InspectorDetailsState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val onDismissRequestState = rememberUpdatedState(onDismissRequest)
    val isSearchBarVisibleState = remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val onBackIconClicked = remember {
        {
            if (isSearchBarVisibleState.value) {
                isSearchBarVisibleState.value = false
            } else {
                onDismissRequestState.value.invoke()
            }
        }
    }
    val onSearchIconClicked = remember {
        {
            isSearchBarVisibleState.value = true
        }
    }
    val messageState = rememberUpdatedState(
        (state as? InspectorDetailsState.Loaded)
            ?.let { stringResource(it.attributesStatus.message) }
    )
    val actionOk = stringResource(R.string.qatoolkit_inspector_details_action_ok)
    val onInfoIconClicked = remember<() -> Unit> {
        {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = messageState.value!!,
                    actionLabel = actionOk,
                    duration = SnackbarDuration.Indefinite,
                )
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high
                ) {
                    TopAppBarNavigationIcon(
                        onBackIconClicked
                    )
                    if (state is InspectorDetailsState.Loaded && isSearchBarVisibleState.value) {
                        SearchBar(
                            searchQuery = searchQuery,
                            onSearchQueryChange = onSearchQueryChange,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(Modifier.width(16.dp))
                        TopAppBarTitle(
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                if (state is InspectorDetailsState.Loaded && !isSearchBarVisibleState.value) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.medium
                    ) {
                        TopAppBarActions(
                            onSearchIconClicked = onSearchIconClicked,
                            infoIcon = state.attributesStatus.icon,
                            onInfoIconClicked = onInfoIconClicked
                        )
                    }
                }
            }
        },
        content = {
            InspectorDetailsContent(
                state,
                searchQuery,
            )
        },
    )
}