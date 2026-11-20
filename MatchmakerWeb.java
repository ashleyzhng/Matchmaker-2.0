import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.*;
import java.util.*;
/**
 * ----------------------------------------------------------------------------
 * File Name:    MatchmakerWeb.java
 * Authors:      Ashley Zhong, Joyce Lin, Kathy Wu
 * Course:       ICS4U1
 * Description:  Runs Matchmaker as a website. It starts a small web server
 *               that comes with Java, then each page in the browser is sent
 *               here. This class reads what the user typed, calls the same
 *               Matchmaker methods the console menus use, saves the files,
 *               and sends back the next page.
 *
 *               How to run:  javac *.java   then   java MatchmakerWeb
 *               then open http://localhost:8080 in a browser.
 * ----------------------------------------------------------------------------
 */
public class MatchmakerWeb implements HttpHandler {
    public static final int PORT = 8080;
    public static final String COOKIE = "mmsession";

    private Matchmaker mm;
    private Preferences prefs;
    private UserPages userPages;
    private AdminPages adminPages;
    // session id -> username of the person logged in
    private HashMap<String, String> sessions;
    // session id -> message to show on the next page
    private HashMap<String, String> flashes;
    // session id -> the match that was just made (shown on the New Match page)
    private HashMap<String, Match> lastMatch;

    /**
     * Starts the website.
     *
     * @param args An optional port number
     */
    public static void main(String[] args) throws IOException {
        int port = PORT;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        Matchmaker system = new Matchmaker();
        if(new File("appeals.txt").exists()){
            system.loadAppealsFromFile("appeals.txt");
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MatchmakerWeb(system));
        server.start();
        System.out.println();
        System.out.println("Matchmaker is running! Open http://localhost:" + port + " in your browser.");
        System.out.println("Press Ctrl+C to stop.");
    }

    /**
     * Creates the web handler for a Matchmaker system.
     */
    public MatchmakerWeb(Matchmaker system){
        mm = system;
        prefs = new Preferences();
        userPages = new UserPages(mm, prefs);
        adminPages = new AdminPages(mm, prefs);
        sessions = new HashMap<String, String>();
        flashes = new HashMap<String, String>();
        lastMatch = new HashMap<String, Match>();
    }

    /**
     * Called by the web server for every page or file the browser asks for.
     */
    public void handle(HttpExchange ex) throws IOException {
        try{
            String path = ex.getRequestURI().getPath();
            if(path.equals("/favicon.ico")){
                // browsers ask for this on their own; answering here keeps it from using up a page message
                ex.getResponseHeaders().set("Location", "/images/brand_cupid.png");
                ex.sendResponseHeaders(302, -1);
                ex.close();
                return;
            }
            if(path.startsWith("/static/") || path.startsWith("/images/") || path.startsWith("/fonts/")){
                sendFile(ex, path);
                return;
            }
            boolean post = ex.getRequestMethod().equalsIgnoreCase("POST");
            HashMap<String, String> params = parse(ex.getRequestURI().getRawQuery());
            if(post){
                String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                params.putAll(parse(body));
            }
            String sid = sessionId(ex);
            Account a = null;
            if(sid != null && sessions.containsKey(sid)){
                a = mm.searchAccountByUsername(sessions.get(sid));
            }
            if(a instanceof User){
                ((User)a).updateMatches();
            }
            route(ex, path, post, params, sid, a);
        }catch(Exception e){
            e.printStackTrace();
            sendHtml(ex, 500, Html.page("Error", "blush", "<div class=\"center-page\"><div class=\"card\"><h1>Something went wrong</h1><p class=\"muted\">"
                + Html.esc(e.toString()) + "</p><a class=\"btn btn-primary\" href=\"/\">Back to start</a></div></div>"));
        }
    }

