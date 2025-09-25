import 'package:flutter/services.dart';

import '../../pl_bubble.dart';

const kHeadTitleError = 'Failed to check overlay permission:';

// Method channel for communicating with native permission implementation
class PermissionChannel {
  static const MethodChannel _channel = MethodChannel(
    BubbleConstants.methodChannelName,
  );

  static const String _hasPermissionMethod = 'hasOverlayPermission';
  static const String _requestPermissionMethod = 'requestOverlayPermission';
  static const String _hasNotificationPermissionMethod =
      'hasNotificationPermission';
  static const String _requestNotificationPermissionMethod =
      'requestNotificationPermission';

  /// Check if overlay permission is granted
  static Future<bool> hasOverlayPermission() async {
    try {
      final result = await _channel.invokeMethod(_hasPermissionMethod);
      return result as bool? ?? false;
    } catch (e) {
      throw OverlayPermissionException(
        '$kHeadTitleError check overlay permission: $e',
        'PERMISSION_CHECK_ERROR',
      );
    }
  }

  /// Request overlay permission
  static Future<bool> requestOverlayPermission() async {
    try {
      final result = await _channel.invokeMethod(_requestPermissionMethod);
      return result as bool? ?? false;
    } catch (e) {
      throw OverlayPermissionException(
        '$kHeadTitleError request overlay permission: $e',
        'PERMISSION_REQUEST_ERROR',
      );
    }
  }

  /// Check if notification permission is granted
  static Future<bool> hasNotificationPermission() async {
    try {
      final result = await _channel.invokeMethod(
        _hasNotificationPermissionMethod,
      );
      return result as bool? ?? false;
    } catch (e) {
      throw NotificationPermissionException(
        '$kHeadTitleError check notification permission: $e',
        'PERMISSION_CHECK_ERROR',
      );
    }
  }

  /// Request notification permission
  static Future<bool> requestNotificationPermission() async {
    try {
      final result = await _channel.invokeMethod(
        _requestNotificationPermissionMethod,
      );
      return result as bool? ?? false;
    } catch (e) {
      throw NotificationPermissionException(
        '$kHeadTitleError request notification permission: $e',
        'PERMISSION_REQUEST_ERROR',
      );
    }
  }
}
