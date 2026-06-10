import java.util.*;

public class MatchmakerRunner{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Matchmaker system = new Matchmaker();

        boolean running = true;
        String currentMenu = "main";
        User currentAccount = null;

        while(running){
            switch (currentMenu){
                case "main":
                    currentMenu = system.getMainMenu(sc);
                    break;
                case "login":
                    LoginResults loginResults = system.login(sc);
                    currentMenu = loginResults.getNextMenu();
                    currentAccount = loginResults.getUser();
                    break;
                case "user":
                    system.getUserMenu(currentAccount, sc);
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