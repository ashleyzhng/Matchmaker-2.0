public class LoginResults {
    private String nextMenu;
    private Account account;

    public LoginResults(String n, Account a) {
        nextMenu = n;
        account = a;
    }

    public String getNextMenu() {
        return nextMenu;
    }
    public void setNextMenu(String n) {
        nextMenu = n;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account a) {
        account = a;
    }
}
