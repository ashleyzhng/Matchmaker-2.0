import java.time.*;
import java.util.*;
/**
 * ----------------------------------------------------------------------------
 * File Name:    UserPages.java
 * Authors:      Ashley Zhong
 * Course:       ICS4U1
 * Description:  Builds the HTML for the login and sign-up pages and every page
 *               a regular user sees (home, matches, events, profile, settings
 *               and reporting). The pages only show information - the work is
 *               done by the Matchmaker classes in MatchmakerWeb.
 * ----------------------------------------------------------------------------
 */
public class UserPages {
    private Matchmaker mm;
    private Preferences prefs;

    /**
     * Creates the page builder.
     *
     * @param m The Matchmaker system with all the data
     * @param p The saved themes and avatars
     */
    public UserPages(Matchmaker m, Preferences p){
        mm = m;
        prefs = p;
    }

    private String layout(Account a, String active, String flash, String content){
        return Html.layout(a, prefs.getTheme(a.getUsername()), prefs.getAvatar(a.getUsername()), active, flash, content);
    }

    // =====================================================================
    // Login, sign up and password reset
    // =====================================================================

    /**
     * The welcome and login page.
     */
    public String login(String flash){
        String brand = "<section class=\"brand\"><div class=\"logo\">" + Html.character("brand_cupid", "") + "<span>matchmaker</span></div>"
            + "<div class=\"stack\"><h1>Find your person,<br>or your people.</h1><p style=\"font-size:18px;max-width:460px\">Match by shared interests or by who lives nearby, or meet at a single or double date event.</p></div>"
            + Html.character("brand_cupid", "width:300px;height:300px")
            + "<div class=\"pills\"><span>Interest</span><span>Distance</span><span>Single date</span><span>Double date</span></div></section>";
        String form = "<section class=\"auth-side\"><form class=\"auth-card\" method=\"post\" action=\"/login\">"
            + Html.avatar("love_letter", "s96") + "<span class=\"script\">hi again, lovely</span><h1>Welcome back</h1>"
            + "<p class=\"muted\">Log in to see your matches and events.</p>" + Html.flash(flash)
            + Html.input("Username", "username", "text", "", "required autofocus")
            + Html.input("Password", "password", "password", "", "required")
            + "<div class=\"row\" style=\"justify-content:flex-end\"><a class=\"btn btn-ghost\" href=\"/reset\">Forgot password?</a></div>"
            + "<button class=\"btn btn-primary wide\">Log in</button><div class=\"divider\">or</div>"
            + "<a class=\"btn btn-secondary wide\" href=\"/signup\">Create an account</a>"
            + "<p class=\"muted small\" style=\"text-align:center\">Admins log in here too, and go straight to the admin dashboard.</p></form></section>";
        return Html.page("Welcome", "blush", "<div class=\"auth\">" + brand + form + "</div>");
    }

    /**
     * The reset password page.
     */
    public String reset(String flash){
        String body = "<div class=\"center-page\"><form class=\"card\" method=\"post\" action=\"/reset\" style=\"width:480px\">"
            + "<span class=\"script\">it happens to everyone</span><h1>Reset password</h1>"
            + "<p class=\"muted\">Your email must be verified before you can reset your password.</p>" + Html.flash(flash)
            + Html.input("Username", "username", "text", "", "required") + Html.input("Email", "email", "email", "", "required")
            + Html.input("New password", "password", "password", "", "required")
            + "<div class=\"row\"><a class=\"btn btn-secondary\" href=\"/\">Back</a><button class=\"btn btn-primary\">Reset password</button></div></form></div>";
        return Html.page("Reset password", "blush", body);
    }

    /**
     * The create account page.
     */
    public String signup(String flash, Map<String, String> old){
        String v = "";
        String body = "<div class=\"center-page\"><div class=\"card\"><form class=\"stack\" method=\"post\" action=\"/signup\" style=\"gap:22px\">"
            + "<div class=\"row between\"><div class=\"row\">" + Html.character("avatar_cupcake", "width:88px;height:88px")
            + "<div class=\"stack\" style=\"gap:4px\"><h1>Create your account</h1><p class=\"muted\">Your account type (Teen, Adult or Senior) is set from your birthday.</p></div></div></div>"
            + Html.flash(flash)
            + "<div class=\"grid grid-2\">"
            + Html.input("Username", "username", "text", get(old, "username"), "required")
            + Html.input("Password", "password", "password", "", "required")
            + Html.input("Email", "email", "email", get(old, "email"), "required")
            + Html.input("Full name", "name", "text", get(old, "name"), "required")
            + Html.input("Birthday", "birthday", "date", get(old, "birthday"), "required")
            + Html.input("Postal code", "postal", "text", get(old, "postal"), "required maxlength=\"7\"")
            + Html.input("City", "city", "text", get(old, "city"), "required")
            + Html.input("Bio", "bio", "text", get(old, "bio"), "") + "</div>"
            + "<div class=\"grid grid-2\"><div class=\"stack\" style=\"gap:8px\"><strong class=\"small\">Gender</strong>"
            + segmented("gender", new String[]{"M", "F"}, new String[]{"Male", "Female"}, get(old, "gender").isEmpty() ? "F" : get(old, "gender"))
            + "</div><div class=\"stack\" style=\"gap:8px\"><strong class=\"small\">Interested in</strong>"
            + segmented("pgender", new String[]{"M", "F", "O"}, new String[]{"Male", "Female", "Other"}, get(old, "pgender").isEmpty() ? "M" : get(old, "pgender"))
            + "</div></div>"
            + "<div class=\"stack\" style=\"gap:10px\"><strong class=\"small\">Pick your interests</strong><p class=\"muted small\">These can't be changed later. Events you rate highly will grow them over time.</p><div class=\"chips\">";
        String[] names = Html.interestNames();
        for(int i = 0; i < names.length; i++){
            body += "<label class=\"chip\"><input type=\"checkbox\" name=\"tag" + i + "\" value=\"1\"" + (get(old, "tag" + i).equals("1") ? " checked" : "") + "><span>" + names[i] + "</span></label>";
        }
        body += "</div></div>"
            + "<div class=\"card soft stack\"><strong>If you are 18 or older</strong><p class=\"muted small\">Teens can leave this part blank.</p><div class=\"grid grid-2\">"
            + Html.input("Occupation", "occupation", "text", get(old, "occupation"), "")
            + Html.input("Marital status", "marital", "text", get(old, "marital"), "")
            + Html.input("Youngest age you'd like to match with", "minAge", "number", get(old, "minAge"), "min=\"18\" max=\"100\"")
            + Html.input("Oldest age you'd like to match with", "maxAge", "number", get(old, "maxAge"), "min=\"18\" max=\"100\"")
            + "</div><label class=\"chip\"><input type=\"checkbox\" name=\"mobility\" value=\"true\"" + (get(old, "mobility").equals("true") ? " checked" : "")
            + "><span>65+ only: I have mobility limitations</span></label></div>"
            + "<div class=\"stack\" style=\"gap:10px\"><strong class=\"small\">Pick your avatar</strong>" + avatarPicker(get(old, "avatar").isEmpty() ? "cupcake" : get(old, "avatar")) + "</div>"
            + "<div class=\"row\" style=\"justify-content:flex-end\"><a class=\"btn btn-secondary\" href=\"/\">Back</a><button class=\"btn btn-primary\">Create account</button></div></form>"
            + "<form class=\"row\" method=\"post\" action=\"/signup/file\" style=\"border-top:1px solid var(--border);padding-top:18px\"><span class=\"muted small\">Have a file of accounts?</span>"
            + "<input type=\"text\" name=\"file\" placeholder=\"accounts.txt\" style=\"max-width:220px\"><button class=\"btn btn-ghost\">Load from file</button></form></div></div>";
        return Html.page("Create account", "blush", body);
    }

