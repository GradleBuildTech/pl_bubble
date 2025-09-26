package com.example.pl_bubble

import android.content.Context
import com.example.pl_bubble.models.toBubbleConfig
import com.example.pl_bubble.utils.ChannelConstant
import io.flutter.plugin.common.MethodChannel

/*
    * Singleton service to manage communication between Flutter and native Android code.
    * Handles method calls from Flutter and initializes the ActiveBubbleService.
 */
class ChannelService private constructor(){
    // To ensure the service is only initialized once.
    private var isRunning = false

    // Instance of the ActiveBubbleService to manage bubble notifications.
    lateinit var activeBubbleService: ActiveBubbleService

    companion object {
        @Volatile
        private var INSTANCE: ChannelService? = null

        fun getInstance(): ChannelService {
            return INSTANCE ?: synchronized(this) {
                val instance = ChannelService()
                INSTANCE = instance
                instance
            }
        }
    }

    // Initializes the ActiveBubbleService with the provided configuration.
    private  fun initActivateService(argument: Any) {
        if(argument !is Map<*, *>) return
        val bubbleConfig = argument.toBubbleConfig()
        if(bubbleConfig == null) return
        ServiceInstance.bubbleConfig = bubbleConfig
        activeBubbleService = ActiveBubbleService()
    }

    // Handles method calls from Flutter and performs corresponding actions.
    fun doAction(
        context: Context,
        method: String,
        argument: Any,
        result: MethodChannel.Result
    ) {
        when(method)  {
            ChannelConstant.INITIAL_BUBBLE_SERVICE_METHOD ->
                if (!isRunning) {
                    initActivateService(argument)
                    isRunning = true
                }
            ChannelConstant.SHOW_BUBBLE_METHOD ->
                if (isRunning) {
                    activeBubbleService.showBubble()
                }
        }
    }

}