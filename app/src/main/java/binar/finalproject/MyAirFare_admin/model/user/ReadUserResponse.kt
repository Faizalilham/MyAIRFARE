package binar.finalproject.MyAirFare_admin.model.user

import binar.finalproject.MyAirFare.model.login.Carts

data class ListUserResponse(
    val users : MutableList<User>
)

data class SingleUserResponse(
    val user : User
)

data class User(
    val id : String,
    val username : String,
    val email : String,
    val password : String,
    val g_id : String,
    val f_name : String,
    val l_name : String,
    val active : Boolean,
    val access_level : Int,
    val deleted : Boolean,
    val photo : String,
    val createdAt : String,
    val updatedAt : String,
    val carts : MutableList<Carts>
)

data class ListEmailResponse(
    val user : MutableList<UserEmail>
)

data class UserEmail(
    val id : String,
    val username : String,
    val email : String,
    val password : String,
    val g_id : String,
    val f_name : String,
    val l_name : String,
    val active : Boolean,
    val access_level : Int,
    val deleted : Boolean,
    val photo : String,
    val visa_ID : String?,
    val createdAt : String,
    val updatedAt : String,
    val trx : MutableList<Transaksi>
)

data class Transaksi(
    val id : String
)