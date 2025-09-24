import 'package:flutter/material.dart';
import 'bubble_position.dart';

/// Configuration class for bubble appearance and behavior
class BubbleConfig {
  /// Initial position of the bubble
  final BubblePosition startPosition;

  /// Size of the bubble
  final Size bubbleSize;

  /// Size of the expanded bubble
  final Size expandedSize;

  /// Whether the bubble should animate to screen edges
  final bool animateToEdge;

  /// Whether the bubble can be dragged
  final bool isDraggable;

  /// Distance threshold for closing the bubble
  final double closeDistance;

  /// Distance from bottom edge to trigger close
  final double closeBottomDistance;

  /// Whether to show close animation
  final bool showCloseAnimation;

  /// Bubble icon/asset
  final Widget? bubbleIcon;

  /// Close button icon/asset
  final Widget? closeIcon;

  /// Background color of the bubble
  final Color backgroundColor;

  /// Border radius of the bubble
  final double borderRadius;

  /// Shadow configuration
  final List<BoxShadow>? shadows;

  /// Animation duration for expand/collapse
  final Duration animationDuration;

  /// Animation curve for expand/collapse
  final Curve animationCurve;

  const BubbleConfig({
    this.startPosition = const BubblePosition(x: 0, y: 0),
    this.bubbleSize = const Size(60, 60),
    this.expandedSize = const Size(300, 400),
    this.animateToEdge = true,
    this.isDraggable = true,
    this.closeDistance = 200.0,
    this.closeBottomDistance = 100.0,
    this.showCloseAnimation = false,
    this.bubbleIcon,
    this.closeIcon,
    this.backgroundColor = Colors.blue,
    this.borderRadius = 30.0,
    this.shadows,
    this.animationDuration = const Duration(milliseconds: 300),
    this.animationCurve = Curves.easeInOut,
  });

  /// Create a copy of this config with modified values
  BubbleConfig copyWith({
    BubblePosition? startPosition,
    Size? bubbleSize,
    Size? expandedSize,
    bool? animateToEdge,
    bool? isDraggable,
    double? closeDistance,
    double? closeBottomDistance,
    bool? showCloseAnimation,
    Widget? bubbleIcon,
    Widget? closeIcon,
    Color? backgroundColor,
    double? borderRadius,
    List<BoxShadow>? shadows,
    Duration? animationDuration,
    Curve? animationCurve,
  }) {
    return BubbleConfig(
      startPosition: startPosition ?? this.startPosition,
      bubbleSize: bubbleSize ?? this.bubbleSize,
      expandedSize: expandedSize ?? this.expandedSize,
      animateToEdge: animateToEdge ?? this.animateToEdge,
      isDraggable: isDraggable ?? this.isDraggable,
      closeDistance: closeDistance ?? this.closeDistance,
      closeBottomDistance: closeBottomDistance ?? this.closeBottomDistance,
      showCloseAnimation: showCloseAnimation ?? this.showCloseAnimation,
      bubbleIcon: bubbleIcon ?? this.bubbleIcon,
      closeIcon: closeIcon ?? this.closeIcon,
      backgroundColor: backgroundColor ?? this.backgroundColor,
      borderRadius: borderRadius ?? this.borderRadius,
      shadows: shadows ?? this.shadows,
      animationDuration: animationDuration ?? this.animationDuration,
      animationCurve: animationCurve ?? this.animationCurve,
    );
  }

  /// Convert to JSON for platform communication
  Map<String, dynamic> toJson() {
    return {
      'startPosition': startPosition.toJson(),
      'bubbleSize': {'width': bubbleSize.width, 'height': bubbleSize.height},
      'expandedSize': {
        'width': expandedSize.width,
        'height': expandedSize.height,
      },
      'animateToEdge': animateToEdge,
      'isDraggable': isDraggable,
      'closeDistance': closeDistance,
      'closeBottomDistance': closeBottomDistance,
      'showCloseAnimation': showCloseAnimation,
      'backgroundColor': backgroundColor.value,
      'borderRadius': borderRadius,
      'animationDuration': animationDuration.inMilliseconds,
    };
  }

  /// Create from JSON
  factory BubbleConfig.fromJson(Map<String, dynamic> json) {
    return BubbleConfig(
      startPosition: BubblePosition.fromJson(json['startPosition']),
      bubbleSize: Size(
        json['bubbleSize']['width']?.toDouble() ?? 60.0,
        json['bubbleSize']['height']?.toDouble() ?? 60.0,
      ),
      expandedSize: Size(
        json['expandedSize']['width']?.toDouble() ?? 300.0,
        json['expandedSize']['height']?.toDouble() ?? 400.0,
      ),
      animateToEdge: json['animateToEdge'] ?? true,
      isDraggable: json['isDraggable'] ?? true,
      closeDistance: json['closeDistance']?.toDouble() ?? 200.0,
      closeBottomDistance: json['closeBottomDistance']?.toDouble() ?? 100.0,
      showCloseAnimation: json['showCloseAnimation'] ?? false,
      backgroundColor: Color(json['backgroundColor'] ?? Colors.blue.value),
      borderRadius: json['borderRadius']?.toDouble() ?? 30.0,
      animationDuration: Duration(
        milliseconds: json['animationDuration'] ?? 300,
      ),
    );
  }
}
