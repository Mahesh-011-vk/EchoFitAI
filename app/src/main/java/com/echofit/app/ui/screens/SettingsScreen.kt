package com.echofit.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echofit.app.ui.components.GlassmorphicCard
import com.echofit.app.ui.components.PremiumButton
import com.echofit.app.ui.components.ThreeDText
import com.echofit.app.ui.theme.*
import com.echofit.app.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val apiKey by viewModel.geminiApiKey.collectAsState()
    val remindersList by viewModel.reminders.collectAsState()

    var keyInput by remember(apiKey) { mutableStateOf(apiKey) }
    var isSyncingCloud by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlack.copy(alpha = 0f))
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        ThreeDText(
            text = "SETTINGS",
            fontSize = 28.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Manage your reminders, keys, and data logs",
            style = Typography.bodyMedium,
            color = TextGray,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // API Key Section
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Key, contentDescription = "Key", tint = GoldAccent)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "GEMINI COCHING ENGINE KEY",
                    style = Typography.labelLarge,
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = keyInput,
                onValueChange = {
                    keyInput = it
                    viewModel.geminiApiKey.value = it
                },
                placeholder = { Text("Enter Google Gemini API Key", color = TextGray) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = GoldAccent,
                    unfocusedBorderColor = BorderGray,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Used to analyze food/photos. Leave blank to use smart offline mock generators.",
                style = Typography.labelMedium,
                color = TextGray
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Daily Reminder Alerts Toggles
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "Reminders", tint = GoldAccent)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "WORKMANAGER REMINDERS",
                    style = Typography.labelLarge,
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            remindersList.forEach { rem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = rem.name, style = Typography.bodyLarge, color = TextWhite)
                        Text(text = "Scheduled at ${rem.time}", style = Typography.labelMedium, color = TextGray)
                    }
                    Switch(
                        checked = rem.isEnabled,
                        onCheckedChange = { viewModel.updateReminderSetting(rem.id, it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = SuccessGreen,
                            uncheckedThumbColor = TextGray,
                            uncheckedTrackColor = BorderGray
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Cloud Database Sync Control
        GlassmorphicCard(modifier = Modifier.fillMaxWidth(), isGlowing = true) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.CloudSync, contentDescription = "Sync", tint = GoldAccent)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "FIREBASE CLOUD DATABASE SYNC",
                    style = Typography.labelLarge,
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Automatically synchronizes local Room DB datasets with secure Cloud Firestore backends.",
                style = Typography.bodyMedium,
                color = TextWhite
            )
            Spacer(modifier = Modifier.height(16.dp))

            val syncBrush = Brush.linearGradient(listOf(OrangeGradStart, OrangeGradEnd))
            PremiumButton(
                text = if (isSyncingCloud) "UPLOADING TO FIRESTORE..." else "FORCE FIREBASE SYNC",
                onClick = {
                    isSyncingCloud = true
                    scope.launch {
                        viewModel.syncLocalDataToCloud()
                        kotlinx.coroutines.delay(2000)
                        isSyncingCloud = false
                        Toast.makeText(context, "Cloud sync complete!", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !isSyncingCloud,
                buttonBrush = if (!isSyncingCloud) syncBrush else null
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Medical/AI Disclaimer Card
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Info", tint = ErrorRed)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AI MEDICAL DISCLAIMER",
                    style = Typography.labelLarge,
                    color = ErrorRed,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "AI diet plan generation, caloric vision scanning, and physical posture checks are calculated estimates and must not replace professional clinical therapy or customized medical diagnostics. Use responsibly.",
                style = Typography.labelMedium,
                color = TextGray,
                textAlign = TextAlign.Start
            )
        }

        Spacer(modifier = Modifier.height(72.dp))
    }
}
