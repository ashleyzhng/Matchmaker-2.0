public class Parent extends Account {
    public static final int MAX_TEEN = 5;
    private Teen[] teens;
    private Match[] approveMatches;
    private Event[] teenEvents;
    private int numTeens;
    private int numMatches;

    public Parent(String u, String p, String e, int v){
        super(u, p, e, v);
        teens = new Teen[MAX_TEEN];
        approveMatches = new Match[User.MAX_MATCHES*MAX_TEEN];
        teenEvents = new Event[User.MAX_MATCHES];
    }
    // accessors mutators 
    public Teen[] getTeens(){
        return teens;
    }
    public void setTeens(Teen[] t){
        teens = t;
    }
    public Match[] getApproveMatches(){
        return approveMatches;
    }
    public void setApproveMatches(Match[] m){
        approveMatches = m;
    }
    public Event[] getTeenEvents(){
        return teenEvents;
    }
    public void setTeenEvents(Event[] e){
        teenEvents = e;
    }
    // other methods
    public boolean addTeen(Teen t){
        if(t instanceof Teen && numTeens < MAX_TEEN){
            teens[numTeens] = t;
            numTeens++;
            t.setParentalControls(true);
            t.setParent(this);
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
    public void addMatch(Match m){
        // add match to approve matches
        approveMatches[numMatches] = m;
        numMatches++;
    }
    public void approveMatch(Teen t, Match m){
        for(int i = 0; i < numMatches; i++){
            if(approveMatches[i] == m){
                // add match to teen matches
                approveMatches[i] = null;
                numMatches--;
                t.storeMatch(m);
                for(int j = 0; j < approveMatches.length - 1; j++){
                    approveMatches[j] = approveMatches[j+1];
                    approveMatches[j+1] = null;
                }
            }
        }
        
    }
    public void rejectMatch(Match m){
        // remove match from approve matches
        for(int i = 0; i < numMatches; i++){
            if(approveMatches[i] == m){
                approveMatches[i] = null;
                numMatches--;
                break;
            }
        }
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
