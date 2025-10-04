package com.example.pl_bubble.permissions

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.pl_bubble.permissions.utils.PermissionType
import io.flutter.plugin.common.PluginRegistry.RequestPermissionsResultListener

/*
    * Class to handle notification permission related constants and callbacks
 */
class BubblePermission {
    companion object {
        /*
            * Request code for notification permission
            * This code is used to identify the permission request in the onActivityResult callback
         */
        const val NOTIFICATION_REQUEST_CODE = 0x1001

        const val OVERLAY_REQUEST_CODE = 0x1002

        @Volatile
        private var INSTANCE: BubblePermission? = null

        // Get singleton instance
        fun getInstance(): BubblePermission {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BubblePermission().also { INSTANCE = it }
            }
        }
    }

    //Result call back
    interface ResultCallBack {
        fun onResult(permissionType: PermissionType, errorCode: String?)
    }

    /**
     * Permissions attributes
     */
    private var isNotificationGranted: Boolean = false
    private var isOverlayGranted: Boolean = false

    //Listener for permission result
    private var listener: RequestPermissionsResultListener? = null

    fun getListener(): RequestPermissionsResultListener? {
        return listener
    }

    // Set the listener for permission result
    var onGoing: Boolean = false

    // Check if notification permission is granted
    fun hasNotificationPermission(activity: Activity): Boolean {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }
        if(ContextCompat.checkSelfPermission(activity, POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    // Open notification permission settings with NOTIFICATION_REQUEST_CODE
    fun openNotificationPermissionSettings(activity: Activity) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return
        }
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(POST_NOTIFICATIONS),
            NOTIFICATION_REQUEST_CODE
        )
    }

    // Check if overlay permission is granted
    fun hasOverlayPermission(activity: Activity): Boolean {
        return android.provider.Settings.canDrawOverlays(activity)
    }

    // Open overlay permission settings with OVERLAY_REQUEST_CODE
    fun openOverlayPermissionSettings(activity: Activity) {
        val intent = android.content.Intent(
            android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "package:${activity.packageName}".toUri()
        )
        activity.startActivityForResult(intent, OVERLAY_REQUEST_CODE)
    }

    // Grant both permissions if not already granted
    fun grantPermission(activity: Activity) {
        if(!isNotificationGranted) {
            openNotificationPermissionSettings(activity)
        }
        if(!isOverlayGranted) {
            openOverlayPermissionSettings(activity)
        }
    }


    /**
     * Requests notification permission if not already granted.
     * Handles permission request for Android Tiramisu (API 33) and above.
     * @param activity The activity from which the permission is requested.
     * @param addPermissionListener Function to add the permission result listener.
     * @param callBack Callback to handle the result of the permission request.
     */
    fun requestNotificationPermission(
        activity: Activity,
        addPermissionListener: (RequestPermissionsResultListener?) -> Unit,
        callBack: ResultCallBack
    ) {
        if(onGoing) {
            return callBack.onResult(PermissionType.UNKNOWN, "ON_GOING")
        }

        isNotificationGranted = hasNotificationPermission(activity)
        isOverlayGranted = hasOverlayPermission(activity)

        // If both permissions are already granted, invoke the callback with GRANTED
        if(isNotificationGranted && isOverlayGranted) {
            return callBack.onResult(PermissionType.GRANTED, null)
        }

        // If permission is already granted, invoke the callback with null
        if(listener == null) {
            listener = PermissionListener(
                activity,
                // Callback to handle the result of the permission request
                object : ResultCallBack {
                    override fun onResult(permissionType: PermissionType, errorCode: String?) {
                        if(errorCode == null) {
                            if(permissionType == PermissionType.NOTIFICATION) {
                                isNotificationGranted = true
                            } else if(permissionType == PermissionType.OVERLAY) {
                                isOverlayGranted = true
                            }
                            if(isNotificationGranted && isOverlayGranted) {
                                onGoing = false
                                callBack.onResult(PermissionType.GRANTED, null)
                                return
                            }
                            // Request the other permission if not granted
                            grantPermission(activity)
                            return
                        }
                        // If permission is denied, invoke the callback with the error code
                        if(permissionType == PermissionType.NOTIFICATION) {
                            isNotificationGranted = false
                        } else if(permissionType == PermissionType.OVERLAY) {
                            isOverlayGranted = false
                        }
                    }
                }
            )
            listener?.let { addPermissionListener(it) }
        }

        // Set the onGoing flag to true to indicate that a permission request is in progress
        onGoing = true
        // Request notification permission if not granted
        grantPermission(activity)
    }
}