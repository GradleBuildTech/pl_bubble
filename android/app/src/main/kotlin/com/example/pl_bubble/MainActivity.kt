package com.example.pl_bubble

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity : FlutterActivity() {

    /// Instance of ChannelService to manage communication between Flutter and native Android
    private val service = ChannelService.getInstance()


    /// Flag to track if the service has been started
    private var isServiceStarted = false

    /// BroadcastReceiver to listen for service creation events
    private val serviceCreateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context:Context?, intent: Intent?) {
            // Handle the service creation event
            if (intent?.action == "SERVICE_CREATED" && !isServiceStarted) {
                isServiceStarted = true
                service.startListeningService()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        // Additional initialization if needed
        ServiceInstance.applicationContext = this
    }


    /// Registers a BroadcastReceiver to listen for service creation events when the activity starts.
    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(serviceCreateReceiver, IntentFilter("SERVICE_CREATED"))
    }

    /// Configures the Flutter engine and sets up a MethodChannel to communicate with Flutter.
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Store the FlutterEngine instance in ServiceInstance for later use

        // Initialize the ChannelService with the current context and FlutterEngine
        service.initMainBubbleService(this, flutterEngine)

    }


    /// Unregisters the BroadcastReceiver when the activity is destroyed to prevent memory leaks.
    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager
            .getInstance(this).unregisterReceiver(serviceCreateReceiver)
    }
}
