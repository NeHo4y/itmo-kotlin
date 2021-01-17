package com.github.neho4u.model

//required fields: jobticket, noteText, statusTypeId, workTime
data class FullNote(

    var noteText: String,
    var messageType: String
)

data class JobTicket(
        var id: Int,
        var type: String = "JobTicket"
)
