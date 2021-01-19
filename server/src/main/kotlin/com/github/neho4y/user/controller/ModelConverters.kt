package com.github.neho4y.user.controller

import com.github.neho4u.shared.model.user.RegisterParams
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4y.user.domain.User
import com.github.neho4y.user.model.UserCreationDto

internal fun User.toUserData() = UserData(
    username = username,
    email = email,
    phone = phone,
    role = role,
    id = id
)

internal fun RegisterParams.toCreationDto() = UserCreationDto(
    email = email,
    username = username,
    password = password,
    phone = phone
)
