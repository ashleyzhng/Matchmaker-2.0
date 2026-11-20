import java.time.*;
import java.time.format.DateTimeFormatter;
/**
 * ----------------------------------------------------------------------------
 * File Name:    Html.java
 * Authors:      Ashley Zhong
 * Course:       ICS4U1
 * Description:  Small helper methods that build the pieces of HTML used on
 *               every web page: the page frame, the sidebar, cards, badges,
 *               avatars and event cards.
 * ----------------------------------------------------------------------------
 */
public class Html {
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("EEE, MMM d");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("h:mm a");

    /**
     * Makes text safe to put inside HTML so names or bios cannot break the page.
     */
    public static String esc(Object o){
        if(o == null){
            return "";
        }
        String s = o.toString();
        s = s.replace("&", "&amp;");
        s = s.replace("<", "&lt;");
        s = s.replace(">", "&gt;");
        s = s.replace("\"", "&quot;");
        s = s.replace("'", "&#39;");
        return s;
    }

    /**
     * Builds a full HTML document.
     *
     * @param title The text in the browser tab
     * @param theme The colour theme (for example "blush")
     * @param body  The HTML inside the body tag
     */
    public static String page(String title, String theme, String body){
        return "<!doctype html>\n<html lang=\"en\"><head><meta charset=\"utf-8\">"
            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
            + "<title>" + esc(title) + " · matchmaker</title>"
            + "<link rel=\"stylesheet\" href=\"/static/style.css\"></head>"
            + "<body class=\"theme-" + esc(theme) + "\">" + body + "</body></html>";
    }

    /**
     * Builds a page with the sidebar on the left and the content on the right.
     *
     * @param a       The logged in account
     * @param theme   The user's theme
     * @param avatar  The user's avatar
     * @param active  The sidebar item to highlight
     * @param flash   A message to show at the top ("ok:..." or "err:..."), or null
     * @param content The main page content
     */
    public static String layout(Account a, String theme, String avatar, String active, String flash, String content){
        String[][] nav;
        if(a instanceof Admin){
            nav = new String[][]{{"Dashboard", "/admin"}, {"Users", "/admin/users"}, {"Reports", "/admin/reports"},
                {"Event Review", "/admin/review"}, {"All Events", "/admin/events"}, {"Settings", "/settings"}};
        }else{
            nav = new String[][]{{"Home", "/home"}, {"My Matches", "/matches"}, {"New Match", "/new-match"}, {"Events", "/events"},
                {"Profile", "/profile"}, {"Settings", "/settings"}, {"Report a User", "/report"}};
        }
        String side = "<aside class=\"sidebar\"><a class=\"logo\" href=\"/\">" + character("brand_cupid", "") + "<span>matchmaker</span></a><nav class=\"nav\">";
        for(int i = 0; i < nav.length; i++){
            side += "<a href=\"" + nav[i][1] + "\"" + (nav[i][0].equals(active) ? " class=\"active\"" : "") + ">" + nav[i][0] + "</a>";
        }
        String name = (a instanceof User) ? ((User)a).getName() : a.getUsername() + " · Admin";
        side += "</nav><div class=\"me\">" + avatar(avatar, "s36") + "<div class=\"who\"><strong>" + esc(name)
            + "</strong><a class=\"small\" href=\"/logout\">Log out</a></div></div></aside>";
        return page(active, theme, "<div class=\"app\">" + side + "<main class=\"main\">" + flash(flash) + content + "</main></div>");
    }

    /**
     * Builds the page heading with the handwritten eyebrow, title and subtitle.
     */
    public static String head(String eyebrow, String title, String sub, String right){
        String s = "<div class=\"page-head\"><div class=\"titles\">";
        if(eyebrow != null){
            s += "<span class=\"script\">" + esc(eyebrow) + "</span>";
        }
        s += "<h1>" + esc(title) + "</h1>";
        if(sub != null){
            s += "<p class=\"muted\">" + esc(sub) + "</p>";
        }
        return s + "</div>" + (right == null ? "" : right) + "</div>";
    }

    /**
     * Shows a green or red message box. Messages start with "ok:" or "err:".
     */
    public static String flash(String f){
        if(f == null || f.isEmpty()){
            return "";
        }
        if(f.startsWith("err:")){
            return "<div class=\"flash err\">" + esc(f.substring(4)) + "</div>";
        }
        return "<div class=\"flash ok\">" + esc(f.startsWith("ok:") ? f.substring(3) : f) + "</div>";
    }

    /**
     * Shows one of the character pictures.
     */
    public static String character(String file, String style){
        return "<img class=\"character\" src=\"/images/" + file + ".png\" alt=\"\" style=\"" + style + "\">";
    }

    /**
     * Shows a round avatar picture.
     *
     * @param key  The avatar name (for example "panda")
     * @param size The CSS size class (s36, s48, s96 or s132)
     */
    public static String avatar(String key, String size){
        return "<img class=\"avatar " + size + "\" src=\"/images/avatar_" + esc(key) + ".png\" alt=\"\">";
    }

    /**
     * Builds a coloured status badge from a match status letter.
     */
    public static String badge(char status){
        if(status == 'A'){
            return "<span class=\"badge accepted\">Accepted</span>";
        }else if(status == 'C'){
            return "<span class=\"badge completed\">Completed</span>";
        }else if(status == 'R'){
            return "<span class=\"badge rejected\">Rejected</span>";
        }
        return "<span class=\"badge pending\">Pending</span>";
    }

    /**
     * Builds a badge with any text and colour (pending, accepted, completed or rejected).
     */
    public static String badge(String kind, String text){
        return "<span class=\"badge " + kind + "\">" + esc(text) + "</span>";
    }

