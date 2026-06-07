public class Parent extends Account {
    public static final int MAX_TEEN = 5;
    private Teen[] teens;
    private Match[] approveMatches;
    private Event[] teenEvents;
    private int numTeens;

    public Parent(String u, String p, String e, int v){
        super(u, p, e, v);
        teens = new Teen[MAX_TEEN];
        approveMatches = new Match[User.MAX_MATCHES];
        teenEvents = new Event[User.MAX_MATCHES];
    }
    // accessors mutators - do i even need them?
    public boolean addTeen(Teen t){
        if(t instanceof Teen && numTeens < MAX_TEEN){
            teens[numTeens] = t;
            numTeens++;
            t.setParentalControls(true);
            return true;
        }else{
            return false;
        }
    }
    public void viewTeenMatches(Teen t){
        // print matches for teen
        System.out.println("Matches for " + t.getName() + ":");
        for(int i = 0; i < t.getNumMatches(); i++){
            System.out.println(t.getMatches()[i].toString());
        }
    }
    public void viewTeenEvents(Teen t){
        // print events for teen
        System.out.println("Events for " + t.getName() + ":");
        /*
        for(int i = 0; i < t.getNumEvents(); i++){
            System.out.println(t.getEvents()[i].toString());
        }*/
    }
    public void approveMatch(Match m){
        // add match to approve matches
    }
    public void removeTeen(Teen t){
        for(int i = 0; i < teens.length; i++){
            if(teens[i] == t){
                teens[i] = null;
                numTeens--;
                // shift teens down
                for(int j = i; j < teens.length - 1; j++){
                    teens[j] = teens[j+1];
                    teens[j+1] = null;
                }                
            }
        }
    }
}
