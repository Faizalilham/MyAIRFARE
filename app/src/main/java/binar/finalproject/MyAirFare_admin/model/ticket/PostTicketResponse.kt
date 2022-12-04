package binar.finalproject.MyAirFare_admin.model.ticket


data class PostTicketResponse(
   val ticket : PostTicket
)


data class PostTicket(
    val id : String,
    val name : String,
    val from : String,
    val dest : String,
    val date_air : String,
    val price : Int,
    val no_chair : Int,
    val type : String,
    val trip_type : String,
    val deleted : Boolean,
    val logo : String,
    val estimated_up_dest : String,
    val flight_number : String,
    val kelas : String,
    val updatedAt : String,
    val createdAt : String

)
