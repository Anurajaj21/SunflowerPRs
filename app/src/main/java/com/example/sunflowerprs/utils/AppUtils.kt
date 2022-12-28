package com.example.sunflowerprs.utils

import android.content.res.Resources
import android.util.TypedValue

object AppUtils {
    fun Float.toPixel(resources : Resources): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            resources.displayMetrics
        ).toInt()
    }
}