public class User extends Account {
    public static final int MAX_MATCHES = 5;
    private String name;
    private String birthday;
    private String postalCode;
    private String city;
    private String bio;
    private char gender;
    private char preferredGender;
    private boolean matched;
    private Match[] matches;
    // event history
    // event hosted
    private Tag interestTag;

    public User(String u, String p, String e, int v, String n, String b, String pc, String c, char g, char pg){
        super(u, p, e, v);
        name = n;
        birthday = b;
        postalCode = pc;
        city = c;
        gender = g;
        preferredGender = pg;
        interestTag = new Tag(0,0,0,0,0,0,0,0,0,0);
    }
    // accesors mutators
    public char getGender(){
        return gender;
    }
    public char getPreferredGender(){
        return preferredGender;
    }
    public boolean editProfile(String s){
        return true;
    }

    public boolean  isAvailable(){
        return false;
    }
    public void storeMatch(Match m){
        //blehbleh
    }
    public void deleteMatch(Match m){
        //blehbleh
    }
    
}
