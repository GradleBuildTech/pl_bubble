import 'dart:async';

import '../models/bubble_action.dart';

/// Observer bubble is a singleton class that is used to observe the bubble events
/// It is used to observe the bubble events and emit the events to the stream
/// It is used to observe the bubble events and emit the events to the stream
class ObserverBubble {
  static final ObserverBubble _instance = ObserverBubble._();

  factory ObserverBubble() => _instance;

  static ObserverBubble get instance => _instance;

  ObserverBubble._();

  final _controller = StreamController<BubbleAction>.broadcast();

  Stream<BubbleAction> get stream => _controller.stream;

  void emit(BubbleAction event) => _controller.add(event);

  void listen(Function(BubbleAction) listener) {
    _controller.stream.listen(listener);
  }

  void close() => _controller.close();
}

/// BubbleDelgation is a singleton class that is used to delegate the bubble actions
class BubbleDelgation {
  ObserverBubble get observerBubble => ObserverBubble.instance;

  void action(BubbleAction event) => observerBubble.emit(event);
}
