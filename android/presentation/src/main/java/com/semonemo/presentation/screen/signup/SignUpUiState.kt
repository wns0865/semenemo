package com.semonemo.presentation.screen.signup

import com.semonemo.presentation.util.Validator

data class SignUpUiState(
    val nickname: String = "",
    val password: String = "",
    val profileImageUrl: String = "",
) {
    fun validate(): Boolean =
        profileImageUrl.isNotBlank() &&
            nickname.isNotBlank() &&
            password.isNotBlank() &&
            Validator.validationNickname(nickname).isEmpty() &&
            Validator
                .validationPassword(
                    password,
                ).isEmpty()
}
