package com.stickermaker.app.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stickermaker.app.ui.editor.StickerEditorScreen
import com.stickermaker.app.ui.theme.StickerMakerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var isDarkMode by mutableStateOf(false)
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, proceed with storage operations
        } else {
            // Permission denied, show message or handle accordingly
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check and request storage permission
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                // Permission is granted, proceed with storage operations
            }
            else -> {
                // Request the permission
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        setContent {
            val systemInDarkTheme = isSystemInDarkTheme()
            LaunchedEffect(systemInDarkTheme) {
                isDarkMode = systemInDarkTheme
            }

            StickerMakerTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "editor"
                    ) {
                        composable("editor") {
                            StickerEditorScreen(
                                onNavigateToGallery = {
                                    navController.navigate("gallery")
                                }
                            )
                        }
                        composable("gallery") {
                            // GalleryScreen implementation will be added later
                        }
                        composable("settings") {
                            // SettingsScreen implementation will be added later
                            // Here we'll add theme toggle and other settings
                        }
                    }
                }
            }
        }
    }
}