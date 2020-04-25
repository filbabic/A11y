package com.filip.babic.a11y

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // TODO lead to previously built activities/fragments
    // TODO add a way to switch between activity & fragment mode
    textContrast.setOnClickListener {

    }

    imageViewContentDescription.setOnClickListener {

    }

    touchArea.setOnClickListener {

    }

    listItemContentDescription.setOnClickListener {

    }

    colorblindSupportTwoColors.setOnClickListener {

    }

    colorblindSupportThreeColors.setOnClickListener {

    }
  }
}
