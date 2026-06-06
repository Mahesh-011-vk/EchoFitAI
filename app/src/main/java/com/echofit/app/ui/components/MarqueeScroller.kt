package com.echofit.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.echofit.app.ui.theme.PrimaryBlack
import kotlinx.coroutines.delay

data class LogoItem(
    val id: String,
    val name: String,
    val srcUrl: String,
    val startColor: Color,
    val endColor: Color
)

val LOGO_LIST = listOf(
    LogoItem("procure", "Procure", "https://cdn.svgl.app/directory/brand/procure.svg", Color(0xFF3B82F6), Color(0xFF4F46E5)),
    LogoItem("shopify", "Shopify", "https://cdn.svgl.app/directory/brand/shopify.svg", Color(0xFFFACC15), Color(0xFFD97706)),
    LogoItem("blender", "Blender", "https://cdn.svgl.app/directory/brand/blender.svg", Color(0xFF60A5FA), Color(0xFF2563EB)),
    LogoItem("figma", "Figma", "https://cdn.svgl.app/directory/brand/figma.svg", Color(0xFFA855F7), Color(0xFFDB2777)),
    LogoItem("spotify", "Spotify", "https://cdn.svgl.app/directory/brand/spotify.svg", Color(0xFFEC4899), Color(0xFFE11D48)),
    LogoItem("lottielab", "Lottielab", "https://cdn.svgl.app/directory/brand/lottielab.svg", Color(0xFFFDE047), Color(0xFF22C55E)),
    LogoItem("google-cloud", "Google Cloud", "https://cdn.svgl.app/directory/brand/google-cloud.svg", Color(0xFF38BDF8), Color(0xFF3B82F6)),
    LogoItem("bing", "Bing", "https://cdn.svgl.app/directory/brand/bing.svg", Color(0xFF22D3EE), Color(0xFF0D9488))
)

@Composable
fun LogoCard(logo: LogoItem, imageLoader: ImageLoader) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Hover / Press animations matching React Card scale (1.5 to 1.0) and opacity (0 to 1)
    val targetScale = if (isPressed) 1.0f else 1.5f
    val targetAlpha = if (isPressed) 0.15f else 0.0f

    val scale by animateFloatAsState(targetValue = targetScale, label = "card_scale")
    val alpha by animateFloatAsState(targetValue = targetAlpha, label = "card_alpha")

    Box(
        modifier = Modifier
            .size(width = 160.dp, height = 96.dp)
            .clip(RoundedCornerShape(48.dp))
            .background(Color.White)
            .border(1.dp, Color(0x3E8B5CF6), RoundedCornerShape(48.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { /* Click placeholder */ },
        contentAlignment = Alignment.Center
    ) {
        // Gradient overlay active on interaction
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
                .background(
                    Brush.linearGradient(
                        colors = listOf(logo.startColor, logo.endColor)
                    )
                )
        )

        // Logo Image
        // Invert to dark slate/black color tint on press
        val colorFilter = if (isPressed) ColorFilter.tint(Color(0xFF0A1B33)) else null

        Image(
            painter = rememberAsyncImagePainter(
                model = logo.srcUrl,
                imageLoader = imageLoader
            ),
            contentDescription = logo.name,
            colorFilter = colorFilter,
            modifier = Modifier
                .size(height = 32.dp, width = 110.dp)
        )
    }
}

@Composable
fun MarqueeScroller(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }

    // Quadruple the logos list to make sure it scrolls indefinitely and seamlessly on large screens
    val logos = remember { LOGO_LIST + LOGO_LIST + LOGO_LIST + LOGO_LIST }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        while (true) {
            scrollState.scrollBy(2f) // Smooth scroll speed
            delay(16) // ~60fps smooth rendering
            
            // Seamlessly snap offset back once we scroll past half the items
            if (scrollState.value >= scrollState.maxValue / 2) {
                scrollState.scrollTo(scrollState.value - (scrollState.maxValue / 2))
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
    ) {
        // Horizontal list
        Row(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(scrollState, enabled = false),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            logos.forEachIndexed { index, logo ->
                LogoCard(logo = logo, imageLoader = imageLoader)
            }
        }

        // Left Fading Edge Overlay Mask
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(48.dp)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(PrimaryBlack, Color.Transparent)
                    )
                )
        )

        // Right Fading Edge Overlay Mask
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(48.dp)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, PrimaryBlack)
                    )
                )
        )
    }
}
