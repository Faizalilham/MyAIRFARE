package binar.finalproject.MyAirFare_admin.utils

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DatePicker {

    fun datePicker(context : Context) : String{
        var result = ""
        val formatDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH)
        val day = now.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(context,DatePickerDialog.OnDateSetListener{_, mYear, mMount, mDay ->
                now.set(Calendar.YEAR,mYear)
                now.set(Calendar.MONTH,mMount)
                now.set(Calendar.DAY_OF_MONTH,mDay)
                result = formatDate.format(now.time)
            },year,month,day
        )
        datePicker.show()
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MMMM-dd ")
        return LocalDateTime.now().format(formatter)
    }

}