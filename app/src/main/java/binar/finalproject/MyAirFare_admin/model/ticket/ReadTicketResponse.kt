package binar.finalproject.MyAirFare_admin.model.ticket

import android.os.Parcelable
import binar.finalproject.MyAirFare.model.login.Carts
import kotlinx.parcelize.Parcelize

data class ReadTicketResponse(
    val tickets : MutableList<Tickets>
)

data class ReadTicketByIdResponse(
    val ticket : Tickets
)

@Parcelize
data class Tickets(
    val id : String,
    val name : String,
    val from : String,
    val dest : String,
    val date_air : String,
    val price : Int,
    val no_chair : Int,
    val type : Int,
    val trip_type : String,
    val deleted : Boolean,
    val logo : String,
    val flight_number : String,
    val kelas : Int,
    val estimated_up_dest : String,
    val createdAt : String,
    val updatedAt : String,
    val available : MutableList<AvailableTickets>
):Parcelable

@Parcelize
data class AvailableTickets(
    val id : Int,
    val ticked_id : String,
    val user_id : String,
    val chair_number : Int?,
    val createdAt : String,
    val updatedAt : String
):Parcelable