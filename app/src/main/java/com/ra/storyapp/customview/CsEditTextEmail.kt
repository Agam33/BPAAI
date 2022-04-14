package com.ra.storyapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.ra.storyapp.R
import com.ra.storyapp.utils.isValidEmail

class CsEditTextEmail: AppCompatEditText {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, after: Int) {
                if(s.toString().isNotEmpty() && !isValidEmail(s))
                    error = context.getString(R.string.txt_error_edt_email)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
}