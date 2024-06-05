package com.dicoding.storyapp.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EmailInput (context: Context, attrs: AttributeSet) : TextInputLayout(context, attrs) {
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                if (Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()) {
                    error = null
                } else {
                    error = "Mohon masukkan alamat e-mail yang benar"
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    fun setEditText (editText: TextInputEditText) {
        editText.addTextChangedListener(textWatcher)
    }
}