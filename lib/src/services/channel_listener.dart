import 'package:flutter/services.dart';
import 'package:pl_bubble/src/utils/logger.dart';

import '../models/bubble_event_data.dart';
import '../models/bubble_events.dart';
import '../utils/constants.dart';

/// Singleton class to listen to native channel events from Android/iOS.
///
/// Usage:
/// ```dart
/// final subscription = ChannelListener.listenToEventBridge(
///   onEvent: (event) {
///     print("Received bubble event: $event");
///   },
/// );
///
/// // Later, when no longer needed:
/// subscription.cancel();
/// ```
class ChannelListener {
  ChannelListener._();

  static const MethodChannel _channel = MethodChannel(
    BubbleConstants.methodChannelName,
  );

  static const String _eventBridge = "eventBridge";

  static final List<ValueChanged<BubbleEvent>> _listeners = [];

  static bool _isInitialized = false;

  /// Start listening to event bridge calls from native side.
  /// Returns a [VoidCallback] (unsubscribe function) to stop listening.
  static VoidCallback listenToEventBridge({
    required ValueChanged<BubbleEvent> onEvent,
  }) {
    if (_isInitialized) return () {};

    Logger.d("ChannelListener", "Listening to event bridge");

    _channel.setMethodCallHandler(_handleMethodCall);
    _isInitialized = true;

    _listeners.add(onEvent);

    return () => _listeners.remove(onEvent);
  }

  /// Handle method call from native side
  static Future<void> _handleMethodCall(MethodCall call) async {
    try {
      Logger.d("ChannelListener", "Received method call: ${call.method}");

      switch (call.method) {
        case _eventBridge:
          if (call.arguments is Map) {
            final data = Map<String, dynamic>.from(call.arguments);
            final event = BubbleEventData.fromJson(data).event;
            for (final listener in List<ValueChanged<BubbleEvent>>.from(
              _listeners,
            )) {
              listener(event);
            }
          } else {
            Logger.d(
              "ChannelListener",
              "⚠️ ChannelListener: Unexpected arguments for $_eventBridge",
            );
          }
          break;

        default:
          Logger.d(
            "ChannelListener",
            "⚠️ ChannelListener: Unhandled method ${call.method}",
          );
      }
    } catch (e, stack) {
      Logger.d("ChannelListener", "❌ ChannelListener error: $e\n$stack");
    }
  }
}
