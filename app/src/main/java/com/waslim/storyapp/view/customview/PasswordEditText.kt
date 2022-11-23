package com.waslim.storyapp.view.customview

import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.waslim.storyapp.R

class PasswordEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        transformationMethod = PasswordTransformationMethod()

        addTextChangedListener(afterTextChanged = {
            checkPassword(it.toString())
        })
    }

    private fun checkPassword(password: String) {
        when (password.length) {
            in 1..5 -> showError()
            else -> return
        }
    }

    private fun showError() = context.getString(R.string.password_harus_6_karakter).also { error = it }

}