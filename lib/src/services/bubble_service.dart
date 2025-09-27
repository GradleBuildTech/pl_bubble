import 'dart:async';
import '../models/bubble_config.dart';
import '../models/bubble_position.dart';
import '../models/bubble_events.dart';

/// Abstract service interface for bubble functionality
abstract class BubbleService {
  /// Stream of bubble events
  Stream<BubbleEvent> get eventStream;

  /// Current bubble state
  BubbleState get currentState;

  /// Whether the bubble is currently visible
  bool get isVisible;

  /// Whether the bubble is currently expanded
  bool get isExpanded;

  /// Initialize the bubble service with configuration
  Future<void> initialize(BubbleConfig config);

  /// Show the bubble with given configuration
  Future<void> showBubble();

  /// Hide the bubble
  Future<void> hideBubble();

  /// Expand the bubble
  Future<void> expandBubble({bool isRemoveBubble = false});

  /// Close the expand bubble
  Future<void> closeExpandBubble();

  /// Hide the expand bubble
  Future<void> hideExpandBubble();

  /// Move bubble to specific position
  Future<void> moveBubble(BubblePosition position);

  /// Update bubble configuration
  Future<void> updateConfig(BubbleConfig config);

  /// Close the bubble completely
  Future<void> closeBubble();

  /// Check if overlay permission is granted
  Future<bool> hasOverlayPermission();

  /// Request overlay permission
  Future<bool> requestOverlayPermission();

  /// Add event listener
  void addEventListener(BubbleEventListener listener);

  /// Remove event listener
  void removeEventListener(BubbleEventListener listener);

  /// Dispose the service
  Future<void> dispose();
}
