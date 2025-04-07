package com.stickermaker.app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stickermaker.app.data.dao.StickerDao
import com.stickermaker.app.data.model.Sticker

@Database(entities = [Sticker::class], version = 1, exportSchema = false)
abstract class StickerDatabase : RoomDatabase() {
    abstract fun stickerDao(): StickerDao
}

package com.stickermaker.app.data.dao

import androidx.room.*
import com.stickermaker.app.data.model.Sticker
import kotlinx.coroutines.flow.Flow

@Dao
interface StickerDao {
    @Query("SELECT * FROM stickers ORDER BY createdAt DESC")
    fun getAllStickers(): Flow<List<Sticker>>

    @Query("SELECT * FROM stickers WHERE id = :stickerId")
    suspend fun getStickerById(stickerId: Long): Sticker?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSticker(sticker: Sticker): Long

    @Update
    suspend fun updateSticker(sticker: Sticker)

    @Delete
    suspend fun deleteSticker(sticker: Sticker)

    @Query("DELETE FROM stickers WHERE id = :stickerId")
    suspend fun deleteStickerById(stickerId: Long)
}