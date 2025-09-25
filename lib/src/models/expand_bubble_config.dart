/// Configuration class for expanded bubble appearance and behavior
class ExpandBubbleConfig {
  /// Width of the expanded bubble
  final double width;

  /// Height of the expanded bubble
  final double height;

  /// Route engine of the expanded bubble
  final String routeEngine;

  const ExpandBubbleConfig({
    required this.width,
    required this.height,
    required this.routeEngine,
  });

  Map<String, dynamic> toJson() {
    return {'width': width, 'height': height, 'routeEngine': routeEngine};
  }

  factory ExpandBubbleConfig.fromJson(Map<String, dynamic> json) {
    return ExpandBubbleConfig(
      width: json['width'],
      height: json['height'],
      routeEngine: json['routeEngine'],
    );
  }
}
