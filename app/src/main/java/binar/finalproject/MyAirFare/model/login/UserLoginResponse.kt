package binar.finalproject.MyAirFare.model.login


data class UserLoginResponse(
    val token : String,
    val user : UserLogin
)

data class UserLogin(
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

data class Carts(
    val id : String,
    val user_id : String,
    val ticket_id : String,
    val status : String,
    val deleted : Boolean,
    val createdAt : String,
    val updatedAt : String
)
