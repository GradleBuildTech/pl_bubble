package com.example.pl_bubble.bubble.event

interface BubbleListener {
    fun onFingerDown(x: Float, y: Float) {}
    fun onFingerUp(x: Float, y: Float) {}
    fun onFingerMove(x: Float, y: Float) {}
}