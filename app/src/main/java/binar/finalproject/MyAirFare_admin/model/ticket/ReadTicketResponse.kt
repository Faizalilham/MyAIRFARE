package binar.finalproject.MyAirFare_admin.model.ticket

import binar.finalproject.MyAirFare.model.login.Carts

data class ReadTicketResponse(
    val tickets : MutableList<Tickets>
)

data class ReadTicketByIdResponse(
    val tickets : Tickets
)

data class Tickets(
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
    val flight_number : String,
    val kelas : String,
    val estimated_up_dest : String,
    val createdAt : String,
    val updatedAt : String,
    val carts : MutableList<Carts>,
    val available : MutableList<AvailableTickets>
)

data class AvailableTickets(
    val id : Int,
    val ticked_id : String,
    val user_id : String,
    val chair_number : Int,
    val createdAt : String,
    val updatedAt : String
)