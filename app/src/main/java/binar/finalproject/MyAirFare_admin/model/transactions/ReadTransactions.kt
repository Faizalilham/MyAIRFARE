package binar.finalproject.MyAirFare_admin.model.transactions

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ReadTransactionsResponse(
    val transaction : MutableList<Transactions>
)


@Parcelize
data class Transactions(
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
    val carts : MutableList<CartWaitListTransactions>,
    val checkins : MutableList<CheckIn>
):Parcelable

@Parcelize
data class CartWaitListTransactions(
    val id : String,
    val trx_id : String,
    val ticket_id : String,
    val status : String,
    val deleted : Boolean,
    val chair_number : Int?,
    val createdAt : String,
    val updatedAt : String,
    val ticket : ScheduleTransactions
):Parcelable

@Parcelize
data class ScheduleTransactions(
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
    val updatedAt : String
):Parcelable


@Parcelize
data class UserLoginTransactions(
    val id : String,
    val username : String,
    val f_name : String,
    val l_name : String,
    val email : String,
    val g_id : String,
    val password : String,
    val access_level : Int,
    val active : Boolean,
    val deleted : Boolean,
    val photo : String,
    val createdAt : String,
    val updatedAt : String
):Parcelable

@Parcelize
data class CheckIn(
    val id : Int,
    val trx_id : Int,
    val status : String,
    val createdAt : String,
    val updatedAt : String
):Parcelable
