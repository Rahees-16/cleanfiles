package com.rahees.cleanfiles.navigation

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
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
import com.rahees.cleanfiles.ui.onboarding.OnboardingScreen
import com.rahees.cleanfiles.ui.search.SearchScreen
import com.rahees.cleanfiles.ui.settings.SettingsScreen
import com.rahees.cleanfiles.ui.trash.TrashScreen
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding")

object Routes {
    const val HOME = "home"
    const val BROWSER = "browser/{path}"
    const val CATEGORY = "category/{type}"
    const val ANALYSIS = "analysis"
    const val CLEANER = "cleaner"
    const val DUPLICATES = "duplicates"
    const val SEARCH = "search"
    const val TRASH = "trash"
    const val SETTINGS = "settings"
    const val ONBOARDING = "onboarding"

    fun browser(path: String): String = "browser/${Uri.encode(path)}"
    fun category(type: FileCategory): String = "category/${type.name}"
}

@Composable
fun CleanFilesNavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val onboardingCompleted by context.onboardingDataStore.data.map { prefs ->
        prefs[booleanPreferencesKey("onboarding_completed")] ?: false
    }.collectAsState(initial = true)

    NavHost(
        navController = navController,
        startDestination = if (onboardingCompleted) Routes.HOME else Routes.ONBOARDING
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onComplete = {
                    scope.launch {
                        context.onboardingDataStore.edit { prefs ->
                            prefs[booleanPreferencesKey("onboarding_completed")] = true
                        }
                    }
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

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

        composable(Routes.TRASH) {
            TrashScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