    /**
     * Sends each web address to the right page or action.
     */
    private void route(HttpExchange ex, String path, boolean post, HashMap<String, String> p, String sid, Account a) throws IOException {
        String flash = takeFlash(sid);

        // ---------- pages anyone can see ----------
        if(path.equals("/")){
            if(a instanceof Admin){ redirect(ex, "/admin", null); return; }
            if(a instanceof User){ redirect(ex, "/home", null); return; }
            sendHtml(ex, 200, userPages.login(flash));
            return;
        }
        if(path.equals("/login") && post){ login(ex, p); return; }
        if(path.equals("/logout")){
            if(sid != null){ sessions.remove(sid); }
            redirect(ex, "/", sid, "ok:You have logged out.");
            return;
        }
        if(path.equals("/reset")){
            if(post){ resetPassword(ex, p, sid); } else { sendHtml(ex, 200, userPages.reset(flash)); }
            return;
        }
        if(path.equals("/signup")){
            if(post){ signup(ex, p, sid); } else { sendHtml(ex, 200, userPages.signup(flash, null)); }
            return;
        }
        if(path.equals("/signup/file") && post){
            String file = p.getOrDefault("file", "").trim();
            if(safeFileName(file) && new File(file).exists()){
                mm.loadAccountsFromFile(file);
                saveAll();
                redirect(ex, "/", sid, "ok:Accounts loaded from " + file + ". You can log in now.");
            }else{
                redirect(ex, "/signup", sid, "err:Could not find a file called " + file + ".");
            }
            return;
        }

        // ---------- everything below needs someone logged in ----------
        if(a == null){
            redirect(ex, "/", sid, "err:Please log in first.");
            return;
        }
        if(path.equals("/events/view")){
            Event e = mm.getEvents().searchEventByTitle(p.getOrDefault("t", ""));
            if(e == null){ redirect(ex, a instanceof Admin ? "/admin/events" : "/events", sid, "err:That event does not exist."); return; }
            sendHtml(ex, 200, userPages.eventView(a, flash, e));
            return;
        }
        if(path.startsWith("/settings")){
            settings(ex, path, post, p, sid, a, flash);
            return;
        }
        if(a instanceof Admin){
            admin(ex, path, post, p, sid, (Admin)a, flash);
            return;
        }
        User u = (User)a;
        if(path.equals("/home")){ sendHtml(ex, 200, userPages.home(u, flash)); return; }
        if(path.equals("/new-match")){
            if(post){ newMatch(ex, p, sid, u); }
            else { sendHtml(ex, 200, userPages.newMatch(u, flash, lastMatch.remove(sid))); }
            return;
        }
        if(path.equals("/matches")){ sendHtml(ex, 200, userPages.matches(u, flash, p.get("tab"))); return; }
        if(path.equals("/matches/decide") && post){ decide(ex, p, sid, u); return; }
        if(path.equals("/matches/complete")){
            int i = toInt(p.get("i"), -1);
            if(i < 0 || i >= u.getNumMatches() || u.getMatches()[i] == null){ redirect(ex, "/matches", sid, "err:That match is not available anymore."); return; }
            if(post){ complete(ex, p, sid, u, i); } else { sendHtml(ex, 200, userPages.complete(u, flash, i)); }
            return;
        }
        if(path.equals("/events")){ sendHtml(ex, 200, userPages.events(u, flash, p)); return; }
        if(path.equals("/events/host")){
            if(post){ hostEvent(ex, p, sid, u, null); }
            else { sendHtml(ex, 200, userPages.hostForm(u, flash, typeOf(p.get("type")), null)); }
            return;
        }
        if(path.equals("/events/edit")){
            Event e = mm.getEvents().searchPendingEventByTitle(p.getOrDefault(post ? "old" : "t", ""));
            if(e == null || e.getCurrentUser1() == null || !e.getCurrentUser1().getUsername().equals(u.getUsername())){
                redirect(ex, "/events", sid, "err:You can only edit your own pending events.");
                return;
            }
            if(post){ hostEvent(ex, p, sid, u, e); } else { sendHtml(ex, 200, userPages.hostForm(u, flash, null, e)); }
            return;
        }
        if(path.equals("/profile")){ sendHtml(ex, 200, userPages.profile(u, flash)); return; }
        if(path.equals("/profile/archive") && post){
            String file = p.getOrDefault("file", "").trim();
            if(!safeFileName(file)){ redirect(ex, "/profile", sid, "err:Use a simple file name like my_events.txt."); return; }
            mm.getEvents().saveAllEvents(file, u.getEventHistory());
            redirect(ex, "/profile", sid, "ok:Event history saved to " + file + ".");
            return;
        }
        if(path.equals("/report")){
            if(post){ report(ex, p, sid, u); } else { sendHtml(ex, 200, userPages.report(u, flash)); }
            return;
        }
        redirect(ex, "/home", sid, "err:That page does not exist.");
    }

    // =====================================================================
    // Login, reset password, sign up
    // =====================================================================

