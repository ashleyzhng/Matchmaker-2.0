import java.io.*;
import java.util.*;

public class MatchFileManager {

    private Matchmaker matchmaker;
    private Match[] matches;
    private ArrayList<Match> matchHistory;

    public MatchFileManager(Matchmaker mm){
        this.matchmaker = mm;
        matches = null;
        matchHistory = null;
    }
    public Match[] getMatches(){
        return matches;
    }
    public ArrayList<Match> getMatchHistory(){
        return matchHistory;
    }

    public void loadMatches(String username) {
        try {
            BufferedReader in = new BufferedReader(
                new FileReader("matches/matches_" + username + ".txt")
            );

            int numMatches = Integer.parseInt(in.readLine());
            User user = matchmaker.searchByUsername(username);
            user.setNumMatches(numMatches);
            matches = new Match[numMatches];

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
                        Double.parseDouble(in.readLine()),
                        in.readLine(), in.readLine(), in.readLine(), in.readLine(), in.readLine(),
                        matchmaker.getEvents().searchEventByTitle(in.readLine()),
                        matchmaker.getAccounts().searchByUsername(in.readLine()),
                        matchmaker.getAccounts().searchByUsername(in.readLine()),
                        in.readLine().charAt(0),
                        in.readLine().charAt(0)
                    );
                }

                else if(matchType.equals("Double")){
                    matches[i] = new DoubleEventMatch(
                        Double.parseDouble(in.readLine()),
                        in.readLine(), in.readLine(), in.readLine(), in.readLine(), in.readLine(),
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

            int historySize = Integer.parseInt(in.readLine());
            matchHistory = new ArrayList<Match>();

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
                        Double.parseDouble(in.readLine()),
                        in.readLine(), in.readLine(), in.readLine(), in.readLine(), in.readLine(),
                        matchmaker.getEvents().searchEventByTitle(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        matchmaker.searchByUsername(in.readLine()),
                        in.readLine().charAt(0),
                        in.readLine().charAt(0)
                    ));
                }

                else if(matchType.equals("Double")){
                    matchHistory.add(new DoubleEventMatch(
                        Double.parseDouble(in.readLine()),
                        in.readLine(), in.readLine(), in.readLine(), in.readLine(), in.readLine(),
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
            user.setNumMatches(matches.length);
            

        } catch(IOException e){
            System.out.println("Error loading matches for " + username);
            matches = new Match[0];
            matchHistory = new ArrayList<Match>();
        }
    }

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
                    out.println(((SingleEventMatch)curMatches[i]).getEventType());
                    out.println(((SingleEventMatch)curMatches[i]).getStart());
                    out.println(((SingleEventMatch)curMatches[i]).getEnd());
                    out.println(((SingleEventMatch)curMatches[i]).getDate());
                    out.println(((SingleEventMatch)curMatches[i]).getDuration());
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
                    out.println(((DoubleEventMatch)curMatches[i]).getEventType());
                    out.println(((DoubleEventMatch)curMatches[i]).getStart());
                    out.println(((DoubleEventMatch)curMatches[i]).getEnd());
                    out.println(((DoubleEventMatch)curMatches[i]).getDate());
                    out.println(((DoubleEventMatch)curMatches[i]).getDuration());
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

                    out.println(((SingleEventMatch)m).getEventType());
                    out.println(((SingleEventMatch)m).getStart());
                    out.println(((SingleEventMatch)m).getEnd());
                    out.println(((SingleEventMatch)m).getDate());
                    out.println(((SingleEventMatch)m).getDuration());

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

                    out.println(((DoubleEventMatch)m).getEventType());
                    out.println(((DoubleEventMatch)m).getStart());
                    out.println(((DoubleEventMatch)m).getEnd());
                    out.println(((DoubleEventMatch)m).getDate());
                    out.println(((DoubleEventMatch)m).getDuration());

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