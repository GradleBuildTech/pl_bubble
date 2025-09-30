package com.example.pl_bubble.frame

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.example.pl_bubble.models.ExpandBubbleConfig
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor

object FlutterFrame {
    ///Create Flutter Frame  From Expand Bubble Config
    fun createExpandFrameFromConfig(expandBubbleConfig: ExpandBubbleConfig, context: Context): View {
        val flutterEngine = FlutterEngine(context)
        flutterEngine.navigationChannel.setInitialRoute(expandBubbleConfig.routeEngine)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        val flutterView = FlutterView(context)
        flutterView.layoutParams = ViewGroup.LayoutParams(
            expandBubbleConfig.width.toInt(),
            expandBubbleConfig.height.toInt()
        )
        flutterView.attachToFlutterEngine(flutterEngine)
        return flutterView
    }

    ///Create Bubble Frame
    fun createBubbleFrame(context: Context): View {
        val flutterEngine = FlutterEngine(context)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        val flutterView = FlutterView(context)
        flutterView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        flutterView.attachToFlutterEngine(flutterEngine)
        return flutterView
    }
}