    private void login(HttpExchange ex, HashMap<String, String> p) throws IOException {
        String sid = UUID.randomUUID().toString();
        Account a = mm.searchAccountByUsername(p.getOrDefault("username", "").trim());
        if(a == null){
            redirect(ex, "/", sid, "err:Username not found. Please try again.");
            return;
        }
        if(!a.correctPass(p.get("password"))){
            redirect(ex, "/", sid, "err:Incorrect password. Please try again.");
            return;
        }
        sessions.put(sid, a.getUsername());
        redirect(ex, a instanceof Admin ? "/admin" : "/home", sid);
    }

    private void resetPassword(HttpExchange ex, HashMap<String, String> p, String sid) throws IOException {
        Account a = mm.searchAccountByUsername(p.getOrDefault("username", "").trim());
        if(a == null || !a.getEmail().equals(p.getOrDefault("email", "").trim())){
            redirect(ex, "/reset", sid, "err:The username and email don't match.");
            return;
        }
        if(!a.isEmailVerified()){
            redirect(ex, "/reset", sid, "err:Email not verified. Please verify your email before resetting your password.");
            return;
        }
        a.setPassword(p.get("password"));
        saveAll();
        redirect(ex, "/", sid, "ok:Password reset! You can log in with your new password.");
    }

    private void signup(HttpExchange ex, HashMap<String, String> p, String sid) throws IOException {
        String username = p.getOrDefault("username", "").trim();
        String error = null;
        LocalDate birthday = null;
        try{
            birthday = LocalDate.parse(p.getOrDefault("birthday", ""));
        }catch(Exception e){
            error = "Please enter your birthday.";
        }
        if(username.isEmpty() || username.contains(" ")){
            error = "Usernames can't be blank or have spaces.";
        }else if(mm.getAccounts().usernameExists(username)){
            error = "Username already exists. Please pick another one.";
        }else if(birthday != null && birthday.isAfter(LocalDate.now())){
            error = "Birthday cannot be in the future.";
        }
        int age = birthday == null ? 0 : Period.between(birthday, LocalDate.now()).getYears();
        if(error == null && age < Teen.MIN_AGE){
            error = "You are too young to use the service. Minimum age is 13.";
        }
        int min = toInt(p.get("minAge"), -1);
        int max = toInt(p.get("maxAge"), -1);
        if(error == null && age >= Adult.MIN_AGE && (min < Adult.MIN_AGE || max > 100 || min > max)){
            error = "Please enter the youngest and oldest ages you'd like to match with (18 to 100).";
        }
        if(error != null){
            sendHtml(ex, 200, userPages.signup("err:" + error, p));
            return;
        }
        int[] t = new int[10];
        for(int i = 0; i < 10; i++){
            t[i] = p.containsKey("tag" + i) ? 1 : 0;
        }
        Tag tag = new Tag(t[0], t[1], t[2], t[3], t[4], t[5], t[6], t[7], t[8], t[9]);
        String password = p.getOrDefault("password", "");
        String email = p.getOrDefault("email", "").trim();
        String name = p.getOrDefault("name", "").trim();
        String postal = p.getOrDefault("postal", "").trim().toUpperCase().replace(" ", "");
        String city = p.getOrDefault("city", "").trim();
        String bio = p.getOrDefault("bio", "").trim();
        char gender = p.getOrDefault("gender", "F").charAt(0);
        char pgender = p.getOrDefault("pgender", "M").charAt(0);
        String occupation = p.getOrDefault("occupation", "").trim();
        String marital = p.getOrDefault("marital", "").trim();
        if(age >= Senior.MIN_AGE){
            mm.getAccounts().addAccount(new Senior(username, password, email, name, birthday, postal, city, bio, gender, pgender,
                occupation, marital, max, min, p.containsKey("mobility"), tag));
        }else if(age >= Adult.MIN_AGE){
            mm.getAccounts().addAccount(new Adult(username, password, email, name, birthday, postal, city, bio, gender, pgender,
                occupation, marital, max, min, tag));
        }else{
            mm.getAccounts().addAccount(new Teen(username, password, email, false, name, birthday, postal, city, bio, gender, pgender, tag));
        }
        prefs.setAvatar(username, p.getOrDefault("avatar", "cupcake"));
        saveAll();
        redirect(ex, "/", sid, "ok:Account created successfully. Please log in to start matching!");
    }

    // =====================================================================
    // Matches
    // =====================================================================

