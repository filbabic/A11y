package com.filip.babic.a11y.setup

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.filip.babic.a11y.report.ReportLogger
import com.filip.babic.a11y.scanner.A11yScanner
import com.filip.babic.a11y.scanner.base.ViewScanner
import com.filip.babic.a11y.scanner.general.GeneralViewScanner
import com.filip.babic.a11y.scanner.image.ImageViewScanner
import com.filip.babic.a11y.scanner.text.EditTextScanner
import com.filip.babic.a11y.scanner.text.TextViewScanner
import java.io.File

/**
 * Initializes the A11y library, to start screening your activities & fragments for accessibility
 * issues &  things you do to be more accessibility-friendly.
 */
object A11yInitializer {

  private lateinit var rootFileDirectory: File
  private val scanner by lazy { A11yScanner(buildScannerList()) }
  private val logger by lazy { ReportLogger(rootFileDirectory) }

  private var activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks? = null
  private var fragmentLifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks? = null

  private fun buildScannerList(): List<ViewScanner> {
    val generalViewScanner = GeneralViewScanner()
    val imageViewScanner = ImageViewScanner()
    val textViewScanner = TextViewScanner()
    val editTextScanner = EditTextScanner()

    return listOf(generalViewScanner, imageViewScanner, textViewScanner, editTextScanner)
  }

  fun start(context: Context) {
    val applicationContext = context.applicationContext as? Application ?: return

    rootFileDirectory = buildRootDirectory(context.filesDir)
    applicationContext.registerActivityLifecycleCallbacks(getActivityCallbacks())
  }

  private fun buildRootDirectory(rootInternal: File): File {
    val directory = File(rootInternal, "a11y")

    if (!directory.exists()) {
      directory.mkdirs()
    }

    return directory
  }

  private fun getActivityCallbacks(): Application.ActivityLifecycleCallbacks {
    val callback = activityLifecycleCallbacks

    return if (callback != null) {
      callback
    } else {
      val newCallback = buildActivityCallbacks()
      activityLifecycleCallbacks = newCallback

      newCallback
    }
  }

  private fun buildActivityCallbacks(): Application.ActivityLifecycleCallbacks {
    return object : Application.ActivityLifecycleCallbacks {
      override fun onActivityPaused(activity: Activity) = Unit
      override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
      override fun onActivityStopped(activity: Activity) = Unit
      override fun onActivityResumed(activity: Activity) = Unit
      override fun onActivityStarted(activity: Activity) = Unit

      override fun onActivityDestroyed(activity: Activity) {
        val activityView = activity.window.decorView.rootView as? ViewGroup ?: return

        val hasReportedIssues =
          logger.logReport(scanner.flattenReport(scanner.scanView(activityView), emptyList()))

        showLoggingMessage(activity.applicationContext, hasReportedIssues)
      }

      override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val fragmentManager = (activity as? FragmentActivity)?.supportFragmentManager ?: return
        fragmentManager.registerFragmentLifecycleCallbacks(getFragmentCallbacks(), true)
      }
    }
  }

  private fun getFragmentCallbacks(): FragmentManager.FragmentLifecycleCallbacks {
    val callback = fragmentLifecycleCallbacks

    return if (callback != null) {
      callback
    } else {
      val newCallback = buildFragmentCallbacks()

      fragmentLifecycleCallbacks = newCallback
      newCallback
    }
  }

  private fun buildFragmentCallbacks(): FragmentManager.FragmentLifecycleCallbacks {
    return object : FragmentManager.FragmentLifecycleCallbacks() {

      override fun onFragmentViewDestroyed(fm: FragmentManager, fragment: Fragment) {
        if (fragment.isRemoving) {
          scanFragmentView(fragment)
        }
        super.onFragmentViewDestroyed(fm, fragment)
      }
    }
  }

  private fun scanFragmentView(fragment: Fragment) {
    val view = fragment.view

    if (view is ViewGroup) {
      val hasReportedIssues =
        logger.logReport(scanner.flattenReport(scanner.scanView(view), emptyList()))

      showLoggingMessage(view.context.applicationContext, hasReportedIssues)
    }
  }

  private fun showLoggingMessage(
    context: Context,
    hasReportedIssues: Boolean
  ) {
    Toast.makeText(
      context,
      if (hasReportedIssues) "Logging Report!" else "Nothing to report!",
      Toast.LENGTH_SHORT
    ).show()
  }
}