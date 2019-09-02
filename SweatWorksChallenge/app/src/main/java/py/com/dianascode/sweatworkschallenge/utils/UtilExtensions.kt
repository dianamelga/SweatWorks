package py.com.dianascode.sweatworkschallenge.utils

import android.widget.Toast
import com.google.gson.Gson

fun String.asFormattedDate(): String {
    val milis = UtilTools.ISO8601toCalendar(this).timeInMillis
    return UtilTools.datesFromMilis(milis, "MM/dd/yyyy")
}

fun Any.toJSON(): String {
    return Gson().toJson(this)
}
