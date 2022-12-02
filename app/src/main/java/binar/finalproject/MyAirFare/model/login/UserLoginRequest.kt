package binar.finalproject.MyAirFare.model.login

data class UserLoginRequest(
    val email :String,
    val password : String
)

data class LoginGoogle (
    val credential : String
        )