    private void newMatch(HttpExchange ex, HashMap<String, String> p, String sid, User u) throws IOException {
        if(u.maxMatches()){
            redirect(ex, "/new-match", sid, "err:You cannot make new matches! You already have 5. Finish your existing matches first.");
            return;
        }
        int type = toInt(p.get("type"), 1);
        Match m = mm.generateMatch(u, type);
        boolean found = false;
        if(m instanceof PersonMatch){
            found = ((PersonMatch)m).getSoulmate() != null;
        }else if(m instanceof EventMatch){
            found = ((EventMatch)m).getEvent() != null;
        }
        if(!found){
            redirect(ex, "/new-match", sid, "err:No compatible match right now. Try another match type, or check back once more people join.");
            return;
        }
        saveAll();
        lastMatch.put(sid, m);
        redirect(ex, "/new-match", sid);
    }

    private void decide(HttpExchange ex, HashMap<String, String> p, String sid, User u) throws IOException {
        int i = toInt(p.get("i"), -1);
        if(i < 0 || i >= u.getNumMatches() || u.getMatches()[i] == null){
            redirect(ex, "/matches", sid, "err:That match is not available anymore.");
            return;
        }
        Match match = u.getMatches()[i];
        char decision = "reject".equals(p.get("action")) ? PersonMatch.REJECT : PersonMatch.ACCEPT;
        if(match instanceof PersonMatch){
            ((PersonMatch)match).updateUserDecision(u, decision);
        }else if(match instanceof SingleEventMatch){
            ((SingleEventMatch)match).updateUserDecision(u, decision);
        }else{
            ((DoubleEventMatch)match).updateUserDecision(u, decision);
        }
        saveAll();
        redirect(ex, "/matches", sid, decision == PersonMatch.ACCEPT ? "ok:Match accepted!" : "ok:Match rejected.");
    }

    private void complete(HttpExchange ex, HashMap<String, String> p, String sid, User u, int i) throws IOException {
        Match match = u.getMatches()[i];
        int rating = toInt(p.get("rating"), 5);
        if(rating < 1 || rating > 5){
            rating = 5;
        }
        String message = "ok:Match completed! You rated it " + rating + " / 5.";
        if(match instanceof EventMatch && ((EventMatch)match).getEvent() != null){
            Event e = ((EventMatch)match).getEvent();
            double amount;
            if(e instanceof Virtual){
                String code = p.getOrDefault("code", "").trim().toUpperCase();
                if(code.isEmpty()){
                    amount = e.getOriginalCost();
                }else{
                    ((Virtual)e).setUsedDiscountCode(code);
                    amount = e.calculateCost();
                }
            }else{
                amount = e.calculateCost();
            }
            double paid = toDouble(p.get("paid"), -1);
            if(paid < amount){
                redirect(ex, "/matches/complete?i=" + i, sid, "err:Payment declined. The event costs " + Html.money(amount)
                    + (paid >= 0 ? " and you are still missing $" + String.format("%.2f", amount - paid) : "") + ".");
                return;
            }
            String change = paid > amount ? " Here's your change: $" + String.format("%.2f", paid - amount) + "." : "";
            match.completed();
            if("N".equals(p.get("continue"))){
                e.cancelEvent();
                mm.getEvents().removeApprovedEvent(e);
                saveAll();
                redirect(ex, "/matches", sid, "ok:Date cancelled." + change);
                return;
            }
            if(match instanceof SingleEventMatch){
                ((SingleEventMatch)match).setRating(u, rating);
            }else{
                ((DoubleEventMatch)match).setRating(u, rating);
            }
            e.attendEvent(u, rating);
            mm.getEvents().removeApprovedEvent(e);
            message += " Enjoy the event!" + change;
        }else{
            match.completed();
            ((PersonMatch)match).setRating(rating, u);
        }
        saveAll();
        redirect(ex, "/matches?tab=history", sid, message);
    }

    // =====================================================================
    // Events
    // =====================================================================

