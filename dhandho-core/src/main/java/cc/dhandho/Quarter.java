package cc.dhandho;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public enum Quarter {
    Q1, Q2, Q3, Q4;

    public boolean isEndDateOfQuarter(Date reportDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reportDate);
        switch (this) {
            case Q4:
                return calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.DAY_OF_MONTH) == 31;
            case Q2:
                return calendar.get(Calendar.MONTH) == Calendar.JUNE && calendar.get(Calendar.DAY_OF_MONTH) == 30;
            case Q1:
                return calendar.get(Calendar.MONTH) == Calendar.MARCH && calendar.get(Calendar.DAY_OF_MONTH) == 31;
            case Q3:
                return calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER && calendar.get(Calendar.DAY_OF_MONTH) == 30;
            default:
        }
        return false;
    }
}
