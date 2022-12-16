package binar.finalproject.MyAirFare.model.transactions

import android.os.Parcelable
import binar.finalproject.MyAirFare.model.login.UserLoginResponse
import binar.finalproject.MyAirFare.model.wait_list.CartWaitList
import kotlinx.parcelize.Parcelize

data class PostTransactionsRequest(
    val tickets_id : MutableList<String>,
    val chairs_number : MutableList<Int>
)

data class PostTransactionsResponse(
    val trx : Transactions
)

@Parcelize
data class Transactions(
    val id : Int,
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
    val user : UserLoginResponse,
    val carts : MutableList<CartWaitList>,
    val checkins : MutableList<CheckIn>
):Parcelable

@Parcelize
data class CheckIn(
    val id : String
):Parcelable
