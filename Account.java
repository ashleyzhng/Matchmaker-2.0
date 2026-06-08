public abstract class Account{
    private String username;
    private String password;
    private String email;
    private boolean emailVerified;
    private int verifyCode;
    private boolean accountStatus;

    public Account(String u, String p, String e, int v){
        username = u;
        password = p;
        email = e;
        emailVerified = false;
        accountStatus = true;
    }
    
    public Account(String u, String p, String e, boolean ev, int v){
        username = u;
        password = p;
        email = e;
        emailVerified = ev;
        accountStatus = true;
        if(emailVerified){
            verifyCode = (int)(Math.random() * 1000000);
        }
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

    public int getVerifyCode(){
        return verifyCode;
    }

    public void setVerifyCode(int vc){
        verifyCode = vc;
    }

    public boolean isAccountStatus(){
        return accountStatus;
    }

    public void setAccountStatus(boolean as){
        accountStatus = as;
    }

    public boolean correctPass(String pass){
        if(pass == this.password){
            return true;
        }else{
            return false;
        }
    }

    public String toString(){
        return "Username: " + username + "\nEmail: " + email + "\nEmail Verified: " + emailVerified + "\nAccount Status: " + accountStatus;
    }
}