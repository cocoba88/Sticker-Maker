package com.stickermaker.app.data.repository

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.stickermaker.app.StickerMakerApp
import com.stickermaker.app.data.dao.StickerDao
import com.stickermaker.app.data.model.Sticker
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.FileOutputStream

interface StickerRepository {
    fun getAllStickers(): Flow<List<Sticker>>
    suspend fun getStickerById(id: Long): Sticker?
    suspend fun saveSticker(sticker: Sticker): Long
    suspend fun updateSticker(sticker: Sticker)
    suspend fun deleteSticker(sticker: Sticker)
    suspend fun exportStickerToWhatsApp(sticker: Sticker, bitmap: Bitmap): Uri?
    suspend fun saveStickerToGallery(sticker: Sticker, bitmap: Bitmap): Uri?
}

class StickerRepositoryImpl(
    private val stickerDao: StickerDao
) : StickerRepository {
    override fun getAllStickers(): Flow<List<Sticker>> = stickerDao.getAllStickers()

    override suspend fun getStickerById(id: Long): Sticker? = stickerDao.getStickerById(id)

    override suspend fun saveSticker(sticker: Sticker): Long = stickerDao.insertSticker(sticker)

    override suspend fun updateSticker(sticker: Sticker) = stickerDao.updateSticker(sticker)

    override suspend fun deleteSticker(sticker: Sticker) = stickerDao.deleteSticker(sticker)

    override suspend fun exportStickerToWhatsApp(sticker: Sticker, bitmap: Bitmap): Uri? {
        val context = StickerMakerApp.instance
        val contentResolver = context.contentResolver
        val stickerPackName = "${sticker.name}_pack"
        
        // Buat direktori untuk paket stiker WhatsApp
        val whatsappDir = File(context.getExternalFilesDir(null), "WhatsApp Stickers")
        whatsappDir.mkdirs()
        
        // Simpan bitmap stiker
        val stickerFile = File(whatsappDir, "${sticker.name}.webp")
        FileOutputStream(stickerFile).use { out ->
            // Konversi dan kompres bitmap ke format WebP sesuai persyaratan WhatsApp
            bitmap.compress(Bitmap.CompressFormat.WEBP, 95, out)
        }
        
        // Buat intent untuk mengirim stiker ke WhatsApp
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/webp"
        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            stickerFile
        ))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setPackage("com.whatsapp")
        
        // Jalankan intent
        context.startActivity(intent)
        
        return Uri.fromFile(stickerFile)
    }

    override suspend fun saveStickerToGallery(sticker: Sticker, bitmap: Bitmap): Uri? {
        val context = StickerMakerApp.instance
        val filename = "${sticker.name}_${System.currentTimeMillis()}.png"
        
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        
        uri?.let { imageUri ->
            context.contentResolver.openOutputStream(imageUri)?.use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        }
        
        return uri
    }
}