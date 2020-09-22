package com.filip.babic.a11y.scanner.text

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import com.filip.babic.a11y.report.model.ViewReportItem
import com.filip.babic.a11y.scanner.base.ViewScanner
import com.filip.babic.a11y.utils.hasMinimalTextSize
import com.filip.babic.a11y.utils.isTextContrastCorrect

/**
 * Scans [TextView]s to produce [ViewReportItem]s for them.
 */
internal class TextViewScanner : ViewScanner {

  override fun <T : View> getViewReportItems(view: T): List<ViewReportItem> {
    val textView = view as TextView
    val viewBackground = textView.background as? ColorDrawable

    // TODO figure out how to extrapolate the color of Buttons/RippleDrawable

    val hasGoodContrast = viewBackground?.let { isTextContrastCorrect(textView, it.color) } ?: true
    val hasMinimalTextSize = hasMinimalTextSize(textView)

    return getTextViewReport(hasGoodContrast, hasMinimalTextSize)
  }

  // TODO add extra description as to what is contrast and how to measure it, compare it...
  private fun getTextViewReport(
    hasGoodContrast: Boolean,
    hasMinimalTextSize: Boolean
  ): List<ViewReportItem> {
    val reports = mutableListOf<ViewReportItem>()

    if (!hasGoodContrast) {
      val contrastReport = ViewReportItem(
        "Contrast",
        "This view has very low contrast ratio between the background and the text.",
        "Try to increase the contrast using different colors, following the WCAG 2.0 (https://www.w3.org/TR/WCAG20/) rules."
      )

      reports.add(contrastReport)
    }

    if (!hasMinimalTextSize) {
      val textSizeReport = ViewReportItem(
        "Text Size",
        "This text view's text size is lower than the minimal (12sp).",
        "Your TextViews should have at least 12sp large text size. Make sure you don't use a text that's smaller than that."
      )

      reports.add(textSizeReport)
    }

    return reports
  }

  override fun canScan(view: View): Boolean = view is TextView
}
