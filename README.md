# PL Bubble 🫧

A powerful Flutter plugin for creating floating bubble UI components on Android. This plugin allows you to create draggable, expandable bubbles that can overlay other apps, perfect for quick access features, chat heads, or floating action buttons.

## Features ✨

- 🫧 **Floating Bubble**: Create draggable floating bubbles that stay on top of other apps
- 📱 **Expandable UI**: Expand bubbles to show full Flutter UI content
- 🎯 **Drag & Drop**: Smooth dragging with edge snapping animations
- 🔒 **Permission Management**: Built-in overlay permission handling
- 📡 **Event System**: Comprehensive event listening for user interactions
- 🎨 **Customizable**: Highly configurable appearance and behavior
- 🔄 **State Management**: Track bubble state changes and visibility
- 📱 **Android Support**: Optimized for Android platform

## Installation 📦

Add this to your package's `pubspec.yaml` file:

```yaml
dependencies:
  pl_bubble: ^1.0.0
```

Then run:

```bash
flutter pub get
```

## Android Setup 🤖

### 1. Add Overlay Permission

Add the following permission to your `android/app/src/main/AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

### 2. Add Service Declaration

Add the bubble service to your `AndroidManifest.xml`:

```xml
<service
    android:name="com.example.pl_bubble.BubbleManager"
    android:enabled="true"
    android:exported="false"
    android:foregroundServiceType="mediaProjection" />
```

## Quick Start 🚀

### 1. Basic Setup

```dart
import 'package:pl_bubble/pl_bubble.dart';

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> implements BubbleEventListener {
  @override
  void initState() {
    super.initState();
    _initializeBubble();
  }

  Future<void> _initializeBubble() async {
    // Initialize the bubble service
    await AndroidBubbleService.instance.initialize(
      BubbleConfig(
        startPosition: BubblePosition(x: 0, y: 300),
        bubbleSize: Size(60, 60),
        animateToEdge: true,
        isDraggable: true,
        showBubbleWhenInit: false,
        expandBubbleConfig: ExpandBubbleConfig(
          width: 300.0,
          height: 400.0,
          routeEngine: "/expandBubble",
        ),
      ),
    );

    // Listen to bubble events
    AndroidBubbleService.instance.eventStream.listen((event) {
      switch (event) {
        case BubbleClickEvent():
          AndroidBubbleService.instance.expandBubble();
        case BubbleErrorEvent():
          print('Bubble error: ${event.errorMessage}');
        default:
          break;
      }
    });

    AndroidBubbleService.instance.addEventListener(this);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      routes: {
        "/expandBubble": (context) => ExpandBubblePage(),
        "/": (context) => MainPage(),
      },
    );
  }

  // Bubble event listeners
  @override
  void onVisibilityChange(bool isVisible) {
    print('Bubble visibility: $isVisible');
  }

  @override
  void onExpand(bool isExpanded) {
    print('Bubble expanded: $isExpanded');
  }

  @override
  void onClose() {
    print('Bubble closed');
  }

  @override
  void onAnimateToEdge(BubbleEdgeSide targetEdge) {
    print('Bubble moved to: $targetEdge');
  }

  @override
  void onStateChange(BubbleState previousState, BubbleState newState) {
    print('Bubble state changed');
  }
}
```

### 2. Show/Hide Bubble

```dart
// Show the bubble
await AndroidBubbleService.instance.showBubble();

// Hide the bubble
await AndroidBubbleService.instance.hideBubble();

// Close the bubble completely
await AndroidBubbleService.instance.closeBubble();
```

### 3. Expand Bubble

```dart
// Expand the bubble
await AndroidBubbleService.instance.expandBubble();

// Close the expanded bubble
await AndroidBubbleService.instance.closeExpandBubble();

// Hide the expanded bubble
await AndroidBubbleService.instance.hideExpandBubble();
```

## Configuration ⚙️

### BubbleConfig

```dart
BubbleConfig(
  // Initial position of the bubble
  startPosition: BubblePosition(x: 0, y: 300),
  
  // Size of the bubble
  bubbleSize: Size(60, 60),
  
  // Whether the bubble should animate to screen edges
  animateToEdge: true,
  
  // Whether the bubble can be dragged
  isDraggable: true,
  
  // Distance threshold for closing the bubble
  closeDistance: 200.0,
  
  // Distance from bottom edge to trigger close
  closeBottomDistance: 100.0,
  
  // Whether to show close animation
  showCloseAnimation: false,
  
  // Whether to show the bubble when initializing
  showBubbleWhenInit: false,
  
  // Configuration for the expanded bubble
  expandBubbleConfig: ExpandBubbleConfig(
    width: 300.0,
    height: 400.0,
    routeEngine: "/expandBubble",
  ),
  
  // Configuration for notifications
  notificationConfig: NotificationConfig(
    // Add notification settings here
  ),
)
```

### ExpandBubbleConfig

```dart
ExpandBubbleConfig(
  width: 300.0,           // Width of expanded bubble
  height: 400.0,          // Height of expanded bubble
  routeEngine: "/expandBubble", // Flutter route to show
)
```

## Events 📡

The plugin provides comprehensive event handling:

### Event Types

```dart
// Touch events
BubbleTouchStartEvent()
BubbleTouchMoveEvent(deltaX: 10.0, deltaY: 5.0)
BubbleTouchEndEvent()

