package com.filip.babic.a11y.scanner.image

import android.view.View
import android.widget.ImageView
import com.filip.babic.a11y.report.model.ViewReportItem
import com.filip.babic.a11y.scanner.base.ViewScanner
import com.filip.babic.a11y.utils.isContentDescriptionValid
import com.filip.babic.a11y.utils.shouldNotHaveDescription

/**
 * Scans [ImageView] types, and produces its [ViewReportItem]s.
 */
internal class ImageViewScanner : ViewScanner() {

  override fun <T : View> getViewReportItems(view: T): List<ViewReportItem> {
    val imageView = view as ImageView
    val isValidContentDescription = isContentDescriptionValid(imageView)
    val shouldNotHaveDescription = shouldNotHaveDescription(imageView)

    return buildImageViewReports(
      isValidContentDescription,
      shouldNotHaveDescription
    )
  }

  private fun buildImageViewReports(
    validContentDescription: Boolean,
    shouldNotHaveDescription: Boolean
  ): List<ViewReportItem> {
    val reportItems = mutableListOf<ViewReportItem>()

    if (!validContentDescription) {
      reportItems.add(
        ViewReportItem(
          "Content Description",
          "This view does not have a content description, or has an invalid description.",
          "Content descriptions should not be empty. They should be concise and not contain text such as \"image\" or \"photo\"."
        )
      )
    }

    if (shouldNotHaveDescription) {
      reportItems.add(
        ViewReportItem(
          "Content Description In List Items",
          "Images in lists should not have content description text. This leads to repetitive and redundant information.",
          "Avoid using content description in list items."
        )
      )
    }

    return reportItems
  }

  override fun canScan(view: View): Boolean = view is ImageView
}