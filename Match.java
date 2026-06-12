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
    public abstract void completed();
    
    /* Compares the score of this match with another match.
     * @param other the other match to compare with
     * @return the difference in scores
     */
    public double compareScore(Match other){
        return score - other.score;
    }
}