package protect.FinanceLord.NetWorthEditReports;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
    public static Date firstSecondOfThisMinute() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date;
    }
}