    /**
     * Creates a new pending event, or updates one that is still pending.
     *
     * @param existing The pending event being edited, or null to create a new one
     */
    private void hostEvent(HttpExchange ex, HashMap<String, String> p, String sid, User u, Event existing) throws IOException {
        String type = existing == null ? typeOf(p.get("type")) : Html.eventKind(existing).toLowerCase();
        String back = existing == null ? "/events/host?type=" + type : "/events/edit?t=" + Html.url(existing.getTitle());
        String title = p.getOrDefault("title", "").trim();
        LocalDate date;
        LocalTime start, end;
        double cost;
        try{
            date = LocalDate.parse(p.get("date"));
            start = LocalTime.parse(p.get("start"));
            end = LocalTime.parse(p.get("end"));
            cost = Double.parseDouble(p.get("cost"));
        }catch(Exception e){
            redirect(ex, back, sid, "err:Please fill in the date, times and cost.");
            return;
        }
        String error = null;
        Event sameName = mm.getEvents().searchEventByTitle(title);
        if(title.isEmpty()){
            error = "Please give your event a title.";
        }else if(sameName != null && sameName != existing){
            error = "There is already an event called " + title + ".";
        }else if(date.isBefore(LocalDate.now())){
            error = "Date cannot be earlier than today.";
        }else if(!end.isAfter(start)){
            error = "The event has to end after it starts.";
        }else if(cost < 0){
            error = "Cost cannot be negative.";
        }
        if(error != null){
            redirect(ex, back, sid, "err:" + error);
            return;
        }
        int[] t = new int[10];
        for(int i = 0; i < 10; i++){
            t[i] = p.containsKey("tag" + i) ? 1 : 0;
        }
        if(existing == null){
            Event e;
            if(type.equals("virtual")){
                e = new Virtual(title, date, start, end, cost, t[0], t[1], t[2], t[3], t[4], t[5], t[6], t[7], t[8], t[9], u,
                    p.getOrDefault("platform", "").trim(), toInt(p.get("meetingId"), 0), toInt(p.get("meetingPass"), 0));
            }else if(type.equals("outdoor")){
                e = new Outdoor(title, date, start, end, cost, t[0], t[1], t[2], t[3], t[4], t[5], t[6], t[7], t[8], t[9], u,
                    p.getOrDefault("location", "").trim(), toInt(p.get("temp"), 0), toInt(p.get("precip"), 0));
            }else{
                e = new Indoor(title, date, start, end, cost, t[0], t[1], t[2], t[3], t[4], t[5], t[6], t[7], t[8], t[9], u,
                    p.getOrDefault("location", "").trim(), toInt(p.get("floor"), 0), p.getOrDefault("room", "").trim());
            }
            if(!mm.getEvents().addPendingEvent(e)){
                redirect(ex, back, sid, "err:Reached max capacity for pending events. Try again once more have been approved.");
                return;
            }
            saveAll();
            redirect(ex, "/events", sid, "ok:Event was successfully added. It will show up once an admin approves it.");
            return;
        }
        // editing a pending event
        existing.setTitle(title);
        existing.setDate(date);
        existing.setStartTime(start);
        existing.setEndTime(end);
        existing.setOriginalCost(cost);
        existing.setCategories(new Tag(t[0], t[1], t[2], t[3], t[4], t[5], t[6], t[7], t[8], t[9]));
        if(existing instanceof Virtual){
            ((Virtual)existing).setPlatform(p.getOrDefault("platform", "").trim());
            ((Virtual)existing).setMeetingID(toInt(p.get("meetingId"), 0));
            ((Virtual)existing).setPassword(toInt(p.get("meetingPass"), 0));
        }else if(existing instanceof Outdoor){
            ((Outdoor)existing).setLocation(p.getOrDefault("location", "").trim());
            ((Outdoor)existing).setTemperature(toInt(p.get("temp"), 0));
            ((Outdoor)existing).setPrecipitation(toInt(p.get("precip"), 0));
        }else{
            ((Indoor)existing).setLocation(p.getOrDefault("location", "").trim());
            ((Indoor)existing).setFloorNumber(toInt(p.get("floor"), 0));
            ((Indoor)existing).setRoom(p.getOrDefault("room", "").trim());
        }
        // send it back to the admins for another review
        existing.setEventAdminFeedback(null);
        saveAll();
        redirect(ex, "/events", sid, "ok:Changes saved. Your event is waiting for an admin again.");
    }

    // =====================================================================
    // Settings
    // =====================================================================

