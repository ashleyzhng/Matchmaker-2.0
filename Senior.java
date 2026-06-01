public class Senior extends User{
    public static final int MIN_AGE = 13;
    private String occupation;
    private String maritalStatus;
    private int preferredMaxAge;
    private int preferredMinAge;
    private boolean hasMobilityLimitations;
    
    public Senior(String u, String p, String e, int v, String n, String b, String pc, String c, char g, char pg, String m, int max, int min, boolean h){
        super(u, p, e, v, n, b, pc, c, g, pg);
        maritalStatus = m;
        preferredMaxAge = max;
        preferredMinAge = min;
        hasMobilityLimitations = h;
    }
}

