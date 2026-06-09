package com.echofit.app.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import java.io.File
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.echofit.app.domain.ai.EchoAIEngine
import com.echofit.app.domain.model.ProgressPhoto
import com.echofit.app.ui.components.GlassmorphicCard
import com.echofit.app.ui.components.PremiumButton
import com.echofit.app.ui.components.threeDTiltEffect
import com.echofit.app.ui.components.ThreeDText
import androidx.compose.ui.graphics.Brush
import com.echofit.app.ui.theme.*
import com.echofit.app.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
private fun getBytesFromUri(context: Context, uriString: String): ByteArray? {
    return try {
        val uri = android.net.Uri.parse(uriString)
        context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun createTempImageFile(context: Context): File {
    val timeStamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(java.util.Date())
    val imageFileName = "JPEG_${timeStamp}_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        context.cacheDir
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressPhotoScreen(
    viewModel: MainViewModel
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val weightList by viewModel.weightLogs.collectAsState()
    val photoList by viewModel.progressPhotos.collectAsState()
    val attendanceList by viewModel.attendanceLogs.collectAsState()
    val apiKey by viewModel.geminiApiKey.collectAsState()

    var capturedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var tempImageFile by remember { mutableStateOf<File?>(null) }

    var weightInput by remember { mutableStateOf("") }
    var activePhotoUploadPosition by remember { mutableStateOf<String?>(null) } // "Front", "Side", "Back"
    var showPhotoSourceDialog by remember { mutableStateOf(false) }
    var showCamera by remember { mutableStateOf(false) }

    // Gallery picker launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        val pos = activePhotoUploadPosition ?: "Front"
        if (uri != null) {
            // Copy the picked image to app-local storage so Coil can load it reliably
            try {
                val file = createTempImageFile(context)
                context.contentResolver.openInputStream(uri)?.use { input ->
                    java.io.FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                val localUri = FileProvider.getUriForFile(context, "com.echofit.app.fileprovider", file)
                viewModel.logProgressPhoto(pos, localUri.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                // Fallback: use the content URI directly
                viewModel.logProgressPhoto(pos, uri.toString())
            }
        }
    }

    // Camera permission launcher - opens CameraScreen on grant
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showCamera = true
        } else {
            android.widget.Toast.makeText(context, "Camera permission is required to capture progress photos.", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    val chartWeights = remember(weightList) {
        if (weightList.size >= 2) {
            weightList.takeLast(6).map { it.weight }
        } else {
            listOf(74.5, 73.8, 73.2, 72.5, 72.0, 71.4) // High quality visual mock
        }
    }

    
    // AI Physique Analysis State
    var isAnalyzingPhysique by remember { mutableStateOf(false) }
    var activePhysiqueReportText by remember { mutableStateOf<String?>(null) }

    // --- Full-screen CameraX Live Preview ---
    if (showCamera) {
        CameraScreen(
            position = activePhotoUploadPosition ?: "Front",
            onPhotoCaptured = { uri ->
                val pos = activePhotoUploadPosition ?: "Front"
                viewModel.logProgressPhoto(pos, uri.toString())
                showCamera = false
            },
            onDismiss = {
                showCamera = false
            }
        )
        return  // Don't render the rest of the screen behind the camera
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                ThreeDText(
                    text = "PROGRESS",
                    fontSize = 28.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Track weight, attendance, and secure photos",
                    style = Typography.bodyMedium,
                    color = TextGray
                )
            }
            Icon(imageVector = Icons.Default.Lock, contentDescription = "Secure", tint = GoldAccent)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Weight Logger Panel
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "LOG DAILY WEIGHT",
                style = Typography.labelLarge,
                color = GoldAccent,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    placeholder = { Text("Weight in kg", color = TextGray) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = GoldAccent,
                        unfocusedBorderColor = BorderGray,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = {
                        val wt = weightInput.toDoubleOrNull()
                        if (wt != null) {
                            viewModel.logWeight(wt)
                            weightInput = ""
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkBeige)
                        .size(52.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Last logged weight
            val lastWeight = weightList.lastOrNull()?.weight ?: 0.0
            if (lastWeight > 0.0) {
                Text(
                    text = "Current Weight: $lastWeight kg | Goal: ${viewModel.userProfile.value?.targetWeight ?: "70.0"} kg",
                    style = Typography.bodyMedium,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // WEEKLY PERFORMANCE REPORTS & ANALYTICS
        GlassmorphicCard(modifier = Modifier.fillMaxWidth(), isGlowing = true) {
            Text(
                text = "WEEKLY WEIGHT TREND",
                style = Typography.labelLarge,
                color = GoldAccent,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(12.dp))


            // Canvas drawing for weight progression
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryBlack.copy(alpha = 0.5f))
                    .padding(8.dp)
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    val maxW = (chartWeights.maxOrNull() ?: 80.0) + 1.0
                    val minW = (chartWeights.minOrNull() ?: 70.0) - 1.0
                    val diff = maxW - minW
                    
                    val points = chartWeights.mapIndexed { idx, w ->
                        val x = idx.toFloat() / (chartWeights.size - 1) * size.width
                        val y = size.height - ((w - minW) / diff * size.height).toFloat()
                        androidx.compose.ui.geometry.Offset(x, y)
                    }

                    // 1. Draw 3D shadow path offset
                    for (i in 0 until points.size - 1) {
                        drawLine(
                            color = Color.Black.copy(alpha = 0.5f),
                            start = points[i].copy(y = points[i].y + 6f),
                            end = points[i + 1].copy(y = points[i + 1].y + 6f),
                            strokeWidth = 10f,
                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                    }

                    // 2. Draw connecting volumetric lines
                    for (i in 0 until points.size - 1) {
                        drawLine(
                            color = GoldAccent,
                            start = points[i],
                            end = points[i + 1],
                            strokeWidth = 6f,
                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                        // specular highlight line
                        drawLine(
                            color = Color.White.copy(alpha = 0.4f),
                            start = points[i].copy(y = points[i].y - 1f),
                            end = points[i + 1].copy(y = points[i + 1].y - 1f),
                            strokeWidth = 2f,
                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                    }

                    // 3. Draw dots with 3D button bubble shading
                    points.forEach { pt ->
                        // Shadow
                        drawCircle(
                            color = Color.Black.copy(alpha = 0.6f),
                            radius = 12f,
                            center = pt.copy(y = pt.y + 4f)
                        )
                        // Bevel Base
                        drawCircle(
                            color = DarkGold,
                            radius = 10f,
                            center = pt
                        )
                        // Core Cap
                        drawCircle(
                            color = GoldAccent,
                            radius = 8f,
                            center = pt
                        )
                        // Refractive specular shine
                        drawCircle(
                            color = Color.White.copy(alpha = 0.7f),
                            radius = 2.5f,
                            center = androidx.compose.ui.geometry.Offset(pt.x - 3f, pt.y - 3f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Mon", style = Typography.labelMedium, color = TextGray)
                Text(text = "Tue", style = Typography.labelMedium, color = TextGray)
                Text(text = "Wed", style = Typography.labelMedium, color = TextGray)
                Text(text = "Thu", style = Typography.labelMedium, color = TextGray)
                Text(text = "Fri", style = Typography.labelMedium, color = TextGray)
                Text(text = "Sat/Sun", style = Typography.labelMedium, color = TextGray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // GAMIFICATION VAULT
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GAMIFICATION VAULT",
                    style = Typography.labelLarge,
                    color = GoldAccent,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Level 3 - Gym Warrior",
                    style = Typography.labelMedium,
                    color = LightGold,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // XP Progression bar
            val currentXP = attendanceList.sumOf { it.score } + 120
            val targetLevelXP = 500
            val progressXP = currentXP.toFloat() / targetLevelXP
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "XP Progress Tracker", style = Typography.labelMedium, color = TextGray)
                Text(text = "$currentXP / $targetLevelXP XP", style = Typography.labelMedium, color = TextWhite, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = progressXP.coerceIn(0f, 1f),
                color = OrangeGradEnd,
                trackColor = BorderGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(text = "Your Earned Badges:", style = Typography.labelMedium, color = TextGray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Protein Master Badge
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceDark)
                        .border(1.dp, ProteinPurple.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💪", fontSize = 20.sp)
                        Text("Pro Master", style = Typography.labelMedium, color = TextWhite, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text("Target Met", style = Typography.labelMedium, color = ProteinPurple, fontSize = 9.sp)
                    }
                }

                // Hydration Hero Badge
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceDark)
                        .border(1.dp, HydrateBlue.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💧", fontSize = 20.sp)
                        Text("Water Hero", style = Typography.labelMedium, color = TextWhite, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text("100% Hydrated", style = Typography.labelMedium, color = HydrateBlue, fontSize = 9.sp)
                    }
                }

                // Streak Trophy Badge
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceDark)
                        .border(1.dp, StreakFire.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🏆", fontSize = 20.sp)
                        Text("Consistency", style = Typography.labelMedium, color = TextWhite, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text("7-Day Streak", style = Typography.labelMedium, color = StreakFire, fontSize = 9.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // AI WEEKLY PERFORMANCE REPORT CARD
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "ECHO'S WEEKLY TRANSFORMATION REPORT",
                style = Typography.labelLarge,
                color = GoldAccent,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Analytical Summary:",
                style = Typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = LightGold
            )
            Spacer(modifier = Modifier.height(4.dp))
            
            val totalScore = attendanceList.sumOf { it.score }
            val avgScore = if (attendanceList.isNotEmpty()) totalScore / attendanceList.size else 85
            
            Text(
                text = "• Weight change: Down ${String.format("%.1f", 74.5 - chartWeights.last())} kg from start.\n" +
                       "• Hydration pacing: 92% targets locked.\n" +
                       "• Routine compliance score: $avgScore% consistency registered.\n" +
                       "• Protein targets: Hitting target average of 115g/day.",
                style = Typography.bodyMedium,
                color = TextWhite
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = BorderGray)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Echo AI Weekly Advice:",
                style = Typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
            Text(
                text = "• \"Belly fat visual change is looking solid! Stick to your ${viewModel.goalAnalysis.value?.calorieTarget ?: 1800} kcal deficit. Posture could be slightly more rolled back, keep stretching on those Sundays!\"",
                style = Typography.bodyMedium,
                color = TextWhite,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Secure Progress Photo Slots (Front, Side, Back)
        Text(
            text = "PROGRESS PHOTO CHECKINS",
            style = Typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Front", "Side", "Back").forEach { pos ->
                val hasPhoto = photoList.find { it.position == pos }
                
                Box(modifier = Modifier.weight(1f)) {
                    GlassmorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clickable {
                                activePhotoUploadPosition = pos
                                showPhotoSourceDialog = true
                            },
                        borderColor = if (hasPhoto != null) OrangeGradEnd else BorderGray
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (hasPhoto != null) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    AsyncImage(
                                        model = hasPhoto.photoUri,
                                        contentDescription = "$pos view",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    // Delete overlay button in top-right
                                    IconButton(
                                        onClick = { 
                                            viewModel.deletePhoto(hasPhoto)
                                        },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp)
                                            .size(28.dp)
                                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = ErrorRed,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    // Label indicator at the bottom
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .fillMaxWidth()
                                            .background(Color.Black.copy(alpha = 0.5f))
                                            .padding(vertical = 2.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(pos, style = Typography.labelMedium, color = TextWhite, fontSize = 10.sp)
                                    }
                                }
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Add", tint = TextGray)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(pos, style = Typography.labelMedium, color = TextGray)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AI PHYSIQUE ANALYSIS BUTTON
        val activePhotosCount = photoList.size
        PremiumButton(
            text = "ANALYZE PHYSIQUE TRANSFORMATION",
            onClick = {
                val beforePhoto = photoList.getOrNull(0)
                val afterPhoto = photoList.getOrNull(1)
                if (beforePhoto != null && afterPhoto != null) {
                    val beforeBytes = getBytesFromUri(context, beforePhoto.photoUri)
                    val afterBytes = getBytesFromUri(context, afterPhoto.photoUri)
                    if (beforeBytes != null && afterBytes != null) {
                        isAnalyzingPhysique = true
                        viewModel.analyzeBodyProgress(beforeBytes, afterBytes) { report ->
                            isAnalyzingPhysique = false
                            activePhysiqueReportText = report
                        }
                    } else {
                        android.widget.Toast.makeText(context, "Error reading image files.", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            },
            enabled = activePhotosCount >= 2,
            buttonBrush = if (activePhotosCount >= 2) Brush.linearGradient(listOf(EmeraldGradStart, EmeraldGradEnd)) else null
        )

        activePhysiqueReportText?.let { report ->
            Spacer(modifier = Modifier.height(16.dp))
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "AI PHYSIQUE COMPARATIVE ASSESSMENT",
                    style = Typography.labelLarge,
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = report,
                    style = Typography.bodyMedium,
                    color = TextWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Daily Attendance Registry
        Text(
            text = "DAILY CHECK-IN ATTENDANCE",
            style = Typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (attendanceList.isEmpty()) {
            Text(
                text = "Welcome to EchoFit! Complete today's checklist on Dashboard to log your first attendance day.",
                style = Typography.bodyMedium,
                color = TextGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            )
        } else {
            attendanceList.forEach { attend ->
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = BorderGray
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Date: ${attend.dateString}", style = Typography.bodyMedium, fontWeight = FontWeight.Bold, color = TextWhite)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Diet: ${if (attend.isDietDone) "✓" else "✗"} | Workout: ${if (attend.isWorkoutDone) "✓" else "✗"} | Water: ${if (attend.isWaterDone) "✓" else "✗"}",
                                style = Typography.labelMedium,
                                color = TextGray
                            )
                        }

                        // Score Circle Badge
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(if (attend.score >= 70) SuccessGreen else if (attend.score >= 40) FatYellow else ErrorRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${attend.score}%",
                                color = Color.White,
                                style = Typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(72.dp))
    }

    // Photo Source Choice Dialog
    if (showPhotoSourceDialog) {
        AlertDialog(
            onDismissRequest = { showPhotoSourceDialog = false },
            title = {
                Text(
                    text = "Add Progress Photo",
                    style = Typography.titleMedium,
                    color = GoldAccent
                )
            },
            text = {
                Column {
                    Text(
                        text = "Choose a source for your ${activePhotoUploadPosition ?: ""} view photo:",
                        style = Typography.bodyMedium,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Camera Option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceDark)
                            .clickable {
                                showPhotoSourceDialog = false
                                val hasCameraPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasCameraPermission) {
                                    showCamera = true
                                } else {
                                    requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                }
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            tint = GoldAccent,
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text("Take Photo", style = Typography.bodyLarge, color = TextWhite, fontWeight = FontWeight.Bold)
                            Text("Use camera to capture", style = Typography.labelMedium, color = TextGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Gallery Option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceDark)
                            .clickable {
                                showPhotoSourceDialog = false
                                galleryLauncher.launch("image/*")
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Gallery",
                            tint = GoldAccent,
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text("Choose from Gallery", style = Typography.bodyLarge, color = TextWhite, fontWeight = FontWeight.Bold)
                            Text("Pick an existing image", style = Typography.labelMedium, color = TextGray)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showPhotoSourceDialog = false }) {
                    Text("CANCEL", color = TextGray)
                }
            },
            containerColor = PrimaryBlack.copy(alpha = 0.95f)
        )
    }

    // Comparative Loading Overlay
    if (isAnalyzingPhysique) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryBlack.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = GoldAccent)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Echo Comparing Progress Photos...", style = Typography.bodyLarge, color = GoldAccent)
            }
        }
    }
}
