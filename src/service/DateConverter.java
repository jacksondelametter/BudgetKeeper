package service;

import java.sql.Date;
import java.util.Calendar;

public class DateConverter {

    public static Date getStartDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return new Date(cal.getTime().getTime());
    }

    public static Date getEndDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new Date(cal.getTime().getTime());
    }
}
