import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

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

class MainPage extends StatelessWidget {
  const MainPage({super.key});

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
              onPressed: () {
                print("Open Bubble");

                MethodChannel(
                  "com.example.pl_bubble/bubble",
                ).invokeMethod("OPEN_BUBBLE_CHANNEL", "openBubble");
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
