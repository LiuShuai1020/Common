package com.liushiyu.common

import android.animation.Animator
import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.android.synthetic.main.view_common_edit_text.view.*

/**
 * @author liu shuai
 * 描述：输入框
 */
class CustomEditText(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    companion object {
        const val SLING_HR_EDIT_TEXT_TYPE_SELECT = 10000
        const val SLING_HR_EDIT_TEXT_TYPE_QUERY = 10001
    }

    private var editWatcherListener = EditWatcher()
    private var inputViewFocusListener = InputViewFocusListener()

    private var currentFocus = false
    private var currentInputText = ""
    private var prohibitChange = false

    private var inputType = 0

    var slingHREditClickListener: SlingHREditClickListener? = null
    var slingHREditSearchClickListener: SlingHREditSearchClickListener? = null

    init {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.view_common_edit_text, this)
        initClickListener()
    }

    private fun initClickListener() {
        setOnTouchListener { _, _ ->
            if (!prohibitChange) {
                inputView.isFocusableInTouchMode = true
            }
            false
        }
        inputViewClearButton.setOnClickListener {
            inputViewClearButtonClick()
        }
        inputView.addTextChangedListener(editWatcherListener)
        inputView.onFocusChangeListener = inputViewFocusListener
        clickLayoutView.setOnClickListener {
            if (!prohibitChange) {
                slingHREditClickListener?.onClick()
            }
        }
        inputViewSearchButton.setOnClickListener {
            slingHREditSearchClickListener?.onClick(getInputValue())
        }
    }

    fun setInputHint(hint: String) {
        inputView.hint = hint
    }

    fun setInputType(inputType: Int) {
        if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            inputView.inputType = inputType or InputType.TYPE_CLASS_TEXT
        } else {
            inputView.inputType = inputType
        }
        this.inputType = inputType
    }

    fun setInputGravity(gravity: Int) {
        inputView.gravity = gravity
    }

    fun setInputLeftTitle(leftTitle: String) {
        leftInputTitle.text = leftTitle
    }

    fun setInputValue(inputValue: String = "", prohibitChange: Boolean = false, changeLineColor: Boolean = true) {
        inputView.text = Editable.Factory.getInstance().newEditable(inputValue)
        inputView.setSelection(inputValue.length)
        this.prohibitChange = prohibitChange
        if (inputType == SLING_HR_EDIT_TEXT_TYPE_SELECT) {
            inputView.removeTextChangedListener(editWatcherListener)
            inputView.onFocusChangeListener = null
            inputView.isFocusableInTouchMode = false
            inputView.clearFocus()
            hideClearButton()
            setErrorTextValue(null)
            if (prohibitChange) {
                clickLayoutView.visibility = View.GONE
                if (changeLineColor) {
                    inputViewLine.setBackgroundColor(
                            ContextCompat.getColor(
                                    context,
                                    R.color.color_divide
                            )
                    )
                }
            } else {
                clickLayoutView.visibility = View.VISIBLE
                if (changeLineColor) {
                    inputViewLine.setBackgroundColor(
                            ContextCompat.getColor(
                                    context,
                                    R.color.color_theme_blue
                            )
                    )
                }
            }

        } else if (inputType == SLING_HR_EDIT_TEXT_TYPE_QUERY) {
            clickLayoutView.visibility = View.VISIBLE
            inputView.removeTextChangedListener(editWatcherListener)
            inputView.onFocusChangeListener = null
            inputView.isFocusableInTouchMode = false
            inputView.clearFocus()
            hideClearButton()
            setErrorTextValue(null)
            inputViewLine.setBackgroundColor(
                    ContextCompat.getColor(
                            context,
                            R.color.color_divide
                    )
            )
        } else {
            clickLayoutView.visibility = View.GONE
            if (prohibitChange) {
                inputView.removeTextChangedListener(editWatcherListener)
                inputView.onFocusChangeListener = null
                inputView.isFocusableInTouchMode = false
                inputView.clearFocus()

                if (changeLineColor) {
                    inputViewLine.setBackgroundColor(
                            ContextCompat.getColor(
                                    context,
                                    R.color.color_divide
                            )
                    )
                }

                hideClearButton()
                setErrorTextValue(null)
            } else {
                inputView.addTextChangedListener(editWatcherListener)
                inputView.onFocusChangeListener = inputViewFocusListener
                inputView.isFocusableInTouchMode = true

                inputViewLine.setBackgroundColor(
                        ContextCompat.getColor(
                                context,
                                R.color.color_theme_blue
                        )
                )
            }
        }
    }

    fun showSearchButton() {
        inputViewSearchButton.visibility = View.VISIBLE
    }

    fun getInputValue(): String {
        return inputView.text.toString()
    }

    fun showTips() {
        showTips(null)
    }

    fun showTips(tips: String?) {
        setErrorTextValue(tips)
        YoYo.with(Techniques.Shake)
                .duration(700)
                .withListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        inputViewLine.setBackgroundColor(
                                ContextCompat.getColor(
                                        context,
                                        R.color.color_theme_blue
                                )
                        )
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationStart(animation: Animator?) {
                        inputViewLine.setBackgroundColor(
                                ContextCompat.getColor(
                                        context,
                                        R.color.color_theme_red
                                )
                        )
                    }
                }
                )
                .playOn(this)
    }

    private fun inputViewClearButtonClick() {
        setInputValue("")
    }

    inner class EditWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            currentInputText = s.toString()
            if (s.isEmpty()) {
                hideClearButton()
            } else {
                if (currentFocus) {
                    showClearButton()
                }
            }
            setErrorTextValue(null)
        }
    }

    inner class InputViewFocusListener : OnFocusChangeListener {
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            currentFocus = hasFocus
            if (hasFocus) {
                if (currentInputText.isNotEmpty()) {
                    showClearButton()
                }
            } else {
                hideClearButton()
            }
            setErrorTextValue(null)
        }
    }

    private fun hideClearButton() {
        inputViewClearButton.visibility = View.GONE
    }

    private fun showClearButton() {
        inputViewClearButton.visibility = View.VISIBLE
    }

    private fun setErrorTextValue(tips: String?) {
        if (tips.isNullOrEmpty()) {
            inputViewErrorView.visibility = View.GONE
        } else {
            inputViewErrorView.text = tips
            inputViewErrorView.visibility = View.VISIBLE
        }
    }
}

interface SlingHREditClickListener {
    fun onClick()
}

interface SlingHREditSearchClickListener {
    fun onClick(string: String)
}