package com.ppdm.app.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
// --- 1. IMPORTURI PENTRU ANIMAȚII ---
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
// ------------------------------------
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ppdm.app.data.local.TokenManager
import com.ppdm.app.presentation.detail.ProjectDetailScreen
import com.ppdm.app.presentation.login.LoginScreen
import com.ppdm.app.presentation.projects.ProjectListScreen
import com.ppdm.app.util.ConnectivityStatus
import com.ppdm.app.util.NetworkConnectivityObserver
import com.ppdm.app.util.NotificationHelper
import com.ppdm.app.util.ShakeDetector
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var tokenManager: TokenManager
    @Inject lateinit var networkObserver: NetworkConnectivityObserver
    @Inject lateinit var notificationHelper: NotificationHelper
    @Inject lateinit var shakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            networkObserver.observe().collect { status ->
                val isOnline = (status == ConnectivityStatus.Available)
                notificationHelper.showNetworkStatusNotification(isOnline)
            }
        }

        val token = runBlocking { tokenManager.token.first() }
        val startDestination = if (!token.isNullOrEmpty()) "project_list" else "login"

        setContent {
            val context = LocalContext.current
            val navController = rememberNavController()

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    if (isGranted) {
                    }
                }
            )

            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val permission = Manifest.permission.POST_NOTIFICATIONS
                    if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        launcher.launch(permission)
                    }
                }
            }

            NavHost(
                navController = navController,
                startDestination = startDestination,

                enterTransition = {
                    slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300)) + fadeIn()
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300)) + fadeOut()
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300)) + fadeIn()
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300)) + fadeOut()
                }
            ) {
                composable("login") { LoginScreen(navController) }
                composable("project_list") { ProjectListScreen(navController, shakeDetector = shakeDetector) }
                composable("project_create") { ProjectDetailScreen(null, navController) }
                composable(
                    "project_detail/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.LongType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getLong("id")
                    ProjectDetailScreen(id, navController)
                }
            }
        }
    }
}