package com.filip.babic.a11y.scanner.text

import android.view.View
import android.widget.EditText
import com.filip.babic.a11y.report.model.ViewReportItem
import com.filip.babic.a11y.scanner.base.ViewScanner
import com.filip.babic.a11y.utils.isAutofillHintValid
import com.filip.babic.a11y.utils.isHintValid

internal class EditTextScanner : ViewScanner() {

  override fun canScan(view: View): Boolean = view is EditText

  override fun <T : View> getViewReportItems(view: T): List<ViewReportItem> {
    val editText = view as EditText
    val reports = mutableListOf<ViewReportItem>()

    val isValidHint = isHintValid(editText)
    val isValidAutofillHint = isAutofillHintValid(editText)

    if (!isValidHint) {
      reports.add(
        ViewReportItem(
          "EditText hint missing",
          "The hint for this input is null, or is blank.",
          "Make sure every EditText you use has a descriptive hint, to help visually impaired people understand what they need to input."
        )
      )
    }

    if (!isValidAutofillHint) {
      reports.add(
        ViewReportItem(
          "EditText auto-fill hints missing",
          "Input fields which can be auto-filled require a hint for the auto-fill service.",
          "If you have input fields with the password, email, phone number, name, and similar form data, you should always put in the auto-fill hints in the XML."
        )
      )
    }

    return reports
  }
}