package com.filip.babic.a11y.scanner.image

import android.view.View
import android.widget.ImageView
import com.filip.babic.a11y.report.model.ViewReportItem
import com.filip.babic.a11y.scanner.base.ViewScanner
import com.filip.babic.a11y.scanner.general.GeneralViewScanner

/**
 * Scans [ImageView] types, and produces its [ViewReportItem]s.
 */
class ImageViewScanner(private val generalViewScanner: GeneralViewScanner) :
  ViewScanner() {

  override fun <T : View> getViewReportItems(view: T): List<ViewReportItem> {
    val imageView = view as ImageView

    return generalViewScanner.getViewReportItems(view)
    // TODO add ImageView reports to the general ones
  }

  override fun canScan(view: View): Boolean = view is ImageView
}