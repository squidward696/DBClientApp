package helper;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * contains the methods to help with time conversions
 */
public class TimeConversions {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * converts a localDateTime EST to localDateTime systemDefault
     *
     * @param localDateTime the localDateTime in EST
     * @return the EST time converted to systemDefault()
     */
    public static LocalDateTime convertESTToSD(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTimeEST = ZonedDateTime.of(localDateTime, ZoneId.of("America/New_York"));
        //ZonedDateTime zonedDateTimeEST = localDateTime.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime zonedDateTimeSD = zonedDateTimeEST.withZoneSameInstant(ZoneId.systemDefault());
        return zonedDateTimeSD.toLocalDateTime();
    }

    /**
     * Takes a localDateTime in EST and converts it to UTC
     *
     * @param localDateTime the localDateTime EST
     * @return the localDateTime converted into UTC
     */
    public static LocalDateTime convertESTToUTC(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTimeEST = ZonedDateTime.of(localDateTime, ZoneId.of("America/New_York"));
        //ZonedDateTime zonedDateTimeEST = localDateTime.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime zonedDateTimeUTC = zonedDateTimeEST.withZoneSameInstant(ZoneOffset.UTC);
        return zonedDateTimeUTC.toLocalDateTime();
    }

    /**
     * converts a localDateTime to UTC (to be stored in a database)
     *
     * @param localDateTime the localDateTime (systemDefault)
     * @return the localDateTime (systemDefault) converted to UTC
     */
    public static LocalDateTime convertSDToUTC(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTimeSD = ZonedDateTime.of(localDateTime, ZoneOffset.systemDefault());
        //ZonedDateTime zonedDateTimeSD = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTimeUTC = zonedDateTimeSD.withZoneSameInstant(ZoneOffset.UTC);
        return zonedDateTimeUTC.toLocalDateTime();
    }

    /**
     * converts a localDateTime to EST (where the business is located)
     *
     * @param localDateTime the localDateTime (systemDefault)
     * @return the time from the localDateTime converted to EST
     */
    public static LocalDateTime convertSDToEST(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTimeSD = ZonedDateTime.of(localDateTime, ZoneOffset.systemDefault());
        //ZonedDateTime zonedDateTimeSD = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTimeEST = zonedDateTimeSD.withZoneSameInstant(ZoneId.of("America/New_York"));
        return zonedDateTimeEST.toLocalDateTime();
    }

    /**
     * converts the UTC localDateTime to the systemDefault for each user
     *
     * @param localDateTime the localDateTime from database UTC
     * @return the localDateTime UTC converted to systemDefault()
     */
    public static LocalDateTime convertUTCToSD(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTimeUTC = ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
        //ZonedDateTime zonedDateTimeUTC = localDateTime.atZone(ZoneOffset.UTC);
        ZonedDateTime zonedDateTimeSD = zonedDateTimeUTC.withZoneSameInstant(ZoneId.systemDefault());
        return zonedDateTimeSD.toLocalDateTime();
    }

    /**
     * converts a UTC to EST (where the business is located)
     *
     * @param localDateTime the localDateTime UTC
     * @return the localDateTime converted to EST
     */
    public static LocalDateTime convertUTCToEST(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTimeUTC = ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
        //ZonedDateTime zonedDateTimeUTC = localDateTime.atZone(ZoneOffset.UTC);
        ZonedDateTime zonedDateTimeEST = zonedDateTimeUTC.withZoneSameInstant(ZoneId.of("America/New_York"));
        return zonedDateTimeEST.toLocalDateTime();
    }
}
