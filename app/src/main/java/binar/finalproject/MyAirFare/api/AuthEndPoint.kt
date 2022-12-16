package binar.finalproject.MyAirFare.api



import binar.finalproject.MyAirFare.model.login.LoginGoogle
import binar.finalproject.MyAirFare.model.login.UserLoginRequest
import binar.finalproject.MyAirFare.model.login.UserLoginResponse
import binar.finalproject.MyAirFare.model.register.UserRegisterRequest
import binar.finalproject.MyAirFare.model.register.UserRegisterResponse
import binar.finalproject.MyAirFare.model.reset_password.ForgetPassword
import binar.finalproject.MyAirFare.model.reset_password.ResetPassword
import binar.finalproject.MyAirFare.model.user.CurrentUser
import binar.finalproject.MyAirFare.model.user.UserCurrentUpdate
import okhttp3.MultipartBody
import okhttp3.RequestBody
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


    @Multipart
    @PUT("api/v1/update-profile")
    fun updateCurrentUser(
        @Header("x-access-token")Description : String,
        @Part("username") username : RequestBody,
        @Part("f_name") f_name : RequestBody,
        @Part("l_name") l_name : RequestBody,
        @Part("email") email : RequestBody,
        @Part("password") password : RequestBody,
        @Part("re_type_pass") re_type_pass : RequestBody,
        @Part("title") title : RequestBody,
        @Part image : MultipartBody.Part,
        @Part("visa_number")visa_number : RequestBody
    ): Call<UserCurrentUpdate>

    @POST("api/v1/loginRegGoogle")
    fun doLoginWithGoole(
        @Body loginGoogle : LoginGoogle
    ):Call<UserRegisterResponse>


}