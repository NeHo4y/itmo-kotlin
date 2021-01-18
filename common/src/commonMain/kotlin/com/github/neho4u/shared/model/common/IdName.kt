package com.github.neho4u.shared.model.common

import kotlinx.serialization.Serializable

@Serializable
data class IdName(
    var id: Long,
    val name: String?,
)
