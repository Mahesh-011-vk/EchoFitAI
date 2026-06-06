package com.echofit.app.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echofit.app.ui.theme.*
import android.net.Uri
import android.widget.VideoView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext


/**
 * Custom 3D Gyroscopic Tilt Modifier.
 * Dynamically rotates cards in 3D space toward the finger touch position,
 * returning to the neutral state with smooth spring-physics damping upon release.
 */
fun Modifier.threeDTiltEffect(): Modifier = composed {
    var rotateX by remember { mutableStateOf(0f) }
    var rotateY by remember { mutableStateOf(0f) }
    var width by remember { mutableStateOf(0f) }
    var height by remember { mutableStateOf(0f) }

    val animatedRotateX by animateFloatAsState(
        targetValue = rotateX,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow)
    )
    val animatedRotateY by animateFloatAsState(
        targetValue = rotateY,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow)
    )

    this
        .onGloballyPositioned {
            width = it.size.width.toFloat()
            height = it.size.height.toFloat()
        }
        .graphicsLayer {
            rotationX = animatedRotateX
            rotationY = animatedRotateY
            cameraDistance = 16f * density
        }
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    if (width > 0 && height > 0) {
                        val normalizeX = (down.position.x / width) - 0.5f
                        val normalizeY = (down.position.y / height) - 0.5f
                        rotateX = -normalizeY * 15f
                        rotateY = normalizeX * 15f
                    }
                    
                    var upReceived = false
                    while (!upReceived) {
                        val event = awaitPointerEvent()
                        val anyUp = event.changes.any { it.changedToUp() }
                        if (anyUp) {
                            upReceived = true
                        }
                    }
                    
                    rotateX = 0f
                    rotateY = 0f
                }
            }
        }
}

/**
 * Premium 3D extruded Glassmorphic card.
 * Layered with an gold-metallic depth offset base and a high-refraction glass panel
 * that dynamically tilts on interaction.
 */
@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    backgroundBrush: Brush? = null,
    isGlowing: Boolean = false,
    borderColor: Color = Color.White.copy(alpha = 0.5f),
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .threeDTiltEffect()
            .padding(8.dp)
    ) {
        if (isGlowing) {
            // Glowing neon shadow
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(y = 4.dp)
                    .blur(16.dp)
                    .background(SuccessGreen.copy(alpha = 0.25f), RoundedCornerShape(32.dp))
            )
        } else {
            // High quality multi-layered soft neumorphic shadow
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = 0.dp, y = 6.dp)
                    .blur(12.dp)
                    .background(Color(0x0A000000), RoundedCornerShape(32.dp))
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = 0.dp, y = 2.dp)
                    .blur(4.dp)
                    .background(Color(0x05000000), RoundedCornerShape(32.dp))
            )
        }

        // Top surface card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (isGlowing) Color.White.copy(alpha = 0.8f) else borderColor,
                    shape = RoundedCornerShape(32.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = if (backgroundBrush != null) Color.Transparent else backgroundColor
            ),
            shape = RoundedCornerShape(32.dp)
        ) {
            val columnModifier = if (backgroundBrush != null) {
                Modifier
                    .background(backgroundBrush)
                    .padding(20.dp)
            } else {
                Modifier.padding(20.dp)
            }
            Column(
                modifier = columnModifier
            ) {
                content()
            }
        }
    }
}

/**
 * Advanced double progress ring showing Calories (Gold) and Protein (Purple)
 * rendered with 3D volumetric tubular shading and gloss glare layers.
 */
