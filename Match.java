import java.time.*;

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
    public abstract void execute(Object[] pool);
    public abstract boolean isRejection();
    public abstract boolean isAcceptance();
    public abstract boolean isExpire(ZonedDateTime curDate);
    public abstract void deleteMatch();
    public abstract void storeMatch();
    
    public double compareScore(Match other){
        return score - other.score;
    }


}