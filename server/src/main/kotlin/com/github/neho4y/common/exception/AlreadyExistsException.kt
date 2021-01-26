package com.github.neho4y.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
class AlreadyExistsException(message: String = "Already exists") : BasicException(message)
