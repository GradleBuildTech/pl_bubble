import 'dart:async';
import 'package:pl_bubble/src/services/channel_listener.dart';

import '../models/bubble_config.dart';
import '../models/bubble_position.dart';
import '../models/bubble_events.dart';
import '../exceptions/bubble_exception.dart';
import '../utils/logger.dart';
import 'bubble_channel.dart';
import 'bubble_service.dart';

const kBubbleErrorHead = 'Failed to';

/// Android-specific implementation of bubble service
/// Example:
/// ```dart
/// AndroidBubbleService.instance.initialize(BubbleConfig(
///   bubbleSize: Size(90, 90),
/// ));
/// ```
///
/// Example:
/// ```dart
/// AndroidBubbleService.instance.showBubble();
/// ```
///
/// Example:
/// ```dart
/// AndroidBubbleService.instance.hideBubble();
/// ```
///
/// Example:
/// ```dart
/// AndroidBubbleService.instance.expandBubble();
/// ```
///
class AndroidBubbleService implements BubbleService {
  AndroidBubbleService._();

  factory AndroidBubbleService() => _instance;

  static final AndroidBubbleService _instance = AndroidBubbleService._();

  static AndroidBubbleService get instance => _instance;

  final StreamController<BubbleEvent> _eventController =
      StreamController<BubbleEvent>.broadcast();

  final List<BubbleEventListener> _listeners = [];

  BubbleState _currentState = const BubbleState(
    position: BubblePosition(x: 0, y: 0),
  );

  //Whether the bubble is visible
  bool _isVisible = false;

  //Whether the bubble is expanded
  bool _isExpanded = false;

  //Whether the bubble is initialized
  bool _isInitialized = false;

  //Stream of bubble events, this is the stream that will be used to listen to the bubble events
  @override
  Stream<BubbleEvent> get eventStream => _eventController.stream;

  @override
  BubbleState get currentState => _currentState;

  @override
  bool get isVisible => _isVisible;

  @override
  bool get isExpanded => _isExpanded;

  @override
  Future<void> initialize(BubbleConfig config) async {
    if (_isInitialized) return;

    try {
      // Listen to event bridge
      ChannelListener.listenToEventBridge(onEvent: _handleEvent);

      // Initialize bubble service
      await BubbleChannel.initializeBubbleService(config);

      _isInitialized = true;
    } catch (e) {
      Logger.d('AndroidBubbleService', e.toString());
    }
  }

  @override
  void startEventBridgeListener() {
    // ChannelListener.listenToEventBridge(onEvent: _handleEvent);
  }

  @override
  Future<void> showBubble() async {
    try {
      await BubbleChannel.showBubble();
      _isVisible = true;
      _currentState = _currentState.copyWith(isVisible: true);

      // Notify listeners
      for (final listener in _listeners) {
        listener.onVisibilityChange(true);
      }
    } catch (e) {
      Logger.d('AndroidBubbleService', e.toString());
      throw BubbleException(
        '$kBubbleErrorHead show bubble: $e',
        'SHOW_BUBBLE_ERROR',
      );
    }
  }

  @override
  Future<void> hideBubble() async {
    try {
      await BubbleChannel.hideBubble();
      _isVisible = false;
      _currentState = _currentState.copyWith(isVisible: false);

      // Notify listeners
      for (final listener in _listeners) {
        listener.onVisibilityChange(false);
      }
    } catch (e) {
      Logger.d('AndroidBubbleService', e.toString());
      throw BubbleException('Failed to hide bubble: $e', 'HIDE_BUBBLE_ERROR');
    }
  }

  @override
  Future<void> expandBubble({bool isRemoveBubble = false}) async {
    try {
      await BubbleChannel.expandBubble(isRemoveBubble: isRemoveBubble);
      _isExpanded = true;
      _currentState = _currentState.copyWith(isExpanded: true);

      // Notify listeners
      for (final listener in _listeners) {
        listener.onExpand(true);
      }
    } catch (e) {
      Logger.d('AndroidBubbleService', e.toString());
      throw BubbleException(
        '$kBubbleErrorHead expand bubble: $e',
        'EXPAND_BUBBLE_ERROR',
      );
    }
  }

