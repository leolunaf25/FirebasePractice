package com.lunatcoms.firebasepractice.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lunatcoms.firebasepractice.core.NavigationWrapper
import com.lunatcoms.firebasepractice.ui.theme.FirebasePracticeTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebasePracticeTheme {
                NavigationWrapper()
            }
        }
    }
}
