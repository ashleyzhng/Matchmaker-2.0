import java.util.ArrayList;

public class Admin extends Account {
    private ArrayList<Appeal> appeals;
    private AccountsDatabase userDB;

    //constructs, accessors mutators
    public Admin(String u, String p, String e, int v, AccountsDatabase adb){
        super(u, p, e, v);
        appeals = new ArrayList<Appeal>();
        userDB = adb;
    }

    public ArrayList<Appeal> getAppeals(){
        return appeals;
    }
    public void setAppeals(ArrayList<Appeal> a){
        appeals = a;
    }
    public AccountsDatabase getUserDB(){
        return userDB;
    }
    public void setUserDB(AccountsDatabase adb){
        userDB = adb;
    }

    public void viewAllUsers(){
        User[] users = userDB.getUsers();
        for(int i = 0; i<users.length; i++){
            System.out.println(users[i].getUsername() + " - " + users[i].isAccountStatus());
        }
    }
    public void viewUserInfo(User u){
        if(u == null || !(u instanceof User)){
            System.out.println("User not found.");
        }else{
            System.out.println(u);
        }
    }

    public void viewAllAppeals(){
        for(int i = 0; i<appeals.size(); i++){
            System.out.println(appeals.get(i));
        }
    }
    public void addAppeal(Appeal a){
        appeals.add(a);
    }
    public Admin promoteUser(User u){
        if(u == null || !(u instanceof User)){
            System.out.println("User not found.");
            return null;
        }else{
            Admin newAdmin = new Admin(u.getUsername(), u.getPassword(), u.getEmail(), u.getVerifyCode(), userDB);
            if(userDB.deleteAccount(u, u.getPassword())){
                System.out.println("User promoted to admin.");
                return newAdmin;
            }else{
                System.out.println("Failed to promote user.");
                return null;
            }
        }
    }
}
