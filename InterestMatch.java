/*
 * File Name: InterestMatch.java
 * Name: Ashley Zhong
 * Class: ICS4U1
 * Date: June 2026
 *
 * Description:
 * The InterestMatch class is a subclass of PersonMatch that implements interest-based matching logic.
 */

public class InterestMatch extends PersonMatch{
    /*
     * Constructor for the InterestMatch class
     * Parameters: u - the user for whom to create the match
     */
    public InterestMatch(User u){
        super("interest", u);
    }
    public InterestMatch(double score, User u, User s, String exp, char ownerStatus, char soulmateStatus, int ownerRating, int soulmateRating){
        super(score, "interest", u, s, exp, ownerStatus, soulmateStatus, ownerRating, soulmateRating);
         
    }
    /*
     * Executes the interest-based matching algorithm
     * Parameters: pool - the array of users to match against
     * Returns: none
     */
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
                double tagsCount = evaluateTags(other);
                setScore(tagsCount);
                setSoulmate(other);
                storeMatch();
            }else{
                setScore(0);
            }
        }
    }
    /*
     * Executes the interest-based matching algorithm
     * Parameters: pool - the array of users to match against, idx - the current index, best - the best match found so far, maxTags - the maximum number of matching tags
     * Returns: the best match found
     */
    private User execute(User[] pool, int idx, User best, double maxTags){
        if(pool == null || pool.length == 0 || idx == pool.length){
            return best;
        }
        User current = pool[idx];
        if (current!= null && current!= getOwner() && prelimCheck(current)){
            double score = evaluateTags(current);

            if(best == null || score > maxTags){
                best = current;
                maxTags = score;
            }
            return execute(pool, idx+1, best, maxTags);
        }
        
        
        
        else{
            if(pool[idx] != null && pool[idx]!= getOwner() &&  prelimCheck(pool[idx])){
                double score = evaluateTags(pool[idx]);
                if (score > maxTags){
                    return execute(pool, idx+1, pool[idx],score );
                }
            }
            return execute(pool, idx + 1, best, maxTags);
            
        }
    }
    /*
     * Evaluates the number of matching tags between users
     * Parameters: other - the other user to compare
     * Returns: the number of matching tags
     */
    public double evaluateTags(User other){
        if(other == null){
            return -1;
        }
        User owner = getOwner();
        return owner.getTag().compareTo(other.getTag());
    }


    
}