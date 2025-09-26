import 'dart:ui';

/// Represents a position in 2D space
class BubblePosition {
  final double x;
  final double y;

  const BubblePosition({required this.x, required this.y});

  /// Create from Offset
  factory BubblePosition.fromOffset(Offset offset) {
    return BubblePosition(x: offset.dx, y: offset.dy);
  }

  /// Convert to Offset
  Offset toOffset() => Offset(x, y);

  /// Create a copy with modified values
  BubblePosition copyWith({double? x, double? y}) {
    return BubblePosition(x: x ?? this.x, y: y ?? this.y);
  }

  factory BubblePosition.fromJson(Map<String, dynamic> json) {
    return BubblePosition(x: json['x'], y: json['y']);
  }

  Map<String, dynamic> toJson() {
    return {'x': x, 'y': y};
  }
}

/// Edge side enumeration for bubble positioning
enum BubbleEdgeSide { left, right, top, bottom, none }

/// Bubble state information
class BubbleState {
  final BubblePosition position;

  final BubbleEdgeSide edgeSide;

  final bool isExpanded;

  final bool isVisible;

  const BubbleState({
    required this.position,
    this.edgeSide = BubbleEdgeSide.none,
    this.isExpanded = false,
    this.isVisible = true,
  });

  /// Create a copy with modified values
  BubbleState copyWith({
    BubblePosition? position,
    BubbleEdgeSide? edgeSide,
    bool? isExpanded,
    bool? isVisible,
  }) {
    return BubbleState(
      position: position ?? this.position,
      edgeSide: edgeSide ?? this.edgeSide,
      isExpanded: isExpanded ?? this.isExpanded,
      isVisible: isVisible ?? this.isVisible,
    );
  }

  /// Convert to JSON
  Map<String, dynamic> toJson() {
    return {
      'edgeSide': edgeSide.name,
      'isExpanded': isExpanded,
      'isVisible': isVisible,
    };
  }

  /// Create from JSON
  factory BubbleState.fromJson(Map<String, dynamic> json) {
    return BubbleState(
      position: BubblePosition.fromJson(json['position']),
      edgeSide: BubbleEdgeSide.values.firstWhere(
        (e) => e.name == json['edgeSide'],
        orElse: () => BubbleEdgeSide.none,
      ),
      isExpanded: json['isExpanded'] ?? false,
      isVisible: json['isVisible'] ?? true,
    );
  }
}
