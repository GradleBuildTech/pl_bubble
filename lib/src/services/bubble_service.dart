import 'dart:async';
import 'dart:io';
import '../models/bubble_config.dart';
import '../models/bubble_position.dart';
import '../models/bubble_events.dart';
import 'android_bubble_service.dart';

/// Abstract service interface for bubble functionality
abstract class BubbleService {
  /// Instance of the bubble service
  static BubbleService get instance {
    if (Platform.isAndroid) return AndroidBubbleService.instance;
    throw UnimplementedError('Unsupported platform');
  }

  /// Stream of bubble events
  Stream<BubbleEvent> get eventStream => eventController.stream;

  final StreamController<BubbleEvent> eventController =
      StreamController<BubbleEvent>.broadcast();

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

  /// Dispose the service
  Future<void> dispose();
}
