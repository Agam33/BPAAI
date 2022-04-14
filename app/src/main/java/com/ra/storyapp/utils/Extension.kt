package com.ra.storyapp.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ra.storyapp.R

fun View.hideView(state: Boolean) {
    if (state) this.visibility = View.GONE
    else this.visibility = View.VISIBLE
}

fun ImageView.setImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .placeholder(R.drawable.ic_baseline_person_24)
        .into(this)
}