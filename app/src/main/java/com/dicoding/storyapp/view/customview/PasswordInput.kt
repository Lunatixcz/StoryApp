package com.dicoding.storyapp.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.dicoding.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class PasswordInput (context: Context, attrs: AttributeSet) : TextInputLayout(context, attrs) {

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                if (it.length < 8) {
                    error = context.getString(R.string.password_length_error_message)
                } else {
                    error = null
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }
    fun setEditText(editText: TextInputEditText) {
        editText.addTextChangedListener(textWatcher)
    }
}