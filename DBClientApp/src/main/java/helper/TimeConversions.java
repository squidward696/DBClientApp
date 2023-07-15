package helper;

import java.time.*;

/**
 * contains the methods to help with time conversions
 */
public class TimeConversions {

    /**
     * converts a localDateTime to EST (where the business is located)
     *
     * @param localDateTime the localDateTime
     * @return the localDateTime converted to EST
     */
    public static LocalDateTime dateTimeConversion(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTimeET = zonedDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        return zonedDateTimeET.toLocalDateTime();
    }

    /**
     * converts a localDateTime to EST (where the business is located)
     *
     * @param localDateTime the localDateTime
     * @return the time from the localDateTime converted to EST
     */
    public static LocalTime timeConversion(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTimeET = zonedDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        return zonedDateTimeET.toLocalTime();
    }

    /**
     * converts a localDateTime to UTC (to be stored in a database)
     *
     * @param localDateTime the localDateTime
     * @return the localDateTime converted to UTC
     */
    public static LocalDateTime convertToUTC(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTimeUTC = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        return zonedDateTimeUTC.toLocalDateTime();
    }
}
