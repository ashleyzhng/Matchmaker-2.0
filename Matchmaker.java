import java.time.*;
public class Matchmaker {
    //SHOULD curTime be public?
    public static final ZoneId myZone = ZoneId.systemDefault();
    public static ZonedDateTime curDate = ZonedDateTime.now();
    private ZonedDateTime eventDate = ZonedDateTime.of(2026, 6, 9, 23, 59, 0, 0, myZone);
    public static void main(String[] args) {
        System.out.println(curDate);
    }
}
