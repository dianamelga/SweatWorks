package py.com.dianascode.sweatworkschallenge.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import py.com.dianascode.sweatworkschallenge.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

class UtilTools {
    companion object {
        fun datesFromMilis(milis: Long, format: String?): String {
            var format = format
            if (format == null) {
                format = "yyyy-MM-dd"
            }
            try {
                val d = Date(milis)
                val dateFormat = SimpleDateFormat(format, Locale("en", "US"))
                return dateFormat.format(d)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }

        fun ISO8601fromCalendar(calendar: Calendar): String {
            val date = calendar.time
            val formatted = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .format(date)
            return formatted.substring(0, 22) + ":" + formatted.substring(22)
        }

        fun ISO8601toCalendar(iso8601string: String): Calendar {
            val calendar = GregorianCalendar.getInstance()
            return try {
                var s = iso8601string.replace("Z", "+00:00")
                s = s.substring(0, 22) + s.substring(23)  // to get rid of the ":"
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s)
                calendar.time = date
                calendar
            } catch (e: Exception) {
                //throw new ParseException("Invalid length", 0);
                if (BuildConfig.DEBUG) e.printStackTrace()

                Calendar.getInstance()
            }
        }

        fun hasPermission(permission: String, context: Context): Boolean {
            var ret = false

            // If android sdk version is bigger than 23 the need to check run time permission.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                // return phone read contacts permission grant status.
                val hasPermission = ContextCompat.checkSelfPermission(context, permission)
                // If permission is granted then return true.
                if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                    ret = true
                }
            } else {
                ret = true
            }
            return ret
        }
    }
}