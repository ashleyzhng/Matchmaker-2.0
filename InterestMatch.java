
public class InterestMatch extends PersonMatch{
    private double tagsCount;
    
    public InterestMatch(User u, String exp){
        super("interest", exp, u);
    }

    @Override
    public void execute(Object[] pool){
        if (pool != null){
            User[] poolUser = new User[pool.length];
            if (pool.length > 0) {
                for(int i = 0; i < pool.length; i ++){
                    if(pool[i] instanceof User){
                        poolUser[i]=(User)pool[i];
                    }
                }
            }
            User other = execute(poolUser, 0, null, 0);
            tagsCount = evaluateTags(other);
            setScore(tagsCount);
            setSoulmate(other);
            storeMatch();
        }
    }
    
    private User execute(User[] pool, int idx, User best, double maxTags){
        if(pool == null || pool.length == 0){
            return null;
        }else if (idx == pool.length){
            return best;
        }else{
            //do not need this
            if(pool[idx]!= getOwner() &&  prelimCheck(pool[idx])){
                if (evaluateTags(pool[idx])> maxTags){
                    return execute(pool, idx+1, pool[idx],evaluateTags(pool[idx]) );
                }
            }
            return execute(pool, idx + 1, best, maxTags);
            
        }
    }
    public double evaluateTags(User other){
        User owner = getOwner();
        return owner.getTag().compareTo(other.getTag());
    }

    
}