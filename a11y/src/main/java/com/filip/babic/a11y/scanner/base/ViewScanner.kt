package com.filip.babic.a11y.scanner.base

import android.view.View
import com.filip.babic.a11y.report.model.ViewReportItem

/**
 * Basic abstract View scanner.
 */
abstract class ViewScanner {

  abstract fun <T : View> getViewReportItems(view: T): List<ViewReportItem>

  abstract fun canScan(view: View): Boolean
}