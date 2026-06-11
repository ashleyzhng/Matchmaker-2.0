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
                    currentMenu = menu.getMainMenu(sc, system);
                    break;
                case "login":
                    LoginResults loginResults = menu.login(sc, system);
                    currentMenu = loginResults.getNextMenu();
                    currentAccount = loginResults.getUser();
                    break;
                case "user":
                    menu.getUserMenu(currentAccount, sc); //change return type
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
                case "createAccount":
                    currentMenu = menu.getCreateAccountMenu(sc, system);
                    break;
                case "matchMenu":
                    currentMenu = menu.matchMenu(sc);
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