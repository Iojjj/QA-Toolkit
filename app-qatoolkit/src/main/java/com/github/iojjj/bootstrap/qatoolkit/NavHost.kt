@file:Suppress("nothing_to_inline")

package com.github.iojjj.bootstrap.qatoolkit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable

@Composable
inline fun <T : Enum<T>> NavHost(
    navController: NavHostController,
    startDestination: T,
    modifier: Modifier = Modifier,
    route: T? = null,
    noinline builder: NavGraphBuilder.() -> Unit,
) {
    androidx.navigation.compose.NavHost(
        navController,
        startDestination.name,
        modifier,
        route?.name,
        builder
    )
}

inline fun <T : Enum<T>> NavGraphBuilder.composable(
    route: T,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(route.name, arguments, deepLinks, content)
}

inline fun <T : Enum<T>> NavHostController.navigate(
    route: T,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(route.name, navOptions, navigatorExtras)
}

inline fun <reified T : Enum<T>> NavBackStackEntry.route(): T? {
    return this.destination.route?.let { enumValueOf<T>(it) }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.scopedViewModel(
    key: String? = null,
    factory: ViewModelProvider.Factory? = null,
): T {
    return viewModel(T::class.java, this, key, factory)
}