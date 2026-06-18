package com.example.myapplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object RoleSelection : Screen("role_selection")
    object OtpLogin : Screen("otp_login")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Dashboard : Screen("dashboard")
    object CoursePath : Screen("course_path")
    object Quiz : Screen("quiz")
    object LessonSuccess : Screen("lesson_success")
    object Leaderboard : Screen("leaderboard")
    object Profile : Screen("profile")
    object TeacherDashboard : Screen("teacher_dashboard")
    object AdminDashboard : Screen("admin_dashboard")
    object Settings : Screen("settings")
    object GradeSelection : Screen("grade_selection")
    object Shop : Screen("shop")
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val currentUser by authViewModel.currentUser.collectAsState()
    val isAuthChecked by authViewModel.isAuthChecked.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()

    if (!isAuthChecked) {
        // Simple loading screen while checking auth
        Box(modifier = androidx.compose.ui.Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val startRoute = if (currentUser != null) {
        when(currentUser?.role) {
            "teacher" -> Screen.TeacherDashboard.route
            "admin" -> Screen.AdminDashboard.route
            else -> Screen.Dashboard.route
        }
    } else {
        Screen.Landing.route
    }

    NavHost(navController = navController, startDestination = startRoute) {
        composable(Screen.Landing.route) {
            LandingScreen(
                onLoginClick = { navController.navigate(Screen.RoleSelection.route) },
                onSignUpClick = { navController.navigate(Screen.SignUp.route) }
            )
        }
        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(
                onRoleSelected = { role ->
                    // In a real app, this just stores the preferred role during signup
                    // For now, we use it to navigate to Login
                    navController.navigate(Screen.Login.route)
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.OtpLogin.route) {
            OtpLoginScreen(
                onVerify = { navController.navigate(Screen.Dashboard.route) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onBack = { navController.popBackStack() },
                onLoginSuccess = { 
                    val dest = when(currentUser?.role) {
                        "teacher" -> Screen.TeacherDashboard.route
                        "admin" -> Screen.AdminDashboard.route
                        else -> Screen.Dashboard.route
                    }
                    navController.navigate(dest)
                }
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onBack = { navController.popBackStack() },
                onSignUpSuccess = { 
                    navController.navigate(Screen.GradeSelection.route)
                }
            )
        }
        composable(Screen.GradeSelection.route) {
            GradeSelectionScreen(
                onGradeSelected = { grade ->
                    authViewModel.updateUserGrade(grade) {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.GradeSelection.route) { inclusive = true }
                        }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Dashboard.route) {
            StudentDashboardScreen(
                onStartLesson = { navController.navigate(Screen.CoursePath.route) },
                onNavigateToLeaderboard = { navController.navigate(Screen.Leaderboard.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToShop = { navController.navigate(Screen.Shop.route) }
            )
        }
        composable(Screen.CoursePath.route) {
            CoursePathScreen(
                onLessonSelect = { lessonId -> 
                    navController.navigate(Screen.Quiz.route + "/$lessonId") 
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Quiz.route + "/{lessonId}",
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
            QuizScreen(
                lessonId = lessonId,
                onFinish = { xp -> 
                    navController.navigate(Screen.LessonSuccess.route + "/$xp")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.LessonSuccess.route + "/{xp}",
            arguments = listOf(navArgument("xp") { type = NavType.IntType })
        ) { backStackEntry ->
            val xpEarned = backStackEntry.arguments?.getInt("xp") ?: 20
            LessonSuccessScreen(
                xpEarned = xpEarned,
                onContinue = {
                    navController.navigate(Screen.Dashboard.route)
                }
            )
        }
        composable(Screen.Leaderboard.route) {
            LeaderboardScreen()
        }
        composable(Screen.Shop.route) {
            ShopScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogout = { 
                    authViewModel.logout()
                    navController.navigate(Screen.Landing.route)
                },
                onSwitchToTeacher = {
                    if (authViewModel.isTeacher() || authViewModel.isAdmin()) {
                        navController.navigate(Screen.TeacherDashboard.route)
                    }
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                isAdmin = authViewModel.isAdmin(),
                onNavigateToAdmin = {
                    navController.navigate(Screen.AdminDashboard.route)
                }
            )
        }
        composable(Screen.TeacherDashboard.route) {
            if (authViewModel.isTeacher() || authViewModel.isAdmin()) {
                TeacherDashboardScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable(Screen.AdminDashboard.route) {
            if (authViewModel.isAdmin()) {
                AdminDashboardScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
