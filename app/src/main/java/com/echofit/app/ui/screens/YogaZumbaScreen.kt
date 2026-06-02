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
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echofit.app.ui.components.GlassmorphicCard
import com.echofit.app.ui.components.PremiumButton
import com.echofit.app.ui.components.threeDTiltEffect
import com.echofit.app.ui.components.BackgroundVideoPlayer
import androidx.compose.ui.graphics.Brush
import com.echofit.app.ui.theme.*
import com.echofit.app.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.vector.ImageVector

data class CardioSession(
    val id: String,
    val name: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val difficulty: String,
    val benefits: String,
    val steps: List<String>
)

@Composable
fun YogaZumbaScreen(
    viewModel: MainViewModel
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var activeSessionForPlayer by remember { mutableStateOf<CardioSession?>(null) }
    var isSessionPlaying by remember { mutableStateOf(false) }
    var sessionTimeRemaining by remember { mutableStateOf(0) }

    val yogaSessions = listOf(
        CardioSession("y1", "Morning Flexibility Flow", 15, 80, "Easy", "Improves hamstring range and wakes up joints", listOf("Child's Pose (2 mins)", "Cat-Cow Stretch (3 mins)", "Downward Facing Dog (3 mins)", "Cobra Stretch (3 mins)", "Warrior Pose (4 mins)")),
        CardioSession("y2", "Back Pain Relief Yoga", 10, 60, "Easy", "Decompresses spinal lumbar disc pressure", listOf("Knee-to-Chest Stretch (2 mins)", "Spinal Twist (3 mins)", "Bridge Pose (2 mins)", "Cat-Cow (3 mins)")),
        CardioSession("y3", "Rest-Day Deep Muscle Recovery", 20, 110, "Medium", "Optimal stretching to recover active muscle tissues", listOf("Pigeon Pose (4 mins per side)", "Butterfly Stretch (3 mins)", "Hamstring Stretch (4 mins)", "Corpse Savasana (4 mins)"))
    )

    val zumbaSessions = listOf(
        CardioSession("z1", "Beginner Zumba Beat Dance", 15, 160, "Easy", "Fat-burning Latin beats with quick steps", listOf("Basic Merengue march (3 mins)", "Reggaeton side-step bounce (4 mins)", "Salsa forward hip step (4 mins)", "Cooldown wave stretch (4 mins)")),
        CardioSession("z2", "Latin Salsa Cardio Blast", 20, 220, "Medium", "High-energy salsa footwork targeting core fat loss", listOf("Salsa quick shuffle (5 mins)", "Cumbia hip slide (5 mins)", "Cha-Cha-Cha step (5 mins)", "Cooldown (5 mins)")),
        CardioSession("z3", "10-Minute Rapid Beat Burn", 10, 120, "Hard", "Fast-tempo HIIT dance cardio to burn maximum calories", listOf("High knee bounce (3 mins)", "Zumba hip roll drops (3 mins)", "Fast salsa shuffle (2 mins)", "Cooldown (2 mins)"))
    )

    // Playback Timer ticks
    LaunchedEffect(isSessionPlaying) {
        if (isSessionPlaying) {
            while (sessionTimeRemaining > 0) {
                delay(1000)
                sessionTimeRemaining -= 1
            }
            isSessionPlaying = false
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

        Text(
            text = "Yoga & Dance Studio",
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Active recovery yoga & cardio dance zumba",
            style = Typography.bodyMedium,
            color = TextGray,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Premium 3D Yoga/Zumba Illustration Block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .threeDTiltEffect()
                .padding(bottom = 6.dp, end = 6.dp)
        ) {
            // 3D background base
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = 6.dp, y = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                DarkGold.copy(alpha = 0.8f),
                                PrimaryBlack
                            )
                        )
                    )
                    .border(1.dp, GoldAccent.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            )

            // 3D image layer
            Image(
                painter = painterResource(id = com.echofit.app.R.drawable.exercise_zumba_yoga_3d),
                contentDescription = "3D Zumba & Yoga Flow",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, BorderGray, RoundedCornerShape(16.dp))
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Yoga Card Section
        CardHeader(title = "BEGINNER-FRIENDLY YOGA SESSIONS", icon = Icons.Default.SelfImprovement, tint = SuccessGreen)
        Spacer(modifier = Modifier.height(8.dp))

        yogaSessions.forEach { session ->
            CardioSessionRow(session = session) {
                activeSessionForPlayer = session
                sessionTimeRemaining = session.durationMinutes * 60
                isSessionPlaying = false
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Zumba Card Section
        CardHeader(title = "MUSIC-BASED CARDIO ZUMBA", icon = Icons.Default.MusicNote, tint = ProteinPurple)
        Spacer(modifier = Modifier.height(8.dp))

        zumbaSessions.forEach { session ->
            CardioSessionRow(session = session) {
                activeSessionForPlayer = session
                sessionTimeRemaining = session.durationMinutes * 60
                isSessionPlaying = false
            }
        }

        Spacer(modifier = Modifier.height(72.dp))
    }

    // Media Player Simulation Overlay Dialog
    activeSessionForPlayer?.let { session ->
        AlertDialog(
            onDismissRequest = {
                activeSessionForPlayer = null
                isSessionPlaying = false
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (session.id.startsWith("y")) Icons.Default.SelfImprovement else Icons.Default.MusicNote,
                        contentDescription = "Cardio",
                        tint = GoldAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = session.name, style = Typography.titleMedium, color = TextWhite)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Benefits: ${session.benefits}", style = Typography.bodyMedium, color = TextGray)
                    Text(text = "Estimated Burn: ${session.caloriesBurned} kcal", style = Typography.bodyMedium, color = GoldAccent, fontWeight = FontWeight.Bold)
                    
                    Divider(color = BorderGray, modifier = Modifier.padding(vertical = 4.dp))

                    // Player Controls
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryBlack)
                            .border(1.dp, BorderGray, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSessionPlaying) {
                            BackgroundVideoPlayer(
                                videoResId = com.echofit.app.R.raw.studio_yoga_video,
                                isMuted = true,
                                modifier = Modifier.fillMaxSize()
                            )
                            // Translucent scrim to keep control overlay perfectly legible
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.5f))
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = String.format("%02d:%02d", sessionTimeRemaining / 60, sessionTimeRemaining % 60),
                                style = Typography.titleLarge,
                                fontSize = 32.sp,
                                color = GoldAccent,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { isSessionPlaying = !isSessionPlaying },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(GoldAccent)
                                        .size(42.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play",
                                        tint = PrimaryBlack
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (isSessionPlaying) "STUDIO PLAYER STREAMING" else "PAUSED",
                                    style = Typography.labelMedium,
                                    color = if (isSessionPlaying) SuccessGreen else TextGray
                                )
                            }
                        }
                    }

                    Divider(color = BorderGray, modifier = Modifier.padding(vertical = 4.dp))

                    Text(text = "Follow Routine Steps:", style = Typography.labelLarge, color = GoldAccent, fontWeight = FontWeight.Bold)
                    session.steps.forEach { step ->
                        Text(text = "• $step", style = Typography.bodyMedium, color = TextWhite)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.markWorkoutCompleted() // Log recovery cardio workout done
                        activeSessionForPlayer = null
                        isSessionPlaying = false
                    }
                ) {
                    Text("MARK SESSION COMPLETED", color = GoldAccent, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        activeSessionForPlayer = null
                        isSessionPlaying = false
                    }
                ) {
                    Text("CLOSE", color = ErrorRed)
                }
            },
            containerColor = SurfaceDark
        )
    }
}

@Composable
fun CardHeader(title: String, icon: ImageVector, tint: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = tint, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, style = Typography.labelLarge, color = GoldAccent, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CardioSessionRow(
    session: CardioSession,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = session.name, style = Typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(
                text = "${session.durationMinutes} mins | Burn: ${session.caloriesBurned} kcal | Diff: ${session.difficulty}",
                style = Typography.labelMedium,
                color = TextGray
            )
        }
        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = GoldAccent)
    }
}
