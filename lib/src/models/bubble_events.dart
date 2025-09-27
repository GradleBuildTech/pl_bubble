import 'bubble_position.dart';

/// Base class for all bubble events
sealed class BubbleEvent {}

/// Event when user starts touching the bubble
final class BubbleTouchStartEvent extends BubbleEvent {
  BubbleTouchStartEvent();
}

/// Event when user moves finger while touching the bubble
final class BubbleTouchMoveEvent extends BubbleEvent {
  final double deltaX;
  final double deltaY;

  BubbleTouchMoveEvent({required this.deltaX, required this.deltaY});
}

/// Event when user stops touching the bubble
final class BubbleTouchEndEvent extends BubbleEvent {}

/// Event when bubble is clicked
final class BubbleClickEvent extends BubbleEvent {}

/// Event when bubble starts expanding
final class BubbleExpandEvent extends BubbleEvent {}

/// Event when bubble starts collapsing
final class BubbleCollapseEvent extends BubbleEvent {}

/// Event when bubble is closed
final class BubbleCloseEvent extends BubbleEvent {}

/// Event when bubble animates to screen edge
final class BubbleAnimateToEdgeEvent extends BubbleEvent {
  final BubbleEdgeSide targetEdge;

  BubbleAnimateToEdgeEvent({required this.targetEdge});
}

/// Event when bubble state changes
final class BubbleStateChangeEvent extends BubbleEvent {
  final BubbleState previousState;
  final BubbleState newState;

  BubbleStateChangeEvent({required this.previousState, required this.newState});
}

/// Event when bubble visibility changes
final class BubbleVisibilityChangeEvent extends BubbleEvent {
  final bool isVisible;

  BubbleVisibilityChangeEvent({required this.isVisible});
}

/// Event when bubble permission is requested
final class BubblePermissionRequestEvent extends BubbleEvent {
  final String permission;
  final bool isGranted;

  BubblePermissionRequestEvent({
    required this.permission,
    required this.isGranted,
  });
}

/// Event when bubble encounters an error
class BubbleErrorEvent extends BubbleEvent {
  final String errorMessage;
  final String? errorCode;
  final dynamic errorDetails;

  BubbleErrorEvent({
    required this.errorMessage,
    this.errorCode,
    this.errorDetails,
  });
}

/// Event listener interface for bubble events
abstract class BubbleEventListener {
  void onVisibilityChange(bool isVisible) {}
  void onExpand(bool isExpanded) {}
  void onClose() {}
  void onAnimateToEdge(BubbleEdgeSide targetEdge) {}
  void onStateChange(BubbleState previousState, BubbleState newState) {}
}
