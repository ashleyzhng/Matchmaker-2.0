public abstract class Account{
    private String username;
    private String password;
    private String email;
    private boolean emailVerified;

    public Account(String u, String p, String e){
        username = u;
        password = p;
        email = e;
        emailVerified = false;
    }
    
    public Account(String u, String p, String e, boolean ev){
        username = u;
        password = p;
        email = e;
        emailVerified = ev;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String u){
        username = u;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String p){
        password = p;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String e){
        email = e;
    }

    public boolean isEmailVerified(){
        return emailVerified;
    }

    public void setEmailVerified(boolean ev){
        emailVerified = ev;
    }

    public boolean correctPass(String pass){
        if(pass == this.password){
            return true;
        }else{
            return false;
        }
    }

    public String toString(){
        return "Username: " + username + "\nEmail: " + email + "\nEmail Verified: " + emailVerified;
    }
}