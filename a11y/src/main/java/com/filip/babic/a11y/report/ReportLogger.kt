package com.filip.babic.a11y.report

import com.filip.babic.a11y.report.model.Report
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Logs the serialized report into a text file, which is then saved locally.
 */
internal class ReportLogger(private val rootFileDirectory: File) {

  companion object {
    private const val ID_CONTENT = "android:id/content"
    private const val NO_ROOT_REPORT = "Could not find Root Report"
  }

  fun logReport(report: Report): Boolean {
    val rootReport = fetchRootLayoutReport(report)

    if (!rootReport.hasIssues()) {
      return false
    }

    val stringBuilder = StringBuilder()

    var currentReport = rootReport
    var viewLevel = 0

    while (currentReport.hasNextLevel() || currentReport.viewReports.isNotEmpty()) {
      stringBuilder.appendln("View layer - $viewLevel ${if (viewLevel == 0) "(root)" else ""}")
      stringBuilder.appendln("Parent ID - ${currentReport.parentId}")
      stringBuilder.appendln("Parent Type - ${currentReport.parentType}")

      if (currentReport.viewReports.isNotEmpty()) {
        stringBuilder.appendln("-- View Reports:")

        currentReport.viewReports.forEach { viewReport ->
          stringBuilder.appendln("\t------------------------")
          stringBuilder.appendln("\tView ID - ${viewReport.viewId}")
          stringBuilder.appendln("\tView Type - ${viewReport.viewType}")
          stringBuilder.appendln()
          stringBuilder.appendln("\tParent ID - ${viewReport.parentId}")
          stringBuilder.appendln("\tParent Type - ${viewReport.parentType}")
          stringBuilder.appendln()
          stringBuilder.appendln("\tIssues:")

          viewReport.viewReportItems.forEach { reportItem ->
            stringBuilder.appendln("\t\t Issue Type - ${reportItem.issueType}")
            stringBuilder.appendln("\t\t Issue Description - ${reportItem.issue}")
            stringBuilder.appendln("\t\t Suggestion - ${reportItem.fixSuggestion}")

            stringBuilder.appendln()
          }
        }
      }

      stringBuilder.appendln()

      viewLevel++
      currentReport = currentReport.nextLevelReport ?: break
    }

    val fileId = UUID.randomUUID().toString()

    val outputFile = File(rootFileDirectory, "$fileId.txt")
    val outputStream = FileOutputStream(outputFile)

    outputStream.use {
      val output = stringBuilder.toString()
      it.write(output.toByteArray())
    }

    return true
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

    while (currentReport.hasNextLevel()) {
      if (currentReport.parentId != ID_CONTENT) {
        currentReport = currentReport.nextLevelReport
          ?: throw IllegalStateException(NO_ROOT_REPORT)
      } else {
        return currentReport.nextLevelReport
          ?: throw IllegalStateException(NO_ROOT_REPORT)
      }
    }

    return report
  }
}