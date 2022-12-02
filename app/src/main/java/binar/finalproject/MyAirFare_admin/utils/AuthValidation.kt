package binar.finalproject.MyAirFare.utils

object AuthValidation {

    fun loginValidation(email : String, password:String):String{
        var result = ""
        result = if (email.isEmpty()){
            "please enter email"
        } else {
            "success"
        }
        result = if (!email.contains("@")){
            "please enter valid email"
        } else{
            "success"
        }

        result = if (email.filter { it.isDigit() }.isEmpty()) {
            "email must contain at least one digit"
        }  else {
            "success"
        }

        result = if(password.isEmpty()){
            "please enter password"
        }  else {
            "success"
        }

        result = if(password.length <= 5) {
            "please enter valid password, password must be at least 8 character"
        }else {
            "success"
        }

        return result

    }

    fun registerValidation(
        username : String,
        firstname :String,
        lastname :String,
        email :String,
        password :String,
        confirmPassword : String,
        tittle : String
    ):String{
        var result = ""

        result = if(username.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || email.isEmpty()
            || password.isEmpty() || confirmPassword.isEmpty() || tittle.isEmpty()){
            "field cannot be empety"
        }else{
            "success"
        }

        result = if(!email.contains("@")){
            "Masukkan email yang valid"
        }else{
            "success"
        }

        result = if(password != confirmPassword){
            "periksa password anda"
        }else{
            "success"
        }

        return result

    }

}