package com.example.pl_bubble.events

/*
    * Sealed class representing different bubble events
    * Used for managing bubble visibility and state changes
 */
sealed class BubbleEvent {
    // Event to show the bubble
    object ShowBubble : BubbleEvent()

    // Event to hide the bubble
    object HideBubble : BubbleEvent()

    // Event to toggle the bubble's visibility
    object ToggleBubble : BubbleEvent()

    // Event triggered when the bubble is clicked
    object  OnClickBubble : BubbleEvent()

    // Event to update the bubble's visibility state
    data class UpdateBubbleState(val isVisible: Boolean) : BubbleEvent()

    // Event to move the bubble to a new position
    data class BubbleMovement(val x: Double, val y: Double) : BubbleEvent()
}

//Bubble event Adaptor
fun BubbleEvent.toEventString(): String {
    return when (this) {
        is BubbleEvent.ShowBubble -> "ShowBubble"
        is BubbleEvent.HideBubble -> "HideBubble"
        is BubbleEvent.ToggleBubble -> "ToggleBubble"
        is BubbleEvent.UpdateBubbleState -> "UpdateBubbleState"
        is BubbleEvent.OnClickBubble -> "OnClickBubble"
        is BubbleEvent.BubbleMovement -> "BubbleMovement"
    }
}

fun BubbleEvent.toDataMap(): Any? {
    return when(this) {
        is BubbleEvent.UpdateBubbleState -> mapOf("isVisible" to this.isVisible)
        is BubbleEvent.BubbleMovement -> mapOf("x" to this.x, "y" to this.y)
        else -> null
    }
}


// Data class to encapsulate a bubble event along with optional data
data class BubbleSendFormat (
    val event: BubbleEvent,
    val data: Map<String, Any>? = null
)

// Extension function to convert BubbleSendFormat to a Map for easier serialization or transmission
fun BubbleSendFormat.toMap(): Map<String, Any?> {
    return mapOf(
        "eventType" to event.toEventString(),
        "data" to event.toDataMap()
    )
}
