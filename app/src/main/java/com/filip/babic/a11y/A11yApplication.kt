package com.filip.babic.a11y

import android.app.Application
import com.filip.babic.a11y.setup.A11yInitializer

/**
 * Used to setup the A11y library, and test out the behavior.
 */
class A11yApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    A11yInitializer.start(this)
  }
}