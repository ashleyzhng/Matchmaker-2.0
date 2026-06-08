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
    public void viewAllUsers(){
        Account[] users = userDB.getUsers();
        for(int i = 0; i<users.length; i++){
            System.out.println(users[i].getUsername() + " - " + users[i].isAccountStatus());
        }
    }
    public void viewUserInfo(User u){
        if(u == null){
            System.out.println("User not found.");
            return;
        }

        System.out.println("----- User Information -----");
        System.out.println("Username: " + u.getUsername());
        System.out.println("Name: " + u.getName());
        System.out.println("Email: " + u.getEmail());
        System.out.println("Email Verified: " + u.isEmailVerified());
        System.out.println("Account Active: " + u.isAccountStatus());
        System.out.println("Birthday: " + u.getBirthday());
        System.out.println("City: " + u.getCity());
        System.out.println("Postal Code: " + u.getPostalCode());
        System.out.println("Gender: " + u.getGender());
        System.out.println("Preferred Gender: " + u.getPreferredGender());
        System.out.println("Bio: " + (u.getBio() == null ? "N/A" : u.getBio()));
        System.out.println("Matched: " + u.getMatched());
        System.out.println("Number of Matches: " + u.getNumMatches());
    }
}
