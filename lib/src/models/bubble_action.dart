import 'bubble_config.dart';
import 'bubble_events.dart';
import 'bubble_position.dart';

/// Sealed class representing all possible bubble actions
sealed class BubbleAction {
  const BubbleAction();
}

/// Initialize bubble service with configuration
class InitializeBubbleAction extends BubbleAction {
  final BubbleConfig config;
  const InitializeBubbleAction(this.config);
}

/// Start event bridge listener
class StartEventBridgeListenerAction extends BubbleAction {
  const StartEventBridgeListenerAction();
}

/// Show the bubble
class ShowBubbleAction extends BubbleAction {
  const ShowBubbleAction();
}

/// Hide the bubble
class HideBubbleAction extends BubbleAction {
  const HideBubbleAction();
}

/// Expand the bubble
class ExpandBubbleAction extends BubbleAction {
  final bool isRemoveBubble;
  const ExpandBubbleAction({this.isRemoveBubble = false});
}

/// Close expand bubble
class CloseExpandBubbleAction extends BubbleAction {
  const CloseExpandBubbleAction();
}

/// Hide expand bubble
class HideExpandBubbleAction extends BubbleAction {
  const HideExpandBubbleAction();
}

/// Move bubble to position
class MoveBubbleAction extends BubbleAction {
  final BubblePosition position;
  const MoveBubbleAction(this.position);
}

/// Update bubble configuration
class UpdateConfigAction extends BubbleAction {
  final BubbleConfig config;
  const UpdateConfigAction(this.config);
}

/// Close bubble completely
class CloseBubbleAction extends BubbleAction {
  const CloseBubbleAction();
}

/// Check overlay permission
class HasOverlayPermissionAction extends BubbleAction {
  const HasOverlayPermissionAction();
}

/// Request overlay permission
class RequestOverlayPermissionAction extends BubbleAction {
  const RequestOverlayPermissionAction();
}

/// Add event listener
class AddEventListenerAction extends BubbleAction {
  final BubbleEventListener listener;
  const AddEventListenerAction(this.listener);
}

/// Remove event listener
class RemoveEventListenerAction extends BubbleAction {
  final BubbleEventListener listener;
  const RemoveEventListenerAction(this.listener);
}

/// Dispose service
class DisposeAction extends BubbleAction {
  const DisposeAction();
}
