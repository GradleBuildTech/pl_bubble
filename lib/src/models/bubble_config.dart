import 'package:flutter/material.dart';
import 'bubble_position.dart';
import 'expand_bubble_config.dart';

/// Configuration class for bubble appearance and behavior
class BubbleConfig {
  /// Initial position of the bubble
  final BubblePosition startPosition;

  /// Size of the bubble
  final Size bubbleSize;

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

  final bool showBubbleWhenInit;

  final ExpandBubbleConfig? expandBubbleConfig;

  const BubbleConfig({
    this.showBubbleWhenInit = true,
    this.startPosition = const BubblePosition(x: 0, y: 0),
    this.bubbleSize = const Size(60, 60),
    this.animateToEdge = true,
    this.isDraggable = true,
    this.closeDistance = 200.0,
    this.closeBottomDistance = 100.0,
    this.showCloseAnimation = false,
    this.expandBubbleConfig,
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
    ExpandBubbleConfig? expandBubbleConfig,
    bool? showBubbleWhenInit,
  }) {
    return BubbleConfig(
      showBubbleWhenInit: showBubbleWhenInit ?? this.showBubbleWhenInit,
      startPosition: startPosition ?? this.startPosition,
      bubbleSize: bubbleSize ?? this.bubbleSize,
      animateToEdge: animateToEdge ?? this.animateToEdge,
      isDraggable: isDraggable ?? this.isDraggable,
      closeDistance: closeDistance ?? this.closeDistance,
      closeBottomDistance: closeBottomDistance ?? this.closeBottomDistance,
      showCloseAnimation: showCloseAnimation ?? this.showCloseAnimation,
      expandBubbleConfig: expandBubbleConfig ?? this.expandBubbleConfig,
    );
  }

  /// Convert to JSON for platform communication
  Map<String, dynamic> toJson() {
    return {
      'startPosition': startPosition.toJson(),
      'bubbleSize': {'width': bubbleSize.width, 'height': bubbleSize.height},
      'showBubbleWhenInit': showBubbleWhenInit,
      'animateToEdge': animateToEdge,
      'isDraggable': isDraggable,
      'closeDistance': closeDistance,
      'closeBottomDistance': closeBottomDistance,
      'showCloseAnimation': showCloseAnimation,
      'expandBubbleConfig': expandBubbleConfig?.toJson(),
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
      showBubbleWhenInit: json['showBubbleWhenInit'] ?? true,
      animateToEdge: json['animateToEdge'] ?? true,
      isDraggable: json['isDraggable'] ?? true,
      closeDistance: json['closeDistance']?.toDouble() ?? 200.0,
      closeBottomDistance: json['closeBottomDistance']?.toDouble() ?? 100.0,
      showCloseAnimation: json['showCloseAnimation'] ?? false,
      expandBubbleConfig: json['expandBubbleConfig'] != null
          ? ExpandBubbleConfig.fromJson(json['expandBubbleConfig'])
          : null,
    );
  }
}
