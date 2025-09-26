package com.example.pl_bubble

import com.example.pl_bubble.models.BubbleConfig

/*
    * Singleton object to hold the BubbleConfig instance for global access
 */
object ServiceInstance {
    // Variable to hold the BubbleConfig instance
    var bubbleConfig: BubbleConfig? = null
}