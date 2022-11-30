package binar.finalproject.MyAirFare.model.user


import binar.finalproject.MyAirFare.model.login.Carts


data class CurrentUser(
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
    val updatedAt : String,
    val carts : MutableList<Carts>
)