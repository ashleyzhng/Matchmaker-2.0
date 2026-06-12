public class Appeal {
    private User user;
    private String reason;

    public Appeal(User u, String r){
        user = u;
        reason = r;
    }
    // accessers mutators
    public User getUser(){
        return user;
    }
    public void setUser(User u){
        user = u;
    }
    public String getReason(){
        return reason;
    }
    public void setReason(String r){
        reason = r;
    }

    public String toString(){
        return "User: " + user.getUsername() + "\nReason: " + reason;
        // print basic user info from admin command
    }
}
