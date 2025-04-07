package com.stickermaker.app.ui.editor

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stickermaker.app.StickerMakerApp
import com.stickermaker.app.data.model.AnimationType
import com.stickermaker.app.data.model.Sticker
import com.stickermaker.app.data.repository.StickerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StickerEditorViewModel : ViewModel() {
    private val repository: StickerRepository = StickerMakerApp.getRepository()

    private val _editorState = MutableStateFlow(StickerEditorState())
    val editorState: StateFlow<StickerEditorState> = _editorState.asStateFlow()

    var currentSticker by mutableStateOf<Sticker?>(null)
        private set

    fun updateText(text: String) {
        _editorState.value = _editorState.value.copy(text = text)
    }

    fun updateTextAnimation(animationType: Int) {
        _editorState.value = _editorState.value.copy(textAnimation = animationType)
    }

    fun updateTextColor(color: Int) {
        _editorState.value = _editorState.value.copy(textColor = color)
    }

    fun toggleBorder() {
        _editorState.value = _editorState.value.copy(hasBorder = !_editorState.value.hasBorder)
    }

    fun updateBorderAnimation(animationType: Int) {
        _editorState.value = _editorState.value.copy(borderAnimation = animationType)
    }

    fun updateBorderColor(color: Int) {
        _editorState.value = _editorState.value.copy(borderColor = color)
    }

    fun toggleStroke() {
        _editorState.value = _editorState.value.copy(hasStroke = !_editorState.value.hasStroke)
    }

    fun updateStrokeAnimation(animationType: Int) {
        _editorState.value = _editorState.value.copy(strokeAnimation = animationType)
    }

    fun updateStrokeColor(color: Int) {
        _editorState.value = _editorState.value.copy(strokeColor = color)
    }

    fun updateBackgroundColor(color: Int?) {
        _editorState.value = _editorState.value.copy(backgroundColor = color)
    }

    fun updateBackgroundAnimation(animationType: Int) {
        _editorState.value = _editorState.value.copy(backgroundAnimation = animationType)
    }

    fun updateGifUrl(url: String?) {
        _editorState.value = _editorState.value.copy(
            tenorGifUrl = url,
            isGif = url != null
        )
    }

    fun saveSticker(name: String, imageUri: String) {
        viewModelScope.launch {
            val state = _editorState.value
            val sticker = Sticker(
                name = name,
                imageUri = imageUri,
                text = state.text,
                textAnimation = state.textAnimation,
                textColor = state.textColor,
                hasBorder = state.hasBorder,
                borderAnimation = state.borderAnimation,
                borderColor = state.borderColor,
                hasStroke = state.hasStroke,
                strokeAnimation = state.strokeAnimation,
                strokeColor = state.strokeColor,
                backgroundColor = state.backgroundColor,
                backgroundAnimation = state.backgroundAnimation,
                isGif = state.isGif,
                tenorGifUrl = state.tenorGifUrl
            )
            repository.saveSticker(sticker)
        }
    }

    fun exportToWhatsApp(bitmap: Bitmap) {
        viewModelScope.launch {
            currentSticker?.let { sticker ->
                repository.exportStickerToWhatsApp(sticker, bitmap)
            }
        }
    }

    fun loadSticker(id: Long) {
        viewModelScope.launch {
            currentSticker = repository.getStickerById(id)
            currentSticker?.let { sticker ->
                _editorState.value = StickerEditorState(
                    text = sticker.text,
                    textAnimation = sticker.textAnimation,
                    textColor = sticker.textColor,
                    hasBorder = sticker.hasBorder,
                    borderAnimation = sticker.borderAnimation,
                    borderColor = sticker.borderColor,
                    hasStroke = sticker.hasStroke,
                    strokeAnimation = sticker.strokeAnimation,
                    strokeColor = sticker.strokeColor,
                    backgroundColor = sticker.backgroundColor,
                    backgroundAnimation = sticker.backgroundAnimation,
                    isGif = sticker.isGif,
                    tenorGifUrl = sticker.tenorGifUrl
                )
            }
        }
    }
}

data class StickerEditorState(
    val text: String = "",
    val textAnimation: Int = 0,
    val textColor: Int = 0xFF000000.toInt(),
    val hasBorder: Boolean = false,
    val borderAnimation: Int = 0,
    val borderColor: Int = 0xFF000000.toInt(),
    val hasStroke: Boolean = false,
    val strokeAnimation: Int = 0,
    val strokeColor: Int = 0xFF000000.toInt(),
    val backgroundColor: Int? = null,
    val backgroundAnimation: Int = 0,
    val isGif: Boolean = false,
    val tenorGifUrl: String? = null
)