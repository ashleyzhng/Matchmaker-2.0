import java.time.LocalDate;

public class Teen extends User {
    public static final int MIN_AGE = 13;
    private boolean parentalControls;
    private Parent parent;

    //contructor, acces, muts
    public Teen(String u, String p, String e, String n, LocalDate b, String pc, String c, String bi, char g, char pg, Tag t){
        super(u, p, e, n, b, pc, c, bi, g, pg, t);
        parentalControls = false;
        parent = null;
    }
    // accessors mutators
    public boolean hasParentalControls(){
        return parentalControls;
    }
    public void setParentalControls(boolean pc){
        parentalControls = pc;
    }

    public Parent getParent(){
        return parent;
    }
    public void setParent(Parent p){
        parent = p;
    }

    public void requestParentApproval(Match m){
        if(parentalControls){
            parent.addMatch(m);
        }
    }

    public void storeMatch(Match m){
        if(parentalControls){
            requestParentApproval(m);
        }else{
            super.storeMatch(m);
        }
    }
    
    public String toString(){
        return "ya";
    }
}
