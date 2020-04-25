package com.filip.babic.a11y.scanner.general

import android.view.View
import com.filip.babic.a11y.report.model.ViewReportItem
import com.filip.babic.a11y.scanner.base.ViewScanner
import com.filip.babic.a11y.utils.hasBigEnoughTouchArea

/**
 * Scans generic [View] items, to  produce [ViewItemReport]s about them.
 */
class GeneralViewScanner : ViewScanner() {

  override fun <T : View> getViewReportItems(view: T): List<ViewReportItem> {
    return if (!hasBigEnoughTouchArea(view)) {
      listOf(ViewReportItem(
        "View Hard To Touch",
        "The view area is not large enough, to touch or click. It can be hard for users to activate the view click listener.",
        "Make sure that your views have at least 16dp padding on each side, for the touch area."
      ))
    } else {
       emptyList()
    }
  }

  override fun canScan(view: View): Boolean = true
}