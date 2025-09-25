package com.example.pl_bubble.models
/**
 * Configuration data class for expanding bubble settings.
 *
 * @property width The width of the expanded bubble.
 * @property height The height of the expanded bubble.
 * @property routeEngine The routing engine to be used.
 */
data class ExpandBubbleConfig(
    val width: Double,
    val height: Double,
    val routeEngine: String
)

// Extension function to convert a Map to an ExpandBubbleConfig object
fun Map<*, *>.toExpandBubbleConfig(): ExpandBubbleConfig? {
    val width = this["width"] as? Double ?: return null
    val height = this["height"] as? Double ?: return null
    val routeEngine = this["routeEngine"] as? String ?: return null

    return ExpandBubbleConfig(
        width = width,
        height = height,
        routeEngine = routeEngine
    )
}