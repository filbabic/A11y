package com.filip.babic.a11y.scanner.text

import android.view.View
import android.widget.TextView
import com.filip.babic.a11y.report.model.ViewReportItem
import com.filip.babic.a11y.scanner.base.ViewScanner
import com.filip.babic.a11y.scanner.general.GeneralViewScanner

/**
 * Scans [TextView]s to produce [ViewReportItem]s for them.
 */
class TextViewScanner(private val generalViewScanner: GeneralViewScanner) :
  ViewScanner() {

  override fun <T : View> getViewReportItems(view: T): List<ViewReportItem> {
    val imageView = view as TextView

    return generalViewScanner.getViewReportItems(view)
    // TODO add ImageView reports to the general ones
  }

  override fun canScan(view: View): Boolean = view is TextView
}
