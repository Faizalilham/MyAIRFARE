package binar.finalproject.MyAirFare_admin.utils

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DatePicker {

    var result = ""
    fun datePicker(context : Context,e : EditText) {
        val formatDate = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH)
        val day = now.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(context,
            DatePickerDialog.OnDateSetListener{ _, mYear, mMount, mDay ->
                now.set(Calendar.YEAR,mYear)
                now.set(Calendar.MONTH,mMount)
                now.set(Calendar.DAY_OF_MONTH,mDay)
                e.setText(formatDate.format(now.time))
            },year,month,day
        )
        datePicker.show()
    }

    fun convertDatePicker(dates : String):String{
        val formatDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return formatDate.format(dates)
    }

    fun timePicker(context : Context, manager : FragmentManager, e : EditText){
        var hour = 0
        var minute = 0
        val a = DateFormat.is24HourFormat(context)
        val clockFormat = if(a) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Pilih waktu")
            .build()
        picker.show(manager,"TAG")
        picker.addOnPositiveButtonClickListener {
            hour = picker.hour
            minute = picker.minute
            if(minute == 0 || minute.toString() == "00"){
                val result = "$hour:$minute"+"0:00"
                e.setText(result)
            }else{
                val result = "$hour:$minute:00"
                e.setText(result)
            }



        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")
        return LocalDateTime.now().format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatterDate(date : String):String{
        val inputFormatter =
            DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.getDefault())
        val outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())
        val dates: LocalDate = LocalDate.parse(date, inputFormatter)
        val formattedDate = outputFormatter.format(dates)
        return formattedDate.toString()
    }

}