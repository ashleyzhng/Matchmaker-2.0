
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
                    if(pool[i]!= null && pool[i] instanceof User){
                        poolUser[i]=(User)pool[i];
                    }
                }
            }
            User other = execute(poolUser, 0, null, -1);
            if(other!= null){
                tagsCount = evaluateTags(other);
                setScore(tagsCount);
                setSoulmate(other);
                storeMatch();
            }
        }
    }
    
    private User execute(User[] pool, int idx, User best, double maxTags){
        if(pool == null || pool.length == 0){
            return null;
        }else if (idx == pool.length){
            return best;
        }else{
            if(pool[idx] != null && pool[idx]!= getOwner() &&  prelimCheck(pool[idx])){
                double score = evaluateTags(pool[idx]);
                if (score > maxTags){
                    return execute(pool, idx+1, pool[idx],score );
                }
            }
            return execute(pool, idx + 1, best, maxTags);
            
        }
    }
    public double evaluateTags(User other){
        if(other == null){
            return -1;
        }
        User owner = getOwner();
        return owner.getTag().compareTo(other.getTag());
    }

    
}