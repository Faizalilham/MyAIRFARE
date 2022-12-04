package binar.finalproject.MyAirFare.model.flight

import android.os.Parcelable
import binar.finalproject.MyAirFare.model.login.Carts
import kotlinx.parcelize.Parcelize


data class ScheduleResponse(
    val tickets : Go
)

data class Go(
    val go : MutableList<Schedule>
)

@Parcelize
data class Schedule(
    val id : String,
    val name : String,
    val from : String,
    val dest : String,
    val date_air : String,
    val price : Int,
    val no_chair : Int,
    val type : String,
    val trip_type : String,
    val deleted : Boolean,
    val logo : String,
    val flight_number : String,
    val kelas : String,
    val estimated_up_dest : String,
    val createdAt : String,
    val updatedAt : String,
    val carts : MutableList<Carts>
):Parcelable