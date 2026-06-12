/*
 * File Name: DistanceMatch.java
 * Name: Ashley Zhong
 * Class: ICS4U1
 * Date: June 2026
 *
 * Description:
 * The DistanceMatch class is a subclass of PersonMatch that implements distance-based matching logic.
 */
public class DistanceMatch extends PersonMatch{
    // weight constants used in scoring calculations
    public static final int MAX_DISTANCE_TIER = 6;
    //stores the distance tier between users, used in scoring calculations
    private double distanceTier;

    /*
     * Constructor for DistanceMatch
     * Parameters: u - the user for whom to find matches
     * Returns: None
     */
    public DistanceMatch(User u){
        super("distance", u);
    }

    public DistanceMatch(double score, User u, User s, String exp, char ownerStatus, char soulmateStatus, int ownerRating, int soulmateRating){
        super(score, "distance", u, s, exp, ownerStatus, soulmateStatus, ownerRating, soulmateRating);
        distanceTier = score;
    }
   
    /*
     * Executes the distance-based matching algorithm
     * Parameters: pool - the array of users to match against
     * Returns: None
     */
    @Override
    public void execute(Object[] pool){
        if (pool!= null){
            User[] poolUser = new User[pool.length];
            if (pool.length > 0) {
                for(int i = 0; i < pool.length; i ++){
                    if(pool[i] != null && pool[i] instanceof User){
                        poolUser[i]=(User)pool[i];
                    }
                }
            }
            User other = execute(poolUser, 0, null, -1);
            if (other!= null){
                setScore(distanceTier);
                setSoulmate(other);
                storeMatch();
            }else{
                setScore(0);
            }
            
        }   
    }
    /*
     * Executes the distance-based matching algorithm
     * Parameters: pool - the array of users to match against
     *             idx - the current index in the user pool
     *             best - the best user found so far
     *             maxTier - the maximum distance tier found so far
     * Returns: The best matching user or null if none found
     */
    private User execute(User[] pool, int idx, User best, int maxTier){
        if (pool == null || pool.length == 0){
            return null;
        }else if(idx == pool.length){
            if (best != null){
                distanceTier = calculateDistanceTier(best);;
            }else{
                distanceTier = -1;
            }
            return best;
        }else{
            User current = pool[idx];
            if(current != null && prelimCheck(current)){
                int currentTier = calculateDistanceTier(current); // FIXED: Calculate once and reuse
                
                if (currentTier > maxTier) {
                    return execute(pool, idx + 1, current, currentTier);
                }
            }
            return execute(pool, idx + 1, best, maxTier);
        }
    }
 
    /*
     * Calculates the distance tier between two users based on their postal codes
     * Parameters: other - the other user
     * Returns: The distance tier (0-6)
     */
    public int calculateDistanceTier(User other){
        User owner = getOwner();
        String posCode1 = owner.getPostalCode();
        String posCode2 = other.getPostalCode();
        return calculateDistanceTier(posCode1, posCode2);
    }

    /*
     * Calculates the distance tier between two postal codes
     * Parameters: ps1 - the first postal code
     *             ps2 - the second postal code
     * Returns: The distance tier (0-6)
     */
    public static int calculateDistanceTier(String ps1, String ps2){
        if (ps1 == null || ps2 == null){
            return -1;
        }
        int count = 0;
        boolean exit = false;
        for (int i = 0; i < ps1.length() && !exit; i ++){
            if (ps1.charAt(i) == ps2.charAt(i) && !exit){
                count ++;
            }else{
                exit = true;
            }
        }
        return count;
    } 
}