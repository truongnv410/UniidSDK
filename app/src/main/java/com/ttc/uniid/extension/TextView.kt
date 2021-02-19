package com.unitel.loginsignupsdk.extension

import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter(value = ["startColor", "endColor"], requireAll = true)
fun TextView.setGradientText(startColor: Int, endColor: Int) {
    val textShader: Shader = LinearGradient(0f, 0f, 0f, this.textSize, intArrayOf(startColor, endColor),
        floatArrayOf(0.5f, 1f), Shader.TileMode.CLAMP)
    this.paint.shader = textShader
}