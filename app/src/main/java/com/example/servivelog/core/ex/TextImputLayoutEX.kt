package com.example.servivelog.core.ex

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.setErrorIfInvalid(text: String, error: String, emptyError: String) {
    this.error = when {
        text.isEmpty() -> emptyError
        !validarCampos(text) -> error
        else -> null
    }
}

private fun validarCampos(s: String): Boolean = s.isNotEmpty()