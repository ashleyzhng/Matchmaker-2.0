public class Admin extends Account {
    //constructs, accessors mutators

    public Admin(String u, String p, String e, boolean ev){
        super(u, p, e, ev);
    }

    public void viewAllUsers(AccountsDatabase userDB){
        User[] users = userDB.getUsers();
        for(int i = 0; i<users.length; i++){
            System.out.println(users[i].getUsername() + " - " + users[i].getName());
        }
    }
    public void viewUserInfo(User u){
        if(u == null || !(u instanceof User)){
            System.out.println("User not found.");
        }else{
            System.out.println(u);
        }
    }

    public void viewAllAppeals(AppealDatabase ap){
        for(int i = 0; i<ap.getAppeals().size(); i++){
            System.out.println("#" + (i+1));
            System.out.println(ap.getAppeals().get(i));
        }
    }
    public void addAppeal(Appeal a, AppealDatabase ap){
        ap.getAppeals().add(a);
    }
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
