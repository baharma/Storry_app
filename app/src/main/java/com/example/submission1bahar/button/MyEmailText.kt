package com.example.submission1bahar.button

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns

import androidx.appcompat.widget.AppCompatEditText



class MyEmailText : AppCompatEditText {


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {

        compoundDrawablePadding = 24
        setHint("Email")
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val isValid = Patterns.EMAIL_ADDRESS.matcher(p0.toString()).matches()
                error = if (!isValid && !p0.isNullOrEmpty()) "Invalid Email" else null

            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

}