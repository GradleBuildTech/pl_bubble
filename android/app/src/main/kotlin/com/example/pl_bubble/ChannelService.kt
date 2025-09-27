package com.example.pl_bubble

import android.content.Context
import com.example.pl_bubble.utils.ChannelConstant
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

/*
    * Singleton service to manage communication between Flutter and native Android code.
    * Handles method calls from Flutter and initializes the ActiveBubbleService.
 */
class ChannelService private constructor(){
    // To ensure the service is only initialized once.
    private var isRunning = false

    // Instance of the ActiveBubbleService to manage bubble notifications.
    private val  bubbleManager: BubbleManager = BubbleManager.getInstance()

    companion object {
        @Volatile
        private var INSTANCE: ChannelService? = null

        // Get singleton instance
        fun getInstance(): ChannelService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ChannelService().also { INSTANCE = it }
            }
        }

        // Clear instance (useful for testing or cleanup)
        fun clearInstance() {
            INSTANCE = null
        }
    }

    //[MethodChannel Handler]
    private fun showBubble() {
        bubbleManager.showBubble()
    }

    private fun showExpandBubble(argument: Any) {
        val isRemoveBubble = argument as? Boolean == true
        bubbleManager.showExpandBubble(isRemoveBubble)
    }
    // Handles method calls from Flutter and performs corresponding actions.
    private fun doAction(
        method: String,
        argument: Any,
    ) {
        if(bubbleManager.isServiceInitialized() == false) return
        when(method)  {
            ChannelConstant.SHOW_BUBBLE_METHOD -> showBubble()
            ChannelConstant.EXPAND_BUBBLE_METHOD -> showExpandBubble(argument)
        }
    }

    // Sets up the MethodChannel and EventChannel to communicate with Flutter.
    fun initService(flutterEngine: FlutterEngine, context: Context) {
        // Setting up MethodChannel to listen for method calls from Flutter.
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            ChannelConstant.SERVICE_CHANNEL
        ).setMethodCallHandler { call, result ->
            doAction(
                method = call.method,
                argument = call.arguments ?: Any(),
            )
        }

        // Setting up EventChannel to send events back to Flutter.
        EventChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            ChannelConstant.INITIAL_BUBBLE_SERVICE_METHOD
        ).setStreamHandler(
            BubbleEventBridge(bubbleManager)
        )
    }
}