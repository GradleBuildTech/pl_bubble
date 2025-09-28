import '../../pl_bubble.dart';

/// Data class for recieving data from native side
class BubbleEventData {
  /// Event
  final BubbleEvent event;

  BubbleEventData({required this.event});

  /// Create from JSON
  factory BubbleEventData.fromJson(Map<String, dynamic> json) {
    return BubbleEventData(event: _parseEvent(json));
  }

  /// Parse event from JSON
  static BubbleEvent _parseEvent(Map<String, dynamic> eventData) {
    final eventType = eventData['eventType'] as String? ?? '';
    final data = eventData['data'] as Map<String, dynamic>? ?? {};

    switch (eventType) {
      case 'touchStart':
        return BubbleTouchStartEvent();
      case 'BubbleMovement':
        return BubbleTouchMoveEvent(
          deltaX: data['x']?.toDouble() ?? 0.0,
          deltaY: data['y']?.toDouble() ?? 0.0,
        );
      case 'touchEnd':
        return BubbleTouchEndEvent();
      case 'OnClickBubble':
        return BubbleClickEvent();
      case 'expand':
        return BubbleExpandEvent();
      case 'collapse':
        return BubbleCollapseEvent();
      case 'close':
        return BubbleCloseEvent();
      case 'animateToEdge':
        return BubbleAnimateToEdgeEvent(
          targetEdge: BubbleEdgeSide.values.firstWhere(
            (e) => e.name == data['targetEdge'],
            orElse: () => BubbleEdgeSide.none,
          ),
        );
      case 'stateChange':
        return BubbleStateChangeEvent(
          previousState: BubbleState.fromJson(data['previousState'] ?? {}),
          newState: BubbleState.fromJson(data['newState'] ?? {}),
        );
      case 'visibilityChange':
        return BubbleVisibilityChangeEvent(
          isVisible: data['isVisible'] as bool? ?? false,
        );
      case 'permissionRequest':
        return BubblePermissionRequestEvent(
          permission: data['permission'] as String? ?? '',
          isGranted: data['isGranted'] as bool? ?? false,
        );
      case 'Error':
        return BubbleErrorEvent(
          errorMessage: data['errorMessage'] as String? ?? 'Unknown error',
        );
      default:
        throw BubbleException(
          'Unknown event type: $eventType',
          'UNKNOWN_EVENT',
        );
    }
  }
}
