package com.filip.babic.a11y.scanner.general

import android.view.View
import com.filip.babic.a11y.report.model.ViewReportItem
import com.filip.babic.a11y.scanner.base.ViewScanner
import com.filip.babic.a11y.utils.hasBigEnoughTouchArea

/**
 * Scans generic [View] items, to  produce [ViewItemReport]s about them.
 */
internal class GeneralViewScanner : ViewScanner() {

  // TODO: Add guidelines on using `View.Gone` instead of `0dp` when removing components from UI
  override fun <T : View> getViewReportItems(view: T): List<ViewReportItem> {
    return if (!hasBigEnoughTouchArea(view)) {
      listOf(
        ViewReportItem(
          "View Hard To Touch",
          "The view area is not large enough, to touch or click. It can be hard for users to activate the view click listener.",
          "Make sure that your views are at least 48dp large. Consider adding up to 16dp of padding on each side for the touch area, or increasing your size to be at least 48dp in width and height."
        )
      )
    } else {
      emptyList()
    }
  }

  override fun canScan(view: View): Boolean = true
}