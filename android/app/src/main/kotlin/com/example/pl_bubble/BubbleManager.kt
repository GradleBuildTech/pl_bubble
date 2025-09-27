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
import com.example.pl_bubble.bubble.utils.ext.isServiceRunningInForeground
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
class BubbleManager : BaseBubbleService() {

    companion object {
        const val TAG = "ActiveBubbleService"
        const val THROW_EXCEPTION_BUBBLE_CONFIG = "currentConfig is null, please initialize currentConfig before using the service."
        const val THROW_EXCEPTION_NOT_INITIALIZED = "ActiveBubbleService must be initialized before use. Call initialize() first."
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
     * Initialize the service with configuration
     * Can be called multiple times but only initializes once
     * Subsequent calls with same config are ignored
     */
    fun initialize(config: BubbleConfig? = null) {
        // If already initialized with same config, do nothing
        if (isInitialized && currentConfig == config) {
            return
        }
        
        // If initialized with different config, reset and reinitialize
        if (isInitialized && currentConfig != config) {
            reset()
        }

        currentConfig = config
        isInitialized = true
        
        // Start the service if not already running
        if (!this.isServiceRunningInForeground(BubbleManager::class.java)) {
            val intent = Intent(this, BubbleManager::class.java)
            ContextCompat.startForegroundService(this, intent)
        }
    }

    /**
     * Check if service is initialized
     */
    fun isServiceInitialized(): Boolean = isInitialized

    /**
     * Reset service to uninitialized state
     */
    fun reset() {
        isInitialized = false
        currentConfig = null
    }

    /**
     * Ensure service is initialized before performing operations
     */
    private fun ensureInitialized() {
        if (!isInitialized) {
            throw IllegalStateException(THROW_EXCEPTION_NOT_INITIALIZED)
        }
    }

    override fun onCreate() {
        super.onCreate()
        startNotificationForeground()
        if(currentConfig?.showBubbleWhenInit == true) {
            showBubble()
        }
    }

    // Configure the bubble using BuBubbleBuilder
    override fun configBubble(): BuBubbleBuilder {
        try {
            ensureInitialized()
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
                contentTitle = currentConfig?.notificationConfig?.contentTitle ?: DEFAULT_CONTENT_TITLE,
                contentText = currentConfig?.notificationConfig?.contentText ?: DEFAULT_CONTENT_TEXT
            )
            notificationHelper.createNotificationChannel()
            startForeground(BUBBLE_NOTIFICATION_ID, bubbleNotificationBuilder.build())
        } catch (e: Exception) {
            Log.d(TAG, "Error starting foreground notification: ${e.message}")
            throw IllegalStateException("Error starting foreground notification: ${e.message}", e)
        }
    }

    override fun clearCachedData() { }
    override fun changeBubbleEdgeSideListener(edgeSide: BubbleEdgeSide) { }
    override fun onCheckBubbleTouchLeavesListener(x: Float, y: Float) { }
    override fun onCloseBubbleListener() { }
    override fun refreshBubbleIconStateListener(isClearCachedData: Boolean) { }
}