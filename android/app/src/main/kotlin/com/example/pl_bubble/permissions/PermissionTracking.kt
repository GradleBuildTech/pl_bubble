package com.example.pl_bubble.permissions

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource

/**
 * Singleton class to manage notification listener and overlay permissions.
 * Provides methods to check permission status and open settings.
 */
class PermissionTracking private constructor() {

    companion object {
        const val TAG = "PermissionTracking"

        @Volatile
        private var INSTANCE: PermissionTracking? = null

        fun getInstance(): PermissionTracking {
            return INSTANCE ?: synchronized(this) {
                val instance = PermissionTracking()
                INSTANCE = instance
                instance
            }
        }
    }

    // Check if the app has notification listener permission
    fun hasNotifyPermission(context: Context): Boolean {
        //Tracking current version is under TIRAMISU
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            val packageName = context.packageName
            val flat = Settings.Secure.getString(
                context.contentResolver,
                "enabled_notification_listeners"
            )
            return flat != null && flat.contains(packageName)
        }
        return ContextCompat.checkSelfPermission(
            context,
            permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Open the notification listener settings screen
    fun openNotifyPermission(context: Context) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    // Check if the app has overlay permission
    fun hasOverlayPermission(context: Context): Boolean {
        return Settings.canDrawOverlays(context)
    }

    // Open the overlay permission settings screen
    fun openOverlayPermission(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * Checks both permissions and requests missing ones.
     * Returns true if both permissions are granted.
     */
    fun ensurePermissionsAsync(context: Context): Task<Boolean> {
        val taskSource = TaskCompletionSource<Boolean>()
        var allGranted = true

        if (!hasNotifyPermission(context)) {
            openNotifyPermission(context)
            allGranted = false
        }
        if (!hasOverlayPermission(context)) {
            openOverlayPermission(context)
            allGranted = false
        }

        taskSource.setResult(allGranted)
        return taskSource.task
    }
}