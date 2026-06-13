import java.time.*;

public class TestManualEvent {
    public static void main(String[] args) {
        Matchmaker mm = new Matchmaker();
        User u = mm.searchByUsername("skatergirl16");
        if (u == null) {
            System.out.println("User not found: skatergirl16");
            return;
        }
        
        // Manually create a Virtual event
        Virtual event = new Virtual(
            "ManualTestEvent",
            LocalDate.of(2026, 8, 15),
            LocalTime.of(18, 0),
            LocalTime.of(20, 0),
            15.0,
            0, 1, 1, 0, 0, 0, 0, 0, 0, 0,  // tags: sports, music
            null,  // initialUser
            "zoom",
            999999,
            5555
        );
        
        // Add to approved events
        boolean added = mm.getEvents().addApprovedEvent(event);
        System.out.println("Event added to approved list: " + added);
        System.out.println("Num approved events: " + mm.getEvents().getNumApprovedEvents());
        
        // Now test single event match
        Object[] pool = mm.generateSingleEventPool(u);
        System.out.println("Generated pool size: " + (pool == null ? 0 : pool.length));
        
        if (pool != null && pool.length > 0) {
            for (int i = 0; i < pool.length; i++) {
                Event e = (Event) pool[i];
                System.out.println("  pool[" + i + "]: " + e.getTitle());
            }
        }
        
        EventMatch match = mm.generateSingleEventMatch(u);
        System.out.println("\nGenerated match: " + (match != null ? match.getClass().getSimpleName() : "null"));
        
        if (match != null) {
            Event e = match.getEvent();
            System.out.println("Event attached: " + (e != null));
            if (e != null) {
                System.out.println("Event title: " + e.getTitle());
                System.out.println("Match score: " + match.getScore());
            }
            System.out.println("\nMatch details:");
            System.out.println(match.toString());
        }
    }
}
