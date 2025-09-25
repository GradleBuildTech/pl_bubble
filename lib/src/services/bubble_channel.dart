import 'dart:async';
import 'package:flutter/services.dart';
import '../models/bubble_config.dart';
import '../models/bubble_position.dart';
import '../models/bubble_events.dart';
import '../exceptions/bubble_exception.dart';

// Method channel for communicating with native bubble implementation
class BubbleChannel {
  static const MethodChannel _channel = MethodChannel(
    'com.example.pl_bubble/bubble',
  );

  static const String _updateConfigMethod = 'updateConfig';

  // Bubble methods
  static const String _showBubbleMethod = 'showBubble';
  static const String _hideBubbleMethod = 'hideBubble';
  static const String _moveBubbleMethod = 'moveBubble';
  static const String _closeBubbleMethod = 'closeBubble';

  // Expand bubble methods
  static const String _expandBubbleMethod = 'expandBubble';
  static const String _closeExpandBubbleMethod = 'closeExpandBubble';
  static const String _hideExandBubbleMethod = 'hideExandBubble';

  // Permission methods
  static const String _hasPermissionMethod = 'hasOverlayPermission';
  static const String _requestPermissionMethod = 'requestOverlayPermission';
  static const String _getStateMethod = 'getBubbleState';
  static const String _initialBubbleServiceMethod = 'initialBubbleService';

  static Future<void> initialBubbleService(BubbleConfig config) async {
    try {
      await _channel.invokeMethod(_initialBubbleServiceMethod, config.toJson());
    } on PlatformException catch (e) {
      throw BubbleException(
        'Failed to initial bubble service: ${e.message}',
        e.code,
      );
    }
  }

  ///[BubbleChannel]
  // Show bubble with configuration
  static Future<void> showBubble() async {
    try {
      await _channel.invokeMethod(_showBubbleMethod);
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
  static Future<void> expandBubble() async {
    try {
      await _channel.invokeMethod(_expandBubbleMethod);
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
        return _parseEvent(event);
      }
      throw BubbleException('Invalid event data received', 'INVALID_EVENT');
    });
  }

  // Parse event data from native side
  static BubbleEvent _parseEvent(Map<String, dynamic> eventData) {
    final eventType = eventData['eventType'] as String? ?? '';
    final timestamp = DateTime.fromMillisecondsSinceEpoch(
      eventData['timestamp'] as int? ?? DateTime.now().millisecondsSinceEpoch,
    );
    final position = BubblePosition.fromJson(
      Map<String, dynamic>.from(eventData['position'] ?? {}),
    );

    switch (eventType) {
      case 'touchStart':
        return BubbleTouchStartEvent(timestamp: timestamp, position: position);
      case 'touchMove':
        final data = eventData['data'] as Map<String, dynamic>? ?? {};
        return BubbleTouchMoveEvent(
          timestamp: timestamp,
          position: position,
          previousPosition: BubblePosition.fromJson(
            data['previousPosition'] ?? {},
          ),
          deltaX: data['deltaX']?.toDouble() ?? 0.0,
          deltaY: data['deltaY']?.toDouble() ?? 0.0,
        );
      case 'touchEnd':
        return BubbleTouchEndEvent(timestamp: timestamp, position: position);
      case 'click':
        return BubbleClickEvent(timestamp: timestamp, position: position);
      case 'expand':
        return BubbleExpandEvent(timestamp: timestamp, position: position);
      case 'collapse':
        return BubbleCollapseEvent(timestamp: timestamp, position: position);
      case 'close':
        return BubbleCloseEvent(timestamp: timestamp, position: position);
      case 'animateToEdge':
        final data = eventData['data'] as Map<String, dynamic>? ?? {};
        return BubbleAnimateToEdgeEvent(
          timestamp: timestamp,
          position: position,
          targetEdge: BubbleEdgeSide.values.firstWhere(
            (e) => e.name == data['targetEdge'],
            orElse: () => BubbleEdgeSide.none,
          ),
        );
      case 'stateChange':
        final data = eventData['data'] as Map<String, dynamic>? ?? {};
        return BubbleStateChangeEvent(
          timestamp: timestamp,
          position: position,
          previousState: BubbleState.fromJson(data['previousState'] ?? {}),
          newState: BubbleState.fromJson(data['newState'] ?? {}),
        );
      case 'visibilityChange':
        final data = eventData['data'] as Map<String, dynamic>? ?? {};
        return BubbleVisibilityChangeEvent(
          timestamp: timestamp,
          position: position,
          isVisible: data['isVisible'] as bool? ?? false,
        );
      case 'permissionRequest':
        final data = eventData['data'] as Map<String, dynamic>? ?? {};
        return BubblePermissionRequestEvent(
          timestamp: timestamp,
          position: position,
          permission: data['permission'] as String? ?? '',
          isGranted: data['isGranted'] as bool? ?? false,
        );
      case 'error':
        final data = eventData['data'] as Map<String, dynamic>? ?? {};
        return BubbleErrorEvent(
          timestamp: timestamp,
          position: position,
          errorMessage: data['errorMessage'] as String? ?? 'Unknown error',
          errorCode: data['errorCode'] as String?,
          errorDetails: data['errorDetails'],
        );
      default:
        throw BubbleException(
          'Unknown event type: $eventType',
          'UNKNOWN_EVENT',
        );
    }
  }
}
