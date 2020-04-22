package com.filip.babic.a11y.setup

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.filip.babic.a11y.report.ReportLogger
import com.filip.babic.a11y.scanner.A11yScanner

/**
 * Initializes the A11y library, to start screening your activities & fragments for accessibility
 * issues &  things you do to be more accessibility-friendly.
 */
object A11yInitializer {

  private val scanner by lazy { A11yScanner() }
  private val logger by lazy { ReportLogger() }

  private var activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks? = null
  private var fragmentLifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks? = null

  fun start(context: Context) {
    val applicationContext = context.applicationContext as? Application ?: return

    applicationContext.registerActivityLifecycleCallbacks(getActivityCallbacks())
  }

  fun destroy(context: Context) {
    val applicationContext = context.applicationContext as? Application ?: return

    applicationContext.unregisterActivityLifecycleCallbacks(getActivityCallbacks())
    disposeOfCallbacks()
  }

  private fun disposeOfCallbacks() {
    activityLifecycleCallbacks = null
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
      override fun onActivityStarted(activity: Activity) = Unit
      override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
      override fun onActivityStopped(activity: Activity) = Unit
      override fun onActivityResumed(activity: Activity) = Unit

      override fun onActivityDestroyed(activity: Activity) {
      }

      override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val activityView = activity.window.decorView.rootView as? ViewGroup ?: return

        // TODO scan the view

        val fragmentManager = (activity as? FragmentActivity)?.supportFragmentManager ?: return
        fragmentManager.registerFragmentLifecycleCallbacks(getFragmentCallbacks(), true)

        logger.logReport(scanner.scanView(activityView))
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
      override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
      }

      override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        view: View,
        savedInstanceState: Bundle?
      ) {
        super.onFragmentViewCreated(fm, f, view, savedInstanceState)

        // TODO: optimize this to be done in a separate thread/thread pool, if it's too slow
        logger.logReport(scanner.scanView(view))
      }
    }
  }
}