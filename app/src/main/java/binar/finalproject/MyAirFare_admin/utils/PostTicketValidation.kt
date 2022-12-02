package binar.finalproject.MyAirFare_admin.utils

object  PostTicketValidation {

    fun postTicketValidation(
        airlane : String,from : String,destination : String,date_air : String,
        price : Int ,chair : Int, typeTicket : String,
        flightNumber : String,kelas : String,estimasi : String
    ): String{

        var result = ""
        result = if(airlane.isNotBlank() && from.isNotBlank() && destination.isNotBlank()  && date_air.isNotBlank()
            && typeTicket.isNotBlank() && flightNumber.isNotBlank() && estimasi.isNotBlank() && kelas.isNotBlank()
        ){
            "success"
        }else{
            "Kolom tidak boleh kosong"
        }

        result = if(price > 0 && chair > 0){
            "success"
        }else{
            "Masukkan nilai setiap kolom dengan dengan benar"
        }

        return result
    }
}