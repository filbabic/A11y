package com.filip.babic.a11y.scanner.base

import android.view.View
import com.filip.babic.a11y.report.model.ViewReportItem

/**
 * Common interface for View scanners.
 */
internal interface ViewScanner {

  fun <T : View> getViewReportItems(view: T): List<ViewReportItem>

  fun canScan(view: View): Boolean

}
