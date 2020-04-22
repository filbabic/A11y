package com.filip.babic.a11y.report.model

import kotlinx.serialization.Serializable

/**
 * Describes the report for one view, containing all its items.
 */

@Serializable
data class ViewReport(
  val viewId: String,
  val viewType: String,
  val viewHierarchy: String,
  val viewReportItems: List<ViewReportItem>
)