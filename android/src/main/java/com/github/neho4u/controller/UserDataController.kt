package com.github.neho4u.controller

import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.utils.Client

class UserDataController {
    suspend fun getUserData(): UserData {
        return Client().use { it.user().getMe() }
    }
}
