package com.example.pl_bubble.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Data class to hold notification configuration
@Parcelize
data class NotificationConfig(
    val contentTitle: String,
    val contentText: String
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(contentTitle)
        dest.writeString(contentText)
    }
}

// Extension function to convert a Map to a NotificationConfig object
fun Map<*, *>.toNotificationConfig(): NotificationConfig? {
    val contentTitle = this["contentTitle"] as? String ?: return null
    val contentText = this["contentText"] as? String ?: return null

    return NotificationConfig(
        contentTitle = contentTitle,
        contentText = contentText
    )
}


// Extension function to convert a NotificationConfig object to a Map
fun NotificationConfig.toMap(): Map<String, Any> {
    return mapOf(
        "contentTitle" to contentTitle,
        "contentText" to contentText
    )
}