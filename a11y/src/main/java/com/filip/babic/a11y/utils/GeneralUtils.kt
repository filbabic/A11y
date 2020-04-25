package com.filip.babic.a11y.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import kotlin.math.roundToInt


/**
 * Provides functions for general View checkups, like padding.
 */
private const val MINIMAL_EXTRA_PADDING = 16

fun hasBigEnoughTouchArea(view: View): Boolean {
  val hasListeners = view.hasOnClickListeners()

  return if (!hasListeners) {
    true
  } else {
    val context = view.context

    val rightDp = convertPixelsToDp(view.paddingRight.toFloat(), context)
    val leftDp = convertPixelsToDp(view.paddingRight.toFloat(), context)
    val topDp = convertPixelsToDp(view.paddingRight.toFloat(), context)
    val botDp = convertPixelsToDp(view.paddingRight.toFloat(), context)

    rightDp >= MINIMAL_EXTRA_PADDING && leftDp >= MINIMAL_EXTRA_PADDING
        && topDp >= MINIMAL_EXTRA_PADDING && botDp >= MINIMAL_EXTRA_PADDING
  }
}

fun convertPixelsToDp(pixels: Float, context: Context): Int {
  return (pixels / (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}