// Interaction events
BubbleClickEvent()
BubbleExpandEvent()
BubbleCollapseEvent()
BubbleCloseEvent()

// Animation events
BubbleAnimateToEdgeEvent(targetEdge: BubbleEdgeSide.right)

// State events
BubbleStateChangeEvent(previousState: oldState, newState: newState)
BubbleVisibilityChangeEvent(isVisible: true)

// Error events
BubbleErrorEvent(errorMessage: "Something went wrong")

// Permission events
BubblePermissionRequestEvent(permission: "overlay", isGranted: true)
```

### Event Listening

```dart
// Method 1: Stream listening
AndroidBubbleService.instance.eventStream.listen((event) {
  switch (event) {
    case BubbleClickEvent():
      // Handle bubble click
      break;
    case BubbleErrorEvent():
      // Handle error
      break;
    default:
      break;
  }
});

// Method 2: Event listener interface
class MyBubbleListener implements BubbleEventListener {
  @override
  void onVisibilityChange(bool isVisible) {
    // Handle visibility change
  }

  @override
  void onExpand(bool isExpanded) {
    // Handle expand/collapse
  }

  @override
  void onClose() {
    // Handle close
  }

  @override
  void onAnimateToEdge(BubbleEdgeSide targetEdge) {
    // Handle edge animation
  }

  @override
  void onStateChange(BubbleState previousState, BubbleState newState) {
    // Handle state change
  }
}

// Register listener
AndroidBubbleService.instance.addEventListener(MyBubbleListener());
```

## Permissions 🔒

### Check Permission

```dart
bool hasPermission = await AndroidBubbleService.instance.hasOverlayPermission();
```

### Request Permission

```dart
bool granted = await AndroidBubbleService.instance.requestOverlayPermission();
```

### Handle Permission in UI

```dart
Future<void> _checkAndRequestPermission() async {
  bool hasPermission = await AndroidBubbleService.instance.hasOverlayPermission();
  
  if (!hasPermission) {
    bool granted = await AndroidBubbleService.instance.requestOverlayPermission();
    if (granted) {
      // Permission granted, proceed with bubble
      await AndroidBubbleService.instance.showBubble();
    } else {
      // Permission denied, show explanation
      _showPermissionDialog();
    }
  } else {
    // Permission already granted
    await AndroidBubbleService.instance.showBubble();
  }
}
```

## Advanced Usage 🔧

### Custom Bubble Position

```dart
// Move bubble to specific position
await AndroidBubbleService.instance.moveBubble(
  BubblePosition(x: 100, y: 200)
);
```

### Update Configuration

```dart
// Update bubble configuration
await AndroidBubbleService.instance.updateConfig(
  BubbleConfig(
    bubbleSize: Size(80, 80),
    isDraggable: false,
    // ... other config
  )
);
```

### State Management

```dart
// Get current bubble state
BubbleState currentState = AndroidBubbleService.instance.currentState;

// Check if bubble is visible
bool isVisible = AndroidBubbleService.instance.isVisible;

// Check if bubble is expanded
bool isExpanded = AndroidBubbleService.instance.isExpanded;
```

## Error Handling 🚨

```dart
try {
  await AndroidBubbleService.instance.showBubble();
} on BubbleException catch (e) {
  print('Bubble error: ${e.message}');
  print('Error code: ${e.code}');
} catch (e) {
  print('Unexpected error: $e');
}
```

## Troubleshooting 🔍

### Common Issues

1. **MissingPluginException**: Ensure the plugin is properly registered in Android
2. **Permission Denied**: Check if overlay permission is granted
3. **Bubble Not Showing**: Verify service initialization and configuration
4. **Events Not Firing**: Check event listener registration

### Debug Tips

```dart
// Enable debug logging
import 'package:pl_bubble/src/utils/logger.dart';

Logger.d('MyTag', 'Debug message');
```

## API Reference 📚

### BubbleService Interface

```dart
abstract class BubbleService {
  Stream<BubbleEvent> get eventStream;
  BubbleState get currentState;
  bool get isVisible;
  bool get isExpanded;
  
  Future<void> initialize(BubbleConfig config);
  Future<void> showBubble();
  Future<void> hideBubble();
  Future<void> expandBubble({bool isRemoveBubble = false});
  Future<void> closeExpandBubble();
  Future<void> hideExpandBubble();
  Future<void> moveBubble(BubblePosition position);
  Future<void> updateConfig(BubbleConfig config);
  Future<void> closeBubble();
  Future<bool> hasOverlayPermission();
  Future<bool> requestOverlayPermission();
  void addEventListener(BubbleEventListener listener);
  void removeEventListener(BubbleEventListener listener);
  Future<void> dispose();
}
```

## Contributing 🤝

Contributions are welcome! Please feel free to submit a Pull Request.

## License 📄

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support 💬

If you encounter any issues or have questions, please file an issue on the GitHub repository.

---

Made with ❤️ for Flutter developers