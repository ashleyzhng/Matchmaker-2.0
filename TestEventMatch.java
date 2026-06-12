public class TestEventMatch {
    public static void main(String[] args) {
        Matchmaker mm = new Matchmaker();
        User u = mm.searchByUsername("skatergirl16");
        System.out.println("Approved events count: " + mm.getEvents().getNumApprovedEvents());
        Event[] approved = mm.getEvents().getApprovedEventList();
        for (int i = 0; i < approved.length; i++) {
            if (approved[i] != null) {
                System.out.println(" approved[" + i + "]: " + approved[i].getTitle() + " (" + approved[i].getClass().getSimpleName() + ")");
            }
        }
        System.out.println("Pending events count: " + mm.getEvents().getNumPendingEvents());
        Event[] pending = mm.getEvents().getPendingEventList();
        for (int i = 0; i < pending.length; i++) {
            if (pending[i] != null) {
                System.out.println(" pending[" + i + "]: " + pending[i].getTitle() + " (" + pending[i].getClass().getSimpleName() + ")");
            }
        }
        if (u == null) {
            System.out.println("User not found: skatergirl16");
            return;
        }
        Object[] pool = mm.generateSingleEventPool(u);
        System.out.println("Single event pool size: " + (pool == null ? 0 : pool.length));
        if (pool != null) {
            for (int i = 0; i < pool.length; i++) {
                Event ev = (Event) pool[i];
                System.out.println("  pool[" + i + "]: " + ev.getTitle() + " (users: " + (ev.getCurrentUser1()!=null) + ", " + (ev.getCurrentUser2()!=null) + ")");
            }
        }
        EventMatch em = mm.generateSingleEventMatch(u);
        if (em == null) {
            System.out.println("No EventMatch object returned (null).");
            return;
        }
        Event e = em.getEvent();
        System.out.println("Generated EventMatch: " + em.getClass().getSimpleName());
        System.out.println("Event attached: " + (e != null));
        if (e != null) {
            System.out.println("Event title: " + e.getTitle());
        }
        System.out.println(em.toString());
    }
}
