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
    }
   
    /*
     * Executes the distance-based matching algorithm
     * Parameters: pool - the array of users to match against
     * Returns: None
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

            if (other != null){
                double distanceTier = calculateDistanceTier(other);
                setScore(distanceTier);
                setSoulmate(other);
                storeMatch();
            } else {
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
        private User execute(User[] pool, int idx, User best, double maxTier){

        if (pool == null || pool.length == 0 || idx == pool.length){
            return best;
        }
        User current = pool[idx];
        if(current != null && current != getOwner() && prelimCheck(current)){
            double currentTier = calculateDistanceTier(current);
            
            if(best == null || currentTier > maxTier){
                best = current;
                maxTier = currentTier;
            }
            
        }
        return execute(pool, idx + 1, best, maxTier);

    }
 
    /*
     * Calculates the distance tier between two users based on their postal codes
     * Parameters: other - the other user
     * Returns: The distance tier (0-6)
     */
    public double calculateDistanceTier(User other){
        User owner = getOwner();
        String posCode1 = owner.getPostalCode();
        String posCode2 = other.getPostalCode();
        return (calculateDistanceTier(posCode1, posCode2) / 6.0) * 100;
    }

    /*
     * Calculates the distance tier between two postal codes
     * Parameters: ps1 - the first postal code
     *             ps2 - the second postal code
     * Returns: The distance tier (0-6), 6 being the highest (greates compt.)
     */
    public static int calculateDistanceTier(String ps1, String ps2){
        if (ps1 == null || ps2 == null){
            return -1;
        }
        int count = 0;
        boolean exit = false;
        for (int i = 0; i < 6 && i < ps1.length() && i < ps2.length() && !exit; i ++){
            if (ps1.charAt(i) == ps2.charAt(i)){
                count ++;
            }else{
                exit = true;
            }
        }
        return count;
    } 
}