package com.ra.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.ra.storyapp.R

class CsEditTextPassword: AppCompatEditText {

    constructor(context: Context) : super(context) {
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
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, after: Int) {
                if(s.toString().isNotEmpty() && s.toString().length < 6)
                    error = context.getString(R.string.txt_error_edt_password)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        maxLines = 1
    }
}