@Composable
fun CalorieProgressRing(
    caloriesConsumed: Int,
    caloriesTarget: Int,
    proteinConsumed: Int,
    proteinTarget: Int,
    size: Dp = 180.dp
) {
    val calorieProgress = if (caloriesTarget > 0) caloriesConsumed.toFloat() / caloriesTarget else 0f
    val proteinProgress = if (proteinTarget > 0) proteinConsumed.toFloat() / proteinTarget else 0f

    Box(
        modifier = Modifier
            .size(size)
            .threeDTiltEffect(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size - 20.dp)) {
            // --- CALORIE OUTER RING ---
            // Outer Ring Track shadow backing
            drawArc(
                color = Color.Black.copy(alpha = 0.03f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 20f, cap = StrokeCap.Round)
            )
            // Outer Ring Track (light grey background)
            drawArc(
                color = Color(0xFFE5E7EB),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 16f, cap = StrokeCap.Round)
            )
            // Active Gold cylinder
            drawArc(
                color = OrangeGradEnd,
                startAngle = -90f,
                sweepAngle = (calorieProgress.coerceIn(0f, 1f) * 360f),
                useCenter = false,
                style = Stroke(width = 16f, cap = StrokeCap.Round)
            )

            // --- PROTEIN INNER RING ---
            // Track shadow
            drawArc(
                color = Color.Black.copy(alpha = 0.02f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )
            // Track background (light grey)
            drawArc(
                color = Color(0xFFF3F4F6),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 10f, cap = StrokeCap.Round)
            )
            // Active Green cylinder
            drawArc(
                color = SuccessGreen,
                startAngle = -90f,
                sweepAngle = (proteinProgress.coerceIn(0f, 1f) * 360f),
                useCenter = false,
                style = Stroke(width = 10f, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(caloriesTarget - caloriesConsumed).coerceAtLeast(0)} kcal",
                style = Typography.titleLarge,
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                color = PrimaryBlack
            )
            Text(
                text = "Remaining",
                style = Typography.labelMedium,
                color = TextGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(SuccessGreen)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${proteinConsumed}/${proteinTarget}g Pro",
                    style = Typography.labelMedium,
                    color = PrimaryBlack,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Premium streak badge showing consecutive days completed with extruded 3D layers.
 */
@Composable
fun StreakFireIndicator(
    streakCount: Int
) {
    Box(
        modifier = Modifier
            .padding(bottom = 3.dp, end = 3.dp)
    ) {
        // Base Shadow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 2.dp, y = 2.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color.Black.copy(alpha = 0.15f))
        )

        // Pill top layer
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(DarkBeige)
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(50.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Streak Fire",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$streakCount DAY STREAK",
                style = Typography.labelMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun EchoMotivationalCard(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .threeDTiltEffect()
            .padding(bottom = 4.dp, end = 4.dp)
    ) {
        // Neumorphic shadow backing
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 0.dp, y = 6.dp)
                .blur(10.dp)
                .background(Color.Black.copy(alpha = 0.08f), RoundedCornerShape(32.dp))
        )

        // Top Bubble container (Emerald Gradient)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(Brush.verticalGradient(listOf(EmeraldGradStart, EmeraldGradEnd)))
                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(32.dp))
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Echo Avatar placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "E",
                    color = EmeraldGradEnd,
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Echo AI Coach",
                    style = Typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"$message\"",
                    style = Typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun PremiumButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = DarkBeige,
    buttonBrush: Brush? = null,
    contentColor: Color = Color.White
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val translationY by animateDpAsState(
        targetValue = if (isPressed) 3.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessHigh)
    )

    Box(
        modifier = modifier
            .padding(bottom = 3.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
    ) {
        if (enabled) {
            // Soft shadow behind the button
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(y = 3.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color.Black.copy(alpha = 0.1f))
            )
        }

        // Tactile button top surface
        val surfaceModifier = Modifier
            .offset(y = translationY)
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(26.dp))
            .then(
                if (enabled) {
                    if (buttonBrush != null) Modifier.background(buttonBrush)
                    else Modifier.background(backgroundColor)
                } else {
                    Modifier.background(Color(0xFFE5E7EB))
                }
            )
            .border(
                width = 1.dp,
                color = if (enabled) Color.White.copy(alpha = 0.15f) else Color.Transparent,
                shape = RoundedCornerShape(26.dp)
            )

        Box(
            modifier = surfaceModifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = Typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (enabled) contentColor else TextGray
            )
        }
    }
}

/**
 * Bottom Nav Console floating in 3D above the content layers.
 */
