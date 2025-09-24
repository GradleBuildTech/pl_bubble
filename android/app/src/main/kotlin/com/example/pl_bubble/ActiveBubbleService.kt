package com.example.pl_bubble

import android.graphics.Point
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
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor

class ActiveBubbleService: BaseBubbleService() {
    private lateinit var bubbleNotificationBuilder: NotificationCompat.Builder
    private lateinit var flutterEngine: FlutterEngine
    private lateinit var flutterView: FlutterView

    private val notificationHelper: NotificationHelper
        get() = NotificationHelper(this, NOTIFICATION_CHANNEL_ID, CHANNEL_NAME)

    override fun onCreate() {
        super.onCreate()
        startNotificationForeground()
        showBubble()
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
        bubbleView.setOnClickListener {
            Log.d("ActiveBubbleService", "Bubble clicked!")
            showExpandBubble()
        }

        flutterEngine = FlutterEngine(this)
        flutterEngine.navigationChannel.setInitialRoute("/expandBubble") // optional: set Flutter route
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // Create FlutterView and attach to engine
        flutterView = FlutterView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                400
            )
            attachToFlutterEngine(flutterEngine)
        }

        return BuBubbleBuilder(this)
            .bubbleView(bubbleView)
            .expandView(flutterView)
            .closeView(closeBubbleView)
            .bubbleStartPoint(Point(0, 400))
            .bubbleForceDragging(true)
            .bubbleAnimateToEdgeEnabled(true)
            .bubbleDistanceToClose(200)
            .bubbleCloseBottomDist(100)
            .bubbleAnimatedClose(false)
            .bubbleListener(
                object : BubbleListener {
                    override fun onFingerDown(x: Float, y: Float) { }
                    override fun onFingerMove(x: Float, y: Float) { }
                    override fun onFingerUp(x: Float, y: Float) { }
                }
            )
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