import java.io.*;
import java.util.*;

/*
   File Name: MatchFileManager.java
   Name: Ashley Zhong
   Class: ICS4U1
   Date: June 2026

   Description:
   The MatchFileManager class is responsible for loading and saving
   user match information from text files. It manages both current
   matches and match history, reconstructs Match objects from file
   data, and synchronizes match information with User accounts in
   the Matchmaker system.
*/

public class MatchFileManager {

    ////Reference to the main Matchmaker system
    private Matchmaker matchmaker;
    //Current active matches loaded from file
    private Match[] matches;
    //Past matches loaded from file
    private ArrayList<Match> matchHistory;

    /*
     * Constructor for MatchFileManager
     * Parameters: mm - the Matchmaker system associated with this file manager
     * Returns: None
     */
    public MatchFileManager(Matchmaker mm){
        this.matchmaker = mm;
        matches = null;
        matchHistory = null;
    }

    // -- acessors --
    public Match[] getMatches(){
        return matches;
    }
    public ArrayList<Match> getMatchHistory(){
        return matchHistory;
    }

    /*
     * Loads match information for a user from a text file
     * Parameters: username - the username whose matches are being loaded
     * Returns: None (updates the user's current matches and match history)
     */
    public void loadMatches(String username) {
        try {
            BufferedReader in = new BufferedReader(
                new FileReader("matches/matches_" + username + ".txt")
            );

            int numMatches = Integer.parseInt(in.readLine());
            User user = matchmaker.searchByUsername(username);
            user.setNumMatches(numMatches);
            matches = new Match[5];
            int historySize = Integer.parseInt(in.readLine());
            matchHistory = new ArrayList<Match>();

            for(int i = 0; i < numMatches; i++){

                String matchType = in.readLine();

                if(matchType.equals("Interest")){
                    matches[i] = new InterestMatch(
                        Double.parseDouble(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        in.readLine(),
                        in.readLine().charAt(0),
                        in.readLine().charAt(0),
                        Integer.parseInt(in.readLine()),
                        Integer.parseInt(in.readLine())
                    );
                }

                else if(matchType.equals("Distance")){
                    matches[i] = new DistanceMatch(
                        Double.parseDouble(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        in.readLine(),
                        in.readLine().charAt(0),
                        in.readLine().charAt(0),
                        Integer.parseInt(in.readLine()),
                        Integer.parseInt(in.readLine())
                    );
                }

                else if(matchType.equals("Single")){
                    matches[i] = new SingleEventMatch(
                        "Single", Double.parseDouble(in.readLine()),
                        in.readLine(), in.readLine(), in.readLine(),
                        matchmaker.getEvents().searchEventByTitle(in.readLine()),
                        matchmaker.getAccounts().searchByUsername(in.readLine()),
                        matchmaker.getAccounts().searchByUsername(in.readLine()),
                        in.readLine().charAt(0),
                        in.readLine().charAt(0),
                        Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine())
                    );
                }

                else if(matchType.equals("Double")){
                    matches[i] = new DoubleEventMatch(
                        "Double",
                        Double.parseDouble(in.readLine()),
                         in.readLine(), in.readLine(), in.readLine(),
                        matchmaker.getEvents().searchEventByTitle(in.readLine()),
                        matchmaker.getAccounts().searchByUsername(in.readLine()),
                        matchmaker.getAccounts().searchByUsername(in.readLine()),
                        matchmaker.getAccounts().searchByUsername(in.readLine()),
                        matchmaker.getAccounts().searchByUsername(in.readLine()),
                        in.readLine().charAt(0), in.readLine().charAt(0), in.readLine().charAt(0), in.readLine().charAt(0),
                        Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()),Integer.parseInt(in.readLine())
                    );
                }
            }

            
            for(int i = 0; i < historySize; i++){

                String matchType = in.readLine();

                if(matchType.equals("Interest")){
                    matchHistory.add(new InterestMatch(
                        Double.parseDouble(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        in.readLine(),
                        in.readLine().charAt(0),
                        in.readLine().charAt(0),
                        Integer.parseInt(in.readLine()),
                        Integer.parseInt(in.readLine())
                    ));
                }

                else if(matchType.equals("Distance")){
                    matchHistory.add(new DistanceMatch(
                        Double.parseDouble(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        in.readLine(),
                        in.readLine().charAt(0),
                        in.readLine().charAt(0),
                        Integer.parseInt(in.readLine()),
                        Integer.parseInt(in.readLine())
                    ));
                }

                else if(matchType.equals("Single")){
                    matchHistory.add(new SingleEventMatch(
                        "Single",
                        Double.parseDouble(in.readLine()),
                        in.readLine(), in.readLine(), in.readLine(),
                        matchmaker.getEvents().searchEventByTitle(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        in.readLine().charAt(0),
                        in.readLine().charAt(0),
                        Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine())
                    ));
                }

                else if(matchType.equals("Double")){
                    matchHistory.add(new DoubleEventMatch(
                        "Double",
                        Double.parseDouble(in.readLine()),
                        in.readLine(), in.readLine(), in.readLine(),
                        matchmaker.getEvents().searchEventByTitle(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        in.readLine().charAt(0),
                        in.readLine().charAt(0),
                        in.readLine().charAt(0),
                        in.readLine().charAt(0),
                        Integer.parseInt(in.readLine()),
                        Integer.parseInt(in.readLine()),
                        Integer.parseInt(in.readLine()),
                        Integer.parseInt(in.readLine())
                    ));
                }
            }

            in.close();
            user.setMatches(matches);
            user.setMatchHistory(matchHistory);
        } catch(IOException e){
            System.out.println("Error loading matches for " + username);
            matches = new Match[0];
            matchHistory = new ArrayList<Match>();
            User user = matchmaker.searchByUsername(username);
            user.setMatches(matches);
            user.setMatchHistory(matchHistory);
            user.setNumMatches(0);
        }
    }

     /*
     * Saves match information for a user to a text file
     * Parameters: username - the username whose matches are being saved
     * Returns: None
     */
    public void saveMatches(String username){
        try {
            PrintWriter out = new PrintWriter(
                new FileWriter("matches/matches_" + username + ".txt")
            );
            Match[] curMatches = matchmaker.searchByUsername(username).getMatches();
            out.println(curMatches.length);
            for (int i = 0; i < curMatches.length; i ++){
                if(curMatches[i] instanceof InterestMatch){
                    out.println("Interest");
                    out.println(curMatches[i].getScore());
                    out.println(((InterestMatch)curMatches[i]).getOwner().getUsername());
                    out.println(((InterestMatch)curMatches[i]).getSoulmate().getUsername());
                    out.println(((InterestMatch)curMatches[i]).getStatus(((InterestMatch)curMatches[i]).getOwner()));
                    out.println(((InterestMatch)curMatches[i]).getStatus(((InterestMatch)curMatches[i]).getSoulmate()));
                    out.println(((InterestMatch)curMatches[i]).getRating(((InterestMatch)curMatches[i]).getOwner()));
                    out.println(((InterestMatch)curMatches[i]).getRating(((InterestMatch)curMatches[i]).getSoulmate()));
                }
                else if(curMatches[i] instanceof DistanceMatch){
                    out.println("Distance");
                    out.println(curMatches[i].getScore());
                    out.println(((DistanceMatch)curMatches[i]).getOwner().getUsername());
                    out.println(((DistanceMatch)curMatches[i]).getSoulmate().getUsername());
                    out.println(((DistanceMatch)curMatches[i]).getStatus(((DistanceMatch)curMatches[i]).getOwner()));
                    out.println(((DistanceMatch)curMatches[i]).getStatus(((DistanceMatch)curMatches[i]).getSoulmate()));
                    out.println(((DistanceMatch)curMatches[i]).getRating(((DistanceMatch)curMatches[i]).getOwner()));
                    out.println(((DistanceMatch)curMatches[i]).getRating(((DistanceMatch)curMatches[i]).getSoulmate()));
                }
                else if(curMatches[i] instanceof SingleEventMatch){
                    out.println("Single");
                    out.println(curMatches[i].getScore());
                    out.println(((SingleEventMatch)curMatches[i]).getStart());
                    out.println(((SingleEventMatch)curMatches[i]).getEnd());
                    out.println(((SingleEventMatch)curMatches[i]).getDate());
                    out.println(((SingleEventMatch)curMatches[i]).getEvent().getTitle());
                    out.println(((SingleEventMatch)curMatches[i]).getOwner().getUsername());
                    if (((SingleEventMatch) curMatches[i]).getSoulmate() != null){
                        out.println(((SingleEventMatch) curMatches[i]).getSoulmate().getUsername());
                    } else {
                        out.println("null");
                    }
                }else{
                    out.println("Double");
                    out.println(curMatches[i].getScore());
                    out.println(((DoubleEventMatch)curMatches[i]).getStart());
                    out.println(((DoubleEventMatch)curMatches[i]).getEnd());
                    out.println(((DoubleEventMatch)curMatches[i]).getDate());
                    out.println(((DoubleEventMatch)curMatches[i]).getEvent().getTitle());
                    out.println(((DoubleEventMatch)curMatches[i]).getUser(0).getUsername());
                    if (((DoubleEventMatch) curMatches[i]).getUser(1) != null){
                        out.println(((DoubleEventMatch) curMatches[i]).getUser(1).getUsername());
                    } else {
                        out.println("null");
                    }
                    if (((DoubleEventMatch) curMatches[i]).getUser(2) != null){
                        out.println(((DoubleEventMatch) curMatches[i]).getUser(2).getUsername());
                    } else {
                        out.println("null");
                    }
                    if (((DoubleEventMatch) curMatches[i]).getUser(3) != null){
                        out.println(((DoubleEventMatch) curMatches[i]).getUser(3).getUsername());
                    } else {
                        out.println("null");
                    }
                }
            }
            
            ArrayList<Match> pastMatches = matchmaker.searchByUsername(username).getMatchHistory();
            out.println(pastMatches.size());
            
            for(int k = 0; k < pastMatches.size(); k++){

                Match m = pastMatches.get(k);

                if(m instanceof InterestMatch){
                    out.println("Interest");
                    out.println(m.getScore());

                    out.println(((InterestMatch)m).getOwner().getUsername());
                    out.println(((InterestMatch)m).getSoulmate().getUsername());

                    out.println(((InterestMatch)m).getStatus(((InterestMatch)m).getOwner()));
                    out.println(((InterestMatch)m).getStatus(((InterestMatch)m).getSoulmate()));

                    out.println(((InterestMatch)m).getRating(((InterestMatch)m).getOwner()));
                    out.println(((InterestMatch)m).getRating(((InterestMatch)m).getSoulmate()));
                }

                else if(m instanceof DistanceMatch){
                    out.println("Distance");
                    out.println(m.getScore());

                    out.println(((DistanceMatch)m).getOwner().getUsername());
                    out.println(((DistanceMatch)m).getSoulmate().getUsername());

                    out.println(((DistanceMatch)m).getStatus(((DistanceMatch)m).getOwner()));
                    out.println(((DistanceMatch)m).getStatus(((DistanceMatch)m).getSoulmate()));

                    out.println(((DistanceMatch)m).getRating(((DistanceMatch)m).getOwner()));
                    out.println(((DistanceMatch)m).getRating(((DistanceMatch)m).getSoulmate()));
                }

                else if(m instanceof SingleEventMatch){
                    out.println("Single");
                    out.println(m.getScore());

                    out.println(((SingleEventMatch)m).getEnd());
                    out.println(((SingleEventMatch)m).getDate());

                    out.println(((SingleEventMatch)m).getEvent().getTitle());
                    out.println(((SingleEventMatch)m).getOwner().getUsername());

                    if (((SingleEventMatch)m).getSoulmate() != null){
                        out.println(((SingleEventMatch)m).getSoulmate().getUsername());
                    } else {
                        out.println("null");
                    }
                }

                else if(m instanceof DoubleEventMatch){
                    out.println("Double");
                    out.println(m.getScore());

                    out.println(((DoubleEventMatch)m).getStart());
                    out.println(((DoubleEventMatch)m).getEnd());
                    out.println(((DoubleEventMatch)m).getDate());

                    out.println(((DoubleEventMatch)m).getEvent().getTitle());

                    out.println(((DoubleEventMatch)m).getUser(0).getUsername());

                    if (((DoubleEventMatch)m).getUser(1) != null){
                        out.println(((DoubleEventMatch)m).getUser(1).getUsername());
                    } else {
                        out.println("null");
                    }

                    if (((DoubleEventMatch)m).getUser(2) != null){
                        out.println(((DoubleEventMatch)m).getUser(2).getUsername());
                    } else {
                        out.println("null");
                    }

                    if (((DoubleEventMatch)m).getUser(3) != null){
                        out.println(((DoubleEventMatch)m).getUser(3).getUsername());
                    } else {
                        out.println("null");
                    }
                }
            }

            out.close();

        } catch(IOException e){
            System.out.println("Error saving matches for " + username);
        }
    }
    
}