package com.stickermaker.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.stickermaker.app.data.StickerDatabase
import com.stickermaker.app.data.repository.StickerRepository
import com.stickermaker.app.data.repository.StickerRepositoryImpl

class StickerMakerApp : Application() {
    private lateinit var database: StickerDatabase
    lateinit var repository: StickerRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeDependencies()
    }

    private fun initializeDependencies() {
        database = Room.databaseBuilder(
            applicationContext,
            StickerDatabase::class.java,
            "sticker_database"
        ).build()

        repository = StickerRepositoryImpl(database.stickerDao())
    }

    companion object {
        private lateinit var instance: StickerMakerApp

        fun getRepository(): StickerRepository = instance.repository
    }
}