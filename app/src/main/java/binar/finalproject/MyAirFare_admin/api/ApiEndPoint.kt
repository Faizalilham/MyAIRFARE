package binar.finalproject.MyAirFare.api


import binar.finalproject.MyAirFare_admin.model.ticket.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiEndPoint {


    @GET("api/v1/tickets")
    fun readAllTicket(
        @Header("x-access-token") Description : String
    ):Call<ReadTicketResponse>

    @GET("api/v1/{id}/ticket")
    fun readTicketById(
        @Header("x-access-token") Description : String,
        @Path("id") id : String
    ):Call<ReadTicketByIdResponse>

    @Multipart
    @POST("api/v1/ticket")
    fun doAddTicket(
        @Header("x-access-token") Description : String,
        @Part("airline") airline : RequestBody,
        @Part("from_city") from_city : RequestBody,
        @Part("destination") destination : RequestBody,
        @Part("date_air") date_air : RequestBody,
        @Part("price") price :RequestBody,
        @Part("no_chair") no_chair : RequestBody,
        @Part("type_ticket") type_ticket : RequestBody,
        @Part image : MultipartBody.Part,
        @Part("flightNumber") flightNumber : RequestBody,
        @Part("kelas") kelas : RequestBody,
        @Part("estimated_up_dest") estimated_up_dest : RequestBody
    ): Call<PostTicketResponse>


    @Multipart
    @PUT("api/v1/ticket/{id}/update")
    fun doUpdateTicket(
        @Header("x-access-token") Description : String,
        @Path("id") id : String,
        @Part("airline") airline : RequestBody,
        @Part("from_city") from_city : RequestBody,
        @Part("destination") destination : RequestBody,
        @Part("date_air") date_air : RequestBody,
        @Part("price") price :RequestBody,
        @Part("no_chair") no_chair : RequestBody,
        @Part("type_ticket") type_ticket : RequestBody,
        @Part image : MultipartBody.Part,
        @Part("flightNumber") flightNumber : RequestBody,
        @Part("kelas") kelas : RequestBody,
        @Part("estimated_up_dest") estimated_up_dest : RequestBody,
    ): Call<UpdateTicketResponse>

    @DELETE("api/v1/ticket/{id}/delete")
    fun doDeleteTicket(
        @Header("x-access-token")Description : String,
        @Path("id") id : String
    ): Call<DeleteTicketResponse>

    @GET("api/v1/filter-ticket")
    fun doFilterTicket(
        @Query("from") from : String,
        @Query("destination") destination : String,
        @Query("depart") depart : String,
        @Query("kelas") kelas : String,
        @Query("type_passenger[]") type_passenger : String,
        @Query("return") returns : String,
    ):Call<FilterTicketResponse>




}

