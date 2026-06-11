import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Menu {
    // all menu/ui methods go here
    public LoginResults login(Scanner sc, Matchmaker mm){
        System.out.flush();
        System.out.println("Enter the following information to login('0' to exit):");

        User user = null;
        String username, password;
        while(user == null){
            System.out.print("Username: ");
            username = sc.next();
            if(username.equals("0")){
                return new LoginResults("main", null);
            }
            user = mm.searchByUsername(username);
            if(user == null){
                System.out.println("Username not found. Please try again.");
            }
        }
        
        while(true){
            System.out.print("Password: ");
            password = sc.next();
            if(password.equals("0")){
                return new LoginResults("main", null);
            }
            if(user.correctPass(password)){
                return new LoginResults("login", user);
            }

            System.out.println("Incorrect password. Please try again.");
        }
    }

    public String getMainMenu(Scanner sc, Matchmaker mm){
        System.out.flush();
        System.out.println("Welcome to Matchmaker!");
        System.out.println("=======================");
        System.out.println("1. Login");
        System.out.println("2. Reset Password");
        System.out.println("3. Create Account");
        System.out.println("4. Exit Program");
        System.out.println("=======================");
        int choice = input(1,4, sc);
        if(choice == 1){
            return "login";
        }else if(choice == 2){
            resetPassword(sc, mm);
            return "main";
        }else if(choice == 3){
            return "createAccount";
        }else if(choice == 4){
            return "exit";
        }
        return "";
    }
    public String getUserMenu(User u, Scanner sc){
        System.out.flush();
        System.out.println("Welcome, " + u.getName() + "!");
        System.out.println("=======================");
        System.out.println("1. Match!"); 
        System.out.println("2. Host Event"); // KATHY - add in the sub options/menu for vewiing event history, and events the user hosted
        System.out.println("3. View Profile");
        System.out.println("4. Account Settings");
        System.out.println("5. Logout");
        System.out.println("=======================");
        int choice = input(1,5, sc);
        if(choice == 1){
            return "match";
        }else if(choice == 2){
            // KATHY - hosting event
        }else if(choice == 3){
            u.printProfile();
            return "user";
        }else if(choice == 4){
            return "accountSettings";  
        }else{
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

    public String resetPassword(Scanner sc, Matchmaker mm){
        System.out.flush();
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

    public String getCreateAccountMenu(Scanner sc, Matchmaker mm){
        System.out.flush();
        System.out.println("=======================");
        System.out.println("Pick Account Creation Method");
        System.out.println("1. Load from File");
        System.out.println("2. User Account");
        System.out.println("3. Parent Account");
        System.out.println("4. GO BACK");
        System.out.println("=======================");
        int choice = input(1,4,sc);
        if(choice == 1){
            System.out.print("Enter file name: ");
            mm.getAccounts().loadFromFile(sc.next(), mm);
            return "createAccount";
        }else if(choice == 2){
            createUser(sc, mm);
            System.out.println("Account Created Succesfully. Please Login to start Matching!");
            return "main";
        }else if(choice == 3){
            //parent acc
        }
        return "main";
    }

    public void createUser(Scanner sc, Matchmaker mm){ 
        System.out.flush();
        System.out.println("ENTER THE FOLLOWING INFORMATION (must complete to return to main menu)" );
        System.out.print("Username: ");
        String username = sc.next();
            boolean userExists = mm.getAccounts().usernameExists(username);
            while(userExists){
                System.out.println("Username already exsits. Please input another username");
                System.out.print("Username: ");
                username = sc.next();
                userExists = mm.getAccounts().usernameExists(username);
            }
        System.out.print("Password: ");
        String password = sc.next();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Name: ");
        String name = sc.nextLine();
        LocalDate birthday = validLocalDate(sc);
        System.out.print("Postal Code: ");
        String postalCode = sc.next();
        System.out.print("City: ");
        String city = sc.nextLine();
        System.out.print("Bio: ");
        String bio = sc.nextLine();
        System.out.print("Gender: (Male - 1 Female - 2 Other - 3) ");
        int choice = input(1,3,sc);
            char gender; 
            if(choice == 1){
                gender = 'M';
            }else if(choice ==2){
                gender = 'F';
            }else{
                gender = 'O';
            }
        System.out.print("Preferred Gender: (Male - 1 Female - 2 Other - 3) ");
        choice = input(1,3,sc);
            char preferredGender;
            if(choice == 1){
                preferredGender = 'M';
            }else if(choice ==2){
                preferredGender = 'F';
            }else{
                preferredGender = 'O';
            }
        int age = Period.between(birthday, LocalDate.now()).getYears();
        if(age > Senior.MIN_AGE){
            System.out.println("Occupation: ");
            String occupation = sc.nextLine(); 
            System.out.print("Marital status: ");
            String maritalStatus = sc.nextLine();
            System.out.print("maximum preferred age (max: 100");
            int maxAge = input(Adult.MIN_AGE, 100, sc);
            System.out.print("minimum preferred age (min: " + Adult.MIN_AGE);
            int minAge = input(Adult.MIN_AGE, 100, sc);
            System.out.print("mobility status, invalid input will be treated as false (true/false): ");
            boolean mobilityStatus = Boolean.parseBoolean(sc.nextLine()); 
            Tag interestTag = tagMenu(sc);
            mm.getAccounts().addUser(new Senior(username, password, email, name, birthday, postalCode, city, bio, gender, preferredGender, occupation, maritalStatus, maxAge, minAge, mobilityStatus, interestTag));
        }else if(age > Adult.MIN_AGE){
            System.out.println("Occupation: ");
            String occupation = sc.nextLine(); 
            System.out.print("Marital status: ");
            String maritalStatus = sc.nextLine();
            System.out.print("maximum preferred age (max: 100");
            int maxAge = input(Adult.MIN_AGE, 100, sc);
            System.out.print("minimum preferred age (min: " + Adult.MIN_AGE);
            int minAge = input(Adult.MIN_AGE, 100, sc);
            Tag interestTag = tagMenu(sc);
            mm.getAccounts().addUser(new Adult(username, password, email, name, birthday, postalCode, city, bio, gender, preferredGender, occupation, maritalStatus, maxAge, minAge, interestTag));
        }else if(age > Teen.MIN_AGE){
            Tag interestTag = tagMenu(sc);
            mm.getAccounts().addUser(new Teen(username, password, email, name, birthday, postalCode, city, bio, gender, preferredGender, interestTag));
        }else{
            System.out.println("You are too young to use the service. Minmium age is 13");
        }
    }

    public Tag tagMenu(Scanner sc){
        int religion = 0, sports = 0, music = 0, art = 0, traveling = 0, fitness = 0, gaming = 0, cooking = 0, animal = 0, reading = 0;
        // prompts
        System.out.print("Are you interested in religion? Enter Yes - 1, No - 2): ");
        int choice = input(1,2,sc);
        if(choice == 1){ 
            religion++;
        }
        System.out.print("Are you interested in sports? Enter Yes - 1, No - 2: ");
        choice = input(1,2,sc);
        if(choice == 1){
            sports++;
        }
        System.out.print("Are you interested in music? Enter Yes - 1, No - 2: ");
        choice = input(1,2,sc);
        if(choice == 1){
            music++;
        }
        System.out.print("Are you interested in art? Enter Yes - 1, No - 2: ");
        choice = input(1,2,sc);
        if(choice == 1){
            art++;
        }
        System.out.print("Are you interested in traveling? Enter Yes - 1, No - 2: ");
        choice = input(1,2,sc);
        if(choice == 1){
            traveling++;
        }
        System.out.print("Are you interested in fitness? Enter Yes - 1, No - 2: ");
        choice = input(1,2,sc);
        if(choice == 1){
            fitness++;
        }
        System.out.print("Are you interested in gaming? Enter Yes - 1, No - 2: ");
        choice = input(1,2,sc);
        if(choice == 1){
            gaming++;
        }
        System.out.print("Are you interested in cooking? Enter Yes - 1, No - 2: ");
        choice = input(1,2,sc);
        if(choice == 1){
            cooking++;
        }
        System.out.print("Are you interested in animals? Enter Yes - 1, No - 2: ");
        choice = input(1,2,sc);
        if(choice == 1){
            animal++;
        }
        System.out.print("Are you interested in reading? Enter Yes - 1, No - 2: ");
        choice = input(1,2,sc);
        if(choice == 1){
            reading++;
        }
        return new Tag(religion, sports, music, art, traveling, fitness, gaming, cooking, animal, reading);
    }

    public String newMatchMenu(Scanner sc, User user, Matchmaker mm){
        System.out.println("=======================");
        System.out.println("New Match Menu: ");
        System.out.println("1. Interest Match");
        System.out.println("2. Distance Match");
        System.out.println("3. Single Date Match");
        System.out.println("4. Double Date Match");
        System.out.println("Please note: Double dates have a much lower success rate!");
        System.out.println("5. Exit");
        System.out.println("=======================");
        System.out.print("OPTION: ");
        int choice = sc.nextInt();
        while(choice < 1 || choice > 4){
            System.out.println("Invalid option. Please try again.");
                System.out.print("OPTION: ");
                choice = sc.nextInt();
        }
        if (choice >= 1 && choice <= 3){
           System.out.println(mm.generateMatch(user, choice));

        } 
        return "matchMenu";
    }
    public String matchMenu(Scanner sc){
        System.out.println("=======================");
        System.out.println("Match Menu: ");
        System.out.println("1. View Current Matches"); // ASHLEY - can u add this option in ur thing: View Match History (there should be a user method for it)
        System.out.println("2. Create New Match");
        System.out.println("3. Exit");
        System.out.println("=======================");
        System.out.print("OPTION: ");
        int choice = sc.nextInt();
        while (choice < 1 || choice > 3){
            System.out.println("Invalid option. Please try again.");
            System.out.print("OPTION: ");
            choice = sc.nextInt();
        }
        if(choice == 1){
            return "currentMatchMenu";
        }else if (choice == 2){
            return "mainMatchMenu";
        }else{
            return "user";
        }
    }
    public String currentMatchMenu(Scanner sc, User user){
         System.out.println("=======================");
        System.out.println("Current Matches Menu: ");
        int matchNum = user.getMatches().length;
        Match[] match = user.getMatches();
        System.out.println("You have " + matchNum + " active Matches!");
        for (int i = 0; i < matchNum ; i ++){
            if (match[i] instanceof InterestMatch){
                System.out.println((i+1) + ". InterestMatch: Expiry Date is " + ((InterestMatch)match[i]).getExpiryDate());
            }else if (match[i] instanceof DistanceMatch){
                System.out.println((i+1) + ". DistanceMatch: Expiry Date is " + ((DistanceMatch)match[i]).getExpiryDate());
            }else if (match[i] instanceof SingleEventMatch){
                EventMatch eMatch = (SingleEventMatch)match[i];
                System.out.println( (i+1) + ". EventMatch -  Event Date:" + eMatch.getDate() + ", Start Time: " + eMatch.getStart() + ", Duration: " + eMatch.getDuration()); 
            }else{
               EventMatch eMatch = (DoubleEventMatch)match[i];
                System.out.println( (i+1) + ". EventMatch -  Event Date:" + eMatch.getDate() + ", Start Time: " + eMatch.getStart() + ", Duration: " + eMatch.getDuration()); 
            }
        }
        System.out.println((match.length + 1) + " Exit");
        System.out.print("OPTION: ");
        int choice = sc.nextInt();
        while(choice < 1 && choice > match.length){
            System.out.println("Invalid option. Please try again.");
            System.out.print("OPTION: ");
            choice = sc.nextInt();
        }
        if(choice == match.length + 1){
            return singleMatchMenu(match[choice-1], sc, user);
        }
        return "matchMenu";
    }

    public String singleMatchMenu(Match match, Scanner sc, User user){
        if (match == null){
            System.out.println("Match not available anymore!");
            return "matchMenu";
        }
        System.out.println("=======================");
        System.out.println(match);
        System.out.println("1. Accept  Match");
        System.out.println("2. Reject Match");
        System.out.println("3. Exit");
        System.out.println("=======================");
        System.out.print("OPTION: ");
        int choice = sc.nextInt();
        while(choice <1 || choice > 3){
            System.out.println("Invalid option. Please try again.");
            System.out.print("OPTION: ");
            choice = sc.nextInt();
        }
        if (choice == 1){
            if (match instanceof  PersonMatch){
               ((PersonMatch)match).updateUserDecision(user, PersonMatch.ACCEPT);
            }else if (match instanceof SingleEventMatch){
                ((SingleEventMatch)match).updateUserDecision(PersonMatch.ACCEPT);
            }else{
                ((DoubleEventMatch)match).updateUserDecision(user, PersonMatch.ACCEPT);
            }
            System.out.println("Decision Updated");
        }else if (choice == 2){
            if (match instanceof  PersonMatch){
               ((PersonMatch)match).updateUserDecision(user, PersonMatch.REJECT);
            }else if (match instanceof SingleEventMatch){
                ((SingleEventMatch)match).updateUserDecision(PersonMatch.REJECT);
            }else{
                ((DoubleEventMatch)match).updateUserDecision(user, PersonMatch.REJECT);
            }
            System.out.println("Decision Updated");
        }
        return "matchMenu";
    }

    public int input(int firstChoice, int lastChoice, Scanner sc){ // change to menu input
    int choice;
    while(true){
        System.out.print("OPTION: ");
        if(!sc.hasNextInt()){
            System.out.println("Invalid Input. Please try again.");
            sc.next();
        }else{
            choice = sc.nextInt();
            if(choice >= firstChoice && choice <= lastChoice){
                return choice;
            }
        }
    }
    }
    public LocalDate validLocalDate(Scanner sc){
        LocalDate birthday = null;
        while (birthday == null) {
            System.out.print("Birthday (YYYY-MM-DD): ");
            try {
                LocalDate temp = LocalDate.parse(sc.next());
                if(temp.isAfter(LocalDate.now())) {
                    System.out.println("Birthday cannot be in the future.");
                }else{
                    birthday = temp;
                }
            }catch(DateTimeParseException e){
                System.out.println("Invalid date. Please use the format YYYY-MM-DD.");
            }
        }
        return birthday;
    }

    
}
