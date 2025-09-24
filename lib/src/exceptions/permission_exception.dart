import 'package:pl_bubble/src/utils/permissions.dart';

import 'bubble_exception.dart';

/// Exception for permission-related errors
class PermissionException extends BubbleException {
  final PermissionType permission;
  final String? reason;

  const PermissionException(
    String message,
    String? code,
    this.permission, [
    this.reason,
    dynamic details,
    StackTrace? stackTrace,
  ]) : super(message, code, details, stackTrace);

  /// Create a copy with modified values
  @override
  PermissionException copyWith({
    String? message,
    String? code,
    PermissionType? permission,
    String? reason,
    dynamic details,
    StackTrace? stackTrace,
  }) {
    return PermissionException(
      message ?? this.message,
      code ?? this.code,
      permission ?? this.permission,
      reason ?? this.reason,
      details ?? this.details,
      stackTrace ?? this.stackTrace,
    );
  }

  /// Convert to JSON
  @override
  Map<String, dynamic> toJson() {
    return {...super.toJson(), 'permission': permission, 'reason': reason};
  }

  /// Create from JSON
  factory PermissionException.fromJson(Map<String, dynamic> json) {
    return PermissionException(
      json['message'] as String? ?? 'Permission error',
      json['code'] as String?,
      PermissionType.fromString(json['permission'] as String?),
      json['reason'] as String?,
      json['details'],
      json['stackTrace'] != null
          ? StackTrace.fromString(json['stackTrace'])
          : null,
    );
  }
}

/// Exception for overlay permission errors
class OverlayPermissionException extends PermissionException {
  const OverlayPermissionException(
    String message, [
    String? code,
    String? reason,
    dynamic details,
    StackTrace? stackTrace,
  ]) : super(
         message,
         code,
         PermissionType.overlay,
         reason,
         details,
         stackTrace,
       );
}

/// Exception for notification permission errors
class NotificationPermissionException extends PermissionException {
  const NotificationPermissionException(
    String message, [
    String? code,
    String? reason,
    dynamic details,
    StackTrace? stackTrace,
  ]) : super(
         message,
         code,
         PermissionType.notification,
         reason,
         details,
         stackTrace,
       );
}
