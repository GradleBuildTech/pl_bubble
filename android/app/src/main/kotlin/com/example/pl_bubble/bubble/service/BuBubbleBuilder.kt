package com.example.pl_bubble.bubble.service

import android.content.Context
import android.graphics.Point
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.example.pl_bubble.bubble.event.BubbleListener
import com.example.pl_bubble.bubble.utils.CLOSE_BOTTOM_DIST
import com.example.pl_bubble.models.BubbleConfig


/**
 * BuBubbleBuilder is a class that takes in a context
 * It is used to build the bubble
 * It is used to build the bubble
 * Example:
 * ```kotlin
 * val bubble = BuBubbleBuilder(context).bubbleViewCompose {
 *      BubbleViewContent()
 *      }
 *      .closeBubbleViewCompose { CloseBubbleViewContent()}
 *      ```
 */
class BuBubbleBuilder(private val context: Context) {

    /**
     * ✨ Config data of bubbleView
     * This can be a View or a ComposeView.
     * If it is a View, it will be used as the bubble view.
     * If it is a ComposeView, it will be used to display the bubble content.
     * 📃 startPoint is the initial position of the bubble view.
     * 📃 isAnimateToEdgeEnabled is a flag to enable or disable the animation to the edge of the screen.
     * 📃 forceDragging is a flag to force the bubble to be dragged.
     * */

    internal var bubbleView: View? = null
    internal var bubbleComposeView: ComposeView? = null
    internal var startPoint = Point(0, 0)
    internal var isAnimateToEdgeEnabled = true
    internal var listener: BubbleListener? = null
    internal var forceDragging: Boolean = true
    // ---------------------------------------------------

    /**
     * ✨ Config data of closeBubbleView
     * This can be a View or a ComposeView.
     * If it is a View, it will be used as the close button.
     * If it is a ComposeView, it will be used to display the close button content.
     * 📃 distanceToClose is the distance to close the bubble view.
     * 📃 closeBottomDist is the distance from the bottom of the screen to the close button.
     * 📃 animatedClose is a flag to enable or disable the animation when closing the bubble view.
     */
    internal var closeView: View? = null
    internal var closeComposeView: ComposeView? = null
    internal var distanceToClose: Int = 100
    internal var closeBottomDist: Int = CLOSE_BOTTOM_DIST
    internal var animatedClose: Boolean = true
    // ---------------------------------------------------


    /**
     * ✨ Config data of expandBubbleView
     * This can be a ComposeView.
     * It will be used to display the expanded bubble view.
     * 📃 expandDragToClose is a flag to enable or disable the drag to close feature.
     */
    internal var expandView: View? = null
    internal var expandBubbleView: ComposeView? = null
    internal var expandDragToClose = false
    /// ---------------------------------------------------

    ///✨ Config data of flowKeyboardBubbleView
    internal var flowKeyboardBubbleView: ComposeView? = null


    /// Handle builder pattern
    fun bubbleView(view: View): BuBubbleBuilder {
        this.bubbleView = view
        return this
    }

    fun bubbleComposeView(content: (@Composable () -> Unit)): BuBubbleBuilder {
        this.bubbleComposeView = ComposeView(context).apply {
            setContent(content)
        }
        return this
    }

    fun closeBubbleViewCompose(content: @Composable () -> Unit): BuBubbleBuilder {
        this.closeComposeView = ComposeView(context).apply {
            setContent(content)
        }
        return this
    }

    fun closeView(view: View): BuBubbleBuilder {
        this.closeView = view
        return this
    }

    fun expandView(view: View): BuBubbleBuilder {
        this.expandView = view
        return this
    }

    fun expandBubbleViewCompose(content: @Composable () -> Unit): BuBubbleBuilder {
        this.expandBubbleView = ComposeView(context).apply {
            setContent(content)
        }
        return this
    }

    fun expandBubbleViewDragToClose(expandDragToClose: Boolean): BuBubbleBuilder {
        this.expandDragToClose = expandDragToClose
        return this
    }


    fun flowKeyboardBubbleViewCompose(content: @Composable () -> Unit): BuBubbleBuilder {
        this.flowKeyboardBubbleView = ComposeView(context).apply {
            setContent(content)
        }
        return this
    }

    fun bubbleStartPoint(point: Point): BuBubbleBuilder {
        this.startPoint = point
        return this
    }

    fun bubbleListener(listener: BubbleListener): BuBubbleBuilder {
        this.listener = listener
        return this
    }

    fun bubbleForceDragging(forceDragging: Boolean): BuBubbleBuilder {
        this.forceDragging = forceDragging
        return this
    }

    fun bubbleDistanceToClose(distanceToClose: Int): BuBubbleBuilder {
        this.distanceToClose = distanceToClose
        return this
    }

    fun bubbleCloseBottomDist(closeBottomDist: Int): BuBubbleBuilder {
        this.closeBottomDist = closeBottomDist
        return this
    }

    fun bubbleAnimateToEdgeEnabled(isAnimateToEdgeEnabled: Boolean): BuBubbleBuilder {
        this.isAnimateToEdgeEnabled = isAnimateToEdgeEnabled
        return this
    }

    fun bubbleAnimatedClose(animatedClose: Boolean): BuBubbleBuilder {
        this.animatedClose = animatedClose
        return this
    }

    fun fromBubbleConfig(bubbleConfig: BubbleConfig): BuBubbleBuilder {
        this.startPoint = Point(
            bubbleConfig.startX.toInt(),
            bubbleConfig.startY.toInt()
        )
        this.isAnimateToEdgeEnabled = bubbleConfig.animateToEdge
        this.forceDragging = bubbleConfig.isDraggable

        this.distanceToClose = bubbleConfig.closeDistance.toInt()
        this.closeBottomDist = bubbleConfig.closeBottomDistance.toInt()

        this.animatedClose = bubbleConfig.showCloseAnimation

        return this
    }
}