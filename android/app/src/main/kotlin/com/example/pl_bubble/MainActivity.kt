package com.example.pl_bubble

import com.example.pl_bubble.utils.ChannelConstant
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    /// Singleton instance of ChannelService to handle method calls from Flutter.
    private val channelService: ChannelService = ChannelService.getInstance()

    /// Configures the Flutter engine and sets up a MethodChannel to communicate with Flutter.
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            ChannelConstant.SERVICE_CHANNEL
        ).setMethodCallHandler { call , result ->
            channelService.doAction(
                result = result,
                method = call.method,
                argument = call.arguments,
                context = this,
                flutterEngine = flutterEngine,
            )
        }
    }
}
