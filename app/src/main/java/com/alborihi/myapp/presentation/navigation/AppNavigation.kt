package com.alborihi.myapp.presentation.navigation

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alborihi.myapp.presentation.ui.screens.analytics.AnalyticsScreen
import com.alborihi.myapp.presentation.ui.screens.analytics.AnalyticsViewModel
import com.alborihi.myapp.presentation.ui.screens.backup.BackupScreen
import com.alborihi.myapp.presentation.ui.screens.backup.BackupViewModel
import com.alborihi.myapp.presentation.ui.screens.dashboard.DashboardScreen
import com.alborihi.myapp.presentation.ui.screens.dashboard.DashboardViewModel
import com.alborihi.myapp.presentation.ui.screens.export.ExportScreen
import com.alborihi.myapp.presentation.ui.screens.export.ExportViewModel
import com.alborihi.myapp.presentation.ui.screens.grades.GradeEntryScreen
import com.alborihi.myapp.presentation.ui.screens.grades.GradeEntryViewModel
import com.alborihi.myapp.presentation.ui.screens.grades.GradesTableScreen
import com.alborihi.myapp.presentation.ui.screens.grades.GradesTableViewModel
import com.alborihi.myapp.presentation.ui.screens.import.ImportScreen
import com.alborihi.myapp.presentation.ui.screens.import.ImportViewModel
import com.alborihi.myapp.presentation.ui.screens.reports.ReportsScreen
import com.alborihi.myapp.presentation.ui.screens.reports.ReportsViewModel
import com.alborihi.myapp.presentation.ui.screens.settings.SettingsScreen
import com.alborihi.myapp.presentation.ui.screens.settings.SettingsViewModel
import com.alborihi.myapp.presentation.ui.screens.students.StudentsScreen
import com.alborihi.myapp.presentation.ui.screens.students.StudentsViewModel
import com.alborihi.myapp.presentation.ui.screens.student.StudentFormScreen
import com.alborihi.myapp.presentation.ui.screens.student.StudentFormViewModel
import com.alborihi.myapp.presentation.ui.screens.student.StudentProfileScreen
import com.alborihi.myapp.presentation.ui.screens.student.StudentProfileViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * NavHost كامل مع Bottom Navigation.
 * جميع الشاشات تُحمّل كـ Lazy (لا يتم إنشاء ViewModels إلا عند الحاجة).
 */
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // تحديد الشاشة النشطة لعنوان الشريط العلوي
    val currentScreen = Screen.entries.find { it.route == currentRoute }

    Scaffold(
        topBar = {
            if (currentScreen != null && currentScreen.title.isNotEmpty()) {
                TopAppBar(
                    title = { androidx.compose.material3.Text(currentScreen.title) },
                    actions = {}
                )
            }
        },
        bottomBar = {
            val bottomNavItems = Screen.entries.filter { it.showInBottomNav }
            if (bottomNavItems.isNotEmpty()) {
                BottomAppBar(
                    actions = {}
                ) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            icon = { Icon(screen.icon(), contentDescription = screen.title) },
                            label = { androidx.compose.material3.Text(screen.title) },
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = androidx.compose.ui.Modifier.padding(paddingValues)
        ) {
            // S1 — لوحة التحكم
            composable(Screen.Dashboard.route) {
                val viewModel: DashboardViewModel = koinViewModel()
                DashboardScreen(
                    uiState = viewModel.uiState,
                    onEvent = viewModel::onEvent,
                    onNavigateToStudents = { navController.navigate(Screen.Students.route) },
                    onNavigateToGradeEntry = { studentId ->
                        navController.navigate("${Screen.GradeEntry.route}/$studentId")
                    },
                    onNavigateToProfile = { studentId ->
                        navController.navigate("${Screen.StudentProfile.route}/$studentId")
                    }
                )
            }

            // S2 — إدارة الطلاب
            composable(Screen.Students.route) {
                val viewModel: StudentsViewModel = koinViewModel()
                StudentsScreen(
                    uiState = viewModel.uiState,
                    onEvent = viewModel::onEvent,
                    onNavigateToAddStudent = { navController.navigate(Screen.StudentForm.route) },
                    onNavigateToEditStudent = { studentId ->
                        navController.navigate("${Screen.StudentForm.route}/$studentId")
                    },
                    onNavigateToGradesTable = { studentId ->
                        navController.navigate("${Screen.GradesTable.route}/$studentId")
                    }
                )
            }

            // S2 — نموذج الطالب (إضافة/تعديل)
            composable(
                route = Screen.StudentForm.route,
                arguments = listOf(navArgument(NavRoute.STUDENT_ID) { type = NavType.StringType })
            ) { backStackEntry ->
                val studentId = backStackEntry.arguments?.getString(NavRoute.STUDENT_ID)
                val viewModel: StudentFormViewModel = koinViewModel(
                    parameters = parametersOf(String::class.java to studentId)
                )
                StudentFormScreen(
                    uiState = viewModel.uiState,
                    onEvent = viewModel::onEvent,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToGradesTable = { studentId ->
                        navController.navigate("${Screen.GradesTable.route}/$studentId")
                    }
                )
            }

            // S3 — إدخال الدرجات
            composable(
                route = Screen.GradeEntry.route,
                arguments = listOf(navArgument(NavRoute.STUDENT_ID) { type = NavType.StringType })
            ) { backStackEntry ->
                val studentId = backStackEntry.arguments?.getString(NavRoute.STUDENT_ID)
                val viewModel: GradeEntryViewModel = koinViewModel(
                    parameters = parametersOf(String::class.java to studentId)
                )
                GradeEntryScreen(
                    uiState = viewModel.uiState,
                    onEvent = viewModel::onEvent,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToGradesTable = { studentId ->
                        navController.navigate("${Screen.GradesTable.route}/$studentId")
                    }
                )
            }

            // S4 — جدول الدرجات
            composable(
                route = Screen.GradesTable.route,
                arguments = listOf(navArgument(NavRoute.STUDENT_ID) { type = NavType.StringType })
            ) { backStackEntry ->
                val studentId = backStackEntry.arguments?.getString(NavRoute.STUDENT_ID)
                val viewModel: GradesTableViewModel = koinViewModel(
                    parameters = parametersOf(String::class.java to studentId)
                )
                GradesTableScreen(
                    uiState = viewModel.uiState,
                    onEvent = viewModel::onEvent,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToProfile = { studentId ->
                        navController.navigate("${Screen.StudentProfile.route}/$studentId")
                    }
                )
            }

            // S5 — ملف الطالب
            composable(
                route = Screen.StudentProfile.route,
                arguments = listOf(navArgument(NavRoute.STUDENT_ID) { type = NavType.StringType })
            ) { backStackEntry ->
                val studentId = backStackEntry.arguments?.getString(NavRoute.STUDENT_ID)
                val viewModel: StudentProfileViewModel = koinViewModel(
                    parameters = parametersOf(String::class.java to studentId)
                )
                StudentProfileScreen(
                    uiState = viewModel.uiState,
                    onEvent = viewModel::onEvent,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToGradesTable = { studentId ->
                        navController.navigate("${Screen.GradesTable.route}/$studentId")
                    }
                )
            }

            // S6 — التحليل الإحصائي
            composable(Screen.Analytics.route) {
                val viewModel: AnalyticsViewModel = koinViewModel()
                AnalyticsScreen(
                    uiState = viewModel.uiState,
                    onEvent = viewModel::onEvent
                )
            }

            // S7 — الاستيراد
            composable(Screen.Import.route) {
                val viewModel: ImportViewModel = koinViewModel()
                ImportScreen(
                    uiState = viewModel.uiState,
                    onEvent = viewModel::onEvent,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // S8 — التصدير
            composable(Screen.Export.route) {
                val viewModel: ExportViewModel = koinViewModel()
                ExportScreen(
                    uiState = viewModel.uiState,
                    onEvent = viewModel::onEvent,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // S9 — التقارير
            composable(Screen.Reports.route) {
                val viewModel: ReportsViewModel = koinViewModel()
                ReportsScreen(
                    uiState = viewModel.uiState,
                    onEvent = viewModel::onEvent,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // S10 — الإعدادات
            composable(Screen.Settings.route) {
                val viewModel: SettingsViewModel = koinViewModel()
                SettingsScreen(
                    uiState = viewModel.uiState,
                    onEvent = viewModel::onEvent,
                    onNavigateBack = { navController.popBackStack() },
                    onExitApp = { navController.navigate(Screen.ExitApp.route) }
                )
            }

            // S11 — النسخ الاحتياطي
            composable(Screen.Backup.route) {
                val viewModel: BackupViewModel = koinViewModel()
                BackupScreen(
                    uiState = viewModel.uiState,
                    onEvent = viewModel::onEvent,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