  @override
  Future<void> closeExpandBubble() async {
    try {
      await BubbleChannel.closeExpandBubble();
    } catch (e) {
      Logger.d('AndroidBubbleService', e.toString());
      throw BubbleException(
        '$kBubbleErrorHead close expand bubble: $e',
        'CLOSE_EXPAND_BUBBLE_ERROR',
      );
    }
  }

  @override
  Future<void> hideExpandBubble() async {
    try {
      await BubbleChannel.hideExandBubble();
    } catch (e) {
      Logger.d('AndroidBubbleService', e.toString());
      throw BubbleException(
        '$kBubbleErrorHead hide expand bubble: $e',
        'HIDE_EXPAND_BUBBLE_ERROR',
      );
    }
  }

  @override
  Future<void> moveBubble(BubblePosition position) async {
    try {
      await BubbleChannel.moveBubble(position);
      _currentState = _currentState.copyWith(position: position);
    } catch (e) {
      Logger.d('AndroidBubbleService', e.toString());
      throw BubbleException(
        '$kBubbleErrorHead move bubble: $e',
        'MOVE_BUBBLE_ERROR',
      );
    }
  }

  @override
  Future<void> updateConfig(BubbleConfig config) async {
    try {
      await BubbleChannel.updateConfig(config);
    } catch (e) {
      Logger.d('AndroidBubbleService', e.toString());
      throw BubbleException(
        '$kBubbleErrorHead update config: $e',
        'UPDATE_CONFIG_ERROR',
      );
    }
  }

  @override
  Future<void> closeBubble() async {
    try {
      await BubbleChannel.closeBubble();
      _isVisible = false;
      _isExpanded = false;
      _currentState = _currentState.copyWith(
        isVisible: false,
        isExpanded: false,
      );

      // Notify listeners
      for (final listener in _listeners) {
        listener.onClose();
      }
    } catch (e) {
      Logger.d('AndroidBubbleService', e.toString());
      throw BubbleException(
        '$kBubbleErrorHead close bubble: $e',
        'CLOSE_BUBBLE_ERROR',
      );
    }
  }

  @override
  Future<bool> hasOverlayPermission() async {
    try {
      return await BubbleChannel.hasOverlayPermission();
    } catch (e) {
      Logger.d('AndroidBubbleService', e.toString());
      throw BubbleException(
        '$kBubbleErrorHead check permission: $e',
        'PERMISSION_CHECK_ERROR',
      );
    }
  }

  @override
  Future<bool> requestOverlayPermission() async {
    try {
      return await BubbleChannel.requestOverlayPermission();
    } catch (e) {
      Logger.d('AndroidBubbleService', e.toString());
      throw BubbleException(
        '$kBubbleErrorHead request permission: $e',
        'PERMISSION_REQUEST_ERROR',
      );
    }
  }

  @override
  void addEventListener(BubbleEventListener listener) {
    _listeners.add(listener);
  }

  @override
  void removeEventListener(BubbleEventListener listener) {
    _listeners.remove(listener);
  }

  @override
  Future<void> dispose() async {
    await _eventController.close();
    _listeners.clear();
  }

  /// Handle events from native side
  void _handleEvent(BubbleEvent event) {
    Logger.d("AndroidBubbleService", "Received event: $event");
    _eventController.add(event);

    // Update internal state based on event
    if (event is BubbleStateChangeEvent) {
      _currentState = event.newState;
      _isVisible = event.newState.isVisible;
      _isExpanded = event.newState.isExpanded;
    } else if (event is BubbleVisibilityChangeEvent) {
      _isVisible = event.isVisible;
      _currentState = _currentState.copyWith(isVisible: event.isVisible);
    }

    // Notify listeners
    for (final listener in _listeners) {
      _notifyListener(listener, event);
    }
  }

  /// Notify specific listener about event
  void _notifyListener(BubbleEventListener listener, BubbleEvent event) {}
}
