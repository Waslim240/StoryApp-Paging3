package com.waslim.storyapp.view.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.waslim.storyapp.R

class EmailEditText : AppCompatEditText {
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
        addTextChangedListener(afterTextChanged = {
            validateEmail(it.toString())
        })

    }

    private fun validateEmail(email: String) {
        when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> showError()
            else -> return
        }
    }

    private fun showError() = context.getString(R.string.email_harus_valid).also { error = it }

}