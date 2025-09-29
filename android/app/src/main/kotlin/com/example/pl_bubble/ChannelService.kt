package com.example.pl_bubble

import android.content.Context
import android.util.Log
import com.example.pl_bubble.utils.ChannelConstant
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

/*
    * Singleton service to manage communication between Flutter and native Android code.
    * Handles method calls from Flutter and initializes the ActiveBubbleService.
 */
class ChannelService private constructor(){

    // Instance of the ActiveBubbleService to manage bubble notifications.
    private val  bubbleManager: BubbleManager = BubbleManager.getInstance()

    private var bubbleEventBride: BubbleEventBridge? = null

    companion object {

        const val TAG = "ChannelService"

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

    private fun initialBubbleService(
        argument: Any,
        context: Context,
        flutterEngine: FlutterEngine,
        result: MethodChannel.Result? = null
    ) {
        Log.d(TAG, "initialBubbleService called")
        // Initialize the bubble service with the provided configuration
        if(bubbleEventBride != null) return

        // Initialize the BubbleEventBridge to handle event communication
        bubbleEventBride = BubbleEventBridge(
            bubbleManager = bubbleManager,
            activityContext = context,
            arguments = argument,
            flutterEngine = flutterEngine,
        )

    }
    // Handles method calls from Flutter and performs corresponding actions.
    fun doAction(
        method: String,
        argument: Any,
        context: Context,
        flutterEngine: FlutterEngine,
        result: MethodChannel.Result
    ) {
        try {
            when(method)  {
                ChannelConstant.SHOW_BUBBLE_METHOD -> showBubble()
                ChannelConstant.EXPAND_BUBBLE_METHOD -> showExpandBubble(argument)
                ChannelConstant.INITIAL_BUBBLE_METHOD  -> initialBubbleService(argument, context, flutterEngine, result)
                else -> result.notImplemented()
            }
        } catch (exception: Exception) {
            Log.d(TAG, "Error handling method call: ${exception.message}")
            throw exception
        }
    }
}