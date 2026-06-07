public class Teen extends User {
    public static final int MIN_AGE = 13;
    private boolean parentalControls;

    //contructor, acces, muts
    public Teen(String u, String p, String e, int v, String n, String b, String pc, String c, char g, char pg){
        super(u, p, e, v, n, b, pc, c, g, pg);
    }
    // accessors mutators
    public boolean hasParentalControls(){
        return parentalControls;
    }
    public void setParentalControls(boolean pc){
        parentalControls = pc;
    }

    public void requestParentApproval(){
        if(parentalControls){
            // add match into parent approves
        }
    }
    
    public String toString(){
        return "ya";
    }
}
