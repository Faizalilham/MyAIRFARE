package binar.finalproject.MyAirFare.model.user


import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CurrentUser(
   val user : CurrentUsers
):Parcelable

@Parcelize
data class CurrentUsers(
    val id : String,
    val username : String,
    val email : String,
    val password : String,
    val g_id : String?,
    val f_name : String,
    val l_name : String,
    val active : Boolean,
    val access_level : Int,
    val deleted : Boolean,
    val photo : String,
    val visa_ID : String?,
    val createdAt : String,
    val updatedAt : String,
//    val carts : MutableList<Carts>
):Parcelable