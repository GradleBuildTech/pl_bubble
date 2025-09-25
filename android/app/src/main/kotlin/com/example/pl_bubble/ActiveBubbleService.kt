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
import com.example.pl_bubble.models.BubbleConfig
import com.example.pl_bubble.utils.BUBBLE_NOTIFICATION_ID
import com.example.pl_bubble.utils.CHANNEL_NAME
import com.example.pl_bubble.utils.NOTIFICATION_CHANNEL_ID

class ActiveBubbleService constructor(
    val bubbleConfig: BubbleConfig
): BaseBubbleService() {
    private lateinit var bubbleNotificationBuilder: NotificationCompat.Builder

    private val notificationHelper: NotificationHelper
        get() = NotificationHelper(this, NOTIFICATION_CHANNEL_ID, CHANNEL_NAME)

    override fun onCreate() {
        super.onCreate()
        startNotificationForeground()
        if(bubbleConfig.showBubbleWhenInit) {
            showBubble()
        }
    }

    override fun configBubble(): BuBubbleBuilder {

        val closeBubbleView = ImageView(this).apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close_bubble)).apply {
                layoutParams = ViewGroup.LayoutParams(100, 100)
            }
        }

        ///Add click to bubble
        val bubbleView =  ImageView(this).apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_flutter)).apply {
                layoutParams = ViewGroup.LayoutParams(150, 150)
            }
        }

        val expandView = if(bubbleConfig.expandBubbleConfig != null) FlutterFrame.createExpandFrameFromConfig(
            expandBubbleConfig = bubbleConfig.expandBubbleConfig,
            context = this
        ) else null


        val builder =  BuBubbleBuilder(this)
            .bubbleView(bubbleView)
            .closeView(closeBubbleView)
            .fromBubbleConfig(bubbleConfig)
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

    override fun startNotificationForeground() {
        bubbleNotificationBuilder = notificationHelper.initNotificationBuilder(
            smallIcon = R.drawable.ic_flutter,
            contentTitle = "Test Bubble Service",
            contentText = "This is a test bubble service notification",
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