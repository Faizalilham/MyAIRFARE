package binar.finalproject.MyAirFare.model.register

data class UserRegisterResponse(
    val token : String,
    val user : UserRegister
)

data class UserRegister(
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
)

