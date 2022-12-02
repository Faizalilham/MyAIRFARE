package binar.finalproject.MyAirFare.utils

object EditProfileValidation {

    fun editProfile(username : String,f_name : String,l_name : String,password :String,confirm_password :String,email : String,tittle :String): String{

        var result = ""
        result = if(username.isNotEmpty() && f_name.isNotEmpty() && l_name.isNotEmpty() && email.isNotEmpty() && tittle.isNotEmpty()){
            "success"
        }else{
            "Kolom yang anda kosongkan wajib diisi"
        }

        result = if(email.contains("@") && email.length < 10 ){
            "success"
        }else{
            "Masukkan email dengan benar"
        }

        result = if(password == confirm_password){
            "success"
        }else{
            "Kata sandi tidak sama"
        }
        return result

    }
}