package protect.Finia.TimeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * A utility class to process time
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class TimeProcessor {

    /**
     * The method is used to get rid of hours, minutes, and seconds
     * in order for the database to better pinpoint a time of the net worth item.
     *
     * @param year the year of the time
     * @param month the month of the time
     * @param day the date of the time
     */
    Date setTime(int year, int month, int day){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = calendar.getTime();
        return date;
    }

    public static String getStringFromDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CANADA);
        return dateFormat.format(date);
    }

    public static Date parseDateString(String dateStr, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CANADA);
        return dateFormat.parse(dateStr);
    }
}
