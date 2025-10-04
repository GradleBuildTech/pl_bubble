package com.example.pl_bubble

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.pl_bubble.permissions.BubblePermission
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

@Suppress("DEPRECATION")
class BubblePlugin : FlutterPlugin, ActivityAware {

    /// Flag to track if the service has been started
    private var isServiceStarted = false

    /// ActivityPluginBinding to manage activity lifecycle events
    private var activityBinding: ActivityPluginBinding? = null

    /// Reference to the FlutterEngine
    private var flutterEngine: FlutterEngine? = null

    companion object {
        const val TAG = "BubblePlugin"
    }

    private var service : BubbleHandler? = null

    /// BroadcastReceiver to listen for service creation events
    private val serviceCreateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "SERVICE_CREATED" && !isServiceStarted) {
                isServiceStarted = true
                service?.startListeningService()
            }
        }
    }

    // ----------------- FlutterPlugin -----------------
    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        flutterEngine = binding.flutterEngine
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        flutterEngine = null
    }

    // ----------------- ActivityAware -----------------
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityBinding = binding

        val activity = binding.activity
        ServiceInstance.applicationContext = activity

        // register BroadcastReceiver
        LocalBroadcastManager.getInstance(activity)
            .registerReceiver(serviceCreateReceiver, IntentFilter("SERVICE_CREATED"))

        service = BubbleHandler.getInstance() ?: BubbleHandler(
            activity,
            BubblePermission.getInstance(),
        ) { listener ->
            if(listener == null) return@BubbleHandler
            activityBinding?.addRequestPermissionsResultListener(listener)
        }

        // init service với context + flutterEngine
        flutterEngine?.let { engine ->
            service?.initMainBubbleService(activity, engine)
        }?.run {
            Log.d(TAG, "FlutterEngine is null, cannot initialize service.")
        }

    }

    override fun onDetachedFromActivityForConfigChanges() {
        cleanup()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivity() {
        cleanup()
    }

    private fun cleanup() {
        activityBinding?.activity?.let {
            LocalBroadcastManager.getInstance(it)
                .unregisterReceiver(serviceCreateReceiver)
        }
        activityBinding = null
        isServiceStarted = false
    }
}
