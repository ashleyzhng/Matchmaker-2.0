/*
   File Name: Match.java
   Name: Ashley Zhong
   Class: ICS4U1
   Date: June 2026

   Description:
   The Match class is an abstract superclass used in a matchmaking
   system. It stores a compatibility score and provides abstract
   methods that subclasses must implement to evaluate, manage,
   and store matches.
*/

/* public void deleteMatch(String name){
for(int i = 0; i < matches.length; i++){
            if(matches[i] instanceof DoubleEventMatch && (EventMatch)matches[i].getEventTitle().equals(name)){
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
        public Event getEventMatch(String name){
        for(int i = 0; i < matches.length; i++){
            if(matches[i] instanceof  EventMatch && (EventMatch)matches[i].getEventTitle().equals(name)){
                return ((EventMatch)matches[i]).getEvent();
            }
        }   
        return null;
    }   

*/

public abstract class Match{
    //stores the compatibility percentage
    private double score;

    //constructors
    public Match(){
        score = -1;
    }
    public Match (double s){
        score = s;
    }

    //---accessors and mutators as necessary---
     public double getScore(){
        return score;
    }
    public void setScore(double s){
        score = s;
    }
    
    //---abstract methods that subclasses must implement, refer to subclasses for further details---
    public abstract void execute(Object[] pool);
    public abstract boolean isAcceptance();
    public abstract boolean isExpire();
    public abstract void storeMatch();
    public abstract boolean prelimCheck(User u);
    public abstract boolean checkAgeCompatibility(User u);
    public abstract boolean checkGenderCompatibilty(User u);
    public abstract String toString();
    
    /* Compares the score of this match with another match.
     * @param other the other match to compare with
     * @return the difference in scores
     */
    public double compareScore(Match other){
        return score - other.score;
    }
}