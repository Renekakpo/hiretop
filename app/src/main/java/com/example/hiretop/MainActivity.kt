package com.example.hiretop

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.hiretop.app.HireTopApp
import com.example.hiretop.helpers.PermissionsHelper
import com.example.hiretop.ui.theme.HiretopTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var permissionsHelper: PermissionsHelper

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.media_permission_alert_message),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContent {
            HiretopTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HireTopApp()
                }
            }
        }

        requestNeededPermissions()
    }

    private fun requestNeededPermissions() {
        if (!permissionsHelper.hasGalleryPermission() || !permissionsHelper.hasNetworkStatePermission()) {
            permissionsHelper.requestGalleryPermission(
                activity = this@MainActivity,
                onPermissionResult = {
                    if (it) {
                        Log.e("PermissionHelper-MainActivity", "PErmission granted")
                    } else {
                        Log.e("PermissionHelper-MainActivity", "PErmission granted")
                    }
                }
            )
        }
    }
}