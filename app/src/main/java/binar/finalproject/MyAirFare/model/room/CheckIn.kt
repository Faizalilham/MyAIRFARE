package binar.finalproject.MyAirFare.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CheckIn(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val order_id : String,
    val from : String,
    val dest : String,
    val chair_number : String,
    val status : String,
    val date_air : String = ""
)