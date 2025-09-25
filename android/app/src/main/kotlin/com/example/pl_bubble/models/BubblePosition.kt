package com.example.pl_bubble.models

// Data class to hold the position of a bubble on the screen
data class BubblePosition(
    var x: Double = 0.0,
    var y: Double = 0.0
)

// Extension function to convert a Map to a BubblePosition object
fun Map<*, *>.toBubblePosition(): BubblePosition? {
    val x = this["x"] as? Double ?: return null
    val y = this["y"] as? Double ?: return null

    return BubblePosition(
        x = x,
        y = y
    )
}