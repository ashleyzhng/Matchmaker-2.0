import java.time.ZonedDateTime;

public class DistanceMatch extends PersonMatch{
   
    public static final int MAXTIER = 5;
    private int distanceTier;

    public DistanceMatch(User u, ZonedDateTime exp){
        super("distance", exp, u);
    }
   
    public void execute(Object[] pool){
        //bleh
    }
/* 
    public int calculateDistanceTier(User other){
        User owner = getOwner();
        String posCode1 = owner.getPostalCode();
        String posCode2 = other.getPostalCode();
        return calculateDistanceTier(posCode1, posCode2);
    }

    public int calculateDistanceTier(String ps1, String ps2){
        
    }
    */
    
    
}