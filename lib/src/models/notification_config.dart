/// Configuration class for notification appearance and behavior
class NotificationConfig {
  /// Title of the notification
  final String contentTitle;

  /// Text of the notification
  final String contentText;

  const NotificationConfig({
    required this.contentTitle,
    required this.contentText,
  });

  /// Convert to JSON for platform communication
  Map<String, dynamic> toJson() {
    return {'contentTitle': contentTitle, 'contentText': contentText};
  }

  /// Create from JSON
  factory NotificationConfig.fromJson(Map<String, dynamic> json) {
    return NotificationConfig(
      contentTitle: json['contentTitle'],
      contentText: json['contentText'],
    );
  }
}
