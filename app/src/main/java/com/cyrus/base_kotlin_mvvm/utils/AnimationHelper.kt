package com.cyrus.base_kotlin_mvvm.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import androidx.core.animation.addListener
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

fun View.fadeIn(duration: Long = 500, onEnd: () -> Unit = {}) {
    val animator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    animator.duration = duration
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.addListener(onEnd = { onEnd.invoke() })
    animator.start()
}

fun View.fadeInDynamic(duration: Long = 500, onEnd: () -> Unit = {}) {
    clearAnimation()
    if (alpha == 1f) return
    val animator = ObjectAnimator.ofFloat(this, "alpha", alpha, 1f)
    animator.duration = duration
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.addListener(onEnd = { onEnd.invoke() })
    animator.start()
}

fun View.fadeInAndGoUp(duration: Long = 500, valueFromY: Float = 1f, onEnd: () -> Unit = {}) {
    val animationSet = AnimationSet(true)

    val fadeIn = AlphaAnimation(0f, 1f)
    fadeIn.duration = duration

    val translateUp = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, valueFromY,
        Animation.RELATIVE_TO_SELF, 0f
    )
    translateUp.duration = duration + 100

    animationSet.addAnimation(fadeIn)
    animationSet.addAnimation(translateUp)
    animationSet.interpolator = FastOutSlowInInterpolator()
    animationSet.fillAfter = true
    animationSet.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {

        }

        override fun onAnimationEnd(p0: Animation?) {
            onEnd.invoke()
        }

        override fun onAnimationRepeat(p0: Animation?) {

        }
    })
    startAnimation(animationSet)
}

fun View.fadeOutAndGoUp(duration: Long = 500, valueToY: Float = 0.5f, onEnd: () -> Unit = {}) {
    val animationSet = AnimationSet(true)

    val fadeIn = AlphaAnimation(1f, 0f)
    fadeIn.duration = duration

    val translateUp = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, -valueToY
    )
    translateUp.duration = duration +100

    animationSet.addAnimation(fadeIn)
    animationSet.addAnimation(translateUp)
    animationSet.interpolator = FastOutSlowInInterpolator()
    animationSet.fillAfter = true
    animationSet.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {

        }

        override fun onAnimationEnd(p0: Animation?) {
            onEnd.invoke()
        }

        override fun onAnimationRepeat(p0: Animation?) {

        }
    })
    startAnimation(animationSet)
}

fun View.fadeOut(duration: Long = 300, onEnd: () -> Unit = {}) {
    val fadeOutAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    fadeOutAnimator.duration = duration
    fadeOutAnimator.interpolator = AccelerateInterpolator()
    fadeOutAnimator.addListener(onEnd = { onEnd.invoke() })
    fadeOutAnimator.start()
}

fun View.fadeOutDynamic(duration: Long = 300, onEnd: () -> Unit = {}) {
    if (alpha == 0f) return
    clearAnimation()
    val fadeOutAnimator = ObjectAnimator.ofFloat(this, "alpha", alpha, 0f)
    fadeOutAnimator.duration = duration
    fadeOutAnimator.interpolator = AccelerateInterpolator()
    fadeOutAnimator.addListener(onEnd = { onEnd.invoke() })
    fadeOutAnimator.start()
}

fun View.fadeInAndScaleUp(duration: Long = 500, onEnd: () -> Unit = {}) {
    val scaleUpX = ObjectAnimator.ofFloat(this, "scaleX", 0.5f, 1f)
    val scaleUpY = ObjectAnimator.ofFloat(this, "scaleY", 0.5f, 1f)

    val fadeIn = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)

    val animatorSet = AnimatorSet()
    animatorSet.playTogether(scaleUpX, scaleUpY, fadeIn)
    animatorSet.duration = duration
    animatorSet.interpolator = FastOutSlowInInterpolator()
    animatorSet.addListener(onEnd = { onEnd.invoke() })
    animatorSet.start()
}

