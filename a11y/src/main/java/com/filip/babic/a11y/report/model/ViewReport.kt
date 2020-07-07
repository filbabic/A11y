package com.filip.babic.a11y.report.model

import kotlinx.serialization.Serializable

/**
 * Describes the report for one view, containing all its items.
 */

@Serializable
internal data class ViewReport(
  val parentId: String,
  val parentType: String,
  val viewId: String,
  val viewType: String,
  val viewReportItems: List<ViewReportItem>
)

