import 'dart:async';
import 'package:flutter/services.dart';
import '../models/bubble_config.dart';
import '../models/bubble_position.dart';
import '../models/bubble_events.dart';
import '../exceptions/bubble_exception.dart';
import '../models/bubble_event_data.dart';
import '../utils/constants.dart';

// Method channel for communicating with native bubble implementation
class BubbleChannel {
  static const MethodChannel _channel = MethodChannel(
    BubbleConstants.methodChannelName,
  );

  static const EventChannel _initialBubbleServiceChannel = EventChannel(
    'com.example.pl_bubble/initialBubbleService',
  );

  static const String _updateConfigMethod = 'updateConfig';

  // Bubble methods
  static const String _showBubbleMethod = 'showBubble';
  static const String _hideBubbleMethod = 'hideBubble';
  static const String _moveBubbleMethod = 'moveBubble';
  static const String _closeBubbleMethod = 'closeBubble';
  static const String _initializeBubbleServiceMethod = 'initialBubble';

  // Expand bubble methods
  static const String _expandBubbleMethod = 'expandBubble';
  static const String _closeExpandBubbleMethod = 'closeExpandBubble';
  static const String _hideExandBubbleMethod = 'hideExandBubble';

  // Permission methods
  static const String _hasPermissionMethod = 'hasOverlayPermission';
  static const String _requestPermissionMethod = 'requestOverlayPermission';
  static const String _getStateMethod = 'getBubbleState';

  // Initial bubble service method
  static Stream<BubbleEvent> initialBubbleService(BubbleConfig config) {
    return _initialBubbleServiceChannel
        .receiveBroadcastStream(config.toJson())
        .map((dynamic event) {
          if (event is Map<String, dynamic>) {
            return BubbleEventData.fromJson(event).event;
          }
          throw BubbleException('Invalid event data received', 'INVALID_EVENT');
        });
  }

  static Future<void> initializeBubbleService(BubbleConfig config) async {
    try {
      await _channel.invokeMethod(
        _initializeBubbleServiceMethod,
        config.toJson(),
      );
    } on PlatformException catch (e) {
      throw BubbleException(
        'Failed to initialize bubble service: ${e.message}',
        e.code,
      );
    }
  }

  ///[BubbleChannel]
  // Show bubble with configuration
  static Future<void> showBubble() async {
    try {
      await _channel.invokeMethod(_showBubbleMethod, true);
    } on PlatformException catch (e) {
      throw BubbleException('Failed to show bubble: ${e.message}', e.code);
    }
  }

  // Hide the bubble
  static Future<void> hideBubble() async {
    try {
      await _channel.invokeMethod(_hideBubbleMethod);
    } on PlatformException catch (e) {
      throw BubbleException('Failed to hide bubble: ${e.message}', e.code);
    }
  }

  // Move bubble to specific position
  static Future<void> moveBubble(BubblePosition position) async {
    try {
      await _channel.invokeMethod(_moveBubbleMethod, position.toJson());
    } on PlatformException catch (e) {
      throw BubbleException('Failed to move bubble: ${e.message}', e.code);
    }
  }

  // Close the bubble completely
  static Future<void> closeBubble() async {
    try {
      await _channel.invokeMethod(_closeBubbleMethod);
    } on PlatformException catch (e) {
      throw BubbleException('Failed to close bubble: ${e.message}', e.code);
    }
  }

  ///[BubbleChannel]--------------------------------

  // Update bubble configuration
  static Future<void> updateConfig(BubbleConfig config) async {
    try {
      await _channel.invokeMethod(_updateConfigMethod, config.toJson());
    } on PlatformException catch (e) {
      throw BubbleException('Failed to update config: ${e.message}', e.code);
    }
  }

  ///[ExpandBubbleChannel]

  // Expand the bubble
  static Future<void> expandBubble({bool isRemoveBubble = false}) async {
    try {
      await _channel.invokeMethod(_expandBubbleMethod, isRemoveBubble);
    } on PlatformException catch (e) {
      throw BubbleException('Failed to expand bubble: ${e.message}', e.code);
    }
  }

  static Future<void> closeExpandBubble() async {
    try {
      await _channel.invokeMethod(_closeExpandBubbleMethod);
    } on PlatformException catch (e) {
      throw BubbleException(
        'Failed to close expand bubble: ${e.message}',
        e.code,
      );
    }
  }

  static Future<void> hideExandBubble() async {
    try {
      await _channel.invokeMethod(_hideExandBubbleMethod);
    } on PlatformException catch (e) {
      throw BubbleException(
        'Failed to hide expand bubble: ${e.message}',
        e.code,
      );
    }
  }

  ///[PermissionChannel]
  // Check if overlay permission is granted
  static Future<bool> hasOverlayPermission() async {
    try {
      final result = await _channel.invokeMethod(_hasPermissionMethod);
      return result as bool? ?? false;
    } on PlatformException catch (e) {
      throw BubbleException('Failed to check permission: ${e.message}', e.code);
    }
  }

  // Request overlay permission
  static Future<bool> requestOverlayPermission() async {
    try {
      final result = await _channel.invokeMethod(_requestPermissionMethod);
      return result as bool? ?? false;
    } on PlatformException catch (e) {
      throw BubbleException(
        'Failed to request permission: ${e.message}',
        e.code,
      );
    }
  }

  // Get current bubble state
  static Future<BubbleState> getBubbleState() async {
    try {
      final result = await _channel.invokeMethod(_getStateMethod);
      if (result is Map<String, dynamic>) {
        return BubbleState.fromJson(result);
      }
      throw BubbleException('Invalid state data received', 'INVALID_STATE');
    } on PlatformException catch (e) {
      throw BubbleException('Failed to get bubble state: ${e.message}', e.code);
    }
  }

  // Set up event stream from native side
  static Stream<BubbleEvent> setupEventStream() {
    const EventChannel eventChannel = EventChannel(
      'com.example.pl_bubble/bubble_events',
    );

    return eventChannel.receiveBroadcastStream().map((dynamic event) {
      if (event is Map<String, dynamic>) {
        return BubbleEventData.fromJson(event).event;
      }
      throw BubbleException('Invalid event data received', 'INVALID_EVENT');
    });
  }

  // Parse event data from native side
}