fun View.pressAnimation(scaleLevel: Float = 0.9f, onEnd: () -> Unit = {}) {
    val scaleDownX = ObjectAnimator.ofFloat(this, "scaleX", 1f, scaleLevel)
    val scaleDownY = ObjectAnimator.ofFloat(this, "scaleY", 1f, scaleLevel)

    val scaleUpX = ObjectAnimator.ofFloat(this, "scaleX", scaleLevel, 1f)
    val scaleUpY = ObjectAnimator.ofFloat(this, "scaleY", scaleLevel, 1f)

    scaleDownX.duration = 150
    scaleDownY.duration = 150
    scaleUpX.duration = 300
    scaleUpY.duration = 300

    scaleDownX.interpolator = DecelerateInterpolator()
    scaleDownY.interpolator = DecelerateInterpolator()
    scaleUpX.interpolator = AccelerateDecelerateInterpolator()
    scaleUpY.interpolator = AccelerateDecelerateInterpolator()

    val animatorSet = AnimatorSet()
    animatorSet.play(scaleDownX).with(scaleDownY)
    animatorSet.play(scaleUpX).with(scaleUpY).after(scaleDownX)
    animatorSet.addListener(onEnd = { onEnd.invoke() })
    animatorSet.start()
}

fun View.fadeAndScaleUpDynamic(duration: Long = 300, onEnd: () -> Unit = {}) {
    val scaleUpX = ObjectAnimator.ofFloat(this, "scaleX", scaleX, 1f)
    val scaleUpY = ObjectAnimator.ofFloat(this, "scaleY", scaleY, 1f)

    val fadeIn = ObjectAnimator.ofFloat(this, "alpha", alpha, 1f)

    val animatorSet = AnimatorSet()
    animatorSet.playTogether(scaleUpX, scaleUpY, fadeIn)
    animatorSet.duration = duration
    animatorSet.interpolator = FastOutSlowInInterpolator()
    animatorSet.addListener(onEnd = { onEnd.invoke() })
    animatorSet.start()
}

fun View.fadeAndScaleDownDynamic(duration: Long = 300, onEnd: () -> Unit = {}) {
    val scaleUpX = ObjectAnimator.ofFloat(this, "scaleX", scaleX, 0f)
    val scaleUpY = ObjectAnimator.ofFloat(this, "scaleY", scaleY, 0f)

    val fadeIn = ObjectAnimator.ofFloat(this, "alpha", alpha, 0f)

    val animatorSet = AnimatorSet()
    animatorSet.playTogether(scaleUpX, scaleUpY, fadeIn)
    animatorSet.duration = duration
    animatorSet.interpolator = DecelerateInterpolator()
    animatorSet.addListener(onEnd = { onEnd.invoke() })
    animatorSet.start()
}

fun View.animateHeartbeat(duration: Long = 600) {
    // Define the scale-up animation
    val scaleXUp = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 1.5f)
    val scaleYUp = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 1.5f)

    // Define the scale-down animation
    val scaleXDown = ObjectAnimator.ofFloat(this, "scaleX", 1.5f, 1.0f)
    val scaleYDown = ObjectAnimator.ofFloat(this, "scaleY", 1.5f, 1.0f)

    // Create an AnimatorSet to play animations sequentially
    val scaleUp = AnimatorSet()
    scaleUp.playTogether(scaleXUp, scaleYUp)
    scaleUp.duration = duration / 2

    val scaleDown = AnimatorSet()
    scaleDown.playTogether(scaleXDown, scaleYDown)
    scaleDown.duration = duration / 2

    // Combine the scale-up and scale-down animations
    val heartbeatAnimation = AnimatorSet()
    heartbeatAnimation.playSequentially(scaleUp, scaleDown)
    heartbeatAnimation.interpolator = FastOutSlowInInterpolator()
    heartbeatAnimation.start()
}
