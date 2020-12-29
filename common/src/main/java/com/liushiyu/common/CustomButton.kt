package com.liushiyu.common

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

/**
 * @author liu shuai
 * 描述：自定义Button
 */
class CustomButton(context: Context, attributeSet: AttributeSet) :
    AppCompatTextView(context, attributeSet) {

    init {
        init()
    }

    private fun init() {
        background = context.getDrawable(R.drawable.background_custom_button)
    }
}