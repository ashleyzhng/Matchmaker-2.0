public class Adult extends User {
    public static final int MIN_AGE = 13;
    private String occupation;
    private String maritalStatus;
    private int preferredMaxAge;
    private int preferredMinAge;
    
    public Adult(String u, String p, String e, int v, String n, String b, String pc, String c, char g, char pg, String m, int max, int min){
        super(u, p, e, v, n, b, pc, c, g, pg);
        maritalStatus = m;
        preferredMaxAge = max;
        preferredMinAge = min;
    }
    
    public String getOccupation(){
        return occupation;
    }
    public void setOccupation(String o){
        occupation = o;
    }
    public String getMaritalStatus(){
        return maritalStatus;
    }
    public void setMaritalStatus(String m){
        maritalStatus = m;
    }
    public int getPreferredMaxAge(){
        return preferredMaxAge;
    }
    public void setPreferredMaxAge(int max){
        preferredMaxAge = max;
    }
    public int getPreferredMinAge(){
        return preferredMinAge;
    }
    public void setPreferredMinAge(int min){
        preferredMinAge = min;
    }

    public String toString(){
        return super.toString() + "\nOccupation: " + occupation + "\nMarital Status: " + maritalStatus + "\nPreferred Max Age: " + preferredMaxAge + "\nPreferred Min Age: " + preferredMinAge;
    }

}
