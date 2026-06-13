
import java.time.*;
import java.util.ArrayList;

public class User extends Account {
    public static final int MAX_MATCHES = 5;
    private String name;
    private LocalDate birthday; 
    private String postalCode;
    private String city;
    private String bio;
    private char gender;
    private char preferredGender;
    private boolean matched;
    private Match[] matches;
    private int numMatches;
    private ArrayList<Match> matchHistory;
    private ArrayList<Event> eventHistory;
    private ArrayList<Event> eventHosted;
    private Tag interestTag;

    public User(String u, String p, String e, String n, LocalDate b, String pc, String c, String bi, char g, char pg, Tag t){
        super(u, p, e);
        name = n;
        birthday = b;
        postalCode = pc;
        city = c;
        gender = g;
        preferredGender = pg;
        interestTag = t;
        matches = new Match[MAX_MATCHES];
        numMatches = 0;
        eventHistory = new ArrayList<>();
        eventHosted = new ArrayList<>();
    }

    public User(String u, String p, String e, boolean ev, String n, LocalDate b, String pc, String c, String bi, char g, char pg, Tag t){
        super(u, p, e, ev);
        name = n;
        birthday = b;
        postalCode = pc;
        city = c;
        bio = bi;
        gender = g;
        preferredGender = pg;
        interestTag = t;
        matches = new Match[MAX_MATCHES];
        numMatches = 0;
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

    public void setBirthday(LocalDate b){
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

    public Match[] getMatches(){
        return matches;
    }
    public void setMatches(Match[] m){
        matches = m;
    }
    public void setMatchHistory(ArrayList<Match> mh){
        matchHistory = mh;
    }

    public int getNumMatches(){
        return numMatches;
    }

    public void setNumMatches(int n){
        numMatches = n;
    }

    public ArrayList<Match> getMatchHistory(){
        return matchHistory;
    }
    public ArrayList<Event> getEventHistory(){
        return eventHistory;
    }
    public ArrayList<Event> getEventHosted(){
        return eventHosted;
    }
    public void setEventHistory(ArrayList<Event> eh){
        eventHistory = eh;
    }
    public void setEventHosted(ArrayList<Event> eh){
        eventHosted = eh;
    }

    public Tag getTag(){
        return interestTag;
    }
    
    public void storeMatch(Match m){
        if (numMatches < matches.length){
            matches[numMatches] = m;
            numMatches ++;
        }
    }

    public void storeMatchHistory(Match m){
        matchHistory.add(m);
    }

    public void deleteMatch(Match m){
        for(int i = 0; i < matches.length; i++){
            if(matches[i] == m){
                matches[i] = null;
                numMatches--;
                storeMatchHistory(m);
                for(int j = i; j < matches.length - 1; j++){
                    matches[j] = matches[j+1];
                    matches[j+1] = null;
                }
            }
        }
    }
    public void deleteMatch(String name){
        for(int i = 0; i < matches.length; i++){
            if(matches[i] instanceof EventMatch && ((EventMatch)matches[i]).getEventTitle().equals(name)){
                matches[i] = null;
                numMatches--;
                for(int j = i; j < matches.length - 1; j++){
                    matches[j] = matches[j+1];
                    matches[j+1] = null;
                }
            }
        }
    }
    public EventMatch getEventMatch(String name){
        for(int i = 0; i < matches.length; i++){
            if(matches[i] instanceof EventMatch){ 
                if (((EventMatch)matches[i]).getEventTitle().equals(name)){
                    return ((EventMatch)matches[i]);
                }
            }
        }   
        return null;
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
    
    public void updateMatches(){
        if(numMatches > 0){
            for(int i = 0; i<numMatches; i++){
                if(matches[i].isExpire() && matches[1] != null){
                        deleteMatch(matches[i]);

                }
            }
        }
    }

    public Appeal reportUser(User u, String reason){
        Appeal appeal = new Appeal(u, reason);
        // semd to admin in runner class
        return appeal;
    }

    public Appeal reportUser(User u){
        Appeal appeal = new Appeal(u, "No reason provided");
        // semd to admin in runner class
        return appeal;
    }

    public void printMatchHistory(){
        System.out.println("Match History:");
        for(int i = 0; i < matchHistory.size(); i++){
            System.out.println(matchHistory.get(i));
        }
    }
    public void printEventHistory(){
        System.out.println("Event History:");
        for(int i = 0; i < eventHistory.size(); i++){
            System.out.println(eventHistory.get(i));
        }
    }
    public void printHostedEvents(){
        System.out.println("Hosted Events:");
        for(int i = 0; i < eventHosted.size(); i++){
            System.out.println(eventHosted.get(i));
        }
    }

    public void printProfile(){ 
        System.out.println("Name: " + name);
        System.out.println("Birthday: " + birthday);
        System.out.println("City: " + city);
        System.out.println("Bio: " + bio);
        System.out.println("Gender: " + gender);
        System.out.println("Preferred Gender: " + preferredGender);
    }

    public String toString(){
        return "Username: " + getUsername() + "\nName: " + name + "\nBirthday: " + birthday + "\nPostal Code: " + postalCode + "\nCity: " + city + "\nBio: " + bio + "\nGender";
    }
}
