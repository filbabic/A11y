package com.filip.babic.a11y.report

import android.util.Log
import com.filip.babic.a11y.report.model.Report
import java.lang.IllegalStateException
import java.lang.StringBuilder

/**
 * Logs the serialized report into a text file, which is then saved locally.
 */
internal class ReportLogger {

  // TODO implement a nice way to format the text.
  fun logReport(report: Report) {
    val rootReport = fetchRootLayoutReport(report)
    val stringBuilder = StringBuilder()

    var currentReport = rootReport

//    while (currentReport.isNotEmpty()) {
//
//
//    }

    Log.d("report", report.toString())
  }

  /**
   * In order to filter out the views and reports which are from the Android side (e.g. decor view)
   * we need to find the "android:id/content" ID, and then fetch its only child.
   *
   * Presumably the Content view has only one child, and that is the app view.
   *
   * TODO 5th of July, 2020: Test if this works with multi-window mode, and Foldable devices.
   * */
  private fun fetchRootLayoutReport(report: Report): Report {
    var currentReport = report

    while (currentReport.isNotEmpty()) {
      if (currentReport.parentId != "android:id/content") {
        currentReport = currentReport.childLayerReports?.first()
          ?: throw IllegalStateException("Couldn't find Root report")
      } else {
        return currentReport.childLayerReports?.first()
          ?: throw IllegalStateException("Couldn't find Root report")
      }
    }

    return report
  }

  // TODO see if we can flatten it in the Report-generating area
  private fun flattenLayerReports(report: Report): Report {
    if (report.childLayerReports != null && report.childLayerReports.isNotEmpty()) {

    }

    return report
  }
}