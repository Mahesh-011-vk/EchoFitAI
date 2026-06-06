package com.echofit.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.echofit.app.ui.components.EchoBottomNavigation
import com.echofit.app.ui.components.BackgroundVideoPlayer
import com.echofit.app.ui.screens.*
import com.echofit.app.ui.theme.EchoFitTheme
import com.echofit.app.ui.theme.PrimaryBlack
import com.echofit.app.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EchoFitTheme {
                val viewModel: MainViewModel = viewModel()
                
                // Track onboarding completed profile state
                val userProfile by viewModel.userProfile.collectAsState()
                val selectedTab by viewModel.selectedTab.collectAsState()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.white_silk_bg),
                        contentDescription = "White Silk Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    if (userProfile == null) {
                        // Profile Setup onboarding questionnaire
                        ProfileSetupScreen(
                            viewModel = viewModel,
                            onSetupComplete = {
                                // Onboarding completed, let the flow register naturally
                            }
                        )
                    } else {
                        // Main App Workspace Shell with Navigation
                        Scaffold(
                            containerColor = Color.Transparent,
                            modifier = Modifier.fillMaxSize(),
                            bottomBar = {
                                EchoBottomNavigation(
                                    selectedTab = selectedTab,
                                    onTabSelected = { tab ->
                                        viewModel.selectedTab.value = tab
                                    }
                                )
                            }
                        ) { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                when (selectedTab) {
                                    "HOME" -> DashboardScreen(viewModel = viewModel)
                                    "DIET" -> DietPlanScreen(viewModel = viewModel)
                                    "WORKOUT" -> WorkoutPlanScreen(viewModel = viewModel)
                                    "PROGRESS" -> ProgressPhotoScreen(viewModel = viewModel)
                                    "ECHOAI" -> EchoChatScreen(viewModel = viewModel)
                                    "SETTING" -> SettingsScreen(viewModel = viewModel)
                                    else -> DashboardScreen(viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

