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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
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
            detectTapGestures(
                onPress = { offset ->
                    if (width > 0 && height > 0) {
                        val normalizeX = (offset.x / width) - 0.5f
                        val normalizeY = (offset.y / height) - 0.5f
                        // Map touch relative coordinate to 3D rotation angles (-15 to 15 degrees)
                        rotateX = -normalizeY * 15f
                        rotateY = normalizeX * 15f
                    }
                    tryAwaitRelease()
                    rotateX = 0f
                    rotateY = 0f
                }
            )
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
    backgroundColor: Color = SurfaceDarkGlass,
    borderColor: Color = BorderGray,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .threeDTiltEffect()
            .padding(bottom = 6.dp, end = 6.dp)
    ) {
        // Layer 1: 3D Gold Extruded Bevel Base
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 6.dp, y = 6.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            DarkGold.copy(alpha = 0.9f),
                            PrimaryBlack
                        )
                    )
                )
                .border(1.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
        )

        // Layer 2: Glossy glass card surface container
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, borderColor.copy(alpha = 0.8f), RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
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
            // --- CALORIE OUTER RING (Volumetric Tube 3D Shading) ---
            // 3D Dark drop shadow backing
            drawArc(
                color = Color(0x33000000),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 24f, cap = StrokeCap.Round)
            )
            // Outer Ring Track (Ambient glow)
            drawArc(
                color = Color(0x1AFFFF00),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 18f, cap = StrokeCap.Round)
            )
            // Volumetric Base extrusion shadow
            drawArc(
                color = DarkGold.copy(alpha = 0.4f),
                startAngle = -90f,
                sweepAngle = (calorieProgress.coerceIn(0f, 1f) * 360f),
                useCenter = false,
                style = Stroke(width = 20f, cap = StrokeCap.Round)
            )
            // Active Gold cylinder
            drawArc(
                color = GoldAccent,
                startAngle = -90f,
                sweepAngle = (calorieProgress.coerceIn(0f, 1f) * 360f),
                useCenter = false,
                style = Stroke(width = 16f, cap = StrokeCap.Round)
            )
            // Inner glare/highlight for 3D metallic refraction
            drawArc(
                color = Color.White.copy(alpha = 0.4f),
                startAngle = -90f,
                sweepAngle = (calorieProgress.coerceIn(0f, 1f) * 360f),
                useCenter = false,
                style = Stroke(width = 4f, cap = StrokeCap.Round)
            )

            // --- PROTEIN INNER RING ---
            // Track shadow
            drawArc(
                color = Color(0x1A8E24AA),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )
            // Base shadow
            drawArc(
                color = Color(0x4D000000),
                startAngle = -90f,
                sweepAngle = (proteinProgress.coerceIn(0f, 1f) * 360f),
                useCenter = false,
                style = Stroke(width = 14f, cap = StrokeCap.Round)
            )
            // Active Purple cylinder
            drawArc(
                color = ProteinPurple,
                startAngle = -90f,
                sweepAngle = (proteinProgress.coerceIn(0f, 1f) * 360f),
                useCenter = false,
                style = Stroke(width = 10f, cap = StrokeCap.Round)
            )
            // Inner highlight
            drawArc(
                color = Color.White.copy(alpha = 0.3f),
                startAngle = -90f,
                sweepAngle = (proteinProgress.coerceIn(0f, 1f) * 360f),
                useCenter = false,
                style = Stroke(width = 2f, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(caloriesTarget - caloriesConsumed).coerceAtLeast(0)} kcal",
                style = Typography.titleLarge,
                fontWeight = FontWeight.Black,
                fontSize = 24.sp
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
                        .background(ProteinPurple)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${proteinConsumed}/${proteinTarget}g Pro",
                    style = Typography.labelMedium,
                    color = TextWhite,
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
                .offset(x = 3.dp, y = 3.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFF8B0000))
        )

        // Pill top layer
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFFFF4500))
                .border(1.dp, StreakFire.copy(alpha = 0.7f), RoundedCornerShape(50.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Streak Fire",
                tint = TextWhite,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$streakCount DAY STREAK",
                style = Typography.labelMedium,
                color = TextWhite,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

/**
 * Reusable motivational widget featuring Echo AI coach speech bubbles with 3D backing.
 */
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
        // Beveled shadow base
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 4.dp, y = 4.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.verticalGradient(listOf(DarkGold.copy(alpha = 0.6f), PrimaryBlack)))
        )

        // Top Bubble container
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceDark)
                .border(1.dp, GoldAccent.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Echo Avatar placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Brush.verticalGradient(listOf(GoldAccent, DarkGold)))
                    .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "E",
                    color = PrimaryBlack,
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
                    color = GoldAccent
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "\"$message\"",
                    style = Typography.bodyMedium,
                    color = TextWhite,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

/**
 * Spring-based Pushable Tactile 3D Button.
 * Animates compression travel (translationY) down into its gold-bevel base upon click interaction.
 */
@Composable
fun PremiumButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = GoldAccent,
    contentColor: Color = PrimaryBlack
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val translationY by animateDpAsState(
        targetValue = if (isPressed) 6.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessHigh)
    )

    Box(
        modifier = modifier
            .padding(bottom = 6.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
    ) {
        // Physical Beveled Base
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 6.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (enabled) DarkGold else SurfaceDark)
        )

        // Floating tactile button top surface
        Box(
            modifier = Modifier
                .offset(y = translationY)
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (enabled) backgroundColor else SurfaceDark)
                .border(1.dp, if (enabled) GoldAccent.copy(alpha = 0.5f) else BorderGray, RoundedCornerShape(14.dp)),
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
    val items = listOf("Dashboard", "Diet", "Workout", "Progress", "Echo", "Profile")
    val icons = mapOf(
        "Dashboard" to Icons.Default.LocalFireDepartment,
        "Diet" to Icons.Default.LocalFireDepartment,
        "Workout" to Icons.Default.LocalFireDepartment,
        "Progress" to Icons.Default.LocalFireDepartment,
        "Echo" to Icons.Default.LocalFireDepartment,
        "Profile" to Icons.Outlined.Person
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        // 3D Shadow console backing
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 4.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(PrimaryBlack.copy(alpha = 0.8f))
                .border(1.dp, GoldAccent.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
        )

        // Main Navigation Console
        NavigationBar(
            containerColor = SurfaceDarkGlass,
            tonalElevation = 8.dp,
            modifier = Modifier
                .height(72.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, BorderGray.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
        ) {
            items.forEach { tab ->
                NavigationBarItem(
                    selected = selectedTab == tab,
                    onClick = { onTabSelected(tab) },
                    icon = {
                        Icon(
                            imageVector = icons[tab] ?: Icons.Outlined.Person,
                            contentDescription = tab
                        )
                    },
                    label = {
                        Text(
                            text = tab,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryBlack,
                        selectedTextColor = GoldAccent,
                        indicatorColor = GoldAccent,
                        unselectedIconColor = TextGray,
                        unselectedTextColor = TextGray
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

