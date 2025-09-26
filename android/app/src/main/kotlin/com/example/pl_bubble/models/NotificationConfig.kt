package com.example.pl_bubble.models

// Data class to hold notification configuration
data class NotificationConfig(
    val contentTitle: String,
    val contentText: String
)

// Extension function to convert a Map to a NotificationConfig object
fun Map<*, *>.toNotificationConfig(): NotificationConfig? {
    val contentTitle = this["contentTitle"] as? String ?: return null
    val contentText = this["contentText"] as? String ?: return null

    return NotificationConfig(
        contentTitle = contentTitle,
        contentText = contentText
    )
}