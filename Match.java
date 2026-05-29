public abstract class Match{
    private static int nextMatchNum = 1;
    private int matchNum;
    private double score;
    private User user;

    
    public Match(User u){
        matchNum = nextMatchNum;
        nextMatchNum +=1;
        score = -1;
        user = u;
    }

    //---accessors and mutators as necessary---
    public abstract void execute(Object[] pool);
    public abstract boolean isRejection();
    public abstract boolean isAcceptance();
    public abstract boolean isExpire();
    public abstract void deleteMatch();

    public boolean userAvailable(){
        return user.isUserAvailable();
    }
    public boolean storeMatch(){
        if(userAvailable()){
            user.storeMatch(this);
            return true;
        }
        return false;
    }
    public double compareScore(Match other){
        return score - other.score;
    }


}