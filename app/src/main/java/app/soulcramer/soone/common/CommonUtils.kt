package app.soulcramer.soone.common

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

@Suppress("unused")
val Any?.unit
    get() = Unit

operator fun StringBuilder.plusAssign(string: String) = append(string).unit

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.validate(message: String, validator: (String) -> Boolean) {
    this.afterTextChanged {
        this.error = if (validator(it)) null else message
    }
    this.error = if (validator(this.text.toString())) null else message
}

fun ClosedRange<Long>.random() =
    start + ((endInclusive - start) * Math.random()).toLong()