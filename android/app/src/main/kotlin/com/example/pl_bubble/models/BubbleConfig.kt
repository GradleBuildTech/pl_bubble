package com.example.pl_bubble.models

// Configuration data class for a floating bubble UI component
data class BubbleConfig(
    // Unique identifier for the bubble
    val width: Double,
    val height: Double,

    // Initial position of the bubble on the screen
    val startX: Double,
    val startY: Double,

    // Determines if the bubble should animate to the nearest edge when released
    val animateToEdge: Boolean,

    // Determines if the bubble can be dragged by the user
    val isDraggable: Boolean,

    // Distance thresholds for snapping to edges or closing
    val closeDistance: Double,
    val closeBottomDistance: Double,

    // Animation settings for closing the bubble
    val showCloseAnimation: Boolean,

    // Optional configuration for expanding the bubble
    val expandBubbleConfig: ExpandBubbleConfig?,

    val showBubbleWhenInit: Boolean,
)

// Extension function to convert a Map to a BubbleConfig object
fun Map<*, *>.toBubbleConfig(): BubbleConfig? {
    val bubblePosition = ( this["startPosition"] as? Map<*, *> ?)?.toBubblePosition()
    val startX = bubblePosition?.x ?: return null
    val startY = bubblePosition.y
    val bubbleSize = this["bubbleSize"] as? Map<*, *>
    val width = bubbleSize?.get("width") as? Double ?: return null
    val height = bubbleSize["height"] as? Double ?: return null

    val animateToEdge = this["animateToEdge"] as? Boolean ?: return null
    val isDraggable = this["isDraggable"] as? Boolean ?: return null
    val closeDistance = this["closeDistance"] as? Double ?: return null
    val closeBottomDistance = this["closeBottomDistance"] as? Double ?: return null
    val showCloseAnimation = this["showCloseAnimation"] as? Boolean ?: return null
    val showBubbleWhenInit = this["showBubbleWhenInit"] as? Boolean ?: return null
    val expandBubbleConfig = (this["expandBubbleConfig"] as? Map<*, *>)?.toExpandBubbleConfig()

    return BubbleConfig(
        width = width,
        height = height,
        startX = startX,
        startY = startY,
        animateToEdge = animateToEdge,
        isDraggable = isDraggable,
        closeDistance = closeDistance,
        closeBottomDistance = closeBottomDistance,
        showCloseAnimation = showCloseAnimation,
        expandBubbleConfig = expandBubbleConfig,
        showBubbleWhenInit = showBubbleWhenInit,
    )
}