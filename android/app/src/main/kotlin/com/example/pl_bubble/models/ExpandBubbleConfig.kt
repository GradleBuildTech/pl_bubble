package com.example.pl_bubble.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Configuration data class for expanding bubble settings.
 *
 * @property width The width of the expanded bubble.
 * @property height The height of the expanded bubble.
 * @property routeEngine The routing engine to be used.
 */
@Parcelize
data class ExpandBubbleConfig(
    val width: Double,
    val height: Double,
    val routeEngine: String
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(width)
        dest.writeDouble(height)
        dest.writeString(routeEngine)
    }
}

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

// Extension function to convert an ExpandBubbleConfig object to a Map
fun ExpandBubbleConfig.toMap(): Map<String, Any> {
    return mapOf(
        "width" to width,
        "height" to height,
        "routeEngine" to routeEngine
    )
}