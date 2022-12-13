package binar.finalproject.MyAirFare_admin.model.ticket

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class FilterTicketResponse(
    val tickets : Go
)


data class Go(
    val go : MutableList<Tickets>
)

@Parcelize
data class Schedule(
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
    val carts: MutableList<CartSchedule>
): Parcelable


@Parcelize
data class CartSchedule(
    val id : Int,
    val trx_id : Int,
    val ticket_id : String,
    val status : String,
    val deleted: Boolean,
    val chair_number : Int?,
    val createdAt : String,
    val updatedAt : String,
    val trx : Transaksi
): Parcelable

@Parcelize
data class Transaksi(
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
    val checkins: MutableList<CheckIn>
): Parcelable

@Parcelize
data class CheckIn(
    val id : Int
): Parcelable