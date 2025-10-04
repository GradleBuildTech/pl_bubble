package com.example.pl_bubble

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.pl_bubble.permissions.BubblePermission
import com.example.pl_bubble.permissions.utils.PermissionType
import com.example.pl_bubble.utils.ChannelConstant
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry.RequestPermissionsResultListener

/*
    * Singleton service to manage communication between Flutter and native Android code.
    * Handles method calls from Flutter and initializes the ActiveBubbleService.
 */
class BubbleHandler (
    private val activity: Activity,
    private val permissions: BubblePermission,
    private val addListener: (RequestPermissionsResultListener?) -> Unit,
){

    // Manages bubble-related actions and interactions
    private var bubbleEventBride: BubbleEventBridge? = null

    companion object {
        const val TAG = "ChannelService"

        const val PERMISSION_CHECK_TAG = "PermissionCheck"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: BubbleHandler? = null

        // Get singleton instance
        fun getInstance(): BubbleHandler? {
            if(INSTANCE == null) {
                Log.d(TAG, "BubbleHandler not initialized. Please initialize before use.")
            }
            return INSTANCE
        }
    }

    // Initialize the singleton instance
    init {
        INSTANCE = this
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
        result: MethodChannel.Result
    ) {
        permissions.requestPermission(
           activity = activity,
            addPermissionListener = addListener,
            object: BubblePermission.ResultCallBack {
                override fun onResult(permissionType: PermissionType, errorCode: String?) {
                    if (errorCode != null) {
                        Log.d(
                            PERMISSION_CHECK_TAG,
                            "Permission denied for bubble service. Error code: $errorCode ${permissionType.value}",
                        )
                        result.error(
                            PERMISSION_CHECK_TAG,
                            "Required permissions not granted for bubble service. Error code: $errorCode ${permissionType.value}",
                            null
                        )
                        return
                    }
                    // Proceed only if permission is granted
                    if(permissionType != PermissionType.GRANTED) return

                    // Initialize the BubbleEventBridge only once
                    if (bubbleEventBride != null) return
                    bubbleEventBride = BubbleEventBridge(
                        activityContext = context,
                        arguments = argument,
                        flutterEngine = flutterEngine,
                    )
                    result.success(true)
                }
            }
        )
    }


    fun startListeningService() {
       bubbleEventBride?.startEventListening()
    }

    /**
     * Handles method calls from Flutter and routes them to the appropriate native functions.
     * @param method The method name received from Flutter.
     * @param argument The arguments passed from Flutter.
     * @param context The Android context.
     * @param flutterEngine The Flutter engine instance.
     * @param result The MethodChannel.Result to send results back to Flutter.
     */
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
                ChannelConstant.INITIAL_BUBBLE_METHOD  -> initialBubbleService(argument, context, flutterEngine, result)
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