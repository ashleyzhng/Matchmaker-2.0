import java.util.*;

public class MatchmakerRunner{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Matchmaker system = new Matchmaker();

        boolean running = true;

        while(running){
            system.getMainMenu();
            int response = sc.nextInt();
            String username;
            String password;
            User currentUser = null;

            if(response == 1){
                System.out.println("Enter the following information to login:");
                System.out.print("Username: ");
                username = sc.next();
                System.out.print("Password: ");
                password = sc.next();
                if(system.login(username, password)){
                    currentUser = system.searchByUsername(username);
                    if(currentUser != null){
                        System.out.println("Welcome to " + currentUser.getName() + "!");
                        system.getUserMenu();
                        response = sc.nextInt();
                        System.out.println("under construction");
                    }
                }
            } else if(response == 2){
                system.getAccountCreationMenu();
                response = sc.nextInt();
                if(response == 1){
                    System.out.println("Enter the file name: ");
                    String fileName = sc.next();
                    //system.loadFromFile(fileName);
                } else if(response == 2){
                    System.out.println("Enter the following information to create an account:");
                    System.out.print("Username: ");
                    username = sc.next();
                    System.out.print("Password: ");
                    password = sc.next();
                    System.out.print("Email: ");
                    String email = sc.next();
                    System.out.print("Verification Code: ");
                    int verifyCode = sc.nextInt();
                    System.out.print("Name: ");
                    String name = sc.next();
                    System.out.print("Birthday (YYYY-MM-DD): ");
                    String birthday = sc.next();
                    System.out.print("Postal Code: ");
                    String postalCode = sc.next();
                    System.out.print("City: ");
                    String city = sc.next();
            }
            else if(response == 4){
                running = false;
            }
        }
        
    }
}