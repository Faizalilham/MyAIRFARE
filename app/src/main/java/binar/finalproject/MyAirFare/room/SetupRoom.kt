package binar.finalproject.MyAirFare.room


import androidx.room.Database
import androidx.room.RoomDatabase
import binar.finalproject.MyAirFare.model.room.CheckIn


@Database(
    entities = [CheckIn::class],
    version = 2,
    exportSchema = false
)
abstract class SetupRoom : RoomDatabase() {
    abstract fun daoFavorite() : DaoCheckIn
}