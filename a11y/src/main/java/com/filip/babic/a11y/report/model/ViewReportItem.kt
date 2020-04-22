package com.filip.babic.a11y.report.model

import kotlinx.serialization.Serializable

/**
 * Describes one issue with the layout's A11y attributes.
 */

@Serializable
data class ViewReportItem(
  val issueType: String,
  val issue: String,
  val fixSuggestion: String
)