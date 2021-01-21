package com.github.neho4y.user.model

import com.github.neho4u.shared.model.user.RegisterParams
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4y.user.domain.User

fun User.toUserData() = UserData(
    username = username,
    email = email,
    phone = phone,
    role = role,
    id = id
)

fun RegisterParams.toCreationDto() = UserCreationDto(
    email = email,
    username = username,
    password = password,
    phone = phone
)
