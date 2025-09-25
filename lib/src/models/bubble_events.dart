import 'bubble_position.dart';

/// Base class for all bubble events
abstract class BubbleEvent {
  final DateTime timestamp;
  final BubblePosition position;

  const BubbleEvent({required this.timestamp, required this.position});
}

/// Event when user starts touching the bubble
class BubbleTouchStartEvent extends BubbleEvent {
  const BubbleTouchStartEvent({
    required super.timestamp,
    required super.position,
  });
}

/// Event when user moves finger while touching the bubble
class BubbleTouchMoveEvent extends BubbleEvent {
  final BubblePosition previousPosition;
  final double deltaX;
  final double deltaY;

  const BubbleTouchMoveEvent({
    required super.timestamp,
    required super.position,
    required this.previousPosition,
    required this.deltaX,
    required this.deltaY,
  });
}

/// Event when user stops touching the bubble
class BubbleTouchEndEvent extends BubbleEvent {
  const BubbleTouchEndEvent({
    required super.timestamp,
    required super.position,
  });
}

/// Event when bubble is clicked
class BubbleClickEvent extends BubbleEvent {
  const BubbleClickEvent({required super.timestamp, required super.position});
}

/// Event when bubble starts expanding
class BubbleExpandEvent extends BubbleEvent {
  const BubbleExpandEvent({required super.timestamp, required super.position});
}

/// Event when bubble starts collapsing
class BubbleCollapseEvent extends BubbleEvent {
  const BubbleCollapseEvent({
    required super.timestamp,
    required super.position,
  });
}

/// Event when bubble is closed
class BubbleCloseEvent extends BubbleEvent {
  const BubbleCloseEvent({required super.timestamp, required super.position});
}

/// Event when bubble animates to screen edge
class BubbleAnimateToEdgeEvent extends BubbleEvent {
  final BubbleEdgeSide targetEdge;

  const BubbleAnimateToEdgeEvent({
    required super.timestamp,
    required super.position,
    required this.targetEdge,
  });
}

/// Event when bubble state changes
class BubbleStateChangeEvent extends BubbleEvent {
  final BubbleState previousState;
  final BubbleState newState;

  const BubbleStateChangeEvent({
    required super.timestamp,
    required super.position,
    required this.previousState,
    required this.newState,
  });
}

/// Event when bubble visibility changes
class BubbleVisibilityChangeEvent extends BubbleEvent {
  final bool isVisible;

  const BubbleVisibilityChangeEvent({
    required super.timestamp,
    required super.position,
    required this.isVisible,
  });
}

/// Event when bubble permission is requested
class BubblePermissionRequestEvent extends BubbleEvent {
  final String permission;
  final bool isGranted;

  const BubblePermissionRequestEvent({
    required super.timestamp,
    required super.position,
    required this.permission,
    required this.isGranted,
  });
}

/// Event when bubble encounters an error
class BubbleErrorEvent extends BubbleEvent {
  final String errorMessage;
  final String? errorCode;
  final dynamic errorDetails;

  const BubbleErrorEvent({
    required super.timestamp,
    required super.position,
    required this.errorMessage,
    this.errorCode,
    this.errorDetails,
  });
}

/// Event listener interface for bubble events
abstract class BubbleEventListener {
  void onVisibilityChange(bool isVisible);    
  void onExpand(bool isExpanded);
  void onClose();
  void onAnimateToEdge(BubbleEdgeSide targetEdge);
  void onStateChange(BubbleState previousState, BubbleState newState);
}

/// Event data container for platform communication
class BubbleEventData {
  final String eventType;
  final Map<String, dynamic> data;
  final DateTime timestamp;

  const BubbleEventData({
    required this.eventType,
    required this.data,
    required this.timestamp,
  });

  /// Convert to JSON
  Map<String, dynamic> toJson() {
    return {
      'eventType': eventType,
      'data': data,
      'timestamp': timestamp.millisecondsSinceEpoch,
    };
  }

  /// Create from JSON
  factory BubbleEventData.fromJson(Map<String, dynamic> json) {
    return BubbleEventData(
      eventType: json['eventType'] ?? '',
      data: Map<String, dynamic>.from(json['data'] ?? {}),
      timestamp: DateTime.fromMillisecondsSinceEpoch(
        json['timestamp'] ?? DateTime.now().millisecondsSinceEpoch,
      ),
    );
  }
}
