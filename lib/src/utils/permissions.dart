enum PermissionType {
  overlay("overlay"),
  notification("notification");

  final String value;

  const PermissionType(this.value);

  static PermissionType fromString(String? value) {
    return PermissionType.values.firstWhere(
      (e) => e.value == value,
      orElse: () => PermissionType.overlay,
    );
  }
}

/// Permission status enumeration
enum PermissionStatus {
  granted,
  denied,
  permanentlyDenied,
  restricted,
  limited,
}

/// Permission result class
class PermissionResult {
  final String permission;
  final PermissionStatus status;
  final bool isGranted;
  final String? errorMessage;
  final String? errorCode;

  const PermissionResult({
    required this.permission,
    required this.status,
    required this.isGranted,
    this.errorMessage,
    this.errorCode,
  });

  /// Create from JSON
  factory PermissionResult.fromJson(Map<String, dynamic> json) {
    return PermissionResult(
      permission: json['permission'] as String? ?? '',
      status: PermissionStatus.values.firstWhere(
        (e) => e.name == json['status'],
        orElse: () => PermissionStatus.denied,
      ),
      isGranted: json['isGranted'] as bool? ?? false,
      errorMessage: json['errorMessage'] as String?,
      errorCode: json['errorCode'] as String?,
    );
  }

  /// Convert to JSON
  Map<String, dynamic> toJson() {
    return {
      'permission': permission,
      'status': status.name,
      'isGranted': isGranted,
      'errorMessage': errorMessage,
      'errorCode': errorCode,
    };
  }
}
