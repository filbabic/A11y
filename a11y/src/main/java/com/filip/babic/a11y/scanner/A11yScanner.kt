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
internal class A11yScanner(private val scanners: List<ViewScanner>) {

  /**
   * This function flattens the [Report] Tree, into a Linear, layered report, which is easier to
   * traverse when logging the said report.
   *
   * ---
   * Explanation: If/else checks.
   *
   * If statement checks if there are nested layouts or viewGroups. These layers then have to be
   * flattened and cleaned up.
   *
   * Once we reach the end of layering, the last report is just returned.
   *
   * ---
   *
   * Explanation: If logic.
   *
   * 1. We first have to find all the [ViewReport]s of the next hierarchy layer. These are used to
   * represent all the reports in the same layer.
   *
   * 2. The [nextLevelGroupReports] are used to find the future layers. If there are no child layers
   * we take in the [nestedReports] parameter instead, and flatten its children. This way we prepare
   * for the next layer, as well.
   *
   * 3. We then find the [parentId] & [parentType] properties for better navigation through data.
   *
   * 4. Before traversing deeper into the structure, we flatten the current layer, and build its
   * report for logging.
   *
   * 5.
   *
   * @return [Report] - Flattened report similar to a Linked-list structure, for easy traversal.
   * */
  internal fun flattenReport(report: Report, nestedReports: List<Report> = emptyList()): Report {
    return if (nestedReports.isNotEmpty() || (report.childLayerReports != null && report.childLayerReports.isNotEmpty())) {

      /** 1. Finding & flattening next level ViewReports. */
      val nextLevelViewReports = if (report.childLayerReports == null) {
        nestedReports.flatMap { it.viewReports }
      } else {
        report.childLayerReports.flatMap { it.viewReports }
      }

      /** 2. Finding & flattening next level reports. */
      val nextLevelGroupReports = if (nestedReports.isNotEmpty()) {
        nestedReports.flatMap { it.childLayerReports ?: emptyList() }
      } else {
        report.childLayerReports?.flatMap { it.childLayerReports ?: emptyList() } ?: emptyList()
      }

      /** 3. Determining parentId and parentType properties for easier navigation. */
      val parentId = when {
        nestedReports.isEmpty() || nestedReports.size > 1 -> "N/A"
        else -> nestedReports.first().parentId
      }
      val parentType = when {
        nestedReports.isEmpty() || nestedReports.size > 1 -> "N/A"
        else -> nestedReports.first().parentType
      }

      /** 4. Flatten the current layer, and build its report. */
      val currentFlattenReport = Report(
        parentId = parentId,
        parentType = parentType,
        viewReports = nextLevelViewReports
      )

      /** 5. If there are remaining layers, we recursively jump through them, otherwise we return
       * the current flattened layer. This will make sure we get all branches & nodes reported. */
      if (nextLevelGroupReports.isEmpty()) {
        currentFlattenReport
      } else {
        currentFlattenReport.copy(
          nextLevelReport = flattenReport(
            currentFlattenReport,
            nextLevelGroupReports
          )
        )
      }
    } else {
      report
    }
  }

  /**
   * We presume this is the top-level (parent) View, as such, we don't need to look up its parent.
   *
   * Then we split the children to ViewGroups, and simple Views.
   *
   * The ViewGroups represent the [nestedLayers], other Containers, with more Views.
   * The Views represent [simpleViews], things like TextViews and ImageViews.
   *
   * then we generate a [Report], recursively.
   *
   * @return [Report] - Tree-like Report, where each node is a list of layer reports, and each leaf
   * is a [ViewReport].
   * */
  internal fun scanView(viewGroup: ViewGroup): Report {
    val children = viewGroup.children.toList()

    val nestedLayers = children.mapNotNull { it as? ViewGroup }
    val simpleViews = children.filter { it !is ViewGroup }

    val (viewId, viewType) = getViewBasics(viewGroup)

    val hasNestedLayouts = nestedLayers.isNotEmpty()

    return Report(
      parentId = viewId,
      parentType = viewType,
      viewReports = getChildViewReportItems(
        viewId,
        viewType,
        simpleViews
      ),
      childLayerReports = if (hasNestedLayouts) {
        nestedLayers
          .map { scanView(it) }
          .toList()
          .filter(Report::isNotEmpty)
      } else null
    )
  }

  private fun getChildViewReportItems(
    parentId: String,
    parentName: String,
    views: List<View>
  ): List<ViewReport> {
    return views.map { currentView ->
      val (viewId, viewType) = getViewBasics(currentView)

      ViewReport(parentId, parentName, viewId, viewType, getViewReportItems(currentView))
    }.filter { it.viewReportItems.isNotEmpty() }
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