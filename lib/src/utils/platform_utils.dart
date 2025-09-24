import 'dart:io';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'constants.dart';

/// Utility class for platform-specific operations
class PlatformUtils {
  static const MethodChannel _channel = MethodChannel(
    BubbleConstants.methodChannelName,
  );

  /// Get current platform name
  static String get platformName {
    if (kIsWeb) return 'web';
    return Platform.operatingSystem;
  }

  /// Check if running on Android
  static bool get isAndroid => !kIsWeb && Platform.isAndroid;

  /// Check if running on iOS
  static bool get isIOS => !kIsWeb && Platform.isIOS;

  /// Check if running on Web
  static bool get isWeb => kIsWeb;

  /// Check if running on Desktop
  static bool get isDesktop =>
      !kIsWeb && (Platform.isWindows || Platform.isMacOS || Platform.isLinux);

  /// Check if running on Mobile
  static bool get isMobile => !kIsWeb && (Platform.isAndroid || Platform.isIOS);

  /// Get platform-specific method channel
  static MethodChannel get methodChannel => _channel;

  /// Check if platform supports bubble functionality
  static bool get supportsBubble {
    if (kIsWeb || isIOS) return false;
    return Platform.isAndroid;
  }

  /// Get platform-specific constants
  static Map<String, dynamic> get platformConstants {
    if (isAndroid) {
      return {
        'windowTypeOverlay': BubbleConstants.windowTypeOverlay,
        'windowTypeSystemAlert': BubbleConstants.windowTypeSystemAlert,
        'matchParent': BubbleConstants.matchParent,
        'wrapContent': BubbleConstants.wrapContent,
      };
    } else if (isIOS) {
      return {'supportsOverlay': false, 'supportsSystemAlert': false};
    }
    return {};
  }

  /// Get platform version
  static Future<String> getPlatformVersion() async {
    try {
      final result = await _channel.invokeMethod('getPlatformVersion');
      return result as String? ?? 'Unknown';
    } on PlatformException catch (e) {
      return 'Unknown';
    }
  }

  /// Check if app is in foreground
  static Future<bool> isAppInForeground() async {
    try {
      final result = await _channel.invokeMethod('isAppInForeground');
      return result as bool? ?? true;
    } on PlatformException catch (e) {
      return true;
    }
  }

  /// Check if app is in background
  static Future<bool> isAppInBackground() async {
    try {
      final result = await _channel.invokeMethod('isAppInBackground');
      return result as bool? ?? false;
    } on PlatformException {
      return false;
    }
  }
}
