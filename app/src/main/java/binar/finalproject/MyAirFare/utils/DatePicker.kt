package binar.finalproject.MyAirFare.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.text.format.DateFormat.is24HourFormat
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

object DatePicker {

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

    fun timePicker(context : Context,manager : FragmentManager,e : EditText){
        var hour = 0
        var minute = 0
        val a = is24HourFormat(context)
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
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        val dates: LocalDate = LocalDate.parse(date, inputFormatter)
        val formattedDate = outputFormatter.format(dates)
        return formattedDate.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatterDateExample(date : String,input : String,output : String):String{
        val inputFormatter =
            DateTimeFormatter.ofPattern(input, Locale.getDefault())
        val outputFormatter = DateTimeFormatter.ofPattern(output, Locale.getDefault())
        val dates: LocalDateTime = LocalDateTime.parse(date, inputFormatter)
        val formattedDate = outputFormatter.format(dates)
        return formattedDate.toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun calculationTime(input: String, output: String): String {
        val format = SimpleDateFormat("hh:mm:ss aa")
        val date1 = format.parse("$input pm")
        val date2 = format.parse("$output pm")
        val mills = date1!!.time - date2!!.time
        val hours = (mills / (1000 * 60 * 60)).toInt()
        val mins = (mills % (1000 * 60 * 60)).toInt()

        return if("$hours:$mins" == "00:00"){
            "00:00"
        }else{
            "$hours:$mins"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    fun dateCalculation(date: String):String{
        val odt = OffsetDateTime.parse(date)
        val instant = odt.toInstant()
        val dates = Date.from(instant)
        val a = SimpleDateFormat("dd MMMM yyyy")
        return a.format(dates)
    }


//    @SuppressLint("SimpleDateFormat")
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getDifferentTime(firstTime : String,secondTime : String):String{
//        val odtFirst = OffsetDateTime.parse(firstTime)
//        val instantFirst = odtFirst.toInstant()
//        val datesFirst = Date.from(instantFirst)
//        val odtSecond = OffsetDateTime.parse(secondTime)
//        val instantSecond = odtSecond.toInstant()
//        val datesSecond = Date.from(instantSecond)
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        val a = dateFormat.format(datesFirst)
//        val b = dateFormat.format(datesSecond)
//        val firstFix = dateFormat.parse(a)
//        val secondFix = dateFormat.parse(b)
//        val diff = firstFix!!.time - secondFix!!.time
//        val seconds : Long = diff / 1000
//        val minutes: Long = seconds / 60
//        val hours : Long = minutes / 60
//        val day = hours / 24
//
//        return if(datesFirst.before(datesSecond)){
//            "$day:$hours:$minutes"
//        }else{
//            "Terlewat"
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun timeCalculation(currentDate : String?): String {
        val instant = Instant.parse(currentDate)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.of(TimeZone.getDefault().id))
        return formatter.format(instant)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun timeCalculationTransactions(currentDate : String?): Date {
        val instant = Instant.parse(currentDate)
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy | HH:mm:ss")
            .withZone(ZoneId.of(TimeZone.getDefault().id))
        println(currentDate)
        println(formatter.format(instant).plus(1))
        return Date(formatter.format(instant).plus(1))
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun  getDifferentTime(startDate : String,stopDate : String):String{
        val instantStartDate = Instant.parse(startDate)
        val instantStopDate = Instant.parse(stopDate)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss")
            .withZone(ZoneId.of(TimeZone.getDefault().id))
        val format = SimpleDateFormat("dd/MM/yy HH:mm:ss")
        val start = formatter.format(instantStartDate)
        val stop = formatter.format(instantStopDate)
        var d1: Date? = null
        var d2: Date? = null
        try {
            d1 = format.parse(start)
            d2 = format.parse(stop)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val diff = d2!!.time - d1!!.time

        val diffTotalSeconds = diff / 1000
        val diffTotalMinutes = diff / (60 * 1000)
        val diffTotalHours = diff / (60 * 60 * 1000)

        val diffSeconds = diff / 1000 % 60
        val diffMinutes = diff / (60 * 1000) % 60
        val diffHours = diff / (60 * 60 * 1000) % 60

        return "$diffHours Jam $diffMinutes Menit"
    }


}