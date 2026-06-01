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
}
