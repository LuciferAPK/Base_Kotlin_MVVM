package com.cyrus.base_kotlin_mvvm.ext

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cyrus.base_kotlin_mvvm.utils.AppConfigUtils.dpToPx

var View.isHidden: Boolean
    get() {
        return visibility == View.GONE
    }
    set(value) {
        if (value) {
            if (visibility == View.VISIBLE) {
                visibility = View.GONE
            }
        } else {
            if (visibility != View.VISIBLE) {
                visibility = View.VISIBLE
            }
        }
    }

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.visibleOrGone(visible: Boolean) {
    if (visible) {
        visible()
    } else {
        gone()
    }
}

fun View.visibleOrInvisible(visible: Boolean) {
    if (visible) {
        visible()
    } else {
        invisible()
    }
}

fun View.margin(
    left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null
) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.apply { leftMargin = dpToPx(this) }
        top?.apply { topMargin = dpToPx(this) }
        right?.apply { rightMargin = dpToPx(this) }
        bottom?.apply { bottomMargin = dpToPx(this) }
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun Drawable.tint(color: Int, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN) {
    mutate().setColorFilter(color, mode)
}

var TextView.drawableStart: Drawable?
    get() = drawables[0]
    set(value) = setDrawables(value, drawableTop, drawableEnd, drawableBottom)

var TextView.drawableTop: Drawable?
    get() = drawables[1]
    set(value) = setDrawables(drawableStart, value, drawableEnd, drawableBottom)

var TextView.drawableEnd: Drawable?
    get() = drawables[2]
    set(value) = setDrawables(drawableStart, drawableTop, value, drawableBottom)

var TextView.drawableBottom: Drawable?
    get() = drawables[3]
    set(value) = setDrawables(drawableStart, drawableTop, drawableEnd, value)

@Deprecated(
    "Consider replace with drawableStart to better support right-to-left Layout",
    ReplaceWith("drawableStart")
)
var TextView.drawableLeft: Drawable?
    get() = compoundDrawables[0]
    set(value) = setCompoundDrawablesWithIntrinsicBounds(
        value,
        drawableTop,
        drawableRight,
        drawableBottom
    )

@Deprecated(
    "Consider replace with drawableEnd to better support right-to-left Layout",
    ReplaceWith("drawableEnd")
)
var TextView.drawableRight: Drawable?
    get() = compoundDrawables[2]
    set(value) = setCompoundDrawablesWithIntrinsicBounds(
        drawableLeft,
        drawableTop,
        value,
        drawableBottom
    )

private val TextView.drawables: Array<Drawable?>
    get() = if (Build.VERSION.SDK_INT >= 17) compoundDrawablesRelative else compoundDrawables

private fun TextView.setDrawables(
    start: Drawable?,
    top: Drawable?,
    end: Drawable?,
    buttom: Drawable?
) {
    if (Build.VERSION.SDK_INT >= 17)
        setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, buttom)
    else
        setCompoundDrawablesWithIntrinsicBounds(start, top, end, buttom)
}