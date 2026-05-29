import java.util.ArrayList;

public class Admin extends Account {
    private ArrayList<Appeal> appeals;

    //constructs, accessors mutators
    public Admin(String u, String p, String e, int v){
        super(u, p, e, v);
        appeals = new ArrayList<Appeal>();
    }
    public void viewAllUsers(){
        //search through user array list
        // prints using to string
    }
    public void viewUserInfo(User u){
        //sutff
    }
}
