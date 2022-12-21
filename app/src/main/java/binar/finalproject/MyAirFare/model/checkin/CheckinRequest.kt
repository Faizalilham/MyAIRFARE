package binar.finalproject.MyAirFare.model.checkin

data class CheckinAutoRequest(
    val flight_number : String
)

data class CheckinManualRequest(
    val ticket_id : String,
    val order_id : String
)