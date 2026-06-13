/*
   File Name: Account.java
   Name: Joyce Lin
   Class: ICS4U1
   Date: June 2026

   Description:
   The Admin class is an subclass that extends Account. It represents a Admin account type in the matchmaker system.
*/
public class Admin extends Account {
    //constructor for loading from file
    public Admin(String u, String p, String e, boolean ev){
        super(u, p, e, ev);
    }

    /* Displays all users in the system
    * @param User[] the array of users to display
    */
    public void viewAllUsers(User[] users){
        //User[] users = userDB.getUsers();
        for(int i = 0; i<users.length; i++){
            if(users[i] != null){
                System.out.println(users[i].getUsername() + " - " + users[i].getName());
            }
        }
    }
   /* Displays detailed information about a specific user
    * @param User the user whose information will be displayed
    */
    public void viewUserInfo(User u){
        if(u == null || !(u instanceof User)){
            System.out.println("User not found.");
        }else{
            System.out.println(u);
        }
    }
   /* Displays all appeals currently stored in the appeal database
    * @param AppealDatabase the database containing all appeals
    */
    public void viewAllAppeals(AppealDatabase ap){
        for(int i = 0; i<ap.getAppeals().size(); i++){
            System.out.println("#" + (i+1));
            System.out.println(ap.getAppeals().get(i));
        }
    }
   /* Adds an appeal to the appeal database
    * @param Appeal the appeal to add
    * @param AppealDatabase the database to add the appeal to
    */
    public void addAppeal(Appeal a, AppealDatabase ap){
        ap.getAppeals().add(a);
    }
   /* Promotes a user account to an admin account
    * @param User the user to promote
    * @param AccountsDatabase the database containing user accounts
    * @return the newly created admin account
    */
    public Admin promoteUser(User u, AccountsDatabase userDB){
        if(u == null || !(u instanceof User)){
            System.out.println("User not found.");
            return null;
        }else{
            Admin newAdmin = new Admin(u.getUsername(), u.getPassword(), u.getEmail(), u.isEmailVerified()); 
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
