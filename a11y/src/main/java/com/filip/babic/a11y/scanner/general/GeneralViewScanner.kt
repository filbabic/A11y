package com.filip.babic.a11y.scanner.general

import android.view.View
import com.filip.babic.a11y.report.model.ViewReportItem
import com.filip.babic.a11y.scanner.base.ViewScanner

/**
 * Scans generic [View] items, to  produce [ViewItemReport]s about them.
 */
class GeneralViewScanner : ViewScanner() {

  override fun <T : View> getViewReportItems(view: T): List<ViewReportItem> {
    return emptyList() // TODO
  }

  override fun canScan(view: View): Boolean = true
}