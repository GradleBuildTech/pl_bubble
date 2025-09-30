package com.example.pl_bubble

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity : FlutterActivity() {

    /// Singleton instance of ChannelService to handle method calls from Flutter.

    /// Configures the Flutter engine and sets up a MethodChannel to communicate with Flutter.
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        ChannelService.getInstance().initService(this, flutterEngine)
    }
}
