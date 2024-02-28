package com.example.hiretop.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Helper class for managing permissions related to gallery access and network state.
 * This class provides methods to check for permissions, request permissions, and handle permission results.
 */
class PermissionsHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Checks whether the application has the necessary permission to access the gallery.
     * @return true if permission is granted, false otherwise.
     */
    fun hasGalleryPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Checks whether the application has the necessary permission to access network state.
     * @return true if permission is granted, false otherwise.
     */
    fun hasNetworkStatePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_NETWORK_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Requests the necessary permissions for gallery access and network state.
     * This method launches the permission request dialog if permissions are not already granted.
     * @param activity The activity instance to use for launching the permission request.
     * @param onPermissionResult Callback function to be invoked with the result of the permission request.
     */
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