package com.dicoding.picodiploma.ishowstory.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.ishowstory.R
import com.google.android.material.textfield.TextInputLayout

class PasswordValidationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val editText: EditText
    private val textInputLayout: TextInputLayout

    init {
        inflate(context, R.layout.view_password_validation, this)
        orientation = VERTICAL

        editText = findViewById(R.id.passwordEditText)
        textInputLayout = findViewById(R.id.passwordTextInputLayout)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.length < 8) {
                    textInputLayout.error = context.getString(R.string.password_error)
                } else {
                    textInputLayout.error = null
                }
            }
        })
    }

    fun getPassword(): String = editText.text.toString()
}
