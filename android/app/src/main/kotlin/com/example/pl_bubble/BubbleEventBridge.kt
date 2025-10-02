package com.example.pl_bubble

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.pl_bubble.bubble.utils.ext.isServiceRunningInForeground
import com.example.pl_bubble.events.BubbleEvent
import com.example.pl_bubble.events.BubbleSendFormat
import com.example.pl_bubble.events.toMap
import com.example.pl_bubble.models.toBubbleConfig
import com.example.pl_bubble.utils.ChannelConstant
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

/*
    * Bridge class to handle event channel communication for bubble events
    * Initializes the bubble service with provided configuration
    * Manages event sink for sending events back to Flutter
 */
class BubbleEventBridge(
    val activityContext: Context,
    arguments: Any?,
    flutterEngine: FlutterEngine,
) {

    init {
        initialBubbleService(arguments)
    }

    companion object {
        const val TAG = "BubbleEventBridge"
    }


    private val channel = MethodChannel(
        flutterEngine.dartExecutor.binaryMessenger,
        ChannelConstant.SERVICE_CHANNEL
    )

    // Flag to prevent multiple initializations
    private var isRunning = false

    // Initializes the bubble service with the provided configuration
    private fun initialBubbleService(arguments: Any?) {

        try {
            if(arguments !is Map<*, *>) return
            val bubbleConfig = arguments.toBubbleConfig()
            if(bubbleConfig == null) return
            // Prevent multiple initializations
            ServiceInstance.bubbleConfig = bubbleConfig

            // Initialize the bubble manager with the configuration
            if(!activityContext.isServiceRunningInForeground(BubbleManager::class.java)) {
                val intent = Intent(activityContext, BubbleManager::class.java)
                // Add BubbleConfig as intent extra for service initialization
                intent.putExtra("BUBBLE_CONFIG", bubbleConfig)
                ContextCompat.startForegroundService(activityContext, intent)
            }

        } catch (exception: Exception) {
            Log.d(TAG, "Error initializing bubble service: ${exception.message}")
            channel.invokeMethod(ChannelConstant.EVENT_BRIDGE, BubbleSendFormat(event = BubbleEvent.ErrorEvent(exception.message ?: "Unknown error")))
            throw exception
        }
    }

    /**
     * Starts listening to bubble events and forwards them to Flutter
     * This method sets up the event stream from BubbleManager to Flutter
     */
    fun startEventListening() {
        if (isRunning) {
            Log.d(TAG, "Event listening is already running")
            return
        }

        // Get the BubbleManager instance
        val bubbleManger = BubbleManager.getInstance()

        // Log and return if instance is null
        if(bubbleManger == null) {
            Log.d(TAG, "BubbleManager instance is null, cannot start event listening")
            return
        }

        Log.d(TAG, "Starting event listening")

        isRunning = true
        bubbleManger.listenToEventSink {
            if (!isRunning) return@listenToEventSink
            channel.invokeMethod(ChannelConstant.EVENT_BRIDGE, BubbleSendFormat(event = it).toMap())
        }.run {
            //Print if instance is null
            Log.e(TAG, "BubbleManager instance is null, cannot listen to events")
        }
    }
}