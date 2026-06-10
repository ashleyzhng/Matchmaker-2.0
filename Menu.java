import java.util.Scanner;

public class Menu {
    // all menu/ui methods go here
    public LoginResults login(Scanner sc, Matchmaker mm){
        System.out.println("Enter the following information to login('b' to go back):");
        System.out.print("Username: ");
        String username = sc.next();
        if(username.equals("b")){
            return new LoginResults("main", null);
        }
        while(mm.searchByUsername(username) == null){
            System.out.flush();
            System.out.println("Username not found. Please try again.");
            System.out.print("Username: ");
            username = sc.next();
            if(username.equals("b")){
                return new LoginResults("main", null);
            }
        }
        System.out.print("Password: ");
        String password = sc.next();
        if(password.equals("b")){
            return new LoginResults("main", null);
        }
        while(!mm.searchByUsername(username).correctPass(password)){
            System.out.flush();
            System.out.println("Incorrect password. Please try again.");
            System.out.print("Password: ");
            password = sc.next();
            if(password.equals("b")){
                return new LoginResults("main", null);
            }
        }
        LoginResults results = new LoginResults("login", mm.searchByUsername(username));
        return results;
    }

    public String getMainMenu(Scanner sc){
        System.out.println("Welcome to Matchmaker!");
        System.out.println("=======================");
        System.out.println("1. Login");
        System.out.println("2. Reset Password");
        System.out.println("3. Create Account");
        System.out.println("4. Exit Program");
        System.out.println("=======================");
        System.out.print("OPTION: ");
        int choice = sc.nextInt();
        if(choice == 1){
            return "login";
        }else if(choice == 2){
            return "resetPassword";
        }else if(choice == 3){
            return "createAccount";
        }else if(choice == 4){
            return "exit";
        }
        return "";
    }

    public void getAccountCreationMenu(){
        System.out.println("=======================");
        System.out.println("Pick Account Creation Method: ");
        System.out.println("1. Load From File");
        System.out.println("2. User Account");
        System.out.println("3. Parent Account");
        System.out.println("=======================");
        System.out.print("OPTION: ");
    }

    public String getUserMenu(User u, Scanner sc){
        System.out.println("Welcome, " + u.getName() + "!");
        System.out.println("=======================");
        System.out.println("1. Match!");
        System.out.println("2. View current Matches (" + u.getMatches().length + " matches)");
        System.out.println("3. View Match History");
        System.out.println("4. Host Event");
        System.out.println("5. View Event History");
        System.out.println("6. View Hosted Events");
        System.out.println("7. View Profile");
        System.out.println("8. Account Settings");
        System.out.println("9. Logout");
        System.out.println("=======================");
        System.out.print("OPTION: ");
        int choice = sc.nextInt();
            while(choice < 1 || choice > 9){
                System.out.println("Invalid option. Please try again.");
                System.out.print("OPTION: ");
                choice = sc.nextInt();
            }
        if(choice == 1){
            // ASHLEY - CALL MATCH STUFF
        }else if(choice == 2){
            // ASHLEY - views current matches and approves/denies matches
        }else if(choice == 3){
            System.out.flush();
            u.printMatchHistory();
            System.out.print("Enter 'b' to go back: ");
            String input = sc.next();
            while(!input.equals("b")){
                System.out.print("Invalid input. Please try again: ");
                input = sc.next();
            }
            return "user";
        }else if(choice == 4){
            // KATHY - hosting event
        }else if(choice == 5){
            System.out.flush();
            u.printEventHistory();
            System.out.print("Enter 'b' to go back: ");
            String input = sc.next();
            while(!input.equals("b")){
                System.out.print("Invalid input. Please try again: ");
                input = sc.next();
            }
            return "user";
        }else if(choice == 6){
             System.out.flush();
            u.printHostedEvents();
            System.out.print("Enter 'b' to go back: ");
            String input = sc.next();
            while(!input.equals("b")){
                System.out.print("Invalid input. Please try again: ");
                input = sc.next();
            }
            return "user";
        }else if(choice == 7){
            System.out.flush();
            u.printProfile();
            System.out.print("Enter 'b' to go back: ");
            String input = sc.next();
            while(!input.equals("b")){
                System.out.print("Invalid input. Please try again: ");
                input = sc.next();
            }
            return "user";
        }else if(choice == 8){
            // account settings
            System.out.flush();
            return "accountSettings";                   
        }else if(choice == 9){
            System.out.flush();
            System.out.println("Logout successful.");
            return "logout";
        }
        return "";
    }

    public void getAccountSettingsMenu(){
        System.out.println("=======================");
        System.out.println("Account Settings: ");
        System.out.println("1. Change Username");
        System.out.println("2. Change Password");
        System.out.println("3. Verify Email");
        System.out.println("4. Inactivate Account");
        System.out.println("5. Delete Account");
        System.out.println("6. Other Settings");
        System.out.println("=======================");
        System.out.print("OPTION: ");
    }

    public String getAdminMenu(){
        System.out.println("=======================");
        System.out.println("Admin Menu: ");
        System.out.println("1. View All Users");
        System.out.println("2. View User Info");
        System.out.println("3. View All Appeals");
        System.out.println("4. Promote User to Admin");
        System.out.println("=======================");
        System.out.print("OPTION: ");
        return "";
    }

    public String getParentMenu(){
        System.out.println("=======================");
        System.out.println("Parent Menu: ");
        System.out.println("1. View Child's Matches");
        System.out.println("2. View Child's Match History");
        System.out.println("3. View Child's Event History");
        System.out.println("4. View Child's Hosted Events");
        System.out.println("5. View Child's Profile");
        System.out.println("=======================");
        System.out.print("OPTION: ");
        return "";
    }

    public String getResetPasswordMenu(Scanner sc, Matchmaker mm){
        System.out.println("=======================");
        System.out.println("ENTER: ");
        System.out.print("Username: ");
        String username = sc.next();
        System.out.print("Email: ");
        String email = sc.next();
        User u = mm.searchByUsername(username);
        if(u != null && u.getEmail().equals(email)){
            if(u.isEmailVerified()){
                System.out.println("Enter new password: ");
                String newPass = sc.next();
                u.setPassword(newPass);
                System.out.println("Password reset successful. Your new password is '" + newPass + "'. Please try logging in.");
                return "main";
            }else{
                System.out.println("Email not verified. Please verify your email before resetting password.");
                return "main";
            }
        }
        return "";
    }
}
