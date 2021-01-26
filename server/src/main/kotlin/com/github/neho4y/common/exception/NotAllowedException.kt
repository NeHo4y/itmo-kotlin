package com.github.neho4y.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.FORBIDDEN)
class NotAllowedException(message: String = "Not allowed") : BasicException(message)
