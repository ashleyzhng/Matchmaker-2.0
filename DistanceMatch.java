
public class DistanceMatch extends PersonMatch{
   
    private int distanceTier;

    public DistanceMatch(User u, String exp){
        super("distance", exp, u);
    }
   
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
            }
            
        }   
    }
    public User execute(User[] pool, int idx, User best, int maxTier){
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
 
    public int calculateDistanceTier(User other){
        User owner = getOwner();
        String posCode1 = owner.getPostalCode();
        String posCode2 = other.getPostalCode();
        return calculateDistanceTier(posCode1, posCode2);
    }

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