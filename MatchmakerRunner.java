import java.util.*;
/**
 * ----------------------------------------------------------------------------
 * File Name:    MatchmakerRunner.java
 * Authors:      Ashley Zhong, Joyce Lin, Kathy Wu
 * Course:       ICS4U1
 * Description:  The main entry point for the Matchmaker application. It runs
 * the central execution loop and manages switching between
 * different system menus based on the current program state.
 * ----------------------------------------------------------------------------
 */
public class MatchmakerRunner{
    /**
     * The main execution method that starts the program.
     * Manages the application state machine loop to route users between menus.
     *
     * @param args The command line arguments passed to the program
     */

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        //core databae
        Matchmaker system = new Matchmaker();
        //Matchmaker system = new Matchmaker(file.txt);
        Menu menu = new Menu();

        //keep track of whether the application should stay open
        boolean running = true;
        //keeps track of which menu screen should be shown next
        String currentMenu = "main";
        //holds profile data of the person who loggin in
        Account currentAccount = null;

        // Continues looping through the application views until the exit choice is picked  
        while(running){
            switch (currentMenu){
                case "main":
                    currentMenu = menu.getMainMenu(sc, system);
                    break;
                case "login":
                    LoginResults loginResults = menu.login(sc, system);
                    currentMenu = loginResults.getNextMenu();
                    currentAccount = loginResults.getAccount();
                    break;
                case "user":
                    ((User)currentAccount).updateMatches();
                    currentMenu = menu.getUserMenu((User)currentAccount, sc, system); 
                    break;
                case "admin":
                    currentMenu = menu.getAdminMenu(sc, system, (Admin)currentAccount); 
                    break;
                case "createAccount":
                    currentMenu = menu.getCreateAccountMenu(sc, system);
                    break;
                case "matchMenu":
                    currentMenu = menu.matchMenu(sc, (User)currentAccount, system);
                    break;
                case "logout":
                    currentAccount = null;
                    currentMenu = "main";
                    break;
                case "currentMatchMenu":
                    currentMenu = menu.currentMatchMenu(sc, (User)currentAccount, system);
                    break;
                case "newMatchMenu":
                    currentMenu = menu.newMatchMenu(sc, (User)currentAccount, system);
                    break;
                case "exit":
                    running = false;
                    break;
            }
        }
    }
}