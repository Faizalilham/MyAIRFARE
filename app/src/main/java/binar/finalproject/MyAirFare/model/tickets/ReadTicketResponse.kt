package binar.finalproject.MyAirFare.model.tickets


data class ReadTicketResponse(
    val tickets : MutableList<Schedule>
)

data class ReadTicketByIdResponse(
    val tickets : Schedule
)

