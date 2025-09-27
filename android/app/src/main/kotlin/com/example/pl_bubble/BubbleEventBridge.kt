package com.example.pl_bubble

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
    val bubbleManager: BubbleManager
) : EventChannel.StreamHandler{

    @Volatile
    private var eventSink: EventChannel.EventSink? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    private var isRunning = false

    // Initializes the bubble service with the provided configuration
    private fun initialBubbleService(arguments: Any?) {
        if(arguments !is Map<*, *>) return
        val bubbleConfig = arguments.toBubbleConfig()
        if(bubbleConfig == null) return
        ServiceInstance.bubbleConfig = bubbleConfig
        bubbleManager.initialize(bubbleConfig)
        isRunning = true
    }

    // Called when the event channel is listened to from Flutter
    override fun onListen(
        arguments: Any?,
        events: EventChannel.EventSink?
    ) {
        if(isRunning) return
        initialBubbleService(arguments)

        eventSink = events
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