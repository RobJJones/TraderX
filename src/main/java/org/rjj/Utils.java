package org.rjj;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Utils {

    public static SimpleDateFormat dateTimeFormatter =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public static String formatDateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String formatDateToTimeString(Date date) {
        return new SimpleDateFormat("HH:mm").format(date);
    }

    public static String dateToDirectoryName(Date date) {
        return new SimpleDateFormat("_yyyyMMdd/").format(date);
    }

    public static final List<String> EXCHANGES = List.of("NYSE", "NASDAQ", "LSE", "LSIN");

}
