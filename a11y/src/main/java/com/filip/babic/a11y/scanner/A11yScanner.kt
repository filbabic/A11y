package com.filip.babic.a11y.scanner

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.filip.babic.a11y.report.model.Report
import com.filip.babic.a11y.report.model.ViewReport
import com.filip.babic.a11y.report.model.ViewReportItem
import com.filip.babic.a11y.scanner.base.ViewScanner

/**
 * Operates the scanning process, returning a list of issues found within the View hierarchy.
 */
class A11yScanner(private val scanners: List<ViewScanner>) {

  /**
   * We presume this is the top-level (parent) View, as such, we don't need to look up its parent.
   *
   * Then we split the children to ViewGroups, and simple Views.
   *
   * The ViewGroups represent the [nestedLayers], other Containers, with more Views.
   * The Views represent [simpleViews], things like TextViews and ImageViews.
   *
   * then we generate a [Report], recursively
   * */
  fun scanView(viewGroup: ViewGroup): Report {
    val children = viewGroup.children

    val nestedLayers = children.mapNotNull { it as? ViewGroup }
    val simpleViews = children.filter { it !is ViewGroup }

    return if (children.none { it is ViewGroup }) {
      Report(viewReports = getChildViewReportItems(children.toList()))
    } else {
      Report(
        viewReports = getChildViewReportItems(simpleViews.toList()),
        childLayerReports = nestedLayers.map { scanView(it) }.toList()
      )
    }
  }

  private fun getChildViewReportItems(views: List<View>): List<ViewReport> {
    return views.map { currentView ->
      val (viewId, viewType) = getViewBasics(currentView)

      ViewReport(viewId, viewType, getViewReportItems(currentView))
    }
  }

  private fun getViewReportItems(currentView: View): List<ViewReportItem> {
    val scannersForView = scanners.filter { it.canScan(currentView) }

    return scannersForView.flatMap { it.getViewReportItems(currentView) }.distinct()
  }

  private fun getViewBasics(currentView: View): Pair<String, String> {
    val viewIdInt = currentView.id
    val viewIdentifier =
      if (viewIdInt == View.NO_ID) "no-id" else currentView.resources.getResourceName(viewIdInt)

    val viewType = currentView.javaClass.name

    return Pair(viewIdentifier, viewType)
  }
}