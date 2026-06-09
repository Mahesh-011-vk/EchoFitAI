package com.echofit.app.ui.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.echofit.app.ui.theme.*
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private const val TAG = "EchoFitCamera"

/**
 * Full-screen CameraX live preview composable.
 * Supports:
 *  - Live preview with back/front camera toggle
 *  - Flash toggle
 *  - Capture button with shutter animation
 *  - Saves captured image to app-internal storage and returns the URI
 */
@Composable
fun CameraScreen(
    position: String,    // "Front", "Side", "Back" – label shown in overlay
    onPhotoCaptured: (Uri) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var flashEnabled by remember { mutableStateOf(false) }
    var isCapturing by remember { mutableStateOf(false) }
    var shutterFlash by remember { mutableStateOf(false) }

    val imageCapture = remember { mutableStateOf<ImageCapture?>(null) }
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    // Shutter animation
    val shutterAlpha by animateFloatAsState(
        targetValue = if (shutterFlash) 1f else 0f,
        animationSpec = tween(durationMillis = 120),
        label = "shutterFlash"
    )

    // Clean up executor when leaving
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    val previewView = remember {
        PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    LaunchedEffect(lensFacing, flashEnabled) {
        startCamera(
            context = context,
            previewView = previewView,
            lifecycleOwner = lifecycleOwner,
            lensFacing = lensFacing,
            flashEnabled = flashEnabled,
            imageCaptureState = imageCapture
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // --- CameraX Preview ---
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize(),
            update = {}
        )

        // --- Shutter Flash Overlay ---
        if (shutterAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(shutterAlpha)
                    .background(Color.White)
            )
        }

        // --- Top Bar: Close, Flash, Position Label ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Camera",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Position badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                GoldAccent.copy(alpha = 0.8f),
                                DarkGold.copy(alpha = 0.8f)
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "📸  $position View",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Flash toggle
            IconButton(
                onClick = { flashEnabled = !flashEnabled },
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = if (flashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                    contentDescription = "Toggle Flash",
                    tint = if (flashEnabled) GoldAccent else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // --- Bottom Controls: Switch Camera + Capture ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Instruction text
            Text(
                text = "Position yourself for the $position view",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Empty spacer for symmetry
                Spacer(modifier = Modifier.size(56.dp))

                // Capture button - concentric ring design
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .border(3.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = if (isCapturing) 0.5f else 0.9f))
                        .clickable(enabled = !isCapturing) {
                            val capture = imageCapture.value ?: return@clickable
                            isCapturing = true
                            capturePhoto(context, capture, cameraExecutor) { uri ->
                                isCapturing = false
                                if (uri != null) {
                                    onPhotoCaptured(uri)
                                } else {
                                    Toast
                                        .makeText(
                                            context,
                                            "Failed to capture photo",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    isCapturing = false
                                }
                            }
                            // Trigger shutter flash
                            shutterFlash = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isCapturing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = GoldAccent,
                            strokeWidth = 3.dp
                        )
                    }
                }

                // Switch camera
                IconButton(
                    onClick = {
                        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                            CameraSelector.LENS_FACING_FRONT
                        } else {
                            CameraSelector.LENS_FACING_BACK
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Switch Camera",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }

    // Reset shutter flash
    LaunchedEffect(shutterFlash) {
        if (shutterFlash) {
            delay(150)
            shutterFlash = false
        }
    }
}

/**
 * Starts the CameraX preview and binds ImageCapture use case.
 */
private fun startCamera(
    context: Context,
    previewView: PreviewView,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    lensFacing: Int,
    flashEnabled: Boolean,
    imageCaptureState: MutableState<ImageCapture?>
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
        try {
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val capture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setFlashMode(
                    if (flashEnabled) ImageCapture.FLASH_MODE_ON
                    else ImageCapture.FLASH_MODE_OFF
                )
                .build()

            imageCaptureState.value = capture

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                capture
            )
        } catch (e: Exception) {
            Log.e(TAG, "CameraX binding failed", e)
        }
    }, ContextCompat.getMainExecutor(context))
}

/**
 * Captures a photo and saves it to app-internal storage.
 * Returns the content URI via FileProvider for Coil and Room persistence.
 */
private fun capturePhoto(
    context: Context,
    imageCapture: ImageCapture,
    executor: ExecutorService,
    onResult: (Uri?) -> Unit
) {
    val photosDir = File(context.filesDir, "EchoFit_Photos").apply { mkdirs() }
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val photoFile = File(photosDir, "ECHOFIT_${timeStamp}.jpg")

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(results: ImageCapture.OutputFileResults) {
                val savedUri = FileProvider.getUriForFile(
                    context,
                    "com.echofit.app.fileprovider",
                    photoFile
                )
                Log.d(TAG, "Photo saved: $savedUri")
                // Dispatch back to main thread
                android.os.Handler(android.os.Looper.getMainLooper()).post {
                    onResult(savedUri)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                android.os.Handler(android.os.Looper.getMainLooper()).post {
                    onResult(null)
                }
            }
        }
    )
}
