package binar.finalproject.MyAirFare.api


import binar.finalproject.MyAirFare.model.flight.ScheduleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndPoint {



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

}

