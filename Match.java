
public abstract class Match{
    private static int nextMatchNum = 1;
    private int matchNum;
    private double score;

    
    public Match(){
        matchNum = nextMatchNum;
        nextMatchNum +=1;
        score = -1;
    }

    //---accessors and mutators as necessary---
    public int getMatchNum(){
        return matchNum;
    }
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
    public abstract void deleteMatch();
    public abstract void storeMatch();
    
    public double compareScore(Match other){
        return score - other.score;
    }
    public abstract String toString();
}