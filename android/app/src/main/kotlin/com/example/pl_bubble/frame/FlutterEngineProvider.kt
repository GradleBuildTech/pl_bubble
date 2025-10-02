package com.example.pl_bubble.frame

import android.content.Context
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor

/*
    * Singleton object to manage a single instance of FlutterEngine
    * Ensures that the Flutter engine is reused across the application
 */
object FlutterEngineProvider {
    var flutterEngine: FlutterEngine? = null

    /// Get or create a FlutterEngine instance
    fun getEngine(context: Context): FlutterEngine {
        if (flutterEngine == null) {
            flutterEngine = FlutterEngine(context.applicationContext).apply {
                dartExecutor.executeDartEntrypoint(
                    DartExecutor.DartEntrypoint.createDefault()
                )
            }
        }
        return flutterEngine!!
    }
}