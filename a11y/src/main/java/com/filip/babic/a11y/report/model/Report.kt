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
 */

@Serializable
internal data class Report(
  val parentId: String,
  val parentType: String,
  val viewReports: List<ViewReport> = emptyList(),
  val childLayerReports: List<Report>? = null
)