package util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun plusDays(date:Date,days:Int):Date{
        val calendar:Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH,days)
        return calendar.time
    }

    fun format(date:Date,pattern:String) :String{
        val formatter = SimpleDateFormat(pattern)
        return formatter.format(date)
    }

    fun parse(dateString:String,pattern:String) :Date  {
        val formatter = SimpleDateFormat(pattern)
        return formatter.parse(dateString)

    }
}