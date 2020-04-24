package protect.FinanceLord.NetWorthEditReportsUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
    public static Date firstSecondOfThisMinute() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        Date date = calendar.getTime();
        return date;
    }
}
