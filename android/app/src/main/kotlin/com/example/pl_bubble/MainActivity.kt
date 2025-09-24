package com.example.pl_bubble

import android.content.Intent
import android.util.Log
import com.example.pl_bubble.bubble.utils.ext.isServiceRunningInForeground
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "com.example.pl_bubble/bubble"
        ).setMethodCallHandler { call, result ->
            Log.d("MainActivity", "Method call received: ${call.method}")
            if(!context.isServiceRunningInForeground(ActiveBubbleService::class.java)) {
                val intent = Intent(context, ActiveBubbleService::class.java)
                context.startForegroundService(intent)
            }
        }
    }
}
