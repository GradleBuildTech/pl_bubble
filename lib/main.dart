import 'package:flutter/material.dart';

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

class _MainPageState extends State<MainPage> {
  @override
  void initState() {
    AndroidBubbleService().initialize(
      BubbleConfig(
        startPosition: BubblePosition(x: 0, y: 0),
        bubbleSize: Size(60, 60),
        animateToEdge: true,
        isDraggable: true,
        closeDistance: 200.0,
        expandBubbleConfig: ExpandBubbleConfig(
          width: BubbleConstants.matchParent.toDouble(),
          height: 400.0,
          routeEngine: "/expandBubble",
        ),
      ),
    );
    super.initState();
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
                await AndroidBubbleService().showBubble();
              },
              child: const Text('Open Bubble'),
            ),
          ],
        ),
      ),
    );
  }
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
