package com.readymon.androidinterntaskapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.readymon.androidinterntaskapp.screens.*

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object SignUp : Screen("signup")
    object SignIn : Screen("signin")
    object OtpVerification : Screen("otp/{flow}") {
        fun createRoute(flow: String) = "otp/$flow"
    }
    object Dashboard : Screen("dashboard")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigate = {
                navController.navigate(Screen.SignUp.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateToOtp = { email ->
                    navController.navigate(Screen.OtpVerification.createRoute("signup"))
                },
                onNavigateToSignIn = {
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }

        composable(Screen.SignIn.route) {
            SignInScreen(
                onNavigateToOtp = { email ->
                    navController.navigate(Screen.OtpVerification.createRoute("signin"))
                },
                onNavigateToSignUp = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.OtpVerification.route,
            arguments = listOf(navArgument("flow") { type = NavType.StringType })
        ) { backStackEntry ->
            val flow = backStackEntry.arguments?.getString("flow") ?: "signup"
            OtpVerificationScreen(
                flow = flow,
                onVerificationSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen()
        }
    }
}