@Composable
fun EchoBottomNavigation(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val items = listOf("HOME", "DIET", "WORKOUT", "PROGRESS", "ECHOAI", "SETTING")
    val icons = mapOf(
        "HOME" to Icons.Default.Home,
        "DIET" to Icons.Default.Restaurant,
        "WORKOUT" to Icons.Default.FitnessCenter,
        "PROGRESS" to Icons.Default.TrendingUp,
        "ECHOAI" to Icons.Default.Forum,
        "SETTING" to Icons.Default.Settings
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        // Soft drop shadow backing for bottom nav
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 2.dp, y = 4.dp)
                .background(Color.Black.copy(alpha = 0.04f), RoundedCornerShape(24.dp))
        )

        // Main Navigation Console (frosted dark beige glass)
        NavigationBar(
            containerColor = DarkBeige,
            tonalElevation = 0.dp,
            modifier = Modifier
                .height(80.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            items.forEach { tab ->
                val isSelected = selectedTab == tab
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    alwaysShowLabel = true,
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color.White.copy(alpha = 0.25f) else Color.Transparent)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) Color.White.copy(alpha = 0.3f) else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icons[tab] ?: Icons.Default.Settings,
                                contentDescription = tab,
                                tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = tab,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            softWrap = false,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Color.White.copy(alpha = 0.8f),
                        unselectedTextColor = Color.White.copy(alpha = 0.8f)
                    )
                )
            }
        }
    }
}

/**
 * Loopable Video Player for premium backgrounds and studio streams.
 * Automatically mutes audio for background suitability and loops infinitely.
 */
@Composable
fun BackgroundVideoPlayer(
    videoResId: Int,
    modifier: Modifier = Modifier,
    isMuted: Boolean = true
) {
    val context = LocalContext.current
    AndroidView(
        factory = { ctx ->
            VideoView(ctx).apply {
                val videoPath = "android.resource://${ctx.packageName}/$videoResId"
                setVideoURI(Uri.parse(videoPath))
                setOnPreparedListener { mp ->
                    mp.isLooping = true
                    if (isMuted) {
                        mp.setVolume(0f, 0f)
                    }
                    start()
                }
            }
        },
        modifier = modifier
    )
}

/**
 * Loopable Video Player for network URL video assets, using center-crop (object-cover) scaling.
 */
@Composable
fun BackgroundVideoPlayerUrl(
    videoUrl: String,
    modifier: Modifier = Modifier,
    isMuted: Boolean = true
) {
    var videoWidth by remember { mutableStateOf(0) }
    var videoHeight by remember { mutableStateOf(0) }

    AndroidView(
        factory = { ctx ->
            val view = object : VideoView(ctx) {
                override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
                    val viewWidth = getDefaultSize(0, widthMeasureSpec)
                    val viewHeight = getDefaultSize(0, heightMeasureSpec)
                    if (videoWidth > 0 && videoHeight > 0) {
                        val widthRatio = viewWidth.toFloat() / videoWidth
                        val heightRatio = viewHeight.toFloat() / videoHeight
                        val scale = Math.max(widthRatio, heightRatio)
                        val measuredW = (videoWidth * scale).toInt()
                        val measuredH = (videoHeight * scale).toInt()
                        setMeasuredDimension(measuredW, measuredH)
                    } else {
                        setMeasuredDimension(viewWidth, viewHeight)
                    }
                }
            }
            view.apply {
                setVideoURI(Uri.parse(videoUrl))
                setOnPreparedListener { mp ->
                    videoWidth = mp.videoWidth
                    videoHeight = mp.videoHeight
                    mp.isLooping = true
                    if (isMuted) {
                        mp.setVolume(0f, 0f)
                    }
                    requestLayout()
                    start()
                }
            }
        },
        modifier = modifier
    )
}

/**
 * Custom Volumetric 3D Text Header.
 * Renders a bold text with cascading offset shadows and a metallic white-grey gradient front layer.
 */
@Composable
fun ThreeDText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: androidx.compose.ui.unit.TextUnit = 28.sp,
    letterSpacing: androidx.compose.ui.unit.TextUnit = 3.sp
) {
    Text(
        text = text,
        fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
        letterSpacing = letterSpacing,
        fontWeight = FontWeight.Black,
        fontSize = fontSize,
        color = PrimaryBlack,
        modifier = modifier.padding(8.dp)
    )
}


