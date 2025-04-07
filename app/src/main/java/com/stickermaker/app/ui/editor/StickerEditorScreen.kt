package com.stickermaker.app.ui.editor

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.stickermaker.app.data.model.AnimationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickerEditorScreen(
    viewModel: StickerEditorViewModel = viewModel(),
    onNavigateToGallery: () -> Unit
) {
    val editorState by viewModel.editorState.collectAsState()
    var showColorPicker by remember { mutableStateOf(false) }
    var currentColorTarget by remember { mutableStateOf<ColorTarget>(ColorTarget.Text) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sticker Editor") },
                actions = {
                    IconButton(onClick = { /* Export to WhatsApp */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Export")
                    }
                    IconButton(onClick = { /* Save sticker */ }) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Preview Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Transparent)
                    .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                StickerPreview(
                    text = editorState.text,
                    textColor = Color(editorState.textColor),
                    textAnimation = editorState.textAnimation,
                    hasBorder = editorState.hasBorder,
                    borderColor = Color(editorState.borderColor),
                    borderAnimation = editorState.borderAnimation,
                    hasStroke = editorState.hasStroke,
                    strokeColor = Color(editorState.strokeColor),
                    strokeAnimation = editorState.strokeAnimation,
                    backgroundColor = editorState.backgroundColor?.let { Color(it) },
                    backgroundAnimation = editorState.backgroundAnimation,
                    gifUrl = editorState.tenorGifUrl
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Editor Controls
            EditorControls(
                editorState = editorState,
                onTextChange = viewModel::updateText,
                onTextAnimationChange = viewModel::updateTextAnimation,
                onBorderToggle = viewModel::toggleBorder,
                onBorderAnimationChange = viewModel::updateBorderAnimation,
                onStrokeToggle = viewModel::toggleStroke,
                onStrokeAnimationChange = viewModel::updateStrokeAnimation,
                onColorPickerShow = { target ->
                    currentColorTarget = target
                    showColorPicker = true
                }
            )
        }

        if (showColorPicker) {
            ColorPickerDialog(
                onColorSelected = { color ->
                    when (currentColorTarget) {
                        ColorTarget.Text -> viewModel.updateTextColor(color)
                        ColorTarget.Border -> viewModel.updateBorderColor(color)
                        ColorTarget.Stroke -> viewModel.updateStrokeColor(color)
                        ColorTarget.Background -> viewModel.updateBackgroundColor(color)
                    }
                    showColorPicker = false
                },
                onDismiss = { showColorPicker = false }
            )
        }
    }
}

@Composable
fun StickerPreview(
    text: String,
    textColor: Color,
    textAnimation: Int,
    hasBorder: Boolean,
    borderColor: Color,
    borderAnimation: Int,
    hasStroke: Boolean,
    strokeColor: Color,
    strokeAnimation: Int,
    backgroundColor: Color?,
    backgroundAnimation: Int,
    gifUrl: String?
) {
    val infiniteTransition = rememberInfiniteTransition()
    
    // Animasi untuk teks
    val textScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (textAnimation == AnimationType.SCALE.ordinal) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor ?: Color.Transparent)
    ) {
        if (gifUrl != null) {
            AsyncImage(
                model = gifUrl,
                contentDescription = "GIF Background",
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = text,
            color = textColor,
            style = TextStyle(
                color = textColor
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .scale(textScale)
        )
    }
}

@Composable
fun EditorControls(
    editorState: StickerEditorState,
    onTextChange: (String) -> Unit,
    onTextAnimationChange: (Int) -> Unit,
    onBorderToggle: () -> Unit,
    onBorderAnimationChange: (Int) -> Unit,
    onStrokeToggle: () -> Unit,
    onStrokeAnimationChange: (Int) -> Unit,
    onColorPickerShow: (ColorTarget) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Text Input
        OutlinedTextField(
            value = editorState.text,
            onValueChange = onTextChange,
            label = { Text("Sticker Text") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Animation Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = { onTextAnimationChange((editorState.textAnimation + 1) % 10) }
            ) {
                Text("Text Animation: ${editorState.textAnimation}")
            }

            TextButton(
                onClick = { onColorPickerShow(ColorTarget.Text) }
            ) {
                Text("Text Color")
            }
        }

        // Border Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Switch(
                checked = editorState.hasBorder,
                onCheckedChange = { onBorderToggle() }
            )
            if (editorState.hasBorder) {
                TextButton(
                    onClick = { onBorderAnimationChange((editorState.borderAnimation + 1) % 10) }
                ) {
                    Text("Border Animation: ${editorState.borderAnimation}")
                }
                TextButton(
                    onClick = { onColorPickerShow(ColorTarget.Border) }
                ) {
                    Text("Border Color")
                }
            }
        }

        // Stroke Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Switch(
                checked = editorState.hasStroke,
                onCheckedChange = { onStrokeToggle() }
            )
            if (editorState.hasStroke) {
                TextButton(
                    onClick = { onStrokeAnimationChange((editorState.strokeAnimation + 1) % 10) }
                ) {
                    Text("Stroke Animation: ${editorState.strokeAnimation}")
                }
                TextButton(
                    onClick = { onColorPickerShow(ColorTarget.Stroke) }
                ) {
                    Text("Stroke Color")
                }
            }
        }

        // Background Color
        TextButton(
            onClick = { onColorPickerShow(ColorTarget.Background) }
        ) {
            Text("Background Color")
        }
    }
}

@Composable
fun ColorPickerDialog(
    onColorSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pick a color") },
        text = {
            // Simple color picker implementation
            Column {
                listOf(
                    Color.Black,
                    Color.White,
                    Color.Red,
                    Color.Green,
                    Color.Blue,
                    Color.Yellow,
                    Color.Cyan,
                    Color.Magenta
                ).forEach { color ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(color)
                            .clickable { onColorSelected(color.toArgb()) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

enum class ColorTarget {
    Text, Border, Stroke, Background
}