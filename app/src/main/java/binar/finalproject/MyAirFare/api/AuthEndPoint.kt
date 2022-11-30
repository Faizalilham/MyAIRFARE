package binar.finalproject.MyAirFare.api



import binar.finalproject.MyAirFare.model.login.UserLoginRequest
import binar.finalproject.MyAirFare.model.login.UserLoginResponse
import binar.finalproject.MyAirFare.model.register.UserRegisterRequest
import binar.finalproject.MyAirFare.model.register.UserRegisterResponse
import binar.finalproject.MyAirFare.model.reset_password.ForgetPassword
import binar.finalproject.MyAirFare.model.reset_password.ResetPassword
import binar.finalproject.MyAirFare.model.user.CurrentUser
import retrofit2.Call
import retrofit2.http.*

interface AuthEndPoint {


    @POST("api/v1/login")
    fun doUserLogin(
       @Body userLogin : UserLoginRequest
    ):Call<UserLoginResponse>

    @POST("api/v1/register")
    fun doUserRegister(
        @Body userRegister : UserRegisterRequest
    ):Call<UserRegisterResponse>

    @POST("api/v1/reset-password")
    fun doResetPassword(
       @Body forgetPassword : ForgetPassword
    ):Call<ResetPassword>

    @GET("api/v1/who-am-i")
    fun getCurrentUser(
        @Header("x-access-token")Description : String
    ):Call<CurrentUser>



}