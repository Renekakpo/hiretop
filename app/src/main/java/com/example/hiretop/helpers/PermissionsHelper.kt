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

    fun hasNetworkStatePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_NETWORK_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestGalleryPermission(activity: ComponentActivity, onPermissionResult: (Boolean) -> Unit) {
        val permissions = mutableListOf<String>()
        if (!hasGalleryPermission()) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!hasNetworkStatePermission()) {
            permissions.add(Manifest.permission.ACCESS_NETWORK_STATE)
        }

        val permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap: Map<String, Boolean> ->
            val allPermissionsGranted = permissionsMap.values.all { it }
            onPermissionResult(allPermissionsGranted)
        }

        permissionLauncher.launch(permissions.toTypedArray())
    }
}