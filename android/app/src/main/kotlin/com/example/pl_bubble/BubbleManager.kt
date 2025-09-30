package com.example.pl_bubble

import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.pl_bubble.bubble.event.BubbleListener
import com.example.pl_bubble.bubble.notification.NotificationHelper
import com.example.pl_bubble.bubble.service.BaseBubbleService
import com.example.pl_bubble.bubble.service.BuBubbleBuilder
import com.example.pl_bubble.bubble.utils.BubbleEdgeSide
import com.example.pl_bubble.events.BubbleEvent
import com.example.pl_bubble.frame.FlutterFrame
import com.example.pl_bubble.models.BubbleConfig

import com.example.pl_bubble.utils.BUBBLE_NOTIFICATION_ID
import com.example.pl_bubble.utils.CHANNEL_NAME
import com.example.pl_bubble.utils.NOTIFICATION_CHANNEL_ID
import kotlinx.coroutines.flow.MutableSharedFlow

/*
    * ActiveBubbleService class to manage bubble lifecycle and interactions.
    * Implements singleton pattern with lazy initialization for reusability.
 */
@Suppress("DEPRECATION")
class BubbleManager : BaseBubbleService() {
    companion object {
        const val TAG = "ActiveBubbleService"
        const val THROW_EXCEPTION_BUBBLE_CONFIG = "currentConfig is null, please initialize currentConfig before using the service."
        const val DEFAULT_CONTENT_TEXT = "This is a bubble service notification"
        const val DEFAULT_CONTENT_TITLE = "Bubble Service"

        @Volatile
        private var INSTANCE: BubbleManager? = null
        
        // Get singleton instance
        fun getInstance(): BubbleManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BubbleManager().also { INSTANCE = it }
            }
        }
        // Clear instance (useful for testing or cleanup)
        fun clearInstance() {
            INSTANCE = null
        }
    }


    /*
        * Event sink to send bubble events back to Flutter
        * Uses MutableSharedFlow for coroutine support and multiple subscribers
     */
    private val eventSink: MutableSharedFlow<BubbleEvent> = MutableSharedFlow()

    private fun sink(event: BubbleEvent) {
        eventSink.tryEmit(event)
    }

    suspend fun listenToEventSink(bubbleEvent: (BubbleEvent) -> Unit) {
        eventSink.collect { event -> bubbleEvent(event) }
    }


    // State management
    private var isInitialized = false
    private var currentConfig: BubbleConfig? = null
    

    // Notification builder
    private lateinit var bubbleNotificationBuilder: NotificationCompat.Builder

    // Notification helper instance
    private val notificationHelper: NotificationHelper
        get() = NotificationHelper(this, NOTIFICATION_CHANNEL_ID, CHANNEL_NAME)


    /**
     * Check if service is initialized
     */
    fun isServiceInitialized(): Boolean = isInitialized


    /**
     * Reset service to uninitialized state
     */
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    /*
        * Clean up resources on service destruction
     */
    override fun onDestroy() {
        clearInstance()
        super.onDestroy()
    }

    // After configuring the bubble, start the foreground notification
    override fun afterConfigureBubble() {
        super.afterConfigureBubble()
        startNotificationForeground()
    }

    // Configure the bubble using BuBubbleBuilder
    override fun configBubble(): BuBubbleBuilder {
        try {
            currentConfig = ServiceInstance.bubbleConfig ?: currentConfig

            if(currentConfig == null) {
                throw IllegalStateException(THROW_EXCEPTION_BUBBLE_CONFIG)
            }
            val bubbleWidth = currentConfig!!.width.toInt()
            val bubbleHeight = currentConfig!!.height.toInt()


            // Create close bubble view
            val closeBubbleView = ImageView(this).apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close_bubble)).apply {
                    layoutParams = ViewGroup.LayoutParams(bubbleWidth, bubbleHeight)
                }
            }

            ///Add click to bubble
            val bubbleView = ImageView(this).apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_flutter)).apply {
                    layoutParams = ViewGroup.LayoutParams(bubbleWidth, bubbleHeight)
                }
                setOnClickListener {
                    sink(BubbleEvent.OnClickBubble)
                }
            }

            // Create expand bubble view if config is provided
            val expandView = if(currentConfig!!.expandBubbleConfig != null) FlutterFrame.createExpandFrameFromConfig(
                expandBubbleConfig = currentConfig!!.expandBubbleConfig!!,
                context = this
            ) else null


            val builder =  BuBubbleBuilder(this)
                .bubbleView(bubbleView)
                .closeView(closeBubbleView)
                .bubbleAnimatedClose(false)
                .fromBubbleConfig(currentConfig!!)
                .bubbleListener(
                    object : BubbleListener {
                        override fun onFingerDown(x: Float, y: Float) { }
                        override fun onFingerMove(x: Float, y: Float) {
                            sink(BubbleEvent.BubbleMovement(x.toDouble(), y.toDouble()))
                        }
                        override fun onFingerUp(x: Float, y: Float) { }
                    }
                )

            if(expandView != null) {
                builder.expandView(expandView)
            }

            return builder
        } catch (e: Exception) {
            Log.d(TAG, "Error configuring bubble: ${e.message}")
            throw IllegalStateException("Error configuring bubble: ${e.message}", e)
        }
    }

    // Start foreground notification for the bubble service
    override fun startNotificationForeground() {
        try {
            if(currentConfig == null) {
                throw IllegalStateException(THROW_EXCEPTION_BUBBLE_CONFIG)
            }
            bubbleNotificationBuilder = notificationHelper.initNotificationBuilder(
                smallIcon = R.drawable.ic_flutter,
                contentTitle = currentConfig?.notificationConfig?.contentTitle ?:  DEFAULT_CONTENT_TITLE,
                contentText = currentConfig?.notificationConfig?.contentText ?: DEFAULT_CONTENT_TEXT
            )
            notificationHelper.createNotificationChannel()
            startForeground(BUBBLE_NOTIFICATION_ID, bubbleNotificationBuilder.build())
        } catch (e: Exception) {
            Log.d(TAG, "Error starting foreground notification: ${e.message}")
            throw IllegalStateException("Error starting foreground notification: ${e.message}", e)
        }
    }

    /**
     * Handle intent arguments passed to the service
     */
    override fun handleIntentArguments(intent: Intent) {
        // Get BubbleConfig from ServiceInstance if available
        val config = intent.getParcelableExtra<BubbleConfig>("BUBBLE_CONFIG")
        Log.d(TAG, "Received BubbleConfig from intent: $config")
        if(config != null) {
            currentConfig = config
            isInitialized = true
        }
    }

    override fun clearCachedData() { }
    override fun changeBubbleEdgeSideListener(edgeSide: BubbleEdgeSide) { }
    override fun onCheckBubbleTouchLeavesListener(x: Float, y: Float) { }
    override fun onCloseBubbleListener() { }
    override fun refreshBubbleIconStateListener(isClearCachedData: Boolean) { }
}