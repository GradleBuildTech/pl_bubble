import 'package:flutter/material.dart';
import 'package:pl_bubble/src/utils/logger.dart';

import 'pl_bubble.dart';

void main() {
  runApp(const MainApp());
}

class MainApp extends StatelessWidget {
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      showSemanticsDebugger: false,

      routes: {
        "/expandBubble": (context) => const ExpandBubblePage(),
        "/": (context) => const MainPage(),
      },
    );
  }
}

class MainPage extends StatefulWidget {
  const MainPage({super.key});

  @override
  State<MainPage> createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> implements BubbleEventListener {
  @override
  void initState() {
    super.initState();

    AndroidBubbleService.instance.initialize(
      BubbleConfig(
        startPosition: BubblePosition(x: 0, y: 300),

        bubbleSize: Size(60, 60),
        animateToEdge: true,
        isDraggable: true,
        showBubbleWhenInit: false,

        closeDistance: 200.0,
        expandBubbleConfig: ExpandBubbleConfig(
          width: BubbleConstants.matchParent.toDouble(),
          height: 400.0,
          routeEngine: "/expandBubble",
        ),
      ),
    );

    AndroidBubbleService.instance.eventStream.listen((event) {
      switch (event) {
        case BubbleClickEvent():
          AndroidBubbleService.instance.expandBubble();

        case BubbleErrorEvent():
          Logger.d('MainPage', event.errorMessage);

        default:
          Logger.d("Unknown event: ${event.runtimeType}", "Unknown event");
      }
    });

    AndroidBubbleService.instance.addEventListener(this);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SizedBox(
        width: double.infinity,
        height: double.infinity,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            TextButton(
              onPressed: () async {
                try {
                  await AndroidBubbleService.instance.showBubble();
                } catch (e) {
                  Logger.d('MainPage', e.toString());
                }
              },
              child: const Text('Open Bubble'),
            ),
          ],
        ),
      ),
    );
  }

  @override
  void onAnimateToEdge(BubbleEdgeSide targetEdge) {}

  @override
  void onClose() {}

  @override
  void onExpand(bool isExpanded) {}

  @override
  void onStateChange(BubbleState previousState, BubbleState newState) {}

  @override
  void onVisibilityChange(bool isVisible) {}
}

class ExpandBubblePage extends StatelessWidget {
  const ExpandBubblePage({super.key});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: double.infinity,
      height: 300,

      child: Scaffold(
        body: Center(child: Text("Hello from Flutter inside Bubble 🚀")),
      ),
    );
  }
}