    // =====================================================================
    // Home
    // =====================================================================

    /**
     * The home page for a logged in user.
     */
    public String home(User u, String flash){
        int waiting = 0;
        Match[] ms = u.getMatches();
        for(int i = 0; i < u.getNumMatches(); i++){
            if(ms[i] != null && myStatus(u, ms[i]) == 'P'){
                waiting++;
            }
        }
        int history = u.getMatchHistory() == null ? 0 : u.getMatchHistory().size();
        String hour = LocalTime.now().getHour() < 12 ? "good morning" : (LocalTime.now().getHour() < 18 ? "good afternoon" : "good evening");
        String right = "<div class=\"row\">" + Html.character("avatar_" + prefs.getAvatar(u.getUsername()), "width:96px;height:96px")
            + "<a class=\"btn btn-primary\" href=\"/new-match\">+ New match</a></div>";
        String c = Html.head(LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("EEEE, MMMM d")).toLowerCase(),
            hour + ", " + firstName(u).toLowerCase(), waiting == 0 ? "No matches are waiting on your answer." : "You have " + waiting + " match" + (waiting == 1 ? "" : "es") + " waiting on your answer.", right);
        c += "<div class=\"grid grid-4\">" + Html.stat("Active matches", u.getNumMatches() + " / " + User.MAX_MATCHES, (User.MAX_MATCHES - u.getNumMatches()) + " slots left")
            + Html.stat("Waiting on you", "" + waiting, "Accept or reject")
            + Html.stat("Events attended", "" + u.getEventHistory().size(), "Rate events 3+ to grow your interests")
            + Html.stat("Past matches", "" + history, "View history") + "</div>";
        c += "<div class=\"row between\"><h2>your matches</h2><a class=\"btn btn-ghost\" href=\"/matches\">See all</a></div>";
        if(u.getNumMatches() == 0){
            c += "<div class=\"card soft row\">" + Html.character("brand_cupid", "width:90px;height:90px") + "<div class=\"stack\" style=\"gap:6px\"><h3>no matches yet</h3><p class=\"muted\">Make your first match and it will show up here.</p></div><a class=\"btn btn-primary\" href=\"/new-match\">Make a match</a></div>";
        }else{
            c += "<div class=\"grid grid-3\">";
            for(int i = 0; i < u.getNumMatches() && i < 3; i++){
                if(ms[i] != null){
                    c += matchCard(u, ms[i], i, true);
                }
            }
            c += "</div>";
        }
        c += "<div class=\"row between\"><h2>events near you</h2><a class=\"btn btn-ghost\" href=\"/events\">Browse events</a></div>";
        EventDatabase ev = mm.getEvents();
        ev.sortApprovedByDateThenTime();
        Event[] list = ev.getApprovedEventList();
        String cards = "";
        int shown = 0;
        for(int i = 0; i < ev.getNumApprovedEvents() && shown < 3; i++){
            if(list[i] != null && !list[i].isExpired()){
                cards += Html.eventCard(list[i], null);
                shown++;
            }
        }
        c += shown == 0 ? "<p class=\"muted\">No upcoming events right now.</p>" : "<div class=\"grid grid-3\">" + cards + "</div>";
        return layout(u, "Home", flash, c);
    }

    // =====================================================================
    // New match
    // =====================================================================

    /**
     * The page for making a new match.
     *
     * @param last The match that was just made, or null
     */
    public String newMatch(User u, String flash, Match last){
        String c = Html.head(u.getNumMatches() + " of " + User.MAX_MATCHES + " matches used", "make a new match",
            "Pick how you want to be matched. We only suggest people you have not matched with before.", null);
        if(u.maxMatches()){
            c += "<div class=\"flash err\">You already have 5 matches! Finish your existing matches before making a new one.</div>";
        }
        String[][] opts = {{"1", "interest match", "Find the person whose interests are closest to yours.", "avatar_love_letter", ""},
            {"2", "distance match", "Find someone whose postal code is closest to yours.", "avatar_hungry_baby", ""},
            {"3", "single date", "Join a virtual or outdoor event with one other person.", "single_date", "lower success rate"},
            {"4", "double date", "Join an indoor event with three other people.", "double_date", "lower success rate"}};
        c += "<form class=\"stack\" method=\"post\" action=\"/new-match\"><div class=\"grid grid-4\">";
        for(int i = 0; i < opts.length; i++){
            String img = Html.character(opts[i][3], "");
            c += "<label class=\"pick\"><input type=\"radio\" name=\"type\" value=\"" + opts[i][0] + "\"" + (i == 0 ? " checked" : "") + "><div class=\"card\">" + img
                + "<h3>" + opts[i][1] + "</h3><p class=\"muted small\">" + opts[i][2] + "</p>" + (opts[i][4].isEmpty() ? "" : "<span class=\"low\">" + opts[i][4] + "</span>") + "</div></label>";
        }
        c += "</div><div class=\"row\" style=\"justify-content:flex-end\"><button class=\"btn btn-primary\"" + (u.maxMatches() ? " disabled" : "") + ">Find my match</button></div></form>";
        if(last != null){
            Match[] ms = u.getMatches();
            int idx = -1;
            for(int i = 0; i < u.getNumMatches(); i++){
                if(ms[i] == last){
                    idx = i;
                }
            }
            c += "<div class=\"card row\" style=\"gap:32px;padding:28px\">" + Html.character("brand_cupid", "width:120px;height:120px")
                + "<div class=\"stack grow\" style=\"flex:1;gap:10px\"><span class=\"script\" style=\"color:var(--accepted)\">match made!</span><h2>" + Html.esc(matchHeadline(u, last)) + "</h2>"
                + "<p class=\"muted\">" + Html.esc(matchDetails(u, last)) + "</p><div class=\"row\"><a class=\"btn btn-primary\" href=\"/matches\">View my matches</a></div></div>"
                + "<div style=\"width:340px\">" + matchCard(u, last, idx, idx >= 0) + "</div></div>";
        }
        return layout(u, "New Match", flash, c);
    }

    // =====================================================================
    // My matches
    // =====================================================================

    /**
     * The page listing current matches or match history.
     */
    public String matches(User u, String flash, String tab){
        int history = u.getMatchHistory() == null ? 0 : u.getMatchHistory().size();
        boolean showHistory = "history".equals(tab);
        String c = Html.head(u.getNumMatches() + " active · " + history + " in history", "my matches", "Accept, reject, or finish a match and rate how it went.",
            Html.character("avatar_" + prefs.getAvatar(u.getUsername()), "width:96px;height:96px"));
        c += "<div><div class=\"tabs\"><a href=\"/matches\"" + (showHistory ? "" : " class=\"on\"") + ">Active (" + u.getNumMatches() + ")</a>"
            + "<a href=\"/matches?tab=history\"" + (showHistory ? " class=\"on\"" : "") + ">History (" + history + ")</a></div></div>";
        String cards = "";
        if(showHistory){
            ArrayList<Match> h = u.getMatchHistory();
            for(int i = h.size() - 1; i >= 0; i--){
                cards += matchCard(u, h.get(i), -1, false);
            }
        }else{
            Match[] ms = u.getMatches();
            for(int i = 0; i < u.getNumMatches(); i++){
                if(ms[i] != null){
                    cards += matchCard(u, ms[i], i, true);
                }
            }
        }
        if(cards.isEmpty()){
            c += "<div class=\"card soft row\">" + Html.character("avatar_cupcake", "width:90px;height:90px") + "<p class=\"muted\" style=\"flex:1\">"
                + (showHistory ? "Finished, rejected and expired matches will show up here." : "You have no active matches.") + "</p><a class=\"btn btn-primary\" href=\"/new-match\">Make a match</a></div>";
        }else{
            c += "<div class=\"grid grid-3\">" + cards + "</div>";
        }
        return layout(u, "My Matches", flash, c);
    }

    /**
     * The page for finishing a match: paying for the event (if there is one) and rating it.
     */
    public String complete(User u, String flash, int i){
        Match m = u.getMatches()[i];
        String c = Html.head("how did it go?", "complete your match", matchHeadline(u, m), null);
        c += "<div class=\"row\" style=\"align-items:flex-start;gap:24px\"><div style=\"width:340px\">" + matchCard(u, m, i, false) + "</div>";
        c += "<form class=\"card stack\" method=\"post\" action=\"/matches/complete\" style=\"flex:1;max-width:560px\">" + Html.hidden("i", i);
        if(m instanceof EventMatch && ((EventMatch)m).getEvent() != null){
            Event e = ((EventMatch)m).getEvent();
            c += "<h2>the event is starting</h2>";
            String note;
            if(e instanceof Virtual){
                note = "Have a discount code? DISCOUNT10, DISCOUNT20 and DISCOUNT30 take 10%, 20% or 30% off.";
            }else if(e instanceof Outdoor){
                int p = ((Outdoor)e).getPrecipitation();
                note = "Chance of rain is " + p + "%. " + (p > 70 ? "You get a discount for bad weather!" : (p < 20 ? "Good weather costs 20% extra." : "No weather change to the price."));
            }else{
                note = ((Indoor)e).getUser3() == null ? "This is a single date, so the price stays the same." : "This is a double date, so you get a 30% discount!";
            }
            double price = (e instanceof Virtual) ? e.getOriginalCost() : e.calculateCost();
            c += "<div class=\"card soft row between\"><span class=\"muted\">Event cost</span><strong>" + Html.money(price) + "</strong></div><p class=\"muted small\">" + Html.esc(note) + "</p>";
            if(e instanceof Virtual){
                c += Html.input("Discount code (optional)", "code", "text", "", "");
            }
            c += Html.input("Amount you are paying ($)", "paid", "number", "", "step=\"0.01\" min=\"0\" required")
                + "<div class=\"stack\" style=\"gap:8px\"><strong class=\"small\">Want to continue on the date?</strong>"
                + segmented("continue", new String[]{"Y", "N"}, new String[]{"Yes, enjoy the event", "No, cancel the date"}, "Y") + "</div>";
        }
        c += "<div class=\"stack\" style=\"gap:8px\"><strong class=\"small\">Rate your experience (1 = lowest, 5 = highest)</strong>"
            + segmented("rating", new String[]{"1", "2", "3", "4", "5"}, new String[]{"1 ★", "2 ★", "3 ★", "4 ★", "5 ★"}, "5") + "</div>"
            + "<div class=\"row\" style=\"justify-content:flex-end\"><a class=\"btn btn-secondary\" href=\"/matches\">Back</a><button class=\"btn btn-primary\">Complete match</button></div></form></div>";
        return layout(u, "My Matches", flash, c);
    }

    // =====================================================================
    // Events
    // =====================================================================

    /**
     * The page for browsing approved events and seeing the user's own events.
     */
    public String events(User u, String flash, Map<String, String> q){
        EventDatabase ev = mm.getEvents();
        String sort = get(q, "sort").isEmpty() ? "time" : get(q, "sort");
        String type = get(q, "type").isEmpty() ? "all" : get(q, "type");
        String search = get(q, "q").trim();
        if(sort.equals("title")){
            ev.sortEventByAlphaTitle();
        }else if(sort.equals("cost")){
            ev.sortApprovedByCostThenTitle();
        }else{
            ev.sortApprovedByDateThenTime();
        }
        String c = Html.head(ev.getNumApprovedEvents() + " approved · " + ev.getNumPendingEvents() + " waiting for approval", "events",
            "Browse approved events or host your own. An admin reviews every new event.", "<a class=\"btn btn-primary\" href=\"#host\">+ Host an event</a>");
        c += "<form class=\"card row\" method=\"get\" action=\"/events\" style=\"align-items:flex-end\">"
            + "<div style=\"flex:1;min-width:200px\">" + Html.input("Search by title", "q", "text", search, "placeholder=\"board game bash\"") + "</div>"
            + "<label class=\"field\">Sort by<select name=\"sort\">" + option("time", "Date & time", sort) + option("title", "Title", sort) + option("cost", "Cost", sort) + "</select></label>"
            + "<label class=\"field\">Type<select name=\"type\">" + option("all", "All", type) + option("virtual", "Virtual", type) + option("outdoor", "Outdoor", type) + option("indoor", "Indoor", type) + "</select></label>"
            + Html.input("On date", "date", "date", get(q, "date"), "") + Html.input("Max cost ($)", "max", "number", get(q, "max"), "step=\"0.01\" min=\"0\" style=\"width:120px\"")
            + "<button class=\"btn btn-primary\">Apply</button><a class=\"btn btn-ghost\" href=\"/events\">Clear</a></form>";
        LocalDate onDate = null;
        double max = -1;
        try{ onDate = LocalDate.parse(get(q, "date")); }catch(Exception e){ onDate = null; }
        try{ max = Double.parseDouble(get(q, "max")); }catch(Exception e){ max = -1; }
        String cards = "";
        if(!search.isEmpty()){
            Event found = ev.searchApprovedEventByTitle(search);
            if(found != null){
                cards = Html.eventCard(found, null);
            }
        }else{
            Event[] list = ev.getApprovedEventList();
            for(int i = 0; i < ev.getNumApprovedEvents(); i++){
                Event e = list[i];
                if(e == null || e.getDate() == null) continue;
                if(!type.equals("all") && !Html.eventKind(e).equalsIgnoreCase(type)) continue;
                if(onDate != null && !e.getDate().equals(onDate)) continue;
                if(max >= 0 && e.getOriginalCost() > max) continue;
                cards += Html.eventCard(e, null);
            }
        }
        c += cards.isEmpty() ? "<p class=\"muted\">No events match. Try clearing the filters.</p>" : "<div class=\"grid grid-4\">" + cards + "</div>";
        // the user's own events
        String mine = "";
        Event[] pend = ev.getPendingEventList();
        for(int i = 0; i < ev.getNumPendingEvents(); i++){
            Event e = pend[i];
            if(e != null && e.getCurrentUser1() != null && e.getCurrentUser1().getUsername().equals(u.getUsername())){
                String status = e.getEventAdminFeedback() == null ? Html.badge("pending", "Waiting for admin") : Html.badge("rejected", "Rejected: " + e.getEventAdminFeedback().trim());
                mine += "<div class=\"list-row\"><div class=\"grow\"><strong>" + Html.esc(e.getTitle()) + "</strong><p class=\"muted small\">" + Html.eventKind(e) + " · "
                    + Html.day(e.getDate()) + "</p></div>" + status + "<a class=\"btn btn-secondary\" href=\"/events/edit?t=" + Html.url(e.getTitle()) + "\">Edit</a></div>";
            }
        }
        Event[] appr = ev.getApprovedEventList();
        for(int i = 0; i < ev.getNumApprovedEvents(); i++){
            Event e = appr[i];
            if(e != null && e.getCurrentUser1() != null && e.getCurrentUser1().getUsername().equals(u.getUsername())){
                mine += "<div class=\"list-row\"><div class=\"grow\"><strong>" + Html.esc(e.getTitle()) + "</strong><p class=\"muted small\">" + Html.eventKind(e) + " · "
                    + Html.day(e.getDate()) + "</p></div>" + Html.badge("accepted", "Approved") + "<a class=\"btn btn-ghost\" href=\"/events/view?t=" + Html.url(e.getTitle()) + "\">View</a></div>";
            }
        }
        c += "<div class=\"grid grid-2\"><div class=\"card stack\" id=\"host\"><div class=\"row\">" + Html.character("avatar_gardener", "width:90px;height:90px")
            + "<div class=\"stack\" style=\"gap:4px;flex:1\"><h2>host your own event</h2><p class=\"muted small\">Indoor events can be double dates for up to four people. An admin approves your event first.</p></div></div>"
            + "<div class=\"row\"><a class=\"btn btn-secondary\" href=\"/events/host?type=virtual\">Virtual</a><a class=\"btn btn-secondary\" href=\"/events/host?type=outdoor\">Outdoor</a><a class=\"btn btn-primary\" href=\"/events/host?type=indoor\">Indoor</a></div></div>"
            + "<div class=\"card stack\"><h2>your hosted events</h2>" + (mine.isEmpty() ? "<p class=\"muted\">You haven't hosted any events yet.</p>" : "<div>" + mine + "</div>") + "</div></div>";
        return layout(u, "Events", flash, c);
    }

    /**
     * Shows all the details of one event.
     */
    public String eventView(Account a, String flash, Event e){
        String c = Html.head(Html.eventKind(e).toLowerCase() + " event", e.getTitle().toLowerCase(), null, "<a class=\"btn btn-secondary\" href=\"" + (a instanceof Admin ? "/admin/events" : "/events") + "\">Back</a>");
        c += "<div class=\"grid grid-2\" style=\"align-items:start\"><div>" + Html.eventCard(e, null) + "</div><div class=\"card\">";
        String[][] rows = specRows(e);
        for(int i = 0; i < rows.length; i++){
            c += "<div class=\"field-row\"><span>" + rows[i][0] + "</span><span>" + Html.esc(rows[i][1]) + "</span></div>";
        }
        c += "<div class=\"field-row\"><span>Status</span><span>" + (e.getApproved() ? "Approved" : (e.getEventAdminFeedback() == null ? "Waiting for approval" : "Rejected: " + Html.esc(e.getEventAdminFeedback()))) + "</span></div>";
        c += "<div class=\"field-row\"><span>Hosted by</span><span>" + (e.getCurrentUser1() == null ? "—" : "@" + Html.esc(e.getCurrentUser1().getUsername())) + "</span></div></div></div>";
        return layout(a, a instanceof Admin ? "All Events" : "Events", flash, c);
    }

    /**
     * The form for hosting a new event or editing one that is still pending.
     *
     * @param e The pending event being edited, or null for a new event
     */
    public String hostForm(User u, String flash, String type, Event e){
        boolean edit = e != null;
        if(edit){
            type = Html.eventKind(e).toLowerCase();
        }
        String c = Html.head(edit ? "editing a pending event" : "host an event", type + " event",
            edit ? "Changes are sent to an admin for review again." : "Fill in the details. Your event shows up for others once an admin approves it.", null);
        c += "<form class=\"card stack\" method=\"post\" action=\"" + (edit ? "/events/edit" : "/events/host") + "\">" + Html.hidden("type", type)
            + (edit ? Html.hidden("old", e.getTitle()) : "") + Html.flash(flash) + "<div class=\"grid grid-2\">"
            + Html.input("Title", "title", "text", edit ? e.getTitle() : "", "required")
            + Html.input("Date", "date", "date", edit ? e.getDate() : "", "required min=\"" + LocalDate.now() + "\"")
            + Html.input("Start time", "start", "time", edit ? e.getStartTime() : "", "required")
            + Html.input("End time", "end", "time", edit ? e.getEndTime() : "", "required")
            + Html.input("Base cost ($)", "cost", "number", edit ? "" + e.getOriginalCost() : "", "step=\"0.01\" min=\"0\" required");
        if(type.equals("virtual")){
            Virtual v = edit ? (Virtual)e : null;
            c += Html.input("Platform", "platform", "text", edit ? v.getPlatform() : "", "required placeholder=\"zoom\"")
                + Html.input("Meeting ID", "meetingId", "number", edit ? "" + v.getMeetingID() : "", "required")
                + Html.input("Password", "meetingPass", "number", edit ? "" + v.getPassword() : "", "required");
        }else if(type.equals("outdoor")){
            Outdoor o = edit ? (Outdoor)e : null;
            c += Html.input("Location (postal code)", "location", "text", edit ? o.getLocation() : "", "required")
                + Html.input("Temperature (°C)", "temp", "number", edit ? "" + o.getTemperature() : "", "required")
                + Html.input("Chance of rain (%)", "precip", "number", edit ? "" + o.getPrecipitation() : "", "required min=\"0\" max=\"100\"");
        }else{
            Indoor in = edit ? (Indoor)e : null;
            c += Html.input("Location (postal code)", "location", "text", edit ? in.getLocation() : "", "required")
                + Html.input("Floor number", "floor", "number", edit ? "" + in.getFloorNumber() : "", "required")
                + Html.input("Room (name or number)", "room", "text", edit ? in.getRoom() : "", "required");
        }
        c += "</div><div class=\"stack\" style=\"gap:10px\"><strong class=\"small\">Categories</strong><div class=\"chips\">";
        String[] names = Html.interestNames();
        int[] on = edit ? Html.interestValues(e.getCategories()) : new int[10];
        for(int i = 0; i < names.length; i++){
            c += "<label class=\"chip\"><input type=\"checkbox\" name=\"tag" + i + "\" value=\"1\"" + (on[i] > 0 ? " checked" : "") + "><span>" + names[i] + "</span></label>";
        }
        c += "</div></div><div class=\"row\" style=\"justify-content:flex-end\"><a class=\"btn btn-secondary\" href=\"/events\">Cancel</a><button class=\"btn btn-primary\">"
            + (edit ? "Save changes" : "Send for approval") + "</button></div></form>";
        return layout(u, "Events", null, c);
    }

    // =====================================================================
    // Profile
    // =====================================================================

    /**
     * The user's profile page. Adults and seniors see extra details.
     */
    public String profile(User u, String flash){
        String group = Html.ageGroup(u);
        String c = "<div class=\"hero\">" + Html.avatar(prefs.getAvatar(u.getUsername()), "s132") + "<div class=\"grow stack\" style=\"gap:6px\">"
            + "<div class=\"row\"><h1>" + Html.esc(u.getName().toLowerCase()) + "</h1>" + Html.groupPill(u, true) + "</div>"
            + "<p class=\"muted\">@" + Html.esc(u.getUsername()) + " · " + Html.esc(u.getCity()) + "</p>"
            + (u.getBio() == null || u.getBio().isEmpty() ? "" : "<span class=\"script\">“" + Html.esc(u.getBio()) + "”</span>") + "</div>"
            + "<div class=\"row\"><a class=\"btn btn-secondary\" href=\"/settings#avatar\">Change avatar</a><a class=\"btn btn-primary\" href=\"/settings#account\">Edit profile</a></div></div>";
        String about = "<div class=\"card\"><h2 style=\"margin-bottom:10px\">about " + Html.esc(firstName(u).toLowerCase()) + "</h2>"
            + row("Account type", group + (u instanceof Teen ? " (13–17)" : (u instanceof Senior ? " (65+)" : " (18–64)")))
            + row("Age", "" + Html.age(u)) + row("Birthday", "" + u.getBirthday()) + row("City", u.getCity()) + row("Postal code", u.getPostalCode())
            + row("Gender", gender(u.getGender())) + row("Email", u.getEmail() + (u.isEmailVerified() ? " (verified)" : " (not verified)"));
        if(u instanceof Adult){
            about += row("Occupation", ((Adult)u).getOccupation()) + row("Marital status", ((Adult)u).getMaritalStatus());
        }else if(u instanceof Senior){
            about += row("Occupation", ((Senior)u).getOccupation()) + row("Marital status", ((Senior)u).getMaritalStatus())
                + row("Mobility limitations", ((Senior)u).hasMobilityLimitations() ? "Yes" : "No");
        }
        about += "</div>";
        String look = "<div class=\"card stack\"><h2>looking for</h2><div>" + row("Interested in", gender(u.getPreferredGender()));
        String rule;
        String cls;
        if(u instanceof Adult){
            look += row("Preferred age", ((Adult)u).getPreferredMinAge() + " – " + ((Adult)u).getPreferredMaxAge());
            rule = "Adults (18–64) are matched with adults inside both people's preferred age range.";
            cls = "accepted";
        }else if(u instanceof Senior){
            look += row("Preferred age", ((Senior)u).getPreferredMinAge() + " – " + ((Senior)u).getPreferredMaxAge());
            rule = "Seniors (65+) are matched with seniors inside both people's preferred age range.";
            cls = "pending";
        }else{
            look += row("Who you can match with", "Other teens");
            rule = "Teens (13–17) are only ever matched with other teens.";
            cls = "completed";
        }
        look += "</div><div class=\"rule\" style=\"background:var(--" + cls + "-bg)\">" + rule + "</div><strong class=\"small\">Interests</strong><div class=\"chips\">";
        String[] names = Html.interestNames();
        int[] vals = Html.interestValues(u.getTag());
        for(int i = 0; i < names.length; i++){
            look += "<span class=\"chip-static" + (vals[i] > 0 ? " on" : "") + "\">" + names[i] + (vals[i] > 1 ? " ×" + vals[i] : "") + "</span>";
        }
        look += "</div></div>";
        c += "<div class=\"grid grid-2\" style=\"align-items:start\">" + about + look + "</div>";
        int hosted = 0;
        EventDatabase ev = mm.getEvents();
        for(int i = 0; i < ev.getAllEventList().size(); i++){
            Event e = ev.getAllEventList().get(i);
            if(e.getCurrentUser1() != null && e.getCurrentUser1().getUsername().equals(u.getUsername())){
                hosted++;
            }
        }
        c += "<div class=\"grid grid-3\">" + Html.stat("Active matches", u.getNumMatches() + " / 5", "Up to 5 at once")
            + Html.stat("Events attended", "" + u.getEventHistory().size(), "Rate events 3+ to grow your interests")
            + Html.stat("Events hosted", "" + hosted, "See them on the Events page") + "</div>";
        String hist = "";
        for(int i = 0; i < u.getEventHistory().size(); i++){
            Event e = u.getEventHistory().get(i);
            hist += "<div class=\"list-row\"><div class=\"grow\"><strong>" + Html.esc(e.getTitle()) + "</strong><p class=\"muted small\">" + Html.eventKind(e) + " · " + Html.day(e.getDate()) + "</p></div></div>";
        }
        c += "<div class=\"card stack\"><div class=\"row between\"><h2>event history</h2>"
            + (hist.isEmpty() ? "" : "<form class=\"row\" method=\"post\" action=\"/profile/archive\"><input type=\"text\" name=\"file\" placeholder=\"my_events.txt\" style=\"width:180px\" required><button class=\"btn btn-secondary\">Archive to file</button></form>")
            + "</div>" + (hist.isEmpty() ? "<p class=\"muted\">Events you attend will show up here.</p>" : "<div>" + hist + "</div>") + "</div>";
        return layout(u, "Profile", flash, c);
    }

    // =====================================================================
    // Settings
    // =====================================================================

    /**
     * The settings page: theme, avatar and account details.
     */
    public String settings(Account a, String flash){
        String theme = prefs.getTheme(a.getUsername());
        String c = Html.head("make it yours", "settings", "Pick a theme and an avatar, and manage your account.", null);
        c += "<form class=\"card stack\" method=\"post\" action=\"/settings/theme\" id=\"theme\"><h2>theme</h2><p class=\"muted small\">Changes the background and colours across the whole app.</p><div class=\"grid\" style=\"grid-template-columns:repeat(5,minmax(0,1fr))\">";
        for(int i = 0; i < Preferences.THEMES.length; i++){
            String t = Preferences.THEMES[i];
            c += "<label class=\"theme-option\"><input type=\"radio\" name=\"theme\" value=\"" + t + "\"" + (t.equals(theme) ? " checked" : "") + ">"
                + "<div class=\"preview theme-" + t + "\"><div class=\"side\"><i></i><i></i><i></i><i></i></div><div class=\"content\"><b></b><div class=\"mini\"><i></i><em></em></div></div></div>"
                + "<span class=\"name\">" + Preferences.THEME_NAMES[i].toLowerCase() + "</span></label>";
        }
        c += "</div><div class=\"row\" style=\"justify-content:flex-end\"><button class=\"btn btn-primary\">Save theme</button></div></form>";
        c += "<form class=\"card stack\" method=\"post\" action=\"/settings/avatar\" id=\"avatar\"><h2>avatar</h2><p class=\"muted small\">Pick the character that shows on your profile and match cards.</p>"
            + avatarPicker(prefs.getAvatar(a.getUsername())) + "<div class=\"row\" style=\"justify-content:flex-end\"><button class=\"btn btn-primary\">Save avatar</button></div></form>";
        c += "<form class=\"card stack\" method=\"post\" action=\"/settings/edit\" id=\"account\"><h2>account details</h2><div class=\"grid grid-2\">"
            + Html.input("Username", "username", "text", a.getUsername(), "required")
            + Html.input("New password (leave blank to keep)", "password", "password", "", "")
            + Html.input("Email (changing it needs re-verifying)", "email", "email", a.getEmail(), "required");
        if(a instanceof User){
            User u = (User)a;
            c += Html.input("Full name", "name", "text", u.getName(), "required") + Html.input("Birthday", "birthday", "date", u.getBirthday(), "required")
                + Html.input("Postal code", "postal", "text", u.getPostalCode(), "required maxlength=\"7\"") + Html.input("City", "city", "text", u.getCity(), "required")
                + Html.input("Bio", "bio", "text", u.getBio(), "") + "</div><div class=\"grid grid-2\"><div class=\"stack\" style=\"gap:8px\"><strong class=\"small\">Gender</strong>"
                + segmented("gender", new String[]{"M", "F"}, new String[]{"Male", "Female"}, "" + u.getGender()) + "</div><div class=\"stack\" style=\"gap:8px\"><strong class=\"small\">Interested in</strong>"
                + segmented("pgender", new String[]{"M", "F", "O"}, new String[]{"Male", "Female", "Other"}, "" + u.getPreferredGender()) + "</div>";
            if(u instanceof Adult){
                Adult ad = (Adult)u;
                c += Html.input("Occupation", "occupation", "text", ad.getOccupation(), "") + Html.input("Marital status", "marital", "text", ad.getMaritalStatus(), "")
                    + Html.input("Youngest preferred age", "minAge", "number", ad.getPreferredMinAge(), "min=\"18\" max=\"100\"") + Html.input("Oldest preferred age", "maxAge", "number", ad.getPreferredMaxAge(), "min=\"18\" max=\"100\"");
            }else if(u instanceof Senior){
                Senior s = (Senior)u;
                c += Html.input("Occupation", "occupation", "text", s.getOccupation(), "") + Html.input("Marital status", "marital", "text", s.getMaritalStatus(), "")
                    + Html.input("Youngest preferred age", "minAge", "number", s.getPreferredMinAge(), "min=\"18\" max=\"100\"") + Html.input("Oldest preferred age", "maxAge", "number", s.getPreferredMaxAge(), "min=\"18\" max=\"100\"")
                    + "<label class=\"chip\"><input type=\"checkbox\" name=\"mobility\" value=\"true\"" + (s.hasMobilityLimitations() ? " checked" : "") + "><span>I have mobility limitations</span></label>";
            }
        }
        c += "</div><div class=\"row\" style=\"justify-content:flex-end\"><button class=\"btn btn-primary\">Save details</button></div></form>";
        c += "<div class=\"card stack\"><h2>account</h2><div class=\"list-row\"><div class=\"grow\"><strong>Email</strong><p class=\"muted small\">" + Html.esc(a.getEmail()) + " · needed to reset your password</p></div>"
            + (a.isEmailVerified() ? Html.badge("accepted", "Verified") : Html.postButton("/settings/verify", "btn-secondary", "Verify email", null)) + "</div>";
        if(a instanceof User){
            c += "<form class=\"list-row\" method=\"post\" action=\"/settings/delete\"><div class=\"grow\"><strong>Delete account</strong><p class=\"muted small\">Enter your password. This cannot be undone.</p></div>"
                + "<input type=\"password\" name=\"password\" placeholder=\"Password\" style=\"width:200px\" required><button class=\"btn btn-danger\">Delete account</button></form>";
        }
        c += "</div>";
        return layout(a, "Settings", flash, c);
    }

    // =====================================================================
    // Report a user
    // =====================================================================

    /**
     * The page for reporting another user to the admins.
     */
    public String report(User u, String flash){
        String c = Html.head("we're here to help", "report a user", "Tell the admins about someone who is making the app feel unsafe.", Html.character("avatar_puppy", "width:96px;height:96px"));
        c += "<form class=\"card stack\" method=\"post\" action=\"/report\" style=\"max-width:640px\">"
            + Html.input("Username to report", "username", "text", "", "required")
            + "<label class=\"field\">Reason (optional)<textarea name=\"reason\" placeholder=\"What happened?\"></textarea></label>"
            + "<div class=\"row\" style=\"justify-content:flex-end\"><button class=\"btn btn-primary\">Send report</button></div></form>";
        return layout(u, "Report a User", flash, c);
    }

    // =====================================================================
    // Match card and match helpers
    // =====================================================================

    /**
     * Gets this user's own decision on a match (P, A, C or R).
     */
    public static char myStatus(User u, Match m){
        if(m instanceof PersonMatch){
            return ((PersonMatch)m).getStatus(u);
        }else if(m instanceof SingleEventMatch){
            return ((SingleEventMatch)m).getStatus(u);
        }else if(m instanceof DoubleEventMatch){
            return ((DoubleEventMatch)m).getStatus(u);
        }
        return 'P';
    }

    /**
     * Gets the other people in a match (everyone except this user).
     */
    public static ArrayList<User> others(User u, Match m){
        ArrayList<User> list = new ArrayList<User>();
        if(m instanceof PersonMatch){
            addIfOther(list, u, ((PersonMatch)m).getOwner());
            addIfOther(list, u, ((PersonMatch)m).getSoulmate());
        }else if(m instanceof SingleEventMatch){
            addIfOther(list, u, ((SingleEventMatch)m).getOwner());
            addIfOther(list, u, ((SingleEventMatch)m).getSoulmate());
        }else if(m instanceof DoubleEventMatch){
            for(int i = 0; i < 4; i++){
                addIfOther(list, u, ((DoubleEventMatch)m).getUser(i));
            }
        }
        return list;
    }

    private static void addIfOther(ArrayList<User> list, User me, User other){
        if(other != null && other != me && !other.getUsername().equals(me.getUsername())){
            list.add(other);
        }
    }

    /**
     * A short sentence describing a match, like "it's a match with noah".
     */
    public String matchHeadline(User u, Match m){
        ArrayList<User> o = others(u, m);
        if(m instanceof EventMatch){
            Event e = ((EventMatch)m).getEvent();
            String title = e == null ? ((EventMatch)m).getEventTitle() : e.getTitle();
            return "you're going to " + title;
        }
        return o.isEmpty() ? "your match" : "it's a match with " + firstName(o.get(0)).toLowerCase();
    }

    /**
     * A longer description of a match.
     */
    public String matchDetails(User u, Match m){
        ArrayList<User> o = others(u, m);
        if(m instanceof InterestMatch && !o.isEmpty()){
            return "You and " + firstName(o.get(0)) + " share interests. They will see this match too. It expires in 2 days.";
        }else if(m instanceof DistanceMatch && !o.isEmpty()){
            return "You and " + firstName(o.get(0)) + " live close to each other. They will see this match too. It expires in 2 days.";
        }else if(m instanceof EventMatch){
            return o.isEmpty() ? "You're the first one in. We'll pair you up as others join." : "You'll be there with " + names(o) + ".";
        }
        return "";
    }

    /**
     * Builds a match card with the other person, score, status and buttons.
     *
     * @param i       The match's position in the user's current matches
     * @param actions Whether to show the Accept, Reject and Complete buttons
     */
    public String matchCard(User u, Match m, int i, boolean actions){
        ArrayList<User> o = others(u, m);
        String type, name, handle, details, pic;
        if(m instanceof PersonMatch){
            PersonMatch pm = (PersonMatch)m;
            type = (m instanceof InterestMatch) ? "INTEREST MATCH" : "DISTANCE MATCH";
            User other = o.isEmpty() ? null : o.get(0);
            name = other == null ? "Still searching" : other.getName();
            handle = other == null ? "" : "@" + other.getUsername() + " · " + other.getCity();
            pic = Html.avatar(other == null ? "cupcake" : prefs.getAvatar(other.getUsername()), "s48");
            details = (pm.isExpire() ? "Expired " : "Expires ") + Html.day(pm.getExpiryDate());
            if(other != null){
                details = "They have " + statusWord(pm.getStatus(other)) + " · " + details;
            }
        }else{
            EventMatch em = (EventMatch)m;
            boolean dbl = m instanceof DoubleEventMatch;
            type = dbl ? "DOUBLE DATE" : "SINGLE DATE";
            Event e = em.getEvent();
            name = e == null ? em.getEventTitle() : e.getTitle();
            handle = e == null ? "" : Html.eventKind(e) + " · " + Html.place(e);
            pic = Html.character(dbl ? "double_date" : "single_date", "width:48px;height:48px");
            details = Html.day(em.getDate()) + " · " + Html.time(em.getStart()) + " – " + Html.time(em.getEnd()) + " · " + (o.isEmpty() ? "Still searching for a date" : "With " + names(o));
        }
        double score = Math.max(0, Math.min(100, m.getScore()));
        char mine = myStatus(u, m);
        String badge = m.isAcceptance() ? Html.badge("accepted", "Both accepted") : Html.badge(mine);
        String s = "<div class=\"card match\"><div class=\"head\">" + pic + "<div class=\"who\"><strong>" + Html.esc(name) + "</strong><span class=\"muted small\">"
            + Html.esc(handle) + "</span></div>" + badge + "</div><span class=\"type\">" + type + "</span>"
            + "<div class=\"stack\" style=\"gap:8px\"><div class=\"score-row\"><span class=\"muted small\">Compatibility</span><span class=\"pct\">" + Math.round(score) + "%</span></div>"
            + "<div class=\"bar\"><div style=\"width:" + score + "%\"></div></div></div><p class=\"muted small\">" + Html.esc(details) + "</p>";
        if(actions && i >= 0){
            s += "<div class=\"actions\">";
            if(mine != 'A'){
                s += Html.postButton("/matches/decide", "btn-primary", "Accept", Html.hidden("i", i) + Html.hidden("action", "accept"));
            }else{
                s += "<a class=\"btn btn-primary\" href=\"/matches/complete?i=" + i + "\">Complete &amp; rate</a>";
            }
            s += Html.postButton("/matches/decide", "btn-secondary", "Reject", Html.hidden("i", i) + Html.hidden("action", "reject")) + "</div>";
        }
        return s + "</div>";
    }

    // =====================================================================
    // Small helpers
    // =====================================================================

    /**
     * Builds the avatar choices as radio buttons.
     */
    public static String avatarPicker(String selected){
        String s = "<div class=\"row\" style=\"gap:14px\">";
        for(int i = 0; i < Preferences.AVATARS.length; i++){
            String k = Preferences.AVATARS[i];
            s += "<label class=\"avatar-option\"><input type=\"radio\" name=\"avatar\" value=\"" + k + "\"" + (k.equals(selected) ? " checked" : "")
                + "><img src=\"/images/avatar_" + k + ".png\" alt=\"\">" + Preferences.AVATAR_NAMES[i] + "</label>";
        }
        return s + "</div>";
    }

    /**
     * Builds a row of radio buttons that look like a toggle.
     */
    public static String segmented(String name, String[] values, String[] labels, String selected){
        String s = "<div class=\"segmented\">";
        for(int i = 0; i < values.length; i++){
            s += "<label><input type=\"radio\" name=\"" + name + "\" value=\"" + values[i] + "\"" + (values[i].equals(selected) ? " checked" : "") + "><span>" + labels[i] + "</span></label>";
        }
        return s + "</div>";
    }

    /**
     * Builds one option of a drop-down list.
     */
    public static String option(String value, String label, String selected){
        return "<option value=\"" + value + "\"" + (value.equals(selected) ? " selected" : "") + ">" + label + "</option>";
    }

    /**
     * Builds one label and value row on the profile page.
     */
    public static String row(String label, String value){
        return "<div class=\"field-row\"><span>" + Html.esc(label) + "</span><span>" + Html.esc(value == null || value.isEmpty() ? "—" : value) + "</span></div>";
    }

    /**
     * Turns a status letter into a word.
     */
    public static String statusWord(char c){
        if(c == 'A') return "accepted";
        if(c == 'C') return "completed";
        if(c == 'R') return "rejected";
        return "not answered yet";
    }

    /**
     * Turns a gender letter into a word.
     */
    public static String gender(char c){
        if(c == 'M') return "Male";
        if(c == 'F') return "Female";
        return "Other";
    }

    /**
     * Gets a user's first name.
     */
    public static String firstName(User u){
        if(u.getName() == null || u.getName().isEmpty()){
            return u.getUsername();
        }
        return u.getName().split(" ")[0];
    }

    /**
     * Lists the first names of several users, like "Noah and Ava".
     */
    public static String names(ArrayList<User> list){
        String s = "";
        for(int i = 0; i < list.size(); i++){
            if(i > 0){
                s += (i == list.size() - 1) ? " and " : ", ";
            }
            s += firstName(list.get(i));
        }
        return s;
    }

    /**
     * Gets a value from a map, or an empty string if it is missing.
     */
    public static String get(Map<String, String> m, String key){
        if(m == null || !m.containsKey(key) || m.get(key) == null){
            return "";
        }
        return m.get(key);
    }

    /**
     * The label and value of every detail an admin has to approve for an event.
     */
    public static String[][] specRows(Event e){
        String base = e.getListCategories();
        if(base.endsWith(", ")){
            base = base.substring(0, base.length() - 2);
        }
        String[][] common = {{"Title", e.getTitle()}, {"Date", e.getDate() == null ? "—" : "" + e.getDate()}, {"Start time", Html.time(e.getStartTime())},
            {"End time", Html.time(e.getEndTime())}, {"Base cost", Html.money(e.getOriginalCost())}, {"Categories", base.isEmpty() ? "none" : base}};
        String[][] extra;
        if(e instanceof Virtual){
            Virtual v = (Virtual)e;
            extra = new String[][]{{"Platform", v.getPlatform()}, {"Meeting ID", "" + v.getMeetingID()}, {"Password", "" + v.getPassword()}};
        }else if(e instanceof Outdoor){
            Outdoor o = (Outdoor)e;
            extra = new String[][]{{"Location", o.getLocation()}, {"Temperature", o.getTemperature() + "°C"}, {"Precipitation", o.getPrecipitation() + "%"}};
        }else{
            Indoor in = (Indoor)e;
            extra = new String[][]{{"Location", in.getLocation()}, {"Floor number", "" + in.getFloorNumber()}, {"Room", in.getRoom()}};
        }
        String[][] all = new String[common.length + extra.length][];
        for(int i = 0; i < common.length; i++){
            all[i] = common[i];
        }
        for(int i = 0; i < extra.length; i++){
            all[common.length + i] = extra[i];
        }
        return all;
    }
}
