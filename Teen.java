import java.time.LocalDate;

public class Teen extends User {
    public static final int MIN_AGE = 13;

    //contructor, acces, muts
    public Teen(String u, String p, String e, String n, LocalDate b, String pc, String c, String bi, char g, char pg, Tag t){
        super(u, p, e, n, b, pc, c, bi, g, pg, t);

    }
    
    public String toString(){
        return "ya";
    }
}
