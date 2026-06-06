
public class DistanceMatch extends PersonMatch{
   
    private int distanceTier;

    public DistanceMatch(User u, String exp){
        super("distance", exp, u);
    }
   
    public void execute(Object[] pool){
         User[] poolUser = new User[pool.length];
        if (pool != null && pool.length > 0) {
            for(int i = 0; i < pool.length; i ++){
                if(pool[i] instanceof User){
                    poolUser[i]=(User)pool[i];
                }
            }
        }
        User other = execute(poolUser, 0, null, 0);
        setSoulmate(other);
        storeMatch();
        
    }
    public User execute(User[] pool, int idx, User best, int maxTier){
        if (pool == null || pool.length == 0){
            return null;
        }else if(idx == pool.length){
            return best;
        }else{
            User current = pool[idx];
            if(calculateDistanceTier(current)> maxTier && prelimCheck(pool[idx])){
                return execute(pool, idx+1, current, calculateDistanceTier(current));
             }else{
                return execute(pool, idx + 1, best, maxTier);
             }
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