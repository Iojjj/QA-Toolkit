package com.github.iojjj.bootstrap.qatoolkit

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.github.iojjj.bootstrap.qatoolkit.grid.GridModeRoute
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorModeRoute
import com.github.iojjj.bootstrap.qatoolkit.inspector.ViewNode
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitMode
import com.github.iojjj.bootstrap.qatoolkit.ruler.RulerModeRoute

@Composable
fun ToolkitContentOverlay(
    findRootViewNode: () -> ViewNode?,
    navController: NavHostController,
    onBackPressedDispatcher: OnBackPressedDispatcher,
    viewModelFactory: ViewModelProvider.Factory,
) {

    NavHost(navController, startDestination = ToolkitMode.Initial) {
        composable(ToolkitMode.Initial) {
            /* no-op */
        }
        composable(ToolkitMode.Inspect) { navBackStackEntry ->
            InspectorModeRoute(findRootViewNode, navBackStackEntry, onBackPressedDispatcher, viewModelFactory)
        }
        composable(ToolkitMode.Grid) { navBackStackEntry ->
            GridModeRoute(navBackStackEntry, onBackPressedDispatcher, viewModelFactory)
        }
        composable(ToolkitMode.Ruler) { navBackStackEntry ->
            RulerModeRoute(navBackStackEntry, onBackPressedDispatcher, viewModelFactory)
        }
    }
}