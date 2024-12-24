package  com.library.bookgenerator.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConvertDate {

    public static Date convertStringToDate(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Incorrect date");
        }
    }

    public static Date convertStrToDate(String dateString) {
        try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Incorrect date");
        }
    }
}
