package com.echofit.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echofit.app.domain.model.MealLog
import com.echofit.app.ui.components.GlassmorphicCard
import com.echofit.app.ui.components.PremiumButton
import com.echofit.app.ui.components.threeDTiltEffect
import androidx.compose.ui.graphics.Brush
import com.echofit.app.ui.theme.*
import com.echofit.app.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietPlanScreen(
    viewModel: MainViewModel
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val profile by viewModel.userProfile.collectAsState()
    val dietPlan by viewModel.currentDietPlan.collectAsState()
    val mealsLogged by viewModel.meals.collectAsState()
    val apiKey by viewModel.geminiApiKey.collectAsState()

    var showCameraDialog by remember { mutableStateOf(false) }
    var isAnalyzing by remember { mutableStateOf(false) }
    
    // Correction Dialog State
    var showCorrectionDialog by remember { mutableStateOf(false) }
    var isManualAdd by remember { mutableStateOf(false) }
    var foodNameInput by remember { mutableStateOf("") }
    var portionInput by remember { mutableStateOf("") }
    var caloriesInput by remember { mutableStateOf("") }
    var proteinInput by remember { mutableStateOf("") }
    var carbsInput by remember { mutableStateOf("") }
    var fatsInput by remember { mutableStateOf("") }
    var fiberInput by remember { mutableStateOf("") }

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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "AI Diet Planner",
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Text(
                    text = "Diet Type: ${profile?.dietType ?: "Vegetarian"} | Budget: ${profile?.budgetLevel ?: "Low"}",
                    style = Typography.bodyMedium,
                    color = TextGray
                )
            }

            // Quick Action Triggers (Camera scan and manual add)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = { showCameraDialog = true },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(GoldAccent)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Scan Meal",
                        tint = PrimaryBlack
                    )
                }

                IconButton(
                    onClick = {
                        // Reset all input fields for fresh manual entry
                        foodNameInput = ""
                        portionInput = "1 serving"
                        caloriesInput = "350"
                        proteinInput = "20"
                        carbsInput = "40"
                        fatsInput = "10"
                        fiberInput = "5"
                        isManualAdd = true
                        showCorrectionDialog = true
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(GoldAccent)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Meal Manually",
                        tint = PrimaryBlack
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Macro Targets Overview Card
        dietPlan?.let { plan ->
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "DAILY NUTRITION BUDGETS",
                    style = Typography.labelLarge,
                    color = GoldAccent,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(16.dp))

                val loggedCal = mealsLogged.sumOf { it.calories }
                val loggedPro = mealsLogged.sumOf { it.protein }
                val loggedCarb = mealsLogged.sumOf { it.carbs }
                val loggedFat = mealsLogged.sumOf { it.fats }

                MacroProgressBar("Calories", loggedCal, plan.targetCalories, GoldAccent)
                Spacer(modifier = Modifier.height(10.dp))
                MacroProgressBar("Protein (g)", loggedPro, plan.targetProtein, ProteinPurple)
                Spacer(modifier = Modifier.height(10.dp))
                MacroProgressBar("Carbs (g)", loggedCarb, plan.targetCarbs, CarbOrange)
                Spacer(modifier = Modifier.height(10.dp))
                MacroProgressBar("Fats (g)", loggedFat, plan.targetFats, FatYellow)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Premium 3D Diet Illustration Block
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .threeDTiltEffect()
                    .padding(bottom = 6.dp, end = 6.dp)
            ) {
                // 3D background extrusion base
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
                    painter = painterResource(id = com.echofit.app.R.drawable.diet_healthy_meal_3d),
                    contentDescription = "3D Healthy Meal",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, BorderGray, RoundedCornerShape(16.dp))
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // AI Diet Meal Breakdown
            Text(
                text = "SUGGESTED MEAL SCHEDULE",
                style = Typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            plan.meals.forEach { meal ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .threeDTiltEffect()
                        .padding(bottom = 6.dp, end = 6.dp)
                ) {
                    // 3D beveled extrusion base
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .offset(x = 6.dp, y = 6.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        DarkGold.copy(alpha = 0.8f),
                                        PrimaryBlack
                                    )
                                )
                            )
                            .border(1.dp, GoldAccent.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderGray.copy(alpha = 0.8f), RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = meal.mealName,
                                style = Typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                            Text(
                                text = meal.time,
                                style = Typography.labelMedium,
                                color = TextGray
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        meal.items.forEach { food ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.logMeal(
                                            name = food.name,
                                            portion = food.portion,
                                            calories = food.calories,
                                            protein = food.protein,
                                            carbs = food.carbs,
                                            fats = food.fats,
                                            fiber = food.fiber
                                        )
                                    }
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "• ${food.name} (${food.portion})",
                                    style = Typography.bodyMedium,
                                    color = TextWhite
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${food.calories} kcal | ${food.protein}g Pro",
                                        style = Typography.labelMedium,
                                        color = TextGray
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Log",
                                        tint = GoldAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logged Meals History List
        Text(
            text = "LOGGED MEALS TODAY",
            style = Typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (mealsLogged.isEmpty()) {
            Text(
                text = "No meals logged today yet. Tap camera to scan or select from suggestions!",
                style = Typography.bodyMedium,
                color = TextGray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )
        } else {
            mealsLogged.forEach { log ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceDark)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = log.foodName, style = Typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text(
                            text = "${log.portionSize} | ${log.calories} kcal | ${log.protein}g P | ${log.carbs}g C | ${log.fats}g F",
                            style = Typography.labelMedium,
                            color = TextGray
                        )
                    }
                    IconButton(onClick = { viewModel.removeMeal(log) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(72.dp))
    }

    // Camera Mock dialog
    if (showCameraDialog) {
        AlertDialog(
            onDismissRequest = { showCameraDialog = false },
            title = { Text("Capture / Upload Meal Photo", color = TextWhite) },
            text = {
                Text(
                    "Echo AI will automatically analyze portions and calculate calories using computer vision models.",
                    color = TextGray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCameraDialog = false
                        isAnalyzing = true
                        
                        // Mock vision analysis delay
                        scope.launch {
                            kotlinx.coroutines.delay(2000)
                            isAnalyzing = false
                            
                            // Load mock outputs to correction dialog
                            val mockLog = viewModel.meals.value.firstOrNull() ?: MealLog(
                                foodName = "Chana Masala with 2 Roti",
                                portionSize = "1 Plate",
                                calories = 410,
                                protein = 18,
                                carbs = 58,
                                fats = 8,
                                fiber = 11
                            )
                            
                            foodNameInput = mockLog.foodName
                            portionInput = mockLog.portionSize
                            caloriesInput = mockLog.calories.toString()
                            proteinInput = mockLog.protein.toString()
                            carbsInput = mockLog.carbs.toString()
                            fatsInput = mockLog.fats.toString()
                            fiberInput = mockLog.fiber.toString()
                            
                            showCorrectionDialog = true
                        }
                    }
                ) {
                    Text("SIMULATE CAPTURE", color = GoldAccent)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCameraDialog = false }) {
                    Text("CANCEL", color = TextGray)
                }
            },
            containerColor = SurfaceDark
        )
    }

    // Vision Loading Indicator
    if (isAnalyzing) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryBlack.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = GoldAccent)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Echo AI analyzing calories...", style = Typography.bodyLarge, color = GoldAccent)
            }
        }
    }

    // Manual Correction / Add Dialog
    if (showCorrectionDialog) {
        AlertDialog(
            onDismissRequest = { 
                showCorrectionDialog = false 
                isManualAdd = false
            },
            title = {
                Text(
                    text = if (isManualAdd) "Add Meal Manually" else "Echo AI Meal Estimation",
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
                    Text(
                        text = if (isManualAdd) 
                            "Enter your meal items and macro parameters manually to add to your daily diet logs:" 
                            else "AI estimated your meal macros. Please correct manually if inaccurate before saving:",
                        style = Typography.bodyMedium,
                        color = TextGray
                    )

                    OutlinedTextField(value = foodNameInput, onValueChange = { foodNameInput = it }, label = { Text("Meal Name (e.g. Scrambled Eggs)", color = TextGray) })
                    OutlinedTextField(value = portionInput, onValueChange = { portionInput = it }, label = { Text("Portion (e.g. 3 eggs)", color = TextGray) })
                    OutlinedTextField(value = caloriesInput, onValueChange = { caloriesInput = it }, label = { Text("Calories (kcal)", color = TextGray) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = proteinInput, onValueChange = { proteinInput = it }, label = { Text("Protein (g)", color = TextGray) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = carbsInput, onValueChange = { carbsInput = it }, label = { Text("Carbs (g)", color = TextGray) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = fatsInput, onValueChange = { fatsInput = it }, label = { Text("Fats (g)", color = TextGray) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = fiberInput, onValueChange = { fiberInput = it }, label = { Text("Fiber (g)", color = TextGray) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.logMeal(
                            name = foodNameInput.ifBlank { "Custom Meal" },
                            portion = portionInput.ifBlank { "1 serving" },
                            calories = caloriesInput.toIntOrNull() ?: 300,
                            protein = proteinInput.toIntOrNull() ?: 15,
                            carbs = carbsInput.toIntOrNull() ?: 45,
                            fats = fatsInput.toIntOrNull() ?: 8,
                            fiber = fiberInput.toIntOrNull() ?: 5,
                            uri = if (isManualAdd) "" else "content://mock_vision_uri"
                        )
                        showCorrectionDialog = false
                        isManualAdd = false
                    }
                ) {
                    Text("SAVE TO MEAL LOG", color = GoldAccent, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showCorrectionDialog = false 
                        isManualAdd = false
                    }
                ) {
                    Text(text = if (isManualAdd) "CANCEL" else "REJECT ESTIMATE", color = ErrorRed)
                }
            },
            containerColor = SurfaceDark
        )
    }
}

@Composable
fun MacroProgressBar(
    label: String,
    current: Int,
    target: Int,
    color: Color
) {
    val progress = if (target > 0) current.toFloat() / target else 0f
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = Typography.labelMedium, color = TextGray)
            Text(text = "$current / $target", style = Typography.labelMedium, color = TextWhite, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = progress.coerceIn(0f, 1f),
            color = color,
            trackColor = Color(0x33FFFFFF),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}
