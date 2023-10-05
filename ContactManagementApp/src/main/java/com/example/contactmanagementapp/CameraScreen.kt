package com.example.contactmanagementapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen( state: ContactState, onEvent: (ContactEvent) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        ExtendedFloatingActionButton(
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = androidx.compose.ui.graphics.Color.Black
                )
            },
            text = { Text(text = "Capture") },
            onClick = { capturePhoto(context, cameraController, onEvent, state) },
            modifier = Modifier.padding(16.dp)
        )
    }) { paddingValues ->
        AndroidView(modifier = Modifier
            .padding(paddingValues), factory = { context ->
            PreviewView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(Color.BLACK)
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }.also { previewView ->
                cameraController.bindToLifecycle(lifecycleOwner)
                previewView.controller = cameraController
            }
        })
    }
}

private fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    onEvent: (ContactEvent) -> Unit,
    state: ContactState
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val correctedBitmap: Bitmap = image
                .toBitmap()
                .rotateBitmap(image.imageInfo.rotationDegrees)
            state.currentContact?.let { ContactEvent.SetImage(it, correctedBitmap.toByteArray()) }
                ?.let { onEvent(it) }
            state.currentContact?.image = correctedBitmap.toByteArray()
            state.currentContact?.let { ContactEvent.SetImage(it,correctedBitmap.toByteArray()) }
                ?.let { onEvent(it) }
            image.close()
            onEvent(ContactEvent.RestImage)
        }
        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraContent", "Error capturing image", exception)
        }
    })
}

// corrects the bitmap rotation