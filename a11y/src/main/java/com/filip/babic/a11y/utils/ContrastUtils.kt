package com.filip.babic.a11y.utils

import android.graphics.Typeface
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

/**
 * Helps with calculating the contrast of two colors.
 * Used for texts and their backgrounds.
 */

/**
 * @param textView - The [TextView] for which the contrast is being checked.
 * @param backgroundColor - the color of the background for the [TextView].
 *
 * Color contrast between two colors. By WCAG 2.0 (https://www.w3.org/TR/WCAG20/),
 * the minimum contrast standards for text are defined in a few different scenarios.
 *
 * @return If the contrast is strong enough, depending on if the text is large or small.
 * */

private const val NORMAL_TEXT_TARGET_SIZE = 14f
private const val LARGE_TEXT_TARGET_SIZE = 18f

private const val LARGE_TEXT_CONTRAST = 3.0
private const val NORMAL_TEXT_CONTRAST = 4.5

private const val MINIMAL_TEXT_SIZE = 12f

internal fun hasMinimalTextSize(textView: TextView): Boolean =
  isLargeEnoughText(textView, MINIMAL_TEXT_SIZE)

internal fun isTextContrastCorrect(
  textView: TextView,
  @ColorInt backgroundColor: Int
): Boolean {
  val textContrast = getContrast(textView.currentTextColor, backgroundColor)
  val textStyle = textView.typeface.style

  val isLargeText =
    (isBoldText(textStyle) && isLargeEnoughText(textView, NORMAL_TEXT_TARGET_SIZE)) ||
        (!isBoldText(textStyle) && isLargeEnoughText(textView, LARGE_TEXT_TARGET_SIZE))

  return if (isLargeText) textContrast > LARGE_TEXT_CONTRAST else textContrast > NORMAL_TEXT_CONTRAST
}

private fun isBoldText(textStyle: Int): Boolean {
  return textStyle == Typeface.BOLD || textStyle == Typeface.BOLD_ITALIC
}

private fun isLargeEnoughText(textView: TextView, targetSize: Float): Boolean {
  return (textView.textSize / textView.paint.density) > targetSize
}

private fun getContrast(
  @ColorInt firstColor: Int,
  @ColorInt secondColor: Int
): Double {
  val firstColorLuminance = ColorUtils.calculateLuminance(firstColor)
  val secondColorLuminance = ColorUtils.calculateLuminance(secondColor)

  return if (firstColorLuminance > secondColorLuminance) {
    (firstColorLuminance + 0.05) / (secondColorLuminance + 0.05)
  } else {
    (secondColorLuminance + 0.05) / (firstColorLuminance + 0.05)
  }
}