    private void settings(HttpExchange ex, String path, boolean post, HashMap<String, String> p, String sid, Account a, String flash) throws IOException {
        if(!post){
            sendHtml(ex, 200, userPages.settings(a, flash));
            return;
        }
        if(path.equals("/settings/theme")){
            prefs.setTheme(a.getUsername(), p.getOrDefault("theme", "blush"));
            prefs.save();
            redirect(ex, "/settings", sid, "ok:Theme saved.");
        }else if(path.equals("/settings/avatar")){
            prefs.setAvatar(a.getUsername(), p.getOrDefault("avatar", "cupcake"));
            prefs.save();
            redirect(ex, "/settings", sid, "ok:Avatar saved.");
        }else if(path.equals("/settings/verify")){
            a.setEmailVerified(true);
            saveAll();
            redirect(ex, "/settings", sid, "ok:Email verified.");
        }else if(path.equals("/settings/delete") && a instanceof User){
            if(mm.getAccounts().deleteAccount(a, p.get("password"))){
                saveAll();
                sessions.remove(sid);
                redirect(ex, "/", sid, "ok:Account deleted successfully.");
            }else{
                redirect(ex, "/settings", sid, "err:Incorrect password. Your account was not deleted.");
            }
        }else if(path.equals("/settings/edit")){
            editDetails(ex, p, sid, a);
        }else{
            redirect(ex, "/settings", sid, null);
        }
    }

    private void editDetails(HttpExchange ex, HashMap<String, String> p, String sid, Account a) throws IOException {
        String username = p.getOrDefault("username", "").trim();
        if(username.isEmpty() || username.contains(" ")){
            redirect(ex, "/settings", sid, "err:Usernames can't be blank or have spaces.");
            return;
        }
        if(!username.equals(a.getUsername()) && mm.getAccounts().usernameExists(username)){
            redirect(ex, "/settings", sid, "err:Username already exists. Please input another username.");
            return;
        }
        LocalDate birthday = null;
        if(a instanceof User){
            try{
                birthday = LocalDate.parse(p.get("birthday"));
            }catch(Exception e){
                redirect(ex, "/settings", sid, "err:Please enter a valid birthday.");
                return;
            }
            if(birthday.isAfter(LocalDate.now())){
                redirect(ex, "/settings", sid, "err:Birthday cannot be in the future.");
                return;
            }
        }
        int min = toInt(p.get("minAge"), -1);
        int max = toInt(p.get("maxAge"), -1);
        if((a instanceof Adult || a instanceof Senior) && (min < Adult.MIN_AGE || max > 100 || min > max)){
            redirect(ex, "/settings", sid, "err:Preferred ages must be between 18 and 100.");
            return;
        }
        if(!username.equals(a.getUsername())){
            prefs.rename(a.getUsername(), username);
            a.setUsername(username);
            sessions.put(sid, username);
        }
        if(!p.getOrDefault("password", "").isEmpty()){
            a.setPassword(p.get("password"));
        }
        String email = p.getOrDefault("email", "").trim();
        if(!email.equals(a.getEmail())){
            a.setEmail(email);
            a.setEmailVerified(false);
        }
        if(a instanceof User){
            User u = (User)a;
            u.setName(p.getOrDefault("name", u.getName()).trim());
            u.setBirthday(birthday);
            u.setPostalCode(p.getOrDefault("postal", u.getPostalCode()).trim().toUpperCase().replace(" ", ""));
            u.setCity(p.getOrDefault("city", u.getCity()).trim());
            u.setBio(p.getOrDefault("bio", "").trim());
            u.setGender(p.getOrDefault("gender", "" + u.getGender()).charAt(0));
            u.setPreferredGender(p.getOrDefault("pgender", "" + u.getPreferredGender()).charAt(0));
            if(u instanceof Adult){
                ((Adult)u).setOccupation(p.getOrDefault("occupation", "").trim());
                ((Adult)u).setMaritalStatus(p.getOrDefault("marital", "").trim());
                ((Adult)u).setPreferredMinAge(min);
                ((Adult)u).setPreferredMaxAge(max);
            }else if(u instanceof Senior){
                ((Senior)u).setOccupation(p.getOrDefault("occupation", "").trim());
                ((Senior)u).setMaritalStatus(p.getOrDefault("marital", "").trim());
                ((Senior)u).setPreferredMinAge(min);
                ((Senior)u).setPreferredMaxAge(max);
                ((Senior)u).setHasMobilityLimitations(p.containsKey("mobility"));
            }
        }
        saveAll();
        prefs.save();
        redirect(ex, "/settings", sid, "ok:Your details were saved.");
    }

    // =====================================================================
    // Reporting
    // =====================================================================

