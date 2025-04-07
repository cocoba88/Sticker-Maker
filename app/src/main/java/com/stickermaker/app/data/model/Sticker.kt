package com.stickermaker.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stickers")
data class Sticker(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "Sticker",
    val text: String = "",
    val textSize: Float = 24f,
    val textColor: Int = 0xFF000000.toInt(),
    val textAnimation: AnimationType = AnimationType.NONE,
    
    val hasBorder: Boolean = false,
    val borderWidth: Float = 4f,
    val borderColor: Int = 0xFF000000.toInt(),
    val borderAnimation: AnimationType = AnimationType.NONE,
    val borderStyle: BorderStyle = BorderStyle.SOLID,
    
    val hasStroke: Boolean = false,
    val strokeWidth: Float = 2f,
    val strokeColor: Int = 0xFF000000.toInt(),
    val strokeAnimation: AnimationType = AnimationType.NONE,
    
    val backgroundColor: Int? = null, // null untuk transparan
    val backgroundAnimation: AnimationType = AnimationType.NONE,
    
    val tenorGifUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis()
)

enum class AnimationType {
    // Basic animations
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
    RAINBOW,        // Rainbow color cycle
    
    // New general animations
    FLIP,           // Flip effect
    SWING,          // Swinging motion
    ZOOM,           // Zoom in/out
    BLUR,           // Blur effect
    MORPH,          // Shape morphing
    FLOAT,          // Floating effect
    SPIRAL,         // Spiral motion
    ELASTIC,        // Elastic movement
    JELLY,          // Jelly-like effect
    NEON,           // Neon glow effect
    
    // Background-specific animations
    BG_GRADIENT,    // Gradient color shift
    BG_PARTICLES,   // Floating particles
    BG_RIPPLE,      // Ripple effect
    BG_NOISE,       // Animated noise
    BG_STARS,       // Twinkling stars
    BG_BUBBLES,     // Rising bubbles
    BG_MATRIX,      // Matrix-style effect
    BG_AURORA,      // Aurora borealis effect
    BG_CONFETTI,    // Falling confetti
    BG_PLASMA;      // Plasma wave effect

    companion object {
        fun getDefaultAnimations(): List<AnimationType> = values().toList()

        fun getDescription(type: AnimationType): String = when (type) {
            // Basic animations
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
            
            // New general animations
            FLIP -> "Efek Membalik"
            SWING -> "Efek Mengayun"
            ZOOM -> "Efek Perbesar/Perkecil"
            BLUR -> "Efek Kabur"
            MORPH -> "Efek Berubah Bentuk"
            FLOAT -> "Efek Melayang"
            SPIRAL -> "Efek Spiral"
            ELASTIC -> "Efek Elastis"
            JELLY -> "Efek Jeli"
            NEON -> "Efek Neon"
            
            // Background animations
            BG_GRADIENT -> "Gradien Bergerak"
            BG_PARTICLES -> "Partikel Melayang"
            BG_RIPPLE -> "Riak Air"
            BG_NOISE -> "Noise Animasi"
            BG_STARS -> "Bintang Berkelip"
            BG_BUBBLES -> "Gelembung Naik"
            BG_MATRIX -> "Efek Matrix"
            BG_AURORA -> "Aurora"
            BG_CONFETTI -> "Konfeti Jatuh"
            BG_PLASMA -> "Gelombang Plasma"
        }
    }
}

enum class BorderStyle {
    SOLID,          // Garis solid
    DASHED,         // Garis putus-putus
    DOTTED,         // Garis titik-titik
    DOUBLE,         // Garis ganda
    GROOVE,         // Efek cekungan
    RIDGE,          // Efek cembung
    WAVE,           // Garis bergelombang
    ZIGZAG,         // Garis zigzag
    SCALLOPED,      // Garis berkerang
    CUSTOM;         // Garis kustom

    companion object {
        fun getDefaultStyles(): List<BorderStyle> = values().toList()

        fun getDescription(style: BorderStyle): String = when (style) {
            SOLID -> "Garis Solid"
            DASHED -> "Garis Putus-putus"
            DOTTED -> "Garis Titik-titik"
            DOUBLE -> "Garis Ganda"
            GROOVE -> "Efek Cekungan"
            RIDGE -> "Efek Cembung"
            WAVE -> "Garis Bergelombang"
            ZIGZAG -> "Garis Zigzag"
            SCALLOPED -> "Garis Berkerang"
            CUSTOM -> "Garis Kustom"
        }
    }
}