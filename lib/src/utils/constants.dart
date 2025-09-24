/// Constants for the pl_bubble plugin
class BubbleConstants {
  // Method channel names
  static const String methodChannelName = 'com.example.pl_bubble/bubble';
  static const String eventChannelName = 'com.example.pl_bubble/bubble_events';

  // Method names

  static const String hasPermissionMethod = 'hasOverlayPermission';
  static const String requestPermissionMethod = 'requestOverlayPermission';
  static const String getStateMethod = 'getBubbleState';

  // Event types

  static const String clickEvent = 'click';
  static const String expandEvent = 'expand';
  static const String collapseEvent = 'collapse';
  static const String closeEvent = 'close';
  static const String animateToEdgeEvent = 'animateToEdge';
  static const String stateChangeEvent = 'stateChange';
  static const String visibilityChangeEvent = 'visibilityChange';
  static const String permissionRequestEvent = 'permissionRequest';
  static const String errorEvent = 'error';

  // Default values
  static const double defaultBubbleSize = 60.0;

  static const double defaultExpandedWidth = 300.0;

  static const double defaultExpandedHeight = 400.0;

  static const double defaultBorderRadius = 30.0;
  static const double defaultCloseDistance = 200.0;
  static const double bubbleCloseBottomDistance = 100.0;

  // Permission types
  static const String overlayPermission = 'SYSTEM_ALERT_WINDOW';
  static const String notificationPermission = 'NOTIFICATION';

  // Platform types
  static const String androidPlatform = 'android';
  static const String iosPlatform = 'ios';

  // Service constants
  static const String bubbleServiceName = 'BubbleService';
  static const String notificationChannelId = 'bubble_notification_channel';
  static const String notificationChannelName = 'Bubble Notifications';
  static const String notificationChannelDescription =
      'Notifications for bubble service';

  // Flutter engine constants
  static const String flutterRoute = '/expandBubble';
  static const String dartEntrypoint = 'main';

  // Window manager constants
  static const int windowTypeOverlay = 2038;
  static const int windowTypeSystemAlert = 2003;

  // Layout parameters
  static const int matchParent = -1;
  static const int wrapContent = -2;

  // Cache constants
  static const int maxCacheSize = 100;
  static const int cacheTimeout = 300000; // 5 minutes
}
