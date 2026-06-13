import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * ----------------------------------------------------------------------------
 * File Name:    Menu.java
 * Authors:      Ashley Zhong, Joyce Lin
 * Course:       ICS4U1
 * Description:  Handles the User Interface (UI) console menus for the 
 * Matchmaker system. This class manages states for visitor 
 * navigation, user actions (profile updates, matching, and 
 * reporting), admin controls (event approval and appeal resolutions), 
 * and account authentication workflows.
 * ----------------------------------------------------------------------------
 */
public class Menu {
    // Handles event-related UI screens
    EventMenu em = new EventMenu();
    
     /**
     * Handles the login process for users and admins.
     * Asks for a username and password, checks if they are correct, 
     * and lets the person exit back to the main menu if they type '0'.
     *
     * @param sc  The Scanner used to read what the user types
     * @param mm  The Matchmaker system that holds all account records
     * @return    The login results, including the next screen to show and the logged-in account
     */
    public LoginResults login(Scanner sc, Matchmaker mm){
        System.out.flush();
        System.out.println("Enter the following information to login('0' to exit):");

        Account a = null;
        String username, password;
        while(a == null){
            System.out.print("Username: ");
            username = sc.next();
            if(username.equals("0")){
                return new LoginResults("main", null);
            }
            a = mm.searchAccountByUsername(username);
            if(a == null){
                System.out.println("Username not found. Please try again.");
            }
        }
        
        while(true){
            System.out.print("Password: ");
            password = sc.next();
            if(password.equals("0")){
                return new LoginResults("main", null);
            }
            if(a.correctPass(password)){
                if(a instanceof User){
                    return new LoginResults("user", a);
                }else{
                    return new LoginResults("admin", a);
                }
            }

            System.out.println("Incorrect password. Please try again.");
        }
    }
    /**
     * Displays the very first menu of the program.
     * Gives choices to log in, reset a password, create a new account, or exit.
     *
     * @param sc  The Scanner used to read the user's choice
     * @param mm  The Matchmaker system instance
     * @return    A text string telling the program which screen to open next
     */
    public String getMainMenu(Scanner sc, Matchmaker mm){
        flush();
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

    /**
     * Displays the main menu for a regular user after they log in.
     * Lets the user look at matches, host events, view their profile, 
     * change settings, report another user, or log out.
     *
     * @param u   The User who is currently logged in
     * @param sc  The Scanner used to read the user's choice
     * @param mm  The Matchmaker system instance
     * @return    A text string telling the program which screen to open next
     */
    public String getUserMenu(User u, Scanner sc, Matchmaker mm){
        System.out.println("Welcome, " + u.getName() + "!");
        System.out.println("=======================");
        System.out.println("1. Match Menu"); 
        System.out.println("2. Host Event"); // KATHY - add in the sub options/menu for vewiing event history, and events the user hosted
        System.out.println("3. View Profile");
        System.out.println("4. Account Settings");
        System.out.println("5. Report User");
        System.out.println("6. Logout");
        System.out.println("=======================");
        int choice = input(1,6, sc);
        if(choice == 1){
            return matchMenu(sc, u, mm);
        }else if (choice == 2){
            em.getEventScreen(u);
            return "user";
        }else if(choice == 3){
            u.printProfile();
            return "user";
        }else if(choice == 4){
            return getAccountSettingsMenu(sc, u, mm); 
        }else if(choice == 5){
            sc.nextLine();
            System.out.print("Enter username to report: ");
            String username = sc.nextLine();
            User reportUser = null;
                while(!mm.getAccounts().usernameExists(username)){ 
                    System.out.print("User name does not exists. Please try again.");
                    System.out.print("Username: ");
                    username = sc.nextLine();
                } 
                reportUser = mm.getAccounts().searchByUsername(username);
            System.out.print("Enter reason for report (or keep blank): "); 
            String reason = sc.nextLine();
            mm.getAppeals().addAppeal(reportUser, reason);
            return "user";
        }else if (choice == 6){
            System.out.println("Logout successful.");
            mm.saveAccountsToFile("Test.txt");
            return "logout"; 
        }
        return "user";
    }
    /**
     * Displays the account settings menu.
     * Gives choices to edit profile details, verify an email, or delete the account. 
     * It automatically sends users, admins, or parents to the right screen.
     *
     * @param sc  The Scanner used to read the user's choice
     * @param a   The Account that is being changed
     * @param mm  The Matchmaker system instance that saves the files
     * @return    A text string telling the program which screen to open next
     */
    public String getAccountSettingsMenu(Scanner sc, Account a, Matchmaker mm){
        System.out.println("=======================");
        System.out.println("Account Settings: ");
        System.out.println("1. Edit Account Details");
        System.out.println("2. Verify Email");
        System.out.println("3. Delete Account");
        System.out.println("4. GO BACK");
        System.out.println("=======================");
        int choice = input(1,4,sc);
        if(choice == 1){
            if(a instanceof User){
                userChangeDetails((User)a, sc, mm);
                mm.saveAccountsToFile("Test.txt");
                return "user";
            }else if(a instanceof Admin){
                accountChangeDetails(a, sc, mm);
                mm.saveAccountsToFile("Test.txt");
                return "admin";
            }else{
                accountChangeDetails(a, sc, mm);
                mm.saveAccountsToFile("Test.txt");
                return "parent";
            }
        }else if(choice == 2){
            if(a.isEmailVerified()){
                System.out.println("Email already verified.");
            }else{
                a.setEmailVerified(true);
                System.out.println("Email verified.");
            }
            mm.saveAccountsToFile("Test.txt");
            return "user";
        }else if(choice == 3){
            System.out.print("Enter Password to delete account: ");
            String pass = sc.nextLine();
            System.out.println("Account Deleted Succesfully. Logging out.");
            mm.getAccounts().deleteAccount(a, pass);
            mm.saveAccountsToFile("Test.txt");
            return "logout";
        }
        return "user";
    }

    /**
     * Shows a list of details a User can change on their profile.
     * It adds extra options if the user is an Adult or a Senior, 
     * then sends their choice to the actual profile editor.
     *
     * @param u   The User whose profile details are being changed
     * @param sc  The Scanner used to read the user's choice
     * @param mm  The Matchmaker system instance
     */
    public void userChangeDetails(User u, Scanner sc, Matchmaker mm){
        System.out.println("=======================");
        System.out.println("Choose one of the following to change");
        System.out.println("1. Username");
        System.out.println("2. Password");
        System.out.println("3. Email");
        System.out.println("4. name");
        System.out.println("5. birthday");
        System.out.println("6. postalCode");
        System.out.println("7. city");
        System.out.println("8. bio");
        System.out.println("9. gender");
        System.out.println("10. preferredGender");
        int choice;
        if(u instanceof Adult){
            System.out.println("11. occupation");
            System.out.println("12. marital status");
            System.out.println("13. preferred max age");
            System.out.println("14. preferred min age");
            System.out.println("=======================");
            choice = input(1,14,sc);
        }else if(u instanceof Senior){
            System.out.println("11. occupation");
            System.out.println("12. marital status");
            System.out.println("13. preferred max age");
            System.out.println("14. preferred min age");
            System.out.println("15. mobility limitations");
            System.out.println("=======================");
            choice = input(1,15,sc);
        }else{
            System.out.println("=======================");
            choice = input(1,10,sc);
        }
        editProfile(choice, u, sc, mm);
    }

    /**
     * Shows a list of basic details a generic Account can change.
     * Limits the choices to just Username, Password, and Email (used for Admins or Parents).
     *
     * @param a   The Account being changed
     * @param sc  The Scanner used to read the user's choice
     * @param mm  The Matchmaker system instance
     */
    public void accountChangeDetails(Account a, Scanner sc, Matchmaker mm){
        System.out.println("=======================");
        System.out.println("Choose one of the following to change");
        System.out.println("1. Username");
        System.out.println("2. Password");
        System.out.println("3. Email");
        System.out.println("=======================");
        int choice = input(1,3,sc);
        editProfile(choice, a, sc, mm);
    }

    /**
     * Changes a specific piece of information on a profile based on the user's menu choice.
     * It handles typing in new text, making sure usernames aren't already taken, 
     * setting genders, and updating specific Adult/Senior settings.
     *
     * @param choice  The number of the menu option the user wants to change
     * @param a       The Account or User profile being edited
     * @param sc      The Scanner used to read the new information typed by the user
     * @param mm      The Matchmaker system instance
     */
    public void editProfile(int choice, Account a, Scanner sc, Matchmaker mm){
        User u = null;
        if(a instanceof User){
            u = (User)a;
        }
        switch(choice) {
            case 1:
                System.out.print("Enter new username: ");
                String username = sc.nextLine();
                boolean userExists = mm.getAccounts().usernameExists(username);
                    while(userExists){
                        System.out.println("Username already exsits. Please input another username");
                        System.out.print("Username: ");
                        username = sc.next();
                        userExists = mm.getAccounts().usernameExists(username);
                    }
                a.setUsername(sc.nextLine());
                break;

            case 2:
                System.out.print("Enter new password: ");
                a.setPassword(sc.nextLine());
                break;

            case 3:
                System.out.print("Enter new email: ");
                a.setEmail(sc.nextLine());
                System.out.print("Email needs to be re-verified");
                a.setEmailVerified(false);
                break;

            case 4:
                System.out.print("Enter new name: ");
                u.setName(sc.nextLine());
                break;

            case 5:
                u.setBirthday(validLocalDate(sc)); 
                break;

            case 6:
                System.out.print("Enter new postal code: ");
                u.setPostalCode(sc.nextLine());
                break;

            case 7:
                System.out.print("Enter new city: ");
                u.setCity(sc.nextLine());
                break;

            case 8:
                System.out.print("Enter new bio: ");
                u.setBio(sc.nextLine());
                break;

            case 9:
                System.out.print("Enter new Gender: (Male - 1 Female - 2)");
                int choice2 = input(1,2,sc);
                    if(choice2 == 1){
                        u.setGender('M');
                    }else{
                        u.setGender('F');
                    }
                break;

            case 10:
                System.out.print("Enter new preferred gender: (Male - 1 Female - 2 Other - 3) ");
                choice2 = input(1,3,sc);
                    if(choice2 == 1){
                        u.setPreferredGender('M');
                    }else{
                        u.setPreferredGender('F');
                    }
                break;

            case 11:
                System.out.print("Enter occupation: ");
                if(u instanceof Adult){
                    ((Adult)u).setOccupation(sc.nextLine());
                }else{
                    ((Senior)u).setOccupation(sc.nextLine());
                }
                break;

            case 12:
                System.out.print("Enter marital status: ");
                if(u instanceof Adult){
                    ((Adult)u).setMaritalStatus(sc.nextLine());
                }else{
                    ((Senior)u).setMaritalStatus(sc.nextLine());
                }
                break;

            case 13:
                System.out.print("Enter maximum preferred age (max: 100)");
                choice2 = input(Adult.MIN_AGE, 100, sc);
                if(u instanceof Adult){
                    ((Adult)u).setPreferredMaxAge(choice2);
                }else{
                    ((Senior)u).setPreferredMaxAge(choice2);
                }
                sc.nextLine();
                break;

            case 14:
                System.out.print("Enter minimum preferred age (min: " + Adult.MIN_AGE);
                choice2 = input(Adult.MIN_AGE, 100, sc);
                if(u instanceof Adult){
                    ((Adult)u).setPreferredMinAge(choice2);
                }else{
                    ((Senior)u).setPreferredMinAge(choice2);
                }
                sc.nextLine();
                break;

            case 15:
                System.out.print("Enter mobility limitations (true/false): ");
                ((Senior)u).setHasMobilityLimitations(Boolean.parseBoolean(sc.nextLine()));
                break;
        }
    }

    /**
     * Displays the menu for system Administrators.
     * Gives admins the tools to view users, check reports (appeals), 
     * delete bad accounts, and approve or review community events.
     *
     * @param sc  The Scanner used to read the admin's choice
     * @param mm  The Matchmaker system instance
     * @param ad  The Admin who is currently logged in
     * @return    A text string telling the program which screen to open next
     */
    public String getAdminMenu(Scanner sc, Matchmaker mm, Admin ad){
        System.out.println("=======================");
        System.out.println("Admin Menu: ");
        System.out.println("1. View All Users");
        System.out.println("2. View User Info");
        System.out.println("3. View All Appeals");
        System.out.println("4. View All Events");
        System.out.println("5. Approve pending events");
        System.out.println("6. Promote User to Admin");
        System.out.println("7. Logout");
        System.out.println("=======================");
        int choice = input(1,7,sc);
        if(choice == 1){
            ad.viewAllUsers(mm.getAccounts().sortByUserType(mm.getAccounts().getAccounts()));
            return "admin";
        }else if(choice == 2){
            System.out.print("Enter username: ");
            sc.nextLine();
            String username = sc.nextLine(); 
            ad.viewUserInfo(mm.getAccounts().searchByUsername(username));
            return "admin";
        }else if(choice == 3) {
            ad.viewAllAppeals(mm.getAppeals());
            System.out.println("(Delete User)Resolve Appeal #: ");
            int idx = input(1,mm.getAppeals().getAppeals().size(),sc);
            idx--;
            User d = mm.getAppeals().getAppeals().get(idx).getUser();
            mm.getAccounts().deleteAccount(d, d.getPassword());
            mm.saveAccountsToFile("Test.txt");
            return "admin";
        } else if (choice == 4) {
            em.adminViewEvents();
            return "admin";
        } else if (choice == 5) {
            em.adminEventReview();
            return "admin";
        }
        return "logout";
    }

    /**
     * Helps a user reset their password if they forgot it.
     * Checks if the username matches the email address and makes sure 
     * the email was verified before allowing the password change.
     *
     * @param sc  The Scanner used to read the user's input
     * @param mm  The Matchmaker system instance
     * @return    A text string that sends the user back to the main menu
     */
    public String resetPassword(Scanner sc, Matchmaker mm){
        System.out.println("=======================");
        System.out.println("ENTER the following information");
        System.out.print("Username: ");
        String username = sc.next();
        System.out.print("Email: ");
        String email = sc.next();
        Account u = mm.searchAccountByUsername(username);
        if(u != null && u.getEmail().equals(email)){ // use editProfile  method
            if(u.isEmailVerified()){
                System.out.print("Enter new password: ");
                String newPass = sc.next();
                u.setPassword(newPass);
                System.out.println("Password reset successful. Your new password is '" + newPass + "'. Please try logging in.");
                return "main";
            }else{
                System.out.println("Email not verified. Please verify your email before resetting password.");
                return "main";
            }
        }
        System.out.println("Email entered is Incorrect");
        return "main";
    }
    /**
     * Displays the options for creating a new account.
     * The user can choose to load a batch of accounts from a file 
     * or create a brand new user account by typing in their information.
     *
     * @param sc  The Scanner used to read the user's choice
     * @param mm  The Matchmaker system instance
     * @return    A text string telling the program which screen to open next
     */
    public String getCreateAccountMenu(Scanner sc, Matchmaker mm){
        System.out.flush();
        System.out.println("=======================");
        System.out.println("Pick Account Creation Method");
        System.out.println("1. Load from File");
        System.out.println("2. User Account");
        System.out.println("3. GO BACK");
        System.out.println("=======================");
        int choice = input(1,4,sc);
        if(choice == 1){
            System.out.print("Enter file name: ");
            mm.loadAccountsFromFile(sc.next());
            return "createAccount";
        }else if(choice == 2){
            createUser(sc, mm);
            System.out.println("Account Created Succesfully. Please Login to start Matching!");
            return "main";
        }
        return "main";
    }

    /**
     * Walks a new user through the step-by-step account sign-up process.
     * Checks that the username isn't taken, calculates their age from their birthday, 
     * and automatically creates a Teen, Adult, or Senior account based on how old they are.
     *
     * @param sc  The Scanner used to read all the registration answers
     * @param mm  The Matchmaker system instance where the new account will be saved
     */
    public void createUser(Scanner sc, Matchmaker mm){ 
        System.out.flush();
        System.out.println("ENTER THE FOLLOWING INFORMATION" );
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
        System.out.print("Gender: (Male - 1 Female - 2) ");
        int choice = input(1,2,sc);
            char gender; 
            if(choice == 1){
                gender = 'M';
            }else{
                gender = 'F';
            }
        System.out.print("Preferred Gender: (Male - 1 Female - 2)");
        choice = input(1,2,sc);
            char preferredGender;
            if(choice == 1){
                preferredGender = 'M';
            }else {
                preferredGender = 'F';
            }
        int age = Period.between(birthday, LocalDate.now()).getYears();
        if(age > Senior.MIN_AGE){
            System.out.println("Occupation: ");
            String occupation = sc.nextLine(); 
            System.out.print("Marital status: ");
            String maritalStatus = sc.nextLine();
            System.out.print("maximum preferred age (max: 100)");
            int maxAge = input(Adult.MIN_AGE, 100, sc);
            System.out.print("minimum preferred age (min: " + Adult.MIN_AGE);
            int minAge = input(Adult.MIN_AGE, 100, sc);
            System.out.print("mobility status, invalid input will be treated as false (true/false): ");
            boolean mobilityStatus = Boolean.parseBoolean(sc.nextLine()); 
            Tag interestTag = tagMenu(sc);
            mm.getAccounts().addAccount(new Senior(username, password, email, name, birthday, postalCode, city, bio, gender, preferredGender, occupation, maritalStatus, maxAge, minAge, mobilityStatus, interestTag));
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
            mm.getAccounts().addAccount(new Adult(username, password, email, name, birthday, postalCode, city, bio, gender, preferredGender, occupation, maritalStatus, maxAge, minAge, interestTag));
        }else if(age > Teen.MIN_AGE){
            Tag interestTag = tagMenu(sc);
            mm.getAccounts().addAccount(new Teen(username, password, email, false, name, birthday, postalCode, city, bio, gender, preferredGender, interestTag));
        }else{
            System.out.println("You are too young to use the service. Minmium age is 13");
        }
    }

    /**
     * Asks the user a series of Yes/No questions about their hobbies and interests.
     * Saves these choices into a Tag object so the system can use them to find matches later.
     *
     * @param sc  The Scanner used to read the choices (1 for Yes, 2 for No)
     * @return    A new Tag object filled with the user's hobby preferences
     */
    public Tag tagMenu(Scanner sc){
        int religion = 0, sports = 0, music = 0, art = 0, traveling = 0, fitness = 0, gaming = 0, cooking = 0, animal = 0, reading = 0;
        // prompts
        System.out.println("Information can not be changed later.");
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

    /**
     * Displays the main matching options for a user.
     * Shows how many active matches they have and prevents them from making new ones 
     * if they hit the maximum limit of 5.
     *
     * @param sc  The Scanner used to read the user's choice
     * @param u   The User looking at the match options
     * @param mm  The Matchmaker system instance
     * @return    A text string telling the program which screen to open next
     */
    public String matchMenu(Scanner sc, User u, Matchmaker mm){
        int pending = u.getNumMatches();  
        
        System.out.println("=======================");
        System.out.println("Match Menu: ");
        System.out.println("1. View Current Matches (" + pending + " pending matches!)"); 
        System.out.println("2. Create New Match");
        System.out.println("3. Exit");
        System.out.println("=======================");
        int choice = input(1, 3, sc);
        if(choice == 1){
            currentMatchMenu(sc, u, mm);
            return "currentMatchMenu";
        }else if (choice == 2){
            if(u.getNumMatches() != 5){
                newMatchMenu(sc, u, mm);
                return "newMatchMenu";
            }else{
                System.out.println("You cannot generate New Matches! You already have 5! Finish your existing matches before continuing!");
                matchMenu(sc, u, mm);
                return "matchMenu";
            }
            
        }else if (choice == 3){
            return "user";
        }
        return "user";
    }
    /**
     * A helper method that prints out numbers representing the user's active matches.
     *
     * @param sc    The Scanner instance
     * @param user  The User whose matches are being counted
     * @param mm    The Matchmaker system instance
     * @return      A text string that loops back to the match menu
     */
    public String viewMatches(Scanner sc, User user, Matchmaker mm){
        int numMatch = user.getNumMatches();
        for (int i = 0; i < numMatch; i ++){
            System.out.println(i);
        }
        matchMenu(sc, user, mm);
        return "matchMenu";
    }
    /**
     * Displays options to create a brand new match.
     * Users can choose to match by hobbies (interests), distance, or select single/double date events.
     *
     * @param sc    The Scanner used to read the user's choice
     * @param user  The User requesting a new match
     * @param mm    The Matchmaker system instance that runs the matching algorithm
     * @return      A text string that loops back to the match menu
     */
    public String newMatchMenu(Scanner sc, User user, Matchmaker mm){
        System.out.println("=======================");
        System.out.println("New Match Menu: ");
        System.out.println("1. Interest Match");
        System.out.println("2. Distance Match");
        System.out.println("3. Single Date Match");
        System.out.println("4. Double Date Match");
        System.out.println("Please note: Single Date & Double Dates has a lower success rate!");
        System.out.println("5. Exit");
        System.out.println("=======================");
        int choice = input(1, 5, sc);
            if(choice >= 1 && choice <= 4){
            Match m = mm.generateMatch(user, choice);
            if (m != null){
                if (m instanceof EventMatch && ((EventMatch)m).getEvent() == null){
                    System.out.println("Match generate is not compatible, therefore not saved");
                }else{
                    System.out.println("Match generated and stored successfully!");
                }
                System.out.println(m);
            } else {
                System.out.println("No match could be generated due to incompatibility");
            }
        }
        return matchMenu(sc, user, mm);
    }

    /**
     * Displays a list of all the user's current, active matches.
     * Automatically identifies what kind of match it is to print the correct details, 
     * like expiration dates or event times.
     *
     * @param sc    The Scanner used to read the user's choice
     * @param user  The User looking at their active matches
     * @param mm    The Matchmaker system instance
     * @return      A text string that opens the menu for a single chosen match
     */
    public String currentMatchMenu(Scanner sc, User user, Matchmaker mm){
        System.out.println("=======================");
        System.out.println("Current Matches Menu: ");
        int matchNum = user.getNumMatches();
        Match[] match = user.getMatches();
        System.out.println("You have " + matchNum + " active Matches!");
        for (int i = 0; i < matchNum ; i ++){
            if (match[i] instanceof InterestMatch){
                System.out.println((i+1) + ". InterestMatch: Expiry Date is " + ((InterestMatch)match[i]).getExpiryDate());
            }else if (match[i] instanceof DistanceMatch){
                System.out.println((i+1) + ". DistanceMatch: Expiry Date is " + ((DistanceMatch)match[i]).getExpiryDate());
            }else if (match[i] instanceof SingleEventMatch){
                EventMatch eMatch = (SingleEventMatch)match[i];
                System.out.println( (i+1) + ". EventMatch -  Event Date:" + eMatch.getDate() + ", Start Time: " + eMatch.getStart() + ", End Time: " + eMatch.getEnd()); 
            }else if (match[i] instanceof DoubleEventMatch){
               EventMatch eMatch = (DoubleEventMatch)match[i];
                System.out.println( (i+1) + ". EventMatch -  Event Date:" + eMatch.getDate() + ", Start Time: " + eMatch.getStart() + ", End Time: " + eMatch.getEnd()); 
            }
        }
        System.out.println((matchNum + 1) + " Exit");
        int choice = input(1, matchNum + 1, sc);
        if(choice < matchNum + 1){
            return singleMatchMenu(match[choice-1], sc, user, mm);
        }
        return matchMenu(sc, user, mm);
    }

    /**
     * Displays the details for one specific match and lets the user make a decision.
     * The user can Accept the match, Reject it, or mark it as Completed and give it a rating from 1 to 5.
     *
     * @param match  The specific Match that the user is reviewing
     * @param sc     The Scanner used to read the user's choice and rating
     * @param user   The User interacting with this match
     * @param mm     The Matchmaker system instance
     * @return       A text string that takes the user back to the current matches list
     */
    public String singleMatchMenu(Match match, Scanner sc, User user, Matchmaker mm){
        if (match == null){
            System.out.println("Match not available anymore!");
            return matchMenu(sc, user, mm);
        }
        System.out.println("=======================");
        System.out.println(match);
        System.out.println("1. Accept  Match");
        System.out.println("2. Reject Match");
        System.out.println("3. Complete Match");
        System.out.println("4. Exit");
        System.out.println("=======================");
        int choice = input(1, 4, sc);
        if (choice == 1){
            if (match instanceof  PersonMatch){
               ((PersonMatch)match).updateUserDecision(user, PersonMatch.ACCEPT);
            }else if (match instanceof SingleEventMatch){
                ((SingleEventMatch)match).updateUserDecision(user, PersonMatch.ACCEPT);
            }else{
                ((DoubleEventMatch)match).updateUserDecision(user, PersonMatch.ACCEPT);
            }
            System.out.println("Decision Updated");
        }else if (choice == 2){
            if (match instanceof  PersonMatch){
               ((PersonMatch)match).updateUserDecision(user, PersonMatch.REJECT);
            }else if (match instanceof SingleEventMatch){
                ((SingleEventMatch)match).updateUserDecision(user, PersonMatch.REJECT);
            }else{
                ((DoubleEventMatch)match).updateUserDecision(user, PersonMatch.REJECT);
            }
            System.out.println("Decision Updated");
        }else if (choice == 3){
            match.completed();
            boolean dated = true;
            if (match instanceof SingleEventMatch || match instanceof DoubleEventMatch) {
                em.payForEvent( ((EventMatch)match).getEvent());
                dated = em.dateSim(((EventMatch)match).getEvent(), user);
            } 
            if (dated || match instanceof PersonMatch) {
                System.out.println("Match Completed! Please rate your experience (1-5, 1 being the lowest, 5 being the highest): ");
                int rating = input(1,5,sc);
                if (match instanceof  PersonMatch){
                    ((PersonMatch)match).setRating(rating, user);
                }else if (match instanceof SingleEventMatch){
                    ((SingleEventMatch)match).setRating(user, rating);
                    em.rateEvent(((EventMatch)match).getEvent(), user);
                }else{
                    ((DoubleEventMatch)match).setRating(user, rating);
                    em.rateEvent(((EventMatch)match).getEvent(), user);
                }
                System.out.println("Rating Updated");
            }           
        }else{
            System.out.println("Exiting...");
        }
        return currentMatchMenu(sc, user, mm);
    }

    /**
     * Safely reads a number typed by the user and ensures it falls within a specific range.
     * If the user types letters instead of a number, it catches the error and asks them to try again.
     *
     * @param firstChoice  The lowest valid number the user is allowed to pick
     * @param lastChoice   The highest valid number the user is allowed to pick
     * @param sc           The Scanner used to read the input
     * @return             A validated number that is guaranteed to be within the allowed menu range
     */
    public int input(int firstChoice, int lastChoice, Scanner sc){ // change to menu input
        while(true){
            try {
                System.out.print("OPTION: ");
                int choice = sc.nextInt();
                if(choice < firstChoice || choice > lastChoice){
                    System.out.println("Invalid option. Please try again.");
                }else{
                    return choice;
                }
                
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input. Please try again.");
                sc.next();
            }
        }
    }

    /**
     * Prompts the user to type a date in the YYYY-MM-DD format.
     * Makes sure the text can actually be turned into a real date and checks that it isn't in the future.
     *
     * @param sc  The Scanner used to read the date text
     * @return    A working LocalDate object representing the validated date
     */
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
    
    /**
     * Prints 50 empty lines in the console window to clear old text off the screen.
     */
    public void flush(){
        for(int i = 0; i < 50; i++){
            System.out.println();
        }
    }
}
