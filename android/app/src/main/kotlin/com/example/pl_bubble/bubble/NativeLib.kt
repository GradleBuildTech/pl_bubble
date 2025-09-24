package com.example.pl_bubble.bubble

class NativeLib {

    /**
     * A native method that is implemented by the 'pl_bubble.bubble' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'pl_bubble.bubble' library on application startup.
        init {
            System.loadLibrary("pl_bubble")
        }
    }
}