package com.example.pl_bubble.models

import android.os.Parcel
import android.os.Parcelable

/*
    Data class to hold notification configuration details.
    Implements Parcelable to allow easy passing between Android components.
 */
data class NotificationConfig(
    val contentTitle: String,
    val contentText: String
) : Parcelable {
    constructor(parcel: Parcel): this(
        contentTitle = parcel.readString() ?: "",
        contentText = parcel.readString() ?: ""
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(contentTitle)
        dest.writeString(contentText)
    }

    companion object CREATOR : Parcelable.Creator<NotificationConfig> {
        override fun createFromParcel(parcel: Parcel): NotificationConfig {
            return NotificationConfig(parcel)
        }

        override fun newArray(size: Int): Array<NotificationConfig?> {
            return arrayOfNulls(size)
        }
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