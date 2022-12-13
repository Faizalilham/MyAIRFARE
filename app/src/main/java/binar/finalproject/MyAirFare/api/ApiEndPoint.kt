package binar.finalproject.MyAirFare.api


import binar.finalproject.MyAirFare.model.tickets.ReadTicketByIdResponse
import binar.finalproject.MyAirFare.model.tickets.ReadTicketResponse
import binar.finalproject.MyAirFare.model.tickets.ScheduleResponse
import binar.finalproject.MyAirFare.model.wait_list.WaitList
import binar.finalproject.MyAirFare.model.wait_list.WaitListDeleteResponse
import binar.finalproject.MyAirFare.model.wait_list.WaitListRequest
import binar.finalproject.MyAirFare.model.wait_list.WaitListResponse
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


    @GET("api/v1/get-schedule")
    fun doSearch(
        @Query("from") from : String,
        @Query("destination") destination : String,
        @Query("depart") depart : String
    ): Call<ScheduleResponse>

    @GET("api/v1/filter-ticket")
    fun doFilterTicket(
        @Query("from") from : String,
        @Query("destination") destination : String,
        @Query("depart") depart : String,
        @Query("return") kembalian : String,
        @Query("kelas") kelas : String
    ): Call<ScheduleResponse>

    @GET("api/v1/wait-list")
    fun getAllListCart(
        @Header("x-access-token") Description : String,
    ):Call<WaitListResponse>

    @POST("api/v1/wait-list")
    fun postCart(
        @Header("x-access-token") Description : String,
        @Body tickets : WaitListRequest
    ):Call<WaitList>

    @DELETE("api/v1/wait-list/{id}")
    fun deleteCart(
        @Header("x-access-token") Description : String,
        @Path("id") id : Int
    ):Call<WaitListDeleteResponse>

}

