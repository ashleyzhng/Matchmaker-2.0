import java.util.ArrayList;

public class AppealDatabase {
    private ArrayList<Appeal> appeals;

    public AppealDatabase(){
        appeals = new ArrayList<Appeal>();
    }

    public ArrayList<Appeal> getAppeals(){
        return appeals;
    }
    public void setAppeals(ArrayList<Appeal> a){
        appeals = a;
    }

    public String toString(){
        return "";
    }
}
