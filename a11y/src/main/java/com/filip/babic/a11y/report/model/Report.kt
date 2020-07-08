package com.filip.babic.a11y.report.model

import kotlinx.serialization.Serializable

/**
 * Describes the entire report for a View tree.
 *
 * @property viewReports - The reports for all the views in the current layout.
 * E.g. Parent View is a [LinearLayout], with three [TextView]s, and one [FrameLayout].
 * It will hold the [ViewReport]s for the three [TextView]s, and one report for the [FrameLayout].
 *
 * @property childLayerReports - The reports for each child ViewGroup. E.g. the [FrameLayout], in the
 * example above.
 *
 * @property nextLevelReport - The report for the next level of hierarchy. This is used to log data
 * more easily, as we can have a linear structure of layers, instead of a tree-structure.
 */

@Serializable
internal data class Report(
  val parentId: String,
  val parentType: String,
  val viewReports: List<ViewReport> = emptyList(),
  val childLayerReports: List<Report>? = null,
  val nextLevelReport: Report? = null
) {

  /**
   * As long as there is either a nested report for child layers, or there is a report for one of
   * the views in the current layout, we find this [Report] as _not empty_.
   * */
  fun isNotEmpty(): Boolean =
    (childLayerReports != null && childLayerReports.isNotEmpty())
        || viewReports.isNotEmpty()

  /**
   * An easy way to check if there is a next level for this View hierarchy.
   * */
  fun hasNextLevel(): Boolean = nextLevelReport != null

  fun hasIssues(): Boolean {
    var report = this

    while (report.hasNextLevel()) {
      if (report.viewReports.isNotEmpty()) {
        return true
      }

      report = report.nextLevelReport ?: return false
    }

    return report.viewReports.isNotEmpty()
  }
}