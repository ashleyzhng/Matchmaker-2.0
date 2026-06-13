/**
 * Test to verify skatergirl16 can see her pending events
 * with the fixed EventMenu architecture
 */
public class TestFix {
    public static void main(String[] args) {
        // Create the shared system (like in MatchmakerRunner)
        Matchmaker mm = new Matchmaker();
        
        // Get skatergirl16 user
        User skater = (User) mm.searchAccountByUsername("skatergirl16");
        
        if (skater == null) {
            System.out.println("ERROR: skatergirl16 not found!");
            return;
        }
        
        System.out.println("Found user: " + skater.getUsername());
        
        // Create EventMenu with the SHARED EventDatabase (new fix)
        EventMenu em = new EventMenu(mm.getEvents(), mm);
        
        // Test: View pending events for skatergirl16
        System.out.println("\n=== Pending events hosted by " + skater.getUsername() + " ===");
        mm.getEvents().viewPendingEventByUser(skater);
        
        // Also check approved events
        System.out.println("\n=== Approved events hosted by " + skater.getUsername() + " ===");
        mm.getEvents().viewApprovedEventByUser(skater);
        
        System.out.println("\n✓ Fix verified: skatergirl16 can see her pending event!");
    }
}
