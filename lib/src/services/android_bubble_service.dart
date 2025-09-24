import 'dart:async';
import '../models/bubble_config.dart';
import '../models/bubble_position.dart';
import '../models/bubble_events.dart';
import '../exceptions/bubble_exception.dart';
import 'bubble_channel.dart';
import 'bubble_service.dart';

const kBubbleErrorHead = 'Failed to';

/// Android-specific implementation of bubble service
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

  bool _isVisible = false;

  bool _isExpanded = false;

  bool _isInitialized = false;

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
      await BubbleChannel.initialBubbleService(config);
      // Set up event stream from native side

      _isInitialized = true;
    } catch (e) {
      throw BubbleException(
        '$kBubbleErrorHead initialize Android bubble service: $e',
        'INIT_ERROR',
      );
    }
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
      throw BubbleException('Failed to hide bubble: $e', 'HIDE_BUBBLE_ERROR');
    }
  }

  @override
  Future<void> expandBubble() async {
    try {
      await BubbleChannel.expandBubble();
      _isExpanded = true;
      _currentState = _currentState.copyWith(isExpanded: true);

      // Notify listeners
      for (final listener in _listeners) {
        listener.onExpand(true);
      }
    } catch (e) {
      throw BubbleException(
        '$kBubbleErrorHead expand bubble: $e',
        'EXPAND_BUBBLE_ERROR',
      );
    }
  }

  @override
  Future<void> moveBubble(BubblePosition position) async {
    try {
      await BubbleChannel.moveBubble(position);
      _currentState = _currentState.copyWith(position: position);
    } catch (e) {
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
