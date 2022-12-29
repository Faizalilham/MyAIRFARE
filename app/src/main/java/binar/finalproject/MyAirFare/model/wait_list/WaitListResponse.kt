@file:Suppress("unused")

package binar.finalproject.MyAirFare.model.wait_list

import android.os.Parcelable
import binar.finalproject.MyAirFare.model.login.UserLoginResponse
import binar.finalproject.MyAirFare.model.tickets.AvailableTickets
import binar.finalproject.MyAirFare.model.tickets.Schedule
import kotlinx.parcelize.Parcelize

data class WaitListResponse(
    val wait_list : MutableList<WaitList>
)



data class WaitList(
    val id : Int,
    val user_id : String,
    val price : Int,
    val count : Int,
    val token_trx : String,
    val give_to : String,
    val status : String,
    val deleted: Boolean,
    val createdAt : String,
    val updatedAt : String,
    val user : UserLoginResponse,
    val carts : MutableList<CartWaitList>
)

@Parcelize
data class CartWaitList(
    val id : String,
    val trx_id : String,
    val ticket_id : String,
    val status : String,
    val deleted : Boolean,
    val chair_number : Int?,
    val createdAt : String,
    val updatedAt : String,
    val ticket : Schedule
):Parcelable

data class TicketWaitList(
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
)

data class WaitListDeleteResponse(
    val deleted : String
)


