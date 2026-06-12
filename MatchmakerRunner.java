import java.util.*;

public class MatchmakerRunner{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Matchmaker system = new Matchmaker();
        Menu menu = new Menu();

        boolean running = true;
        String currentMenu = "main";
        Account currentAccount = null;

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
                    menu.getUserMenu((User)currentAccount, sc, system); //change return type
                    break;
                case "admin":
                    currentMenu = menu.getAdminMenu(sc, system, (Admin)currentAccount); 
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