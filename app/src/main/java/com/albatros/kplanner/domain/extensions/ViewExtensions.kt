package com.albatros.kplanner.domain.extensions

import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import com.albatros.kplanner.R

fun View.playAnimation(duration: Long, @AnimRes animId: Int) {
    with(this) {
        val animation = AnimationUtils.loadAnimation(context, animId)
        animation.duration = duration
        startAnimation(animation)
    }
}

fun View.playFadeInAnimation(duration: Long = 500, @AnimRes animId: Int = R.anim.fade_in) {
    with(this) {
        playAnimation(duration, animId)
        visibility = View.VISIBLE
    }
}

fun View.playFadeOutAnimation(duration: Long = 500, @AnimRes animId: Int = R.anim.fade_out) {
    with(this) {
        playAnimation(duration, animId)
        visibility = View.INVISIBLE
    }
}