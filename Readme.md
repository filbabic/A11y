# About the project

This library is built with love for Accessibility (known as A11y), and the need coming from the Android developer ecosystem.

There haven't been (m)any plug-n-play libraries for Android, which detect A11y issues, so I've decided to do my part, and try to expose a small set of algorithms and functions, which detect and report any issues your app might have.


## Scope of the Library

The library covers some of the basic A11y issues that can be found in every Android app:

- Content Description Issues (No description, or bad description)
- Low Contrast issues (Between text & its background)
- Touch Area issues (Small & hard to touch views)
- EditText Hint issues (Missing autofill/hints)

I'd love to support things like deuteranopia, tritanopia and monochromacy color schemes and contrast in the future, but this requires very specific and individualistic approach to all the content, unless I use aggregates and average values, or some form of ML to detect if there might be issues.

## Using the Library

You can use the library, by adding the **latest version** to your module-level `build.gradle` file like so:

```kotlin
dependencies {

  // other project dependencies ...

  implementation 'com.filbabic.a11y:a11y:$latest_version'
}
```

You can check out the latest version, and the Jcenter repository at the following [repository link](https://bintray.com/beta/#/filbabic/A11y/com.filbabic.a11y?tab=overview).

After that, you can just sync the project, and start the **A11y** library, by adding the following line of code to your Application singleton class's `onCreate()`:

```kotlin
override fun onCreate() {
  super.onCreate()

  A11yInitializer.start(this)
}
```

And that's it! Everything will be handled for you, and all the optimizations & cleanup will be performed by A11y.

## Issue detection

The issues are detected in the entire ViewTree, using a recursive algorithm. These tree-structure-reports are then flattened into a linear, linked-list kind of report, which is much easier to iterate over and use when printing out the report issues and suggestions.

These algorithms are written by hand, and I don't guarantee their speed or optimization, but they seem to be working fine so far! **Let me know if you have any suggestions!**

Reports are generated in the `onDestroy()` functions of Fragments, and Activities!

## Reading Reports

Reports are stored within your app's **files** directory, within the internal storage.

You can access it via Android Studio's Device File Explorer, within `data -> data -> yourAppPackage -> files -> a11y`

You can easily find this and read the reports, which are currently just textual and don't have extra styling.
