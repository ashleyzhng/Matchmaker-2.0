public class LoginResults {
    private String nextMenu;
    User user;

    public LoginResults(String n, User u) {
        nextMenu = n;
        user = u;
    }

    public String getNextMenu() {
        return nextMenu;
    }
    public void setNextMenu(String n) {
        nextMenu = n;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User u) {
        user = u;
    }
}
