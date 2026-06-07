
import java.time.*;
import java.util.ArrayList;

public class User extends Account {
    public static final int MAX_MATCHES = 5;
    private String name;
    private LocalDate birthday; // I CHANGED THIS -ASHLEY
    private String postalCode;
    private String city;
    private String bio;
    private char gender;
    private char preferredGender;
    private boolean matched;
    private Match[] matches;
    private int numMatches;
    private ArrayList<Event> eventHistory;
    private ArrayList<Event> eventHosted;
    private Tag interestTag;

    public User(String u, String p, String e, int v, String n, String b, String pc, String c, char g, char pg){
        super(u, p, e, v);
        name = n;
        birthday = LocalDate.parse(b);
        postalCode = pc;
        city = c;
        gender = g;
        preferredGender = pg;
        interestTag = new Tag(0,0,0,0,0,0,0,0,0,0);
        matches = new Match[MAX_MATCHES];
        numMatches = matches.length;
        eventHistory = new ArrayList<>();
        eventHosted = new ArrayList<>();
    }
    // accesors mutators
    public String getName(){
        return name;
    }

    public void setName(String n){
        name = n;
    }

    public LocalDate getBirthday(){
        return birthday;
    }

    public void setBirthday(String b){
        birthday = LocalDate.parse(b);
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

    public Match[] getMatches(){
        return matches;
    }

    public int getNumMatches(){
        return numMatches;
    }

    public void setNumMatches(int n){
        numMatches = n;
    }

    public Tag getTag(){
        return interestTag;
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
        for(int i = 0; i < matches.length; i++){
            if(matches[i] == m){
                matches[i] = null;
                numMatches--;
                for(int j = i; j < matches.length - 1; j++){
                    matches[j] = matches[j+1];
                    matches[j+1] = null;
                }
            }
        }
    }
    public boolean maxMatches(){
        if(numMatches == MAX_MATCHES){
            return true;
        }else{
            return false;
        }
    }
    public void maxEvents(){
        // update later
    }
        
    public boolean isAvailable(LocalTime start, LocalTime end){
        for (int i = 0; i < matches.length; i ++){
            if (matches[i] != null && matches[i] instanceof EventMatch){
                EventMatch eventMatch = (EventMatch) matches[i];
                if (eventMatch.isAcceptance()){
                    if (start.isBefore(eventMatch.getEnd()) && end.isAfter(eventMatch.getStart())){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void updateTags(Event e){
        // update user tags based on event tags
        interestTag.updateTag(e.getCategories().getReligion(), e.getCategories().getSports(),
        e.getCategories().getMusic(), e.getCategories().getArt(), e.getCategories().getTraveling(),
        e.getCategories().getFitness(), e.getCategories().getGaming(), e.getCategories().getCooking(),
        e.getCategories().getAnimal(), e.getCategories().getReading());
    }
    public boolean reportUser(User u, String reason){
        // create appeal object and sent to admin
        Appeal appeal = new Appeal(u, reason);
        // send appeal to admin
        // admin.handleAppeal(appeal);
        return true;
    }
     public boolean blockUser(User u){
        // block user from matching with you
        return true;
    }


}
