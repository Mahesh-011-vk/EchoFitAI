package com.echofit.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echofit.app.domain.model.GoalAnalysis
import com.echofit.app.domain.model.UserProfile
import com.echofit.app.ui.components.GlassmorphicCard
import com.echofit.app.ui.components.PremiumButton
import com.echofit.app.ui.theme.*
import com.echofit.app.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    viewModel: MainViewModel,
    onSetupComplete: () -> Unit
) {
    val scrollState = rememberScrollState()
    val goalAnalysis by viewModel.goalAnalysis.collectAsState()

    var name by remember { mutableStateOf("") }
    var ageStr by remember { mutableStateOf("25") }
    var heightStr by remember { mutableStateOf("175") }
    var weightStr by remember { mutableStateOf("70") }
    var targetWeightStr by remember { mutableStateOf("70") }
    
    // Multi-choice parameters
    var gender by remember { mutableStateOf("Male") }
    var fitnessGoal by remember { mutableStateOf("Fat Loss") }
    var budgetLevel by remember { mutableStateOf("Medium") }
    var dietType by remember { mutableStateOf("Vegetarian") }
    var gymAccess by remember { mutableStateOf("Both") }
    var experienceLevel by remember { mutableStateOf("Beginner") }
    var preferredRestDay by remember { mutableStateOf("Sunday") }
    var workoutTime by remember { mutableStateOf("07:00 PM") }
    var injuries by remember { mutableStateOf("None") }

    var step by remember { mutableStateOf(1) } // 1: Questions, 2: Goal Analysis Summary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlack.copy(alpha = 0f))
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "ECHOFIT",
            style = Typography.titleLarge,
            color = GoldAccent,
            fontWeight = FontWeight.Black,
            fontSize = 32.sp
        )
        Text(
            text = "Personalize Your AI Coach",
            style = Typography.bodyMedium,
            color = TextGray
        )
        
        Spacer(modifier = Modifier.height(20.dp))

        if (step == 1) {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "TELL ECHO ABOUT YOURSELF",
                    style = Typography.titleMedium,
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name", color = TextGray) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = GoldAccent,
                        unfocusedBorderColor = BorderGray,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = ageStr,
                        onValueChange = { ageStr = it },
                        label = { Text("Age", color = TextGray) },
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
                    OutlinedTextField(
                        value = heightStr,
                        onValueChange = { heightStr = it },
                        label = { Text("Height (cm)", color = TextGray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = GoldAccent,
                            unfocusedBorderColor = BorderGray,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = weightStr,
                        onValueChange = { weightStr = it },
                        label = { Text("Weight (kg)", color = TextGray) },
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
                    OutlinedTextField(
                        value = targetWeightStr,
                        onValueChange = { targetWeightStr = it },
                        label = { Text("Target (kg)", color = TextGray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = GoldAccent,
                            unfocusedBorderColor = BorderGray,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Single Selection Lists
                DropdownOptionSelector("Gender", gender, listOf("Male", "Female")) { gender = it }
                Spacer(modifier = Modifier.height(8.dp))
                DropdownOptionSelector("Diet Type", dietType, listOf("Vegetarian", "Eggetarian", "Non-Vegetarian", "Vegan")) { dietType = it }
                Spacer(modifier = Modifier.height(8.dp))
                DropdownOptionSelector("Goal", fitnessGoal, listOf("Fat Loss", "Muscle Gain", "Body Recomposition", "General Fitness")) { fitnessGoal = it }
                Spacer(modifier = Modifier.height(8.dp))
                DropdownOptionSelector("Monthly Budget Level", budgetLevel, listOf("Low", "Medium", "High")) { budgetLevel = it }
                Spacer(modifier = Modifier.height(8.dp))
                DropdownOptionSelector("Training Access", gymAccess, listOf("Gym", "Home", "Both")) { gymAccess = it }
                Spacer(modifier = Modifier.height(8.dp))
                DropdownOptionSelector("Experience Level", experienceLevel, listOf("Beginner", "Intermediate", "Advanced")) { experienceLevel = it }
                Spacer(modifier = Modifier.height(8.dp))
                DropdownOptionSelector("Preferred Rest Day", preferredRestDay, listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")) { preferredRestDay = it }
                Spacer(modifier = Modifier.height(8.dp))
                DropdownOptionSelector("Preferred Workout Time", workoutTime, listOf("06:00 AM", "08:00 AM", "12:00 PM", "05:00 PM", "07:00 PM", "08:00 PM")) { workoutTime = it }
                Spacer(modifier = Modifier.height(8.dp))
                DropdownOptionSelector("Injuries / Limitations", injuries, listOf("None", "Shoulder discomfort", "Knee sensitivity", "Back pain")) { injuries = it }

                Spacer(modifier = Modifier.height(24.dp))

                PremiumButton(
                    text = "CALCULATE AI GOALS",
                    onClick = {
                        val age = ageStr.toIntOrNull() ?: 25
                        val height = heightStr.toDoubleOrNull() ?: 175.0
                        val weight = weightStr.toDoubleOrNull() ?: 70.0
                        val target = targetWeightStr.toDoubleOrNull() ?: 70.0

                        val profile = UserProfile(
                            name = name.ifBlank { "Champion" },
                            age = age,
                            gender = gender,
                            height = height,
                            weight = weight,
                            targetWeight = target,
                            fitnessGoal = fitnessGoal,
                            budgetLevel = budgetLevel,
                            dietType = dietType,
                            gymAccess = gymAccess,
                            experienceLevel = experienceLevel,
                            preferredRestDay = preferredRestDay,
                            workoutTime = workoutTime,
                            injuriesOrLimitations = injuries
                        )
                        viewModel.saveProfile(profile)
                        step = 2
                    },
                    enabled = name.isNotBlank()
                )
            }
        } else if (step == 2) {
            goalAnalysis?.let { analysis ->
                GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "ECHO'S FITNESS ASSESSMENT",
                        style = Typography.titleMedium,
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    StatRow("Your BMI", "${analysis.bmi} (${analysis.bmiCategory})")
                    StatRow("Visual Body Fat %", "${analysis.estimatedBodyFat}%")
                    StatRow("Daily Calorie Target", "${analysis.calorieTarget} kcal")
                    StatRow("Daily Protein Target", "${analysis.proteinTarget}g")
                    StatRow("Hydration Goal", "${analysis.waterIntakeTargetLiters} Liters")
                    StatRow("Suggested Midpoint Weight", "${analysis.suggestedWeightGoal} kg")
                    StatRow("Estimated Fitness Score", "${analysis.fitnessScore} / 100")

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = BorderGray)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "YOUR 30-DAY MISSION",
                        style = Typography.labelLarge,
                        color = GoldAccent,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = analysis.thirtyDayMission,
                        style = Typography.bodyMedium,
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    PremiumButton(
                        text = "ENTER WORKOUT DASHBOARD",
                        onClick = {
                            onSetupComplete()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = Typography.bodyMedium, color = TextGray)
        Text(text = value, style = Typography.bodyLarge, color = TextWhite, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownOptionSelector(
    label: String,
    selectedValue: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = {},
                readOnly = true,
                label = { Text(label, color = TextGray) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = GoldAccent,
                    unfocusedBorderColor = BorderGray,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(SurfaceDark)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, color = TextWhite) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
