package binar.finalproject.MyAirFare.model.checkin

import android.os.Parcelable
import binar.finalproject.MyAirFare.model.tickets.Passenger
import binar.finalproject.MyAirFare.model.tickets.TicketClass
import binar.finalproject.MyAirFare.model.transactions.CheckIn
import binar.finalproject.MyAirFare.model.transactions.Transactions
import binar.finalproject.MyAirFare.model.transactions.UserLoginTransactions
import kotlinx.parcelize.Parcelize

data class CheckinResponse (
    val trxs : MutableList<TransactionsCheckins>
    )

@Parcelize
data class TransactionsCheckins(
    val id : String,
    val user_id : String,
    val price : Int,
    val count : Int,
    val token_trx : String,
    val give_to : String,
    val status : String,
    val deleted: Boolean,
    val order_id : String,
    val createdAt : String,
    val updatedAt : String,
    val user : UserLoginTransactions,
    val carts : MutableList<CartWaitListCheckIn>,
//    val checkins : MutableList<CheckIns>
): Parcelable

@Parcelize
data class CheckIns(
    val id : Int,
    val trx_id : Int,
    val status : String,
    val createdAt : String,
    val updatedAt : String
):Parcelable

@Parcelize
data class CartWaitListCheckIn(
    val id : String,
    val trx_id : String,
    val ticket_id : String,
    val status : String,
    val deleted : Boolean,
    val chair_number : Int?,
    val createdAt : String,
    val updatedAt : String,
    val ticket : ScheduleCheckIns
): Parcelable

@Parcelize
data class ScheduleCheckIns(
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
    val ticketClass : TicketClass,
    val passenger : Passenger,
//    val chair : Chair
): Parcelable

@Parcelize
data class Chair(
    val id : String,
    val ticket_id : String,
    val user_id : String,
    val chair_number : String,
    val createdAt : String,
    val updatedAt : String
):Parcelable