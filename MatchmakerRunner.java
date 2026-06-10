import java.util.*;

public class MatchmakerRunner{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Matchmaker system = new Matchmaker();
        Menu menu = new Menu();

        boolean running = true;
        String currentMenu = "main";
        User currentAccount = null;

        while(running){
            switch (currentMenu){
                case "main":
                    currentMenu = menu.getMainMenu(sc);
                    break;
                case "login":
                    LoginResults loginResults = menu.login(sc, system);
                    currentMenu = loginResults.getNextMenu();
                    currentAccount = loginResults.getUser();
                    break;
                case "user":
                    menu.getUserMenu(currentAccount, sc);
                    break;
                case "admin":
                    // do later  
                    break;
                case "parent":
                    // do later
                    break;
                case "accountSettings":
                    // do later
                    break;
                case "resetPassword":
                    // do later
                    break;
                case "createAccount":
                    // do later
                    break;
                case "matchMenu":
                    currentMenu = menu.matchMenu();
                    break;
                case "logout":
                    currentAccount = null;
                    currentMenu = "main";
                    break;
                case "exit":
                    running = false;
                    break;
            }
        }
    }
}