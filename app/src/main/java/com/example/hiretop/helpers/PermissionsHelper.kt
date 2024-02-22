package com.example.hiretop.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionsHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun hasGalleryPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestGalleryPermission(activity: ComponentActivity, onPermissionResult: (Boolean) -> Unit) {
        val permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            onPermissionResult(isGranted)
        }

        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}