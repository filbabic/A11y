package com.filip.babic.a11y.utils

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TableLayout
import androidx.core.view.ScrollingView
import androidx.viewpager.widget.ViewPager

/**
 * Utilities to check ImageView accessibility options.
 *
 * ImageViews should have content descriptions in most situations.
 *
 * The [contentDescription] is invalid if:
 *
 * - It's empty or null.
 * - It contains redundancy (useless description), such as "image" or "photo".
 * - It has a contentDescription, but shouldn't. E.g. in collections of views like lists, pagers...
 *
 * */

/**
 * @param imageView - ImageView to check the description of.
 *
 * @return If the description is useful and valid in any given situation.
 * */
fun isContentDescriptionInvalid(imageView: ImageView): Boolean {
  return hasBadDescription(imageView) || shouldNotHaveDescription(imageView)
}

private fun lacksContentDescription(imageView: ImageView) = imageView.contentDescription.isNullOrBlank()

private fun hasBadDescription(imageView: ImageView): Boolean {
  val description = imageView.contentDescription

  return lacksContentDescription(imageView) || "image" in description || "photo" in description
}

private fun shouldNotHaveDescription(imageView: ImageView): Boolean {
  val rootView = imageView.rootView
  var currentParentLayerView = getParentViewFromView(imageView) ?: return false

  while (currentParentLayerView !== rootView) {
    if (isParentACollectionView(currentParentLayerView)) return true

    currentParentLayerView = getParentViewFromView(currentParentLayerView) ?: return false
  }

  return isParentACollectionView(rootView)
}

private fun getParentViewFromView(view: View) = view.parent as? ViewGroup

private fun isParentACollectionView(view: View): Boolean {
  return view is TableLayout || view is AdapterView<*> || view is ScrollingView || view is GridLayout
      || view is ViewPager
}