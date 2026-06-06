package com.echofit.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echofit.app.R
import com.echofit.app.ui.components.*
import com.echofit.app.ui.theme.*
import com.echofit.app.ui.viewmodel.MainViewModel

@Composable
fun DashboardScreen(
    viewModel: MainViewModel
) {
    val scrollState = rememberScrollState()
    
    val profile by viewModel.userProfile.collectAsState()
    val dietPlan by viewModel.currentDietPlan.collectAsState()
    val mealsLogged by viewModel.meals.collectAsState()
    val waterLogs by viewModel.waterLogs.collectAsState()
    val attendanceLogs by viewModel.attendanceLogs.collectAsState()
    
    val loggedCal = mealsLogged.sumOf { it.calories }
    val loggedPro = mealsLogged.sumOf { it.protein }
    val targetCalories = dietPlan?.targetCalories ?: 2200
    val targetProtein = dietPlan?.targetProtein ?: 140
    
    val loggedWater = waterLogs.sumOf { it.glasses }
    val targetWater = 12

    val todayAttendance = attendanceLogs.find { it.dateString == java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date()) }
    val workoutCompleted = todayAttendance?.isWorkoutDone ?: false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ThreeDText(
                text = "DASHBOARD",
                fontSize = 28.sp,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            
            StreakFireIndicator(streakCount = 5)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Progress Overview with Ring Chart
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CalorieProgressRing(
                caloriesConsumed = loggedCal,
                caloriesTarget = targetCalories,
                proteinConsumed = loggedPro,
                proteinTarget = targetProtein,
                size = 190.dp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Today's Missions Card
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "TODAY'S MISSION CHECKLIST",
                style = Typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryBlack
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Calories Mission
            MissionRow(
                title = "Hit Daily Calories Target",
                statusText = "$loggedCal / $targetCalories kcal",
                isCompleted = loggedCal >= targetCalories
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            // Protein Mission
            MissionRow(
                title = "Reach Protein Target",
                statusText = "$loggedPro / $targetProtein g",
                isCompleted = loggedPro >= targetProtein
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Workout Mission
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Complete Workout Split",
                        style = Typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlack
                    )
                    Text(
                        text = if (workoutCompleted) "Completed!" else "Action Required",
                        style = Typography.labelSmall,
                        color = if (workoutCompleted) SuccessGreen else TextGray
                    )
                }
                
                if (!workoutCompleted) {
                    Button(
                        onClick = { viewModel.markWorkoutCompleted() },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBeige),
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Log Done", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text("✓", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Hydration Mission
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hydration Tracker",
                        style = Typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlack
                    )
                    Text(
                        text = "$loggedWater / $targetWater glasses",
                        style = Typography.labelSmall,
                        color = TextGray
                    )
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = { viewModel.addWaterGlass() },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBeige),
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text("+ Glass", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    
                    if (loggedWater > 0) {
                        Button(
                            onClick = { viewModel.clearWater() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("Reset", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Echo AI Motivational Guidance Card
        EchoMotivationalCard(
            message = if (loggedCal == 0) {
                "Welcome to a brand new day! Ready to crush your fitness goals? Start by logging your first healthy meal using the Diet planner!"
            } else if (loggedWater < 6) {
                "You're doing great with your calories, but don't forget to keep drinking water. Let's aim to log a couple more glasses right now!"
            } else {
                "Superb progress on all metrics! Consistency is key. Push hard on your active routines and let's wrap this day with a perfect score!"
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Premium 3D Focus Banner Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .threeDTiltEffect()
                .padding(bottom = 6.dp, end = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = 6.dp, y = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.15f))
            )
            
            Image(
                painter = painterResource(id = R.drawable.discipline_bg),
                contentDescription = "Discipline Focus Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun MissionRow(
    title: String,
    statusText: String,
    isCompleted: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = Typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlack
            )
            Text(
                text = statusText,
                style = Typography.labelSmall,
                color = TextGray
            )
        }
        Text(
            text = if (isCompleted) "✓" else "⌛",
            color = if (isCompleted) SuccessGreen else TextGray,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}
