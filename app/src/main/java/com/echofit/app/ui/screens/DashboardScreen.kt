package com.echofit.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echofit.app.domain.ai.EchoAIEngine
import com.echofit.app.ui.components.*
import com.echofit.app.ui.theme.*
import com.echofit.app.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MainViewModel
) {
    val scrollState = rememberScrollState()

    val profile by viewModel.userProfile.collectAsState()
    val analysis by viewModel.goalAnalysis.collectAsState()
    val meals by viewModel.meals.collectAsState()
    val waterList by viewModel.waterLogs.collectAsState()
    val attendanceList by viewModel.attendanceLogs.collectAsState()

    // Calculate daily completion stats
    val targetCal = analysis?.calorieTarget ?: 2000
    val targetPro = analysis?.proteinTarget ?: 120
    val consumedCal = meals.sumOf { it.calories }
    val consumedPro = meals.sumOf { it.protein }
    val glassesWater = waterList.sumOf { it.glasses }
    val waterTargetGlasses = ((analysis?.waterIntakeTargetLiters ?: 3.0) / 0.25).toInt()

    val streak = if (attendanceList.isNotEmpty()) {
        attendanceList.take(7).filter { it.score > 0 }.size
    } else 1

    val motivationalQuote = remember(meals.size, glassesWater) {
        if (meals.isEmpty() && glassesWater == 0) {
            EchoAIEngine.getEchoResponse("motivate")
        } else if (meals.isNotEmpty() && glassesWater > waterTargetGlasses / 2) {
            EchoAIEngine.getEchoResponse("streak", streak)
        } else {
            "Consistency beats intensity, champion. Keep ticking off those targets!"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlack.copy(alpha = 0f))
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Top Greeting Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome, ${profile?.name ?: "Champion"}!",
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    text = "Goal: ${profile?.fitnessGoal ?: "General Fitness"}",
                    style = Typography.bodyMedium,
                    color = TextGray
                )
            }
            StreakFireIndicator(streakCount = streak)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Main Double Progress Rings
        CalorieProgressRing(
            caloriesConsumed = consumedCal,
            caloriesTarget = targetCal,
            proteinConsumed = consumedPro,
            proteinTarget = targetPro
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Echo motivational Speech Widget
        EchoMotivationalCard(message = motivationalQuote)

        Spacer(modifier = Modifier.height(24.dp))

        // Daily Checklist Card
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "TODAY'S MISSION PROGRESS",
                style = Typography.labelLarge,
                color = GoldAccent,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Task list items
            ChecklistRow(
                title = "Log Water Intake",
                subtitle = "Hydration: $glassesWater / $waterTargetGlasses glasses completed",
                isCompleted = glassesWater >= waterTargetGlasses,
                onClick = { viewModel.addWaterGlass() }
            )

            Divider(color = BorderGray, modifier = Modifier.padding(vertical = 12.dp))

            val hasWorkout = attendanceList.firstOrNull()?.isWorkoutDone ?: false
            ChecklistRow(
                title = "Complete Daily Workout",
                subtitle = if (hasWorkout) "Workout Routine Checked!" else "Tap to check off your exercises",
                isCompleted = hasWorkout,
                onClick = { viewModel.markWorkoutCompleted() }
            )

            Divider(color = BorderGray, modifier = Modifier.padding(vertical = 12.dp))

            val hasMeals = meals.isNotEmpty()
            ChecklistRow(
                title = "Log Nutrition Meal",
                subtitle = if (hasMeals) "${meals.size} meals registered today" else "Register a breakfast/lunch plan",
                isCompleted = hasMeals,
                onClick = {
                    viewModel.selectedTab.value = "Diet"
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // REST DAY ACTIVE RECOVERY & CHEAT MEAL SYSTEM PANELS
        val currentDayOfWeek = remember {
            java.text.SimpleDateFormat("EEEE", java.util.Locale.US).format(java.util.Date())
        }
        val restDayConfig = profile?.preferredRestDay ?: "Sunday"
        val isTodayRestDay = currentDayOfWeek.equals(restDayConfig, ignoreCase = true)
        
        var manualRestDayToggle by remember { mutableStateOf(false) }
        val restDayActive = isTodayRestDay || manualRestDayToggle

        var stepsWalked by remember { mutableStateOf(3500) }
        var cheatMealLogged by remember { mutableStateOf(false) }
        var cheatMealCaloriesInput by remember { mutableStateOf("") }
        var isEnteringCheatMeal by remember { mutableStateOf(false) }

        if (restDayActive) {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ACTIVE RECOVERY REST DAY",
                        style = Typography.labelLarge,
                        color = SuccessGreen,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "REST MODE ACTIVE",
                        style = Typography.labelMedium,
                        color = TextGray
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Coach Echo's Recovery Guidelines:",
                    style = Typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent
                )
                Text(
                    text = "• \"Put down the heavy iron and let your fibers repair actively! Take a walk, stretch, and load up on pure hydration.\" \n• Recommended: 8 hours of sleep and a light yoga stretch.",
                    style = Typography.bodyMedium,
                    color = TextWhite,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Rest Day Walking Steps Progression
                val stepsGoal = 8000
                val progressSteps = stepsWalked.toFloat() / stepsGoal
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Active Walking Goal", style = Typography.labelMedium, color = TextGray)
                    Text(text = "$stepsWalked / $stepsGoal Steps", style = Typography.labelMedium, color = TextWhite, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = progressSteps.coerceIn(0f, 1f),
                    color = SuccessGreen,
                    trackColor = Color(0x33FFFFFF),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { stepsWalked = (stepsWalked + 1000).coerceAtMost(stepsGoal * 2) },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).border(1.dp, BorderGray, RoundedCornerShape(8.dp))
                    ) {
                        Text("+1,000 STEPS", color = TextWhite, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            viewModel.selectedTab.value = "Workout" // Redirect to stretches
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("DO STRETCH YOGA", color = PrimaryBlack, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        } else {
            // Option to manually trigger rest mode
            Button(
                onClick = { manualRestDayToggle = true },
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, BorderGray, RoundedCornerShape(10.dp))
            ) {
                Text("TOGGLE MANUAL REST DAY MODE", color = TextGray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // CHEAT MEAL PANEL
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "WEEKLY CHEAT MEAL BUDGET",
                style = Typography.labelLarge,
                color = if (cheatMealLogged) ErrorRed else GoldAccent,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (!cheatMealLogged) {
                if (isEnteringCheatMeal) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Enjoy your cheat meal, but track it so we keep consistency!",
                            style = Typography.bodyMedium,
                            color = TextGray
                        )
                        OutlinedTextField(
                            value = cheatMealCaloriesInput,
                            onValueChange = { cheatMealCaloriesInput = it },
                            placeholder = { Text("Est. calories (e.g. 700)", color = TextGray) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = GoldAccent,
                                unfocusedBorderColor = BorderGray,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { isEnteringCheatMeal = false }) {
                                Text("CANCEL", color = TextGray)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = {
                                    cheatMealLogged = true
                                    isEnteringCheatMeal = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                            ) {
                                Text("LOG CHEAT MEAL", color = PrimaryBlack, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "1 Cheat meal allowed per week",
                                style = Typography.bodyMedium,
                                color = TextWhite
                            )
                            Text(
                                text = "Track rewards compliance",
                                style = Typography.labelMedium,
                                color = TextGray
                            )
                        }
                        Button(
                            onClick = { isEnteringCheatMeal = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("LOG MEAL", color = PrimaryBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                val calLogged = cheatMealCaloriesInput.toIntOrNull() ?: 650
                Column {
                    Text(
                        text = "Cheat Meal Logged: $calLogged kcal registered",
                        style = Typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = ErrorRed
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Coach Echo: \"Enjoy it, but don't let one cheat meal become a cheat week! Drink 4 glasses of water now and let's get straight back to the program tomorrow.\"",
                        style = Typography.bodyMedium,
                        color = TextWhite,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            cheatMealLogged = false
                            cheatMealCaloriesInput = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().border(1.dp, BorderGray, RoundedCornerShape(8.dp))
                    ) {
                        Text("RESET WEEKLY CHEAT", color = TextGray, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Stats Cards Grid
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                DashboardMetricCard(
                    title = "Hydration",
                    value = "${glassesWater * 0.25} Liters",
                    color = HydrateBlue
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.weight(1f)) {
                DashboardMetricCard(
                    title = "XP Gained",
                    value = "${attendanceList.sumOf { it.score }} XP",
                    color = GoldAccent
                )
            }
        }

        Spacer(modifier = Modifier.height(72.dp))
    }
}

@Composable
fun ChecklistRow(
    title: String,
    subtitle: String,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = Typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isCompleted) TextGray else TextWhite
            )
            Text(
                text = subtitle,
                style = Typography.labelMedium,
                color = TextGray
            )
        }
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Status",
            tint = if (isCompleted) GoldAccent else BorderGray,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun DashboardMetricCard(
    title: String,
    value: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .threeDTiltEffect()
            .padding(bottom = 4.dp, end = 4.dp)
    ) {
        // 3D color-matched extrusion base
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 4.dp, y = 4.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            color.copy(alpha = 0.8f),
                            PrimaryBlack
                        )
                    )
                )
                .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
        )

        // Main glass/card surface layer
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGray.copy(alpha = 0.8f), RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = title, style = Typography.labelMedium, color = TextGray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = Typography.titleMedium,
                    color = color,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
