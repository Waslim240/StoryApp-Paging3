package com.waslim.storyapp.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.waslim.storyapp.R

class NameEditText : AppCompatEditText {
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
            checkName(it.toString())
        })
    }

    private fun checkName(name: String) {
        when {
            name.isEmpty() -> showError()
            else -> return
        }
    }

    private fun showError() = context.getString(R.string.nama_harus_di_isi).also { error = it }

}