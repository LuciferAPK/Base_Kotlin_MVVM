package com.cyrus.base_kotlin_mvvm.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import android.widget.TextView
import com.cyrus.base_kotlin_mvvm.App
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

object AppConfigUtils {

	lateinit var displayMetrics: DisplayMetrics

	val widthScreen: Int
		get() = displayMetrics.widthPixels

	val heightScreen: Int
		get() = displayMetrics.heightPixels
	var ASPECT_RATIO_INDEX = 0
	val DATA_RATIO_SCREEN = arrayOf("", "/02_18x9", "/04_19x9", "/06_195x9", "/07_20x9")
	var aspectRatio: Float = 1.78f
	val isTablet = false

/*	fun setup(context: Context) {
		displayMetrics = getScreen(context)
		aspectRatio = if (isTablet) 1f else (heightScreen.toFloat() / widthScreen).round(2)
		ASPECT_RATIO_INDEX = getAspectRatioIndex(aspectRatio)
	}*/

	private fun getAspectRatioIndex(ratio: Float): Int {
		return when {
			ratio <= 1.89f -> 0
			ratio > 1.89f && ratio <= 2.055f -> 1
			ratio > 2.055f && ratio <= 2.14f -> 2
			ratio > 2.14 && ratio <= 2.195f -> 3
			else -> 4
		}
	}

	private fun getScreen(context: Context): DisplayMetrics {
		val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
		val dm = DisplayMetrics()
		windowManager.defaultDisplay.getRealMetrics(dm)
		return dm
	}

	fun setColorText(textView: TextView, colorCode: String) {
		val width = textView.paint.measureText(textView.text.toString())
		val textShader = LinearGradient(
			width, 0f, textView.textSize, 0f,
			intArrayOf(
				Color.parseColor(colorCode),
				Color.parseColor(colorCode),
				Color.parseColor(colorCode)
			),
			null, Shader.TileMode.CLAMP
		)
		textView.paint.shader = textShader
	}

	fun getStringFromAssets(name: String): String? {
		return try {
			App.instance.run { assets.open(name).readTextAndClose() }
		} catch (e: IOException) {
			null
		}
	}

	fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String? {
		return bufferedReader(charset).use { it.readText() }
	}

	fun dpToPx(dp: Float): Int =
		TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics).toInt()

	fun dp2Px(dp: Float): Float =
		TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)

	fun pxToDp(px: Int): Float =
		px / displayMetrics.density

	fun spToPx(sp: Float): Float =
		TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, displayMetrics)

	fun getNavigationBarHeight(context: Context): Int {
		val resources: Resources = context.resources
		val resourceId: Int =
			resources.getIdentifier("navigation_bar_height", "dimen", "android")
		return if (resourceId > 0) {
			resources.getDimensionPixelSize(resourceId)
		} else 0
	}
}