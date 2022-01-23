package ru.soldatov.android.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

interface OnOptionClickListener {

    fun onOptionClickListener(option: Int)
}

@BindingAdapter("percentOfRightAnswer")
fun bindPercentOfRightAnswer(progressBar: ProgressBar, count: Int) {
    progressBar.setProgress(count, true)
}

@BindingAdapter("minPercent")
fun bindMinPercent(progressBar: ProgressBar, count: Int) {
    progressBar.secondaryProgress = count
}

@BindingAdapter("enoughPercent")
fun bindEnoughPercent(progressBar: ProgressBar, value: Boolean) {
    val color = getColorGetState(progressBar.context, value)
    progressBar.progressTintList = ColorStateList.valueOf(color)
}

@BindingAdapter("enoughCount")
fun bindEnoughCount(textView: TextView, value: Boolean) {
    textView.setTextColor(getColorGetState(textView.context, value))
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("onClickListenerOption")
fun bindOnClickListenerOption(textView: TextView, option: OnOptionClickListener) {
    textView.setOnClickListener {
        option.onOptionClickListener(textView.text.toString().toInt())
    }
}

private fun getColorGetState(context: Context, gutResult: Boolean): Int {
    val color = if (gutResult) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, color)
}