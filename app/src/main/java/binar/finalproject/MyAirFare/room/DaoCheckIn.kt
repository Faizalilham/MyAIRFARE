package binar.finalproject.MyAirFare.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import binar.finalproject.MyAirFare.model.room.CheckIn

@Dao
interface DaoCheckIn {

    @Query("SELECT * FROM CheckIn")
    fun getAllCheckIn():MutableList<CheckIn>

    @Insert
    fun insertCheckIn(checkIn :CheckIn)

    @Delete
    suspend fun deleteCheckIn(checkIn: CheckIn)
}