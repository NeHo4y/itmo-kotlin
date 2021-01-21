package com.github.neho4y.integration.camunda.model

sealed class CamundaResult

data class Success(val data: String) : CamundaResult()

object Fail : CamundaResult()
