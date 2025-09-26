package com.example.pl_bubble

import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.pl_bubble.bubble.event.BubbleListener
import com.example.pl_bubble.bubble.notification.NotificationHelper
import com.example.pl_bubble.bubble.service.BaseBubbleService
import com.example.pl_bubble.bubble.service.BuBubbleBuilder
import com.example.pl_bubble.bubble.utils.BubbleEdgeSide
import com.example.pl_bubble.frame.FlutterFrame
import com.example.pl_bubble.utils.BUBBLE_NOTIFICATION_ID
import com.example.pl_bubble.utils.CHANNEL_NAME
import com.example.pl_bubble.utils.NOTIFICATION_CHANNEL_ID

/*
    * ActiveBubbleService class to manage bubble lifecycle and interactions.
 */
class ActiveBubbleService : BaseBubbleService() {

    companion object {
        const val TAG = "ActiveBubbleService"
        const val THROW_EXCEPTION_BUBBLE_CONFIG = "BubbleConfig is null, please initialize BubbleConfig before using the service."
        const val DEFAULT_CONTENT_TEXT = "This is a bubble service notification"
        const val DEFAULT_CONTENT_TITLE = "Bubble Service"
    }

    //Get bubble config from ServiceInstance
    private val bubbleConfig by lazy { ServiceInstance.bubbleConfig }

    // Notification builder
    private lateinit var bubbleNotificationBuilder: NotificationCompat.Builder

    // Notification helper instance
    private val notificationHelper: NotificationHelper
        get() = NotificationHelper(this, NOTIFICATION_CHANNEL_ID, CHANNEL_NAME)

    override fun onCreate() {
        super.onCreate()
        startNotificationForeground()
        if(bubbleConfig?.showBubbleWhenInit == true) {
            showBubble()
        }
    }

    // Configure the bubble using BuBubbleBuilder
    override fun configBubble(): BuBubbleBuilder {
        if(bubbleConfig == null) {
            throw IllegalStateException(THROW_EXCEPTION_BUBBLE_CONFIG)
        }
        val bubbleWidth = bubbleConfig!!.width.toInt()
        val bubbleHeight = bubbleConfig!!.height.toInt()


        // Create close bubble view
        val closeBubbleView = ImageView(this).apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close_bubble)).apply {
                layoutParams = ViewGroup.LayoutParams(bubbleWidth, bubbleHeight)
            }
        }

        ///Add click to bubble
        val bubbleView =  ImageView(this).apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_flutter)).apply {
                layoutParams = ViewGroup.LayoutParams(bubbleWidth, bubbleHeight)
            }
        }

        // Create expand view if expandBubbleConfig is not null
        val expandView = if(bubbleConfig!!.expandBubbleConfig != null) FlutterFrame.createExpandFrameFromConfig(
            expandBubbleConfig = bubbleConfig!!.expandBubbleConfig!!,
            context = this
        ) else null


        val builder =  BuBubbleBuilder(this)
            .bubbleView(bubbleView)
            .closeView(closeBubbleView)
            .fromBubbleConfig(bubbleConfig!!)
            .bubbleListener(
                object : BubbleListener {
                    override fun onFingerDown(x: Float, y: Float) { }
                    override fun onFingerMove(x: Float, y: Float) { }
                    override fun onFingerUp(x: Float, y: Float) { }
                }
            )

        if(expandView != null) {
            builder.expandView(expandView)
        }

        return builder
    }

    // Start foreground notification for the bubble service
    override fun startNotificationForeground() {
        if(bubbleConfig == null) {
            throw IllegalStateException(THROW_EXCEPTION_BUBBLE_CONFIG)
        }

        bubbleNotificationBuilder = notificationHelper.initNotificationBuilder(
            smallIcon = R.drawable.ic_flutter,
            contentTitle = bubbleConfig?.notificationConfig?.contentTitle ?: DEFAULT_CONTENT_TITLE,
            contentText = bubbleConfig?.notificationConfig?.contentText ?: DEFAULT_CONTENT_TEXT
        )
        notificationHelper.createNotificationChannel()
        startForeground(BUBBLE_NOTIFICATION_ID, bubbleNotificationBuilder.build())
    }

    override fun clearCachedData() { }
    override fun changeBubbleEdgeSideListener(edgeSide: BubbleEdgeSide) { }
    override fun onCheckBubbleTouchLeavesListener(x: Float, y: Float) { }
    override fun onCloseBubbleListener() { }
    override fun refreshBubbleIconStateListener(isClearCachedData: Boolean) { }
}