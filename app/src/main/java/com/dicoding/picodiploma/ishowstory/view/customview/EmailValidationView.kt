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

class EmailValidationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val editText: EditText
    private val textInputLayout: TextInputLayout

    init {
        inflate(context, R.layout.view_email_validation, this)
        orientation = VERTICAL

        editText = findViewById(R.id.emailEditText)
        textInputLayout = findViewById(R.id.emailTextInputLayout)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    textInputLayout.error = context.getString(R.string.email_error)
                } else {
                    textInputLayout.error = null
                }
            }
        })
    }

    fun getEmail(): String = editText.text.toString()

}
