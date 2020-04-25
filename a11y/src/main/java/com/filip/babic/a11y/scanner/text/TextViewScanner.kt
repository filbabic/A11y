package com.filip.babic.a11y.scanner.text

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import com.filip.babic.a11y.report.model.ViewReportItem
import com.filip.babic.a11y.scanner.base.ViewScanner
import com.filip.babic.a11y.utils.isTextContrastCorrect

/**
 * Scans [TextView]s to produce [ViewReportItem]s for them.
 */
class TextViewScanner : ViewScanner() {

  override fun <T : View> getViewReportItems(view: T): List<ViewReportItem> {
    val textView = view as TextView
    val viewBackground = textView.background as? ColorDrawable

    return if (viewBackground != null) {
      val hasGoodContrast = isTextContrastCorrect(textView, viewBackground.color)

      getTextViewReport(hasGoodContrast)
    } else {
      emptyList()
    }
  }

  private fun getTextViewReport(hasGoodContrast: Boolean): List<ViewReportItem> {
    val reports = mutableListOf<ViewReportItem>()

    if (!hasGoodContrast) {
      val contrastReport = ViewReportItem(
        "Contrast",
        "This view has very low contrast ratio between the background and the text.",
        "Try to increase the contrast using different colors, following the WCAG 2.0 (https://www.w3.org/TR/WCAG20/) rules."
      )

      reports.add(contrastReport)
    }

    return reports
  }

  override fun canScan(view: View): Boolean = view is TextView
}
