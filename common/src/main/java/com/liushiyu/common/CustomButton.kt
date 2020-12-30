package com.liushiyu.common

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

/**
 * @author liu shuai
 * 描述：自定义Button
 */
class CustomButton : AppCompatTextView {

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {

    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {

    }

    init {
        init()
    }

    private fun init() {
        background = context.getDrawable(R.drawable.background_custom_button)
    }
}