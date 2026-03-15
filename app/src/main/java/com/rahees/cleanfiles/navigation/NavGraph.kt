package com.rahees.cleanfiles.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rahees.cleanfiles.data.model.FileCategory
import com.rahees.cleanfiles.ui.analysis.AnalysisScreen
import com.rahees.cleanfiles.ui.browser.BrowserScreen
import com.rahees.cleanfiles.ui.category.CategoryScreen
import com.rahees.cleanfiles.ui.cleaner.CleanerScreen
import com.rahees.cleanfiles.ui.duplicates.DuplicatesScreen
import com.rahees.cleanfiles.ui.home.HomeScreen
import com.rahees.cleanfiles.ui.search.SearchScreen
import com.rahees.cleanfiles.ui.settings.SettingsScreen

object Routes {
    const val HOME = "home"
    const val BROWSER = "browser/{path}"
    const val CATEGORY = "category/{type}"
    const val ANALYSIS = "analysis"
    const val CLEANER = "cleaner"
    const val DUPLICATES = "duplicates"
    const val SEARCH = "search"
    const val SETTINGS = "settings"

    fun browser(path: String): String = "browser/${Uri.encode(path)}"
    fun category(type: FileCategory): String = "category/${type.name}"
}

@Composable
fun CleanFilesNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToBrowser = { path ->
                    navController.navigate(Routes.browser(path))
                },
                onNavigateToCategory = { category ->
                    navController.navigate(Routes.category(category))
                },
                onNavigateToAnalysis = {
                    navController.navigate(Routes.ANALYSIS)
                },
                onNavigateToCleaner = {
                    navController.navigate(Routes.CLEANER)
                },
                onNavigateToDuplicates = {
                    navController.navigate(Routes.DUPLICATES)
                },
                onNavigateToSearch = {
                    navController.navigate(Routes.SEARCH)
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }

        composable(
            route = Routes.BROWSER,
            arguments = listOf(navArgument("path") { type = NavType.StringType })
        ) { backStackEntry ->
            val path = Uri.decode(backStackEntry.arguments?.getString("path") ?: "/")
            BrowserScreen(
                initialPath = path,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSearch = { navController.navigate(Routes.SEARCH) }
            )
        }

        composable(
            route = Routes.CATEGORY,
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->
            val typeName = backStackEntry.arguments?.getString("type") ?: FileCategory.IMAGES.name
            val category = FileCategory.valueOf(typeName)
            CategoryScreen(
                category = category,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ANALYSIS) {
            AnalysisScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CLEANER) {
            CleanerScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.DUPLICATES) {
            DuplicatesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenFile = { path ->
                    navController.navigate(Routes.browser(java.io.File(path).parent ?: "/"))
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
