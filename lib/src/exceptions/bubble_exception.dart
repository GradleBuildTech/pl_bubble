/// Base exception class for bubble-related errors
class BubbleException implements Exception {
  final String message;
  final String? code;
  final dynamic details;
  final StackTrace? stackTrace;

  const BubbleException(
    this.message, [
    this.code,
    this.details,
    this.stackTrace,
  ]);

  /// Create a copy with modified values
  BubbleException copyWith({
    String? message,
    String? code,
    dynamic details,
    StackTrace? stackTrace,
  }) {
    return BubbleException(
      message ?? this.message,
      code ?? this.code,
      details ?? this.details,
      stackTrace ?? this.stackTrace,
    );
  }

  /// Convert to JSON
  Map<String, dynamic> toJson() {
    return {
      'message': message,
      'code': code,
      'details': details,
      'stackTrace': stackTrace?.toString(),
    };
  }

  /// Create from JSON
  factory BubbleException.fromJson(Map<String, dynamic> json) {
    return BubbleException(
      json['message'] as String? ?? 'Unknown error',
      json['code'] as String?,
      json['details'],
      json['stackTrace'] != null
          ? StackTrace.fromString(json['stackTrace'])
          : null,
    );
  }
}

/// Exception for initialization errors
class BubbleInitializationException extends BubbleException {
  const BubbleInitializationException(
    super.message, [
    super.code,
    super.details,
    super.stackTrace,
  ]);
}

/// Exception for configuration errors
class BubbleConfigurationException extends BubbleException {
  const BubbleConfigurationException(
    super.message, [
    super.code,
    super.details,
    super.stackTrace,
  ]);
}

/// Exception for service errors
class BubbleServiceException extends BubbleException {
  const BubbleServiceException(
    super.message, [
    super.code,
    super.details,
    super.stackTrace,
  ]);
}

/// Exception for widget errors
class BubbleWidgetException extends BubbleException {
  const BubbleWidgetException(
    super.message, [
    super.code,
    super.details,
    super.stackTrace,
  ]);
}

/// Exception for animation errors
class BubbleAnimationException extends BubbleException {
  const BubbleAnimationException(
    super.message, [
    super.code,
    super.details,
    super.stackTrace,
  ]);
}

/// Exception for event errors
class BubbleEventException extends BubbleException {
  const BubbleEventException(
    super.message, [
    super.code,
    super.details,
    super.stackTrace,
  ]);
}

/// Exception for channel communication errors
class BubbleChannelException extends BubbleException {
  const BubbleChannelException(
    String message, [
    String? code,
    dynamic details,
    StackTrace? stackTrace,
  ]) : super(message, code, details, stackTrace);
}

/// Exception for state errors
class BubbleStateException extends BubbleException {
  const BubbleStateException(
    super.message, [
    super.code,
    super.details,
    super.stackTrace,
  ]);
}

/// Exception for validation errors
class BubbleValidationException extends BubbleException {
  const BubbleValidationException(
    super.message, [
    super.code,
    super.details,
    super.stackTrace,
  ]);
}

/// Exception for timeout errors
class BubbleTimeoutException extends BubbleException {
  const BubbleTimeoutException(
    super.message, [
    super.code,
    super.details,
    super.stackTrace,
  ]);
}

/// Exception for resource errors
class BubbleResourceException extends BubbleException {
  const BubbleResourceException(
    super.message, [
    super.code,
    super.details,
    super.stackTrace,
  ]);
}

/// Exception for compatibility errors
class BubbleCompatibilityException extends BubbleException {
  const BubbleCompatibilityException(
    super.message, [
    super.code,
    super.details,
    super.stackTrace,
  ]);
}

/// Exception for performance errors
class BubblePerformanceException extends BubbleException {
  const BubblePerformanceException(
    super.message, [
    super.code,
    super.details,
    super.stackTrace,
  ]);
}

/// Exception for lifecycle errors
class BubbleLifecycleException extends BubbleException {
  const BubbleLifecycleException(
    super.message, [
    super.code,
    super.details,
    super.stackTrace,
  ]);
}
