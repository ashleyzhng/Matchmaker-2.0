import java.io.*;
import java.util.*;
/**
 * ----------------------------------------------------------------------------
 * File Name:    Preferences.java
 * Authors:      Ashley Zhong
 * Course:       ICS4U1
 * Description:  Remembers the theme and avatar each user picked on the
 *               website. Saved in preferences.txt, one user per line:
 *               <username> <theme> <avatar>
 * ----------------------------------------------------------------------------
 */
public class Preferences {
    public static final String FILE = "preferences.txt";
    public static final String[] THEMES = {"blush", "powder-blue", "sage", "lavender", "midnight"};
    public static final String[] THEME_NAMES = {"Blush", "Powder Blue", "Sage", "Lavender", "Midnight"};
    public static final String[] AVATARS = {"hungry_baby", "cupcake", "love_letter", "bunny", "panda", "painter", "puppy", "ukulele", "gardener"};
    public static final String[] AVATAR_NAMES = {"hungry baby", "cupcake cherub", "love letter", "bunny", "panda", "painter", "puppy hugs", "ukulele", "gardener"};

    // username -> {theme, avatar}
    private HashMap<String, String[]> prefs;

    /**
     * Creates the preferences and loads any that were saved before.
     */
    public Preferences(){
        prefs = new HashMap<String, String[]>();
        load();
    }

    /**
     * Reads preferences.txt if it exists.
     */
    public void load(){
        File f = new File(FILE);
        if(!f.exists()){
            return;
        }
        try{
            BufferedReader in = new BufferedReader(new FileReader(f));
            String line = in.readLine();
            while(line != null){
                String[] parts = line.trim().split(" ");
                if(parts.length == 3){
                    prefs.put(parts[0], new String[]{parts[1], parts[2]});
                }
                line = in.readLine();
            }
            in.close();
        }catch(IOException e){
            System.out.println("Error reading " + FILE);
        }
    }

    /**
     * Writes every user's preferences to preferences.txt.
     */
    public void save(){
        try{
            PrintWriter out = new PrintWriter(new FileWriter(FILE));
            for(String user : prefs.keySet()){
                String[] p = prefs.get(user);
                out.println(user + " " + p[0] + " " + p[1]);
            }
            out.close();
        }catch(IOException e){
            System.out.println("Error saving " + FILE);
        }
    }

    /**
     * Gets a user's theme, or blush if they never picked one.
     *
     * @param username The user's username
     * @return         The theme name used in the CSS (for example "sage")
     */
    public String getTheme(String username){
        if(username != null && prefs.containsKey(username)){
            return prefs.get(username)[0];
        }
        return THEMES[0];
    }

    /**
     * Gets a user's avatar. Users who never picked one get one based on
     * their username so it stays the same every time.
     *
     * @param username The user's username
     * @return         The avatar name (for example "panda")
     */
    public String getAvatar(String username){
        if(username != null && prefs.containsKey(username)){
            return prefs.get(username)[1];
        }
        if(username == null){
            return AVATARS[0];
        }
        return AVATARS[Math.abs(username.hashCode()) % AVATARS.length];
    }

    /**
     * Sets a user's theme if it is one of the allowed themes.
     */
    public void setTheme(String username, String theme){
        if(indexOf(THEMES, theme) >= 0){
            prefs.put(username, new String[]{theme, getAvatar(username)});
        }
    }

    /**
     * Sets a user's avatar if it is one of the allowed avatars.
     */
    public void setAvatar(String username, String avatar){
        if(indexOf(AVATARS, avatar) >= 0){
            prefs.put(username, new String[]{getTheme(username), avatar});
        }
    }

    /**
     * Moves a user's preferences to their new username.
     */
    public void rename(String oldName, String newName){
        String[] p = new String[]{getTheme(oldName), getAvatar(oldName)};
        prefs.remove(oldName);
        prefs.put(newName, p);
    }

    /**
     * Finds the position of a value in an array.
     *
     * @return The index, or -1 if it is not there
     */
    public static int indexOf(String[] list, String value){
        for(int i = 0; i < list.length; i++){
            if(list[i].equals(value)){
                return i;
            }
        }
        return -1;
    }
}
