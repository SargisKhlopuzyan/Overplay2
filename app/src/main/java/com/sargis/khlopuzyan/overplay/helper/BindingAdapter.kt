package com.sargis.khlopuzyan.overplay.helper

import android.widget.TextView
import androidx.databinding.BindingAdapter
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Sargis Khlopuzyan on 3/29/2022.
 */

@BindingAdapter("bindTextSize")
fun TextView.bindTextSize(size: StateFlow<Float>) {
    size.value.also {
        textSize = it
    }
}

@BindingAdapter("bindSessionCount")
fun TextView.bindSessionCount(count: StateFlow<Int>) {
    "SESSION COUNT ${count.value}".also {
        text = it
    }
}