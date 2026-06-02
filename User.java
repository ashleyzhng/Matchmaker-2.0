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
    private int numMatches;
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
        matches = new Match[MAX_MATCHES];
        numMatches = matches.length;
    }
    // accesors mutators
    public String getName(){
        return name;
    }

    public void setName(String n){
        name = n;
    }

    public String getBirthday(){
        return birthday;
    }

    public void setBirthday(String b){
        birthday = b;
    }

    public String getPostalCode(){
        return postalCode;
    }

    public void setPostalCode(String pc){
        postalCode = pc;
    }

    public String getCity(){
        return city;
    }

    public void setCity(String c){
        city = c;
    }

    public String getBio(){
        return bio;
    }

    public void setBio(String b){
        bio = b;
    }

    public char getGender(){
        return gender;
    }

    public void setGender(char g){
        gender = g;
    }

    public char getPreferredGender(){
        return preferredGender;
    }

    public void setPreferredGender(char pg){
        preferredGender = pg;
    }

    public boolean getMatched(){
        return matched;
    }

    public void setMatched(boolean m){
        matched = m;
    }

    public int getNunMatches(){
        return numMatches;
    }

    public void setNumMatches(int n){
        numMatches = n;
    }
    
    public boolean editProfile(String s){
        return true;
    }

    public boolean  isAvailable(){
        int count = 0;
        for(int i = 0; i< matches.length; i++){
            if(matches[i] != null){
                count++;
            }
        }
        if(count == MAX_MATCHES){
            return false;
        }else{
            return true;
        }
    }
    public void storeMatch(Match m){
        matches[numMatches] = m;
        numMatches ++;
    }
    public void deleteMatch(Match m){
        //find where it is, shift all and set the last guy to null
    }
    
}