    /**
     * Gets the age group name of a user.
     */
    public static String ageGroup(User u){
        if(u instanceof Teen){
            return "Teen";
        }else if(u instanceof Senior){
            return "Senior";
        }
        return "Adult";
    }

    /**
     * Gets a user's age in years from their birthday.
     */
    public static int age(User u){
        if(u.getBirthday() == null){
            return 0;
        }
        return Period.between(u.getBirthday(), LocalDate.now()).getYears();
    }

    /**
     * Builds the coloured age group pill (Teen, Adult or Senior).
     */
    public static String groupPill(User u, boolean withAge){
        String g = ageGroup(u);
        return "<span class=\"group " + g.toLowerCase() + "\">" + g + (withAge ? " · " + age(u) : "") + "</span>";
    }

    /**
     * Builds a small statistic card.
     */
    public static String stat(String label, String value, String hint){
        return "<div class=\"card stat\"><span class=\"muted small\">" + esc(label) + "</span><span class=\"value\">"
            + esc(value) + "</span><span class=\"hint\">" + esc(hint) + "</span></div>";
    }

    /**
     * Builds a hidden form field.
     */
    public static String hidden(String name, Object value){
        return "<input type=\"hidden\" name=\"" + name + "\" value=\"" + esc(value) + "\">";
    }

    /**
     * Builds a labelled text box.
     */
    public static String input(String label, String name, String type, Object value, String extra){
        return "<label class=\"field\">" + esc(label) + "<input type=\"" + type + "\" name=\"" + name + "\" value=\""
            + esc(value) + "\" " + (extra == null ? "" : extra) + "></label>";
    }

    /**
     * Builds a small form with one button that posts to the given address.
     */
    public static String postButton(String action, String style, String label, String fields){
        return "<form class=\"inline\" method=\"post\" action=\"" + action + "\">" + (fields == null ? "" : fields)
            + "<button class=\"btn " + style + "\">" + esc(label) + "</button></form>";
    }

    /**
     * Gets the type of an event as a word.
     */
    public static String eventKind(Event e){
        if(e instanceof Virtual){
            return "Virtual";
        }else if(e instanceof Outdoor){
            return "Outdoor";
        }
        return "Indoor";
    }

    /**
     * Gets how many people are booked into an event.
     */
    public static int attendees(Event e){
        int n = 0;
        if(e.getCurrentUser1() != null) n++;
        if(e.getCurrentUser2() != null) n++;
        if(e instanceof Indoor){
            if(((Indoor)e).getUser3() != null) n++;
            if(((Indoor)e).getUser4() != null) n++;
        }
        return n;
    }

    /**
     * Gets the most people an event can hold (4 for indoor double dates, 2 otherwise).
     */
    public static int capacity(Event e){
        if(e instanceof Indoor){
            return 4;
        }
        return 2;
    }

    /**
     * Formats a date like "Fri, Nov 20".
     */
    public static String day(LocalDate d){
        if(d == null){
            return "No date";
        }
        return d.format(DAY);
    }

    /**
     * Formats a time like "5:00 PM".
     */
    public static String time(LocalTime t){
        if(t == null){
            return "";
        }
        return t.format(TIME);
    }

    /**
     * Formats a price like "$18.75", or "Free".
     */
    public static String money(double d){
        if(d == 0){
            return "Free";
        }
        return "$" + String.format("%.2f", d);
    }

    /**
     * Gets where an event happens (platform for virtual, location for the others).
     */
    public static String place(Event e){
        if(e instanceof Virtual){
            return ((Virtual)e).getPlatform() + " · ID " + ((Virtual)e).getMeetingID();
        }else if(e instanceof Outdoor){
            return ((Outdoor)e).getLocation();
        }
        return ((Indoor)e).getRoom() + " · " + ((Indoor)e).getLocation();
    }

    /**
     * Builds an event card with its picture, date, place, cost and open spots.
     */
    public static String eventCard(Event e, String footerExtra){
        String kind = eventKind(e);
        boolean dbl = e instanceof Indoor;
        String pic = character(dbl ? "double_date" : "single_date", "");
        int open = capacity(e) - attendees(e);
        return "<div class=\"card event\"><div class=\"banner\"><span class=\"type-chip\">" + kind + " · " + (dbl ? "Double date" : "Single date")
            + "</span>" + pic + "</div><div class=\"body\"><h3><a href=\"/events/view?t=" + url(e.getTitle()) + "\">" + esc(e.getTitle()) + "</a></h3>"
            + "<span class=\"muted small\">" + day(e.getDate()) + " · " + time(e.getStartTime()) + " – " + time(e.getEndTime()) + "</span>"
            + "<span class=\"muted small\">" + esc(place(e)) + "</span>"
            + "<div class=\"foot\"><span class=\"cost\">" + money(e.getOriginalCost()) + "</span><span class=\"spots\">"
            + open + " of " + capacity(e) + " spots open</span></div>" + (footerExtra == null ? "" : footerExtra) + "</div></div>";
    }

    /**
     * Makes text safe to put in a web address.
     */
    public static String url(String s){
        try{
            return java.net.URLEncoder.encode(s, "UTF-8");
        }catch(Exception e){
            return s;
        }
    }

    /**
     * Turns an interest Tag into the list of interest names that are switched on.
     */
    public static String[] interestNames(){
        return new String[]{"Religion", "Sports", "Music", "Art", "Traveling", "Fitness", "Gaming", "Cooking", "Animals", "Reading"};
    }

    /**
     * Gets the 10 interest values from a Tag in the same order as interestNames().
     */
    public static int[] interestValues(Tag t){
        return new int[]{t.getReligion(), t.getSports(), t.getMusic(), t.getArt(), t.getTraveling(),
            t.getFitness(), t.getGaming(), t.getCooking(), t.getAnimal(), t.getReading()};
    }
}
