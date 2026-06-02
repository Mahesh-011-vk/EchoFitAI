package com.echofit.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echofit.app.domain.model.ChatMessage
import com.echofit.app.ui.components.GlassmorphicCard
import com.echofit.app.ui.components.threeDTiltEffect
import com.echofit.app.ui.theme.*
import com.echofit.app.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EchoChatScreen(
    viewModel: MainViewModel
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val messages by viewModel.chatMessages.collectAsState()
    var textInput by remember { mutableStateOf("") }
    var isEchoTyping by remember { mutableStateOf(false) }

    // Pre-set suggestion tags
    val suggestionTags = listOf(
        "Suggest a low-budget protein snack",
        "Missed gym today. Swap for home!",
        "Explain push-up form & mistakes",
        "What is my estimated body fat?"
    )

    // Auto scroll to bottom on new message
    LaunchedEffect(messages.size, isEchoTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlack.copy(alpha = 0f))
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Chat Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Brush.verticalGradient(listOf(GoldAccent, DarkGold))),
                contentAlignment = Alignment.Center
            ) {
                Text("E", color = PrimaryBlack, fontWeight = FontWeight.Black, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Echo AI Coach", style = Typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text("Online | Ready to motivate", style = Typography.labelMedium, color = SuccessGreen)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "CLEAR CHAT",
                color = ErrorRed,
                style = Typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.clearChat() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chat Conversation Thread
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Hey there! I am Echo, your AI Fitness coach.",
                                style = Typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Ask me anything about macros, exercise splits, custom yoga routines, or budget food items. I'm slightly funny but very serious about your progress!",
                                style = Typography.bodyMedium,
                                color = TextGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(messages) { msg ->
                    ChatBubble(message = msg)
                }
            }

            if (isEchoTyping) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = GoldAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Echo is calculating macros...", style = Typography.labelMedium, color = TextGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Pre-set suggestion chips row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestionTags.take(2).forEach { tag ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceDark)
                        .border(1.dp, BorderGray, RoundedCornerShape(20.dp))
                        .clickable {
                            isEchoTyping = true
                            scope.launch {
                                viewModel.sendMessageToEcho(tag)
                                kotlinx.coroutines.delay(1000)
                                isEchoTyping = false
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tag,
                        style = Typography.labelMedium,
                        color = GoldAccent,
                        maxLines = 1,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestionTags.takeLast(2).forEach { tag ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceDark)
                        .border(1.dp, BorderGray, RoundedCornerShape(20.dp))
                        .clickable {
                            isEchoTyping = true
                            scope.launch {
                                viewModel.sendMessageToEcho(tag)
                                kotlinx.coroutines.delay(1000)
                                isEchoTyping = false
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tag,
                        style = Typography.labelMedium,
                        color = GoldAccent,
                        maxLines = 1,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Message Input Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text("Ask Coach Echo...", color = TextGray) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = GoldAccent,
                    unfocusedBorderColor = BorderGray,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (textInput.isNotBlank()) {
                        val toSend = textInput
                        textInput = ""
                        isEchoTyping = true
                        scope.launch {
                            viewModel.sendMessageToEcho(toSend)
                            kotlinx.coroutines.delay(1000)
                            isEchoTyping = false
                        }
                    }
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(GoldAccent)
                    .size(48.dp)
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = PrimaryBlack)
            }
        }

        Spacer(modifier = Modifier.height(72.dp))
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (message.isUser) 16.dp else 4.dp,
        bottomEnd = if (message.isUser) 4.dp else 16.dp
    )
    
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .threeDTiltEffect()
                .padding(bottom = 4.dp, end = 4.dp)
        ) {
            // 3D Shadow/Bevel base offset
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = 4.dp, y = 4.dp)
                    .clip(bubbleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                if (message.isUser) PrimaryBlack else DarkGold.copy(alpha = 0.5f),
                                PrimaryBlack
                            )
                        )
                    )
            )

            // Main Chat Bubble Layer
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = if (message.isUser) BorderGray else GoldAccent.copy(alpha = 0.3f),
                        shape = bubbleShape
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (message.isUser) SurfaceDark else Color(0xFF1E1C14)
                ),
                shape = bubbleShape
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = message.text,
                        style = Typography.bodyLarge,
                        color = TextWhite
                    )
                }
            }
        }
    }
}
