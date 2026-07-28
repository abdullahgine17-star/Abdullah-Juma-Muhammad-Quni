package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.IdeScreen
import com.example.ui.theme.CodeCraftStudioTheme
import com.example.ui.viewmodel.IdeViewModel

class MainActivity : ComponentActivity() {

    private val ideViewModel: IdeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by ideViewModel.isDarkTheme.collectAsStateWithLifecycle()

            CodeCraftStudioTheme(darkTheme = isDarkTheme) {
                IdeScreen(viewModel = ideViewModel)
            }
        }
    }
}
