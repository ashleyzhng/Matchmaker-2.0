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

    public boolean changePass(String curPass, String newPass){
        if(correctPass(curPass)){
            this.password = newPass;
            return true;
        }else{
            return false;
        }
    }
    /* 
    public boolean changeUsername(String pass, String newUsername){
        if(correctPass(pass)){ // and check if uesrname exists or not
            /* 
            if(AccountsDatabase.searchByUsername(newUsername) == null){
                this.username = newUsername;
                return true;
            }else{
                
                System.out.println("Username Already Exsits");
                return false;
            }
        }else{
            System.out.println("Incorrect Password");
            return false;
        }
    }
    */
    public boolean resetPass(String email, String newPass){
        if(this.email == email && emailVerified){
            System.out.println(verifyCode);
            //idk how im gonna do this
            return true;
        }else{
            return false;
        }
    }
    public boolean verifyEmail(String pass){
        if(correctPass(pass)){
            emailVerified = true;
            verifyCode = (int)(Math.random() * 1000000);
            return true;
        }else{
            return false;
        }
    }
    public boolean deleteAccount(String pass){
        if(correctPass(pass)){
            // remove account from user database array
            //call logout() method
            return true;
        }else{
            return false;
        }
    }
    public boolean inactivateAccount(String pass){
        if(correctPass(pass)){
            accountStatus = false;
            //cals id.logout()
            return true;
        }else{
            return false;
        }
    }
    public boolean activateAccount(String pass){
        if(correctPass(pass)){
            /*
            if ID in list of accounts
            if status is inactive
            checks if current password matches 
            if matches
            ^^ add all this stuff in a diff method in matchmaker
            */
           accountStatus = true;
           return true;
        }else{
            return false;
        }
    }
    public String toString(){
        return "Username: " + username;
    }
}