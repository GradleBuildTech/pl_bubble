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

    // Manages bubble-related actions and interactions
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
    }

    //[MethodChannel Handler]
    private fun showBubble() {
        BubbleManager.getInstance()?.showBubble()
    }

    private fun showExpandBubble(argument: Any) {
        val isRemoveBubble = argument as? Boolean == true
        BubbleManager.getInstance()?.showExpandBubble(isRemoveBubble)
    }

    private fun closeExpandBubble() {
        BubbleManager.getInstance()?.hideExpandBubble(showBubbleAfterClose = false)
    }

    private fun initialBubbleService(
        argument: Any,
        context: Context,
        flutterEngine: FlutterEngine,
    ) {
        Log.d(TAG, "initialBubbleService called")
        // Initialize the bubble service with the provided configuration
        if(bubbleEventBride != null) return

        // Initialize the BubbleEventBridge to handle event communication
        bubbleEventBride = BubbleEventBridge(
            activityContext = context,
            arguments = argument,
            flutterEngine = flutterEngine,
        )
    }


    fun startListeningService() {
       bubbleEventBride?.startEventListening()
    }

    // Handles method calls from Flutter and performs corresponding actions.
    private fun doAction(
        method: String,
        argument: Any,
        context: Context,
        flutterEngine: FlutterEngine,
        result: MethodChannel.Result
    ) {
        if(method != ChannelConstant.INITIAL_BUBBLE_METHOD && BubbleManager.getInstance()?.isServiceInitialized() == false) {
            Log.d(TAG, "Bubble service not initialized. Please initialize before calling other methods.")
            result.error("BubbleServiceNotInitialized", "Please initialize bubble service before calling other methods.", null)
            return
        }

        try {
            when(method)  {
                ChannelConstant.SHOW_BUBBLE_METHOD -> showBubble()
                ChannelConstant.EXPAND_BUBBLE_METHOD -> showExpandBubble(argument)
                ChannelConstant.CLOSE_EXPAND_BUBBLE_METHOD -> closeExpandBubble()
                ChannelConstant.INITIAL_BUBBLE_METHOD  -> initialBubbleService(argument, context, flutterEngine)
                else -> result.notImplemented()
            }
        } catch (exception: Exception) {
            Log.d(TAG, "Error handling method call: ${exception.message}")
            throw exception
        }
    }

    /*
        * Initializes the MethodChannel to communicate with Flutter.
        * Sets up the method call handler to process incoming method calls.
     */
    fun initMainBubbleService(
        context: Context,
        flutterEngine: FlutterEngine,
    ) {
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            ChannelConstant.SERVICE_CHANNEL
        ).setMethodCallHandler { call , result ->
            Log.d(TAG, "Method call received: ${call.method} with arguments: ${call.arguments}")
            doAction(
                result = result,
                method = call.method,
                argument = call.arguments,
                context = context,
                flutterEngine = flutterEngine,
            )
        }
    }

}