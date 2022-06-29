package com.albatros.kplanner.domain

import android.view.View
import android.view.animation.AnimationUtils
import com.albatros.kplanner.R

fun View.playFadeInAnimation(duration: Long) {
    with(this) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        animation.duration = duration
        startAnimation(animation)
        visibility = View.VISIBLE
    }
}

fun View.playFadeOutAnimation(duration: Long) {
    with(this) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        animation.duration = duration
        startAnimation(animation)
        visibility = View.INVISIBLE
    }
}