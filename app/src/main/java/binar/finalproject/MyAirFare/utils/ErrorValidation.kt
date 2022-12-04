package binar.finalproject.MyAirFare.utils

object ErrorValidation {

    fun errorAuthValidation(code : Int):String{
        var error = ""
        error = if(code == 401){
            "Akses dilarang, user tidak terdaftar"
        }else if(code == 403){
            "Akses dilarang, akun user belum aktif"
        }else if(code == 404){
            "Akses dilarang, akun user tidak ditemukan"
        }else{
            "terjadi kesalahan server"
        }
        return error

    }
}