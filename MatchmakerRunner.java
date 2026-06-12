import java.util.*;

public class MatchmakerRunner{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Matchmaker system = new Matchmaker();
        //Matchmaker system = new Matchmaker(file.txt);
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
                    //((User)currentAccount).updateMatches();
                    currentMenu = menu.getUserMenu((User)currentAccount, sc, system); //changes!!
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