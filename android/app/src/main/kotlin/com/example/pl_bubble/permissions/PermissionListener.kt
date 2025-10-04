package com.example.pl_bubble.permissions
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import com.example.pl_bubble.permissions.utils.PermissionType
import io.flutter.plugin.common.PluginRegistry


/**
 * Listener to handle the result of notification + overlay permission requests.
 */
internal class PermissionListener(
    private val activity: Activity,
    private val onResultCallBack: BubblePermission.ResultCallBack
) : PluginRegistry.RequestPermissionsResultListener,
    PluginRegistry.ActivityResultListener {

    companion object {
        const val NOTIFICATION_DENIED = "PERMISSION_DENIED"
        const val OVERLAY_DENIED = "OVERLAY_PERMISSION_DENIED"
    }

    private var alreadyCalled: Boolean = false

    // ----------------- Notification permission -----------------
    /**
     * Handle the result of the notification permission request.
     * If the permission is granted, invoke the callback with null.
     * If denied, invoke the callback with an error code.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray
    ): Boolean {
        if (alreadyCalled) return false

        if (requestCode == BubblePermission.NOTIFICATION_REQUEST_CODE) {
            alreadyCalled = true
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                onResultCallBack.onResult(PermissionType.NOTIFICATION, NOTIFICATION_DENIED)
            } else {
                onResultCallBack.onResult(PermissionType.NOTIFICATION, null)
            }
            return true
        }
        return false
    }

    /**
     * ---- Overlay permission ----
     * Handle the result of the overlay permission request.
     * If the permission is granted, invoke the callback with null.
     * If denied, invoke the callback with an error code.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (alreadyCalled) return false

        if (requestCode == BubblePermission.OVERLAY_REQUEST_CODE) {
            alreadyCalled = true
            if (!Settings.canDrawOverlays(activity)) {
                onResultCallBack.onResult(PermissionType.OVERLAY, OVERLAY_DENIED)
            } else {
                onResultCallBack.onResult(PermissionType.OVERLAY, null)
            }
            return true
        }
        return false
    }
}
