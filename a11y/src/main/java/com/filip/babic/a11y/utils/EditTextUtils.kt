package com.filip.babic.a11y.utils

import android.os.Build
import android.text.InputType
import android.widget.EditText

/**
 * Helps determine whether or not [EditText] views are accessible.
 *
 * Each EditText should have a hint, and if the input is a password, name, or similar form items,
 * then it should also have an auto-fill hint.
 * */

val possibleFormInput =
  arrayOf("username", "password", "email", "e-mail", "name", "user", "nickname", "address")

internal fun isHintValid(editText: EditText): Boolean {
  return !editText.hint.isNullOrBlank()
}

internal fun isAutofillHintValid(editText: EditText): Boolean {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val inputType = editText.inputType
    val autofillHints = editText.autofillHints
    val hint = editText.hint?.toString() ?: ""

    val doesNotNeedHints = !hintContainsFormInput(hint) && !isInputValidForAutofill(inputType)

    return doesNotNeedHints || (
        (isInputValidForAutofill(inputType)
            || hintContainsFormInput(hint))
            && autofillHints != null &&
            autofillHints.any { autofillHint -> hintContainsFormInput(autofillHint) }
        )
  } else {
    true
  }
}

internal fun isInputValidForAutofill(inputType: Int): Boolean {
  return inputType == InputType.TYPE_CLASS_PHONE ||
      inputType == InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS ||
      inputType == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD ||
      inputType == InputType.TYPE_NUMBER_VARIATION_PASSWORD ||
      inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS ||
      inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT ||
      inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
      inputType == InputType.TYPE_TEXT_VARIATION_PERSON_NAME ||
      inputType == InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS ||
      inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
}

internal fun hintContainsFormInput(hint: String): Boolean {
  return possibleFormInput.any { possibleEntry -> hint.contains(possibleEntry, true) }
}