package com.example.pl_bubble

import android.content.Context
import com.example.pl_bubble.models.BubbleConfig

/*
    * Singleton object to hold the BubbleConfig instance for global access
 */
object ServiceInstance {
    // Variable to hold the BubbleConfig instance
    var bubbleConfig: BubbleConfig? = null

    // Variable to hold the FlutterEngine instance
    var applicationContext: Context? = null
}