    private void report(HttpExchange ex, HashMap<String, String> p, String sid, User u) throws IOException {
        User target = mm.getAccounts().searchByUsername(p.getOrDefault("username", "").trim());
        if(target == null){
            redirect(ex, "/report", sid, "err:That username does not exist. Please try again.");
            return;
        }
        if(target == u){
            redirect(ex, "/report", sid, "err:You can't report yourself.");
            return;
        }
        mm.getAppeals().addAppeal(target, p.getOrDefault("reason", "").trim().replace("\n", " ").replace("\r", ""));
        saveAll();
        redirect(ex, "/report", sid, "ok:Thanks. Your report was sent to the admins.");
    }

    // =====================================================================
    // Admin
    // =====================================================================

    private void admin(HttpExchange ex, String path, boolean post, HashMap<String, String> p, String sid, Admin a, String flash) throws IOException {
        if(path.equals("/admin")){
            sendHtml(ex, 200, adminPages.dashboard(a, flash));
        }else if(path.equals("/admin/users")){
            sendHtml(ex, 200, adminPages.users(a, flash, p));
        }else if(path.equals("/admin/user")){
            User u = mm.getAccounts().searchByUsername(p.getOrDefault("u", ""));
            if(u == null){ redirect(ex, "/admin/users", sid, "err:User not found."); return; }
            sendHtml(ex, 200, adminPages.userView(a, flash, u));
        }else if(path.equals("/admin/promote") && post){
            User u = mm.getAccounts().searchByUsername(p.getOrDefault("username", ""));
            Admin promoted = a.promoteUser(u, mm.getAccounts());
            if(promoted == null){
                redirect(ex, "/admin/users", sid, "err:Failed to promote user.");
                return;
            }
            mm.getAccounts().addAccount(promoted);
            saveAll();
            redirect(ex, "/admin/users", sid, "ok:" + promoted.getUsername() + " is now an admin.");
        }else if(path.equals("/admin/reports")){
            sendHtml(ex, 200, adminPages.reports(a, flash));
        }else if(path.equals("/admin/resolve") && post){
            ArrayList<Appeal> list = mm.getAppeals().getAppeals();
            int i = toInt(p.get("i"), -1);
            if(i < 0 || i >= list.size()){ redirect(ex, "/admin/reports", sid, "err:That report does not exist."); return; }
            User d = list.get(i).getUser();
            mm.getAccounts().deleteAccount(d, d.getPassword());
            list.remove(i);
            saveAll();
            redirect(ex, "/admin/reports", sid, "ok:Report resolved and @" + d.getUsername() + " was deleted.");
        }else if(path.equals("/admin/review")){
            if(post){ reviewEvent(ex, p, sid); return; }
            Event selected = null;
            if(p.containsKey("t")){
                selected = mm.getEvents().searchPendingEventByTitle(p.get("t"));
            }else if(!adminPages.waiting().isEmpty()){
                selected = adminPages.waiting().get(0);
            }
            // the system checks the event before an admin can review it
            if(selected != null && selected.getEventAdminFeedback() == null){
                selected.autoApproveEvent();
            }
            sendHtml(ex, 200, adminPages.review(a, flash, selected));
        }else if(path.equals("/admin/events")){
            sendHtml(ex, 200, adminPages.events(a, flash));
        }else{
            redirect(ex, "/admin", sid, "err:That page does not exist.");
        }
    }

    /**
     * Approves an event if every detail was approved, otherwise rejects it with the admin's reason.
     */
    private void reviewEvent(HttpExchange ex, HashMap<String, String> p, String sid) throws IOException {
        Event e = mm.getEvents().searchPendingEventByTitle(p.getOrDefault("t", ""));
        if(e == null){
            redirect(ex, "/admin/review", sid, "err:This event does not exist.");
            return;
        }
        int count = toInt(p.get("count"), 0);
        boolean allYes = true;
        for(int i = 0; i < count; i++){
            if("n".equals(p.get("spec" + i))){
                allYes = false;
            }
        }
        if(!allYes){
            String reason = p.getOrDefault("reason", "").trim();
            e.setEventAdminFeedback(reason.isEmpty() ? "Rejected by admin" : reason);
            saveAll();
            redirect(ex, "/admin/review", sid, "ok:" + e.getTitle() + " was rejected.");
            return;
        }
        if(!mm.getEvents().addApprovedEvent(e)){
            redirect(ex, "/admin/review", sid, "err:Reached max capacity of events. Remove old events first.");
            return;
        }
        e.setApproved(true);
        mm.getEvents().removePendingEvent(e);
        saveAll();
        redirect(ex, "/admin/review", sid, "ok:" + e.getTitle() + " was approved!");
    }

