package com.filip.babic.a11y.utils

import android.util.TypedValue
import android.view.View

/**
 * Provides functions for general View checkups, like touch area. Touch area of views needs to be
 * at least [MINIMAL_TOUCH_AREA_SIZE] dp in size. This means each View needs <i>at least</i> that
 * big width and height, with padding, after density conversions.
 *
 * It does not matter if the view has 250dp height, if the width is still only 20 dp, as the View
 * will still be hard to perform touch/click actions on.
 */
private const val MINIMAL_TOUCH_AREA_SIZE = 48f

internal fun hasBigEnoughTouchArea(view: View): Boolean {
  val hasListeners = view.hasOnClickListeners()

  return if (!hasListeners) {
    true
  } else {
    val viewHeight = view.measuredHeight
    val viewWidth = view.measuredWidth
    val viewPaddingHorizontal = view.paddingStart + view.paddingEnd
    val viewPaddingVertical = view.paddingTop + view.paddingBottom

    val viewTotalHeight = viewHeight + viewPaddingVertical
    val viewTotalWidth = viewWidth + viewPaddingHorizontal

    val minimalSizePixels = TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP,
      MINIMAL_TOUCH_AREA_SIZE,
      view.resources.displayMetrics
    ).toInt()

    return viewTotalHeight >= minimalSizePixels && viewTotalWidth >= minimalSizePixels
  }
}