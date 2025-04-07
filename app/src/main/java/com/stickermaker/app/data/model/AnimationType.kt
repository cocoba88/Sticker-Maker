package com.stickermaker.app.data.model

enum class AnimationType {
    NONE,           // No animation
    FADE,           // Fade in/out
    BOUNCE,         // Bouncing effect
    PULSE,          // Pulsing effect
    SHAKE,          // Shaking effect
    ROTATE,         // Rotation animation
    SLIDE,          // Sliding effect
    WAVE,           // Wave motion
    GLITCH,         // Glitch effect
    SPARKLE,        // Sparkling effect
    RAINBOW;        // Rainbow color cycle

    companion object {
        fun getDefaultAnimations(): List<AnimationType> = values().toList()

        fun getDescription(type: AnimationType): String = when (type) {
            NONE -> "Tanpa Animasi"
            FADE -> "Fade In/Out"
            BOUNCE -> "Efek Memantul"
            PULSE -> "Efek Denyut"
            SHAKE -> "Efek Getaran"
            ROTATE -> "Rotasi"
            SLIDE -> "Geser"
            WAVE -> "Gelombang"
            GLITCH -> "Efek Glitch"
            SPARKLE -> "Efek Berkilau"
            RAINBOW -> "Warna Pelangi"
        }
    }
}