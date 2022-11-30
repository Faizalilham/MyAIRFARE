package binar.finalproject.MyAirFare.model.register

data class UserRegisterRequest(
    val username : String,
    val f_name : String,
    val l_name : String,
    val email : String,
    val password : String,
    val re_type_pass : String,
    val title : String
)
