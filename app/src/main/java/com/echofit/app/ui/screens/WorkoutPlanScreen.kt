package com.echofit.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echofit.app.domain.ai.EchoAIEngine
import com.echofit.app.domain.model.Exercise
import com.echofit.app.ui.components.GlassmorphicCard
import com.echofit.app.ui.components.PremiumButton
import com.echofit.app.ui.components.threeDTiltEffect
import com.echofit.app.ui.components.BackgroundVideoPlayer
import com.echofit.app.ui.components.ThreeDText
import androidx.compose.ui.graphics.Brush
import com.echofit.app.ui.theme.*
import com.echofit.app.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WorkoutPlanScreen(
    viewModel: MainViewModel
) {
    val scrollState = rememberScrollState()
    val profile by viewModel.userProfile.collectAsState()
    val workoutPlan by viewModel.currentWorkoutPlan.collectAsState()
    val attendanceList by viewModel.attendanceLogs.collectAsState()

    var showYogaZumbaStudio by remember { mutableStateOf(false) }
    var timerSecondsRemaining by remember { mutableStateOf(60) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var activeExerciseForDetail by remember { mutableStateOf<Exercise?>(null) }
    var activeSubstitutionText by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            while (timerSecondsRemaining > 0) {
                delay(1000)
                timerSecondsRemaining -= 1
            }
            isTimerRunning = false
        }
    }

    if (showYogaZumbaStudio) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { showYogaZumbaStudio = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = GoldAccent)
                ) {
                    Text(
                        text = "← Back to AI Trainer",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                YogaZumbaScreen(viewModel = viewModel)
            }
        }
    } else {
        Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlack.copy(alpha = 0f))
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                ThreeDText(
                    text = "WORKOUT",
                    fontSize = 28.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Active Split: ${workoutPlan?.routineName ?: "Loading..."}",
                    style = Typography.bodyMedium,
                    color = TextGray
                )
            }
        }

        // Yoga & Zumba Studio Card
        GlassmorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .clickable { showYogaZumbaStudio = true },
            borderColor = BorderGray
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Brush.verticalGradient(listOf(OrangeGradStart, OrangeGradEnd))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Studio icon",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Yoga & Dance Studio",
                            style = Typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Active recovery & cardio dance sessions",
                            style = Typography.labelMedium,
                            color = TextGray
                        )
                    }
                }
                
                Text(
                    text = "ENTER →",
                    style = Typography.labelLarge,
                    color = OrangeGradEnd,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))


        // Timer Widget Card
        GlassmorphicCard(modifier = Modifier.fillMaxWidth(), isGlowing = isTimerRunning) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = GoldAccent,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Set Rest Countdown",
                            style = Typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Pace your recovery intervals",
                            style = Typography.labelMedium,
                            color = TextGray
                        )
                    }
                }

                // Digital Timer Clock
                Text(
                    text = String.format("%02d:%02d", timerSecondsRemaining / 60, timerSecondsRemaining % 60),
                    style = Typography.titleMedium,
                    fontSize = 26.sp,
                    color = GoldAccent,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pause / Start Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 3.dp)
                        .clickable { isTimerRunning = !isTimerRunning }
                ) {
                    // Shadow
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .offset(y = 3.dp)
                            .clip(RoundedCornerShape(26.dp))
                            .background(Color.Black.copy(alpha = 0.1f))
                    )
                    // Surface
                    val buttonBrush = if (isTimerRunning) {
                        Brush.linearGradient(listOf(ErrorRed, ErrorRed.copy(alpha = 0.8f)))
                    } else {
                        Brush.linearGradient(listOf(EmeraldGradStart, EmeraldGradEnd))
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(26.dp))
                            .background(buttonBrush)
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(26.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isTimerRunning) "PAUSE REST" else "START 60s REST",
                            color = Color.White,
                            style = Typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Reset Button
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .padding(bottom = 3.dp)
                        .clickable {
                            isTimerRunning = false
                            timerSecondsRemaining = 60
                        }
                ) {
                    // Shadow
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .offset(y = 3.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.1f))
                    )
                    // Surface
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(SurfaceDark)
                            .border(1.dp, BorderGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            tint = PrimaryBlack,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Exercises Checklist Header
        Text(
            text = "TODAY'S WORKOUT EXERCISES",
            style = Typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        workoutPlan?.let { plan ->
            plan.exercises.forEach { ex ->
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { activeExerciseForDetail = ex },
                    borderColor = BorderGray
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ex.name,
                                style = Typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${ex.sets} Sets x ${ex.reps} Reps | Target: ${ex.targetMuscle}",
                                style = Typography.labelMedium,
                                color = TextGray
                            )
                        }
                        IconButton(onClick = { activeExerciseForDetail = ex }) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = "Info", tint = GoldAccent)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mark session completed button
            val hasWorkout = attendanceList.firstOrNull()?.isWorkoutDone ?: false
            PremiumButton(
                text = if (hasWorkout) "WORKOUT COMPLETED!" else "FINISH SESSION",
                onClick = { viewModel.markWorkoutCompleted() },
                enabled = !hasWorkout,
                buttonBrush = if (!hasWorkout) Brush.linearGradient(listOf(EmeraldGradStart, EmeraldGradEnd)) else null
            )
        }

        Spacer(modifier = Modifier.height(72.dp))
    }

    // Exercise details bottom-sheet or dialog
    val activeEx = activeExerciseForDetail
    if (activeEx != null) {
        AlertDialog(
            onDismissRequest = {
                activeExerciseForDetail = null
                activeSubstitutionText = null
            },
            title = {
                Text(
                    text = activeEx.name,
                    style = Typography.titleMedium,
                    color = GoldAccent
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 3D Gym Exercise Illustration Block
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(bottom = 6.dp)
                    ) {
                        Image(
                            painter = painterResource(id = com.echofit.app.R.drawable.exercise_chest_press_3d),
                            contentDescription = "Exercise 3D Illustration",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, BorderGray, RoundedCornerShape(12.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = "Target Muscle: ${activeEx.targetMuscle}", style = Typography.bodyMedium, fontWeight = FontWeight.Bold, color = TextWhite)
                    Text(text = "Equipment: ${activeEx.targetEquipment}", style = Typography.bodyMedium, color = TextWhite)

                    Divider(color = BorderGray, modifier = Modifier.padding(vertical = 4.dp))

                    Text(text = "Movement Instructions:", style = Typography.labelLarge, color = GoldAccent, fontWeight = FontWeight.Bold)
                    activeEx.instructions.forEachIndexed { i, step ->
                        Text(text = "${i + 1}. $step", style = Typography.bodyMedium, color = TextWhite)
                    }

                    Divider(color = BorderGray, modifier = Modifier.padding(vertical = 4.dp))

                    Text(text = "Common Mistakes to Avoid:", style = Typography.labelLarge, color = ErrorRed, fontWeight = FontWeight.Bold)
                    activeEx.commonMistakes.forEach { mistake ->
                        Text(text = "• $mistake", style = Typography.bodyMedium, color = TextGray)
                    }

                    Divider(color = BorderGray, modifier = Modifier.padding(vertical = 4.dp))

                    // Substitution Subsystem
                    Text(text = "AI Workout Substitution Engine", style = Typography.labelLarge, color = GoldAccent, fontWeight = FontWeight.Bold)
                    
                    val subText = activeSubstitutionText
                    if (subText != null) {
                        Text(
                            text = subText,
                            style = Typography.bodyMedium,
                            color = LightGold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x1AFFD700))
                                .padding(10.dp)
                        )
                    } else {
                        Button(
                            onClick = {
                                activeSubstitutionText = EchoAIEngine.substituteExercise(activeEx.name)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Missing equipment? Get alternate swap!", color = GoldAccent)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        activeExerciseForDetail = null
                        activeSubstitutionText = null
                    }
                ) {
                    Text("CLOSE", color = GoldAccent)
                }
            },
            containerColor = SurfaceDark
        )
    }
    }
}
