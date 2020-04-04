package protect.FinanceLord.ui.NetWorthEditReports;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Time;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    public static Date firstDayOfThisMonth() {
        Calendar calendar = new GregorianCalendar();
//        calendar.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date;
    }
}
