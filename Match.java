
public abstract class Match{
    private double score;

    public Match(){
        score = -1;
    }

    //---accessors and mutators as necessary---
     public double getScore(){
        return score;
    }
    public void setScore(double s){
        score = s;
    }
    public abstract void execute(Object[] pool);
    public abstract boolean isRejection();
    public abstract boolean isAcceptance();
    public abstract boolean isExpire();
    public abstract void storeMatch();
    public abstract boolean prelimCheck(User u);
    public abstract boolean checkAgeCompatibility(User u);
    public abstract boolean checkGenderCompatibilty(User u);

    
    public double compareScore(Match other){
        return score - other.score;
    }
    public abstract String toString();
}