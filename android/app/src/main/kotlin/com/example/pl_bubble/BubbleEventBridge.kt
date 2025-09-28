package com.example.pl_bubble

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.pl_bubble.bubble.utils.ext.isServiceRunningInForeground
import com.example.pl_bubble.events.BubbleEvent
import com.example.pl_bubble.events.BubbleSendFormat
import com.example.pl_bubble.models.toBubbleConfig
import io.flutter.plugin.common.EventChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/*
    * Bridge class to handle event channel communication for bubble events
    * Initializes the bubble service with provided configuration
    * Manages event sink for sending events back to Flutter
 */
class BubbleEventBridge(
    val bubbleManager: BubbleManager,
    val activityContext: Context
) : EventChannel.StreamHandler{

    companion object {
        const val TAG = "BubbleEventBridge"
    }

    @Volatile
    private var eventSink: EventChannel.EventSink? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

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
            eventSink?.success(
                BubbleEvent.ErrorEvent
                    (message = "Error initializing bubble service: ${exception.message}")
            )
            throw exception
        }
    }

    // Called when the event channel is listened to from Flutter
    override fun onListen(
        arguments: Any?,
        events: EventChannel.EventSink?
    ) {
        if(isRunning) return
        eventSink = events

        initialBubbleService(arguments)

        scope.launch {
            bubbleManager.listenToEventSink { event ->
                eventSink?.success(BubbleSendFormat(event = event))
            }
        }

    }

    // Called when the event channel is cancelled from Flutter
    override fun onCancel(arguments: Any?) {
        eventSink = null
        scope.coroutineContext.cancel()
    }

}