package com.example.pl_bubble

import android.os.Bundle
import android.os.PersistableBundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity : FlutterActivity() {

    /// Singleton instance of ChannelService to handle method calls from Flutter.
    private val channelService: ChannelService = ChannelService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    /// Configures the Flutter engine and sets up a MethodChannel to communicate with Flutter.
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        channelService.initService(flutterEngine, this)
    }
}