    // =====================================================================
    // Saving and helpers
    // =====================================================================

    /**
     * Saves accounts, matches, events, reports and preferences to their files.
     */
    private void saveAll(){
        mm.saveAccountsToFile("Accounts.txt");
        mm.getEvents().saveAllEvents("event.txt", mm.getEvents().getAllEventList());
        mm.saveAppealsToFile("appeals.txt");
        prefs.save();
    }

    private String takeFlash(String sid){
        if(sid == null){
            return null;
        }
        return flashes.remove(sid);
    }

    private static String typeOf(String t){
        if("virtual".equals(t) || "outdoor".equals(t)){
            return t;
        }
        return "indoor";
    }

    private static boolean safeFileName(String f){
        return f.matches("[A-Za-z0-9_.-]+") && !f.contains("..");
    }

    private static int toInt(String s, int fallback){
        try{
            return Integer.parseInt(s.trim());
        }catch(Exception e){
            return fallback;
        }
    }

    private static double toDouble(String s, double fallback){
        try{
            return Double.parseDouble(s.trim());
        }catch(Exception e){
            return fallback;
        }
    }

    /**
     * Turns "a=1&b=two" from a web address or form into a map of names and values.
     */
    private static HashMap<String, String> parse(String raw){
        HashMap<String, String> map = new HashMap<String, String>();
        if(raw == null || raw.isEmpty()){
            return map;
        }
        String[] pairs = raw.split("&");
        for(int i = 0; i < pairs.length; i++){
            int eq = pairs[i].indexOf('=');
            String key = eq < 0 ? pairs[i] : pairs[i].substring(0, eq);
            String value = eq < 0 ? "" : pairs[i].substring(eq + 1);
            map.put(URLDecoder.decode(key, StandardCharsets.UTF_8), URLDecoder.decode(value, StandardCharsets.UTF_8));
        }
        return map;
    }

    /**
     * Reads the session id from the browser's cookie.
     */
    private static String sessionId(HttpExchange ex){
        List<String> cookies = ex.getRequestHeaders().get("Cookie");
        if(cookies == null){
            return null;
        }
        for(String header : cookies){
            String[] parts = header.split(";");
            for(int i = 0; i < parts.length; i++){
                String c = parts[i].trim();
                if(c.startsWith(COOKIE + "=")){
                    return c.substring(COOKIE.length() + 1);
                }
            }
        }
        return null;
    }

    private void sendHtml(HttpExchange ex, int code, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        ex.sendResponseHeaders(code, bytes.length);
        OutputStream out = ex.getResponseBody();
        out.write(bytes);
        out.close();
    }

    private void redirect(HttpExchange ex, String location, String sid) throws IOException {
        redirect(ex, location, sid, null);
    }

    /**
     * Sends the browser to another page, with an optional message to show there.
     */
    private void redirect(HttpExchange ex, String location, String sid, String message) throws IOException {
        if(sid == null){
            sid = UUID.randomUUID().toString();
        }
        if(message != null){
            flashes.put(sid, message);
        }
        ex.getResponseHeaders().add("Set-Cookie", COOKIE + "=" + sid + "; Path=/; HttpOnly; SameSite=Lax");
        ex.getResponseHeaders().set("Location", location);
        ex.sendResponseHeaders(303, -1);
        ex.close();
    }

    /**
     * Sends a stylesheet, picture or font file.
     */
    private void sendFile(HttpExchange ex, String path) throws IOException {
        String folder = path.startsWith("/static/") ? "web" : (path.startsWith("/images/") ? "images" : "fonts");
        String name = path.substring(path.lastIndexOf('/') + 1);
        File f = new File(folder, name);
        if(!safeFileName(name) || !f.exists()){
            ex.sendResponseHeaders(404, -1);
            ex.close();
            return;
        }
        String type = "application/octet-stream";
        if(name.endsWith(".css")) type = "text/css; charset=utf-8";
        else if(name.endsWith(".png")) type = "image/png";
        else if(name.endsWith(".ttf")) type = "font/ttf";
        byte[] bytes = Files.readAllBytes(f.toPath());
        ex.getResponseHeaders().set("Content-Type", type);
        ex.getResponseHeaders().set("Cache-Control", "max-age=3600");
        ex.sendResponseHeaders(200, bytes.length);
        OutputStream out = ex.getResponseBody();
        out.write(bytes);
        out.close();
    }
}
