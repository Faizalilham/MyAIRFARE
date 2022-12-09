package binar.finalproject.MyAirFare.model.wait_list

import binar.finalproject.MyAirFare.model.login.Carts
import binar.finalproject.MyAirFare.model.login.UserLoginResponse

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
    val carts : Carts
)

data class WaitListDeleteResponse(
    val deleted : MutableList<Int>
)


