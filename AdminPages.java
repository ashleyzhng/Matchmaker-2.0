import java.util.*;
/**
 * ----------------------------------------------------------------------------
 * File Name:    AdminPages.java
 * Authors:      Ashley Zhong
 * Course:       ICS4U1
 * Description:  Builds the HTML for the admin pages: dashboard, users,
 *               reports, event review and all events.
 * ----------------------------------------------------------------------------
 */
public class AdminPages {
    private Matchmaker mm;
    private Preferences prefs;

    /**
     * Creates the admin page builder.
     */
    public AdminPages(Matchmaker m, Preferences p){
        mm = m;
        prefs = p;
    }

    private String layout(Admin a, String active, String flash, String content){
        return Html.layout(a, prefs.getTheme(a.getUsername()), prefs.getAvatar(a.getUsername()), active, flash, content);
    }

    /**
     * Counts the users in one age group ("Teen", "Adult" or "Senior").
     */
    private int count(String group){
        int n = 0;
        User[] users = mm.getAccounts().getUsers();
        for(int i = 0; i < users.length; i++){
            if(users[i] != null && Html.ageGroup(users[i]).equals(group)){
                n++;
            }
        }
        return n;
    }

    /**
     * Gets the pending events that still need an admin decision.
     */
    public ArrayList<Event> waiting(){
        ArrayList<Event> list = new ArrayList<Event>();
        EventDatabase ev = mm.getEvents();
        Event[] pend = ev.getPendingEventList();
        for(int i = 0; i < ev.getNumPendingEvents(); i++){
            if(pend[i] != null && pend[i].getEventAdminFeedback() == null){
                list.add(pend[i]);
            }
        }
        return list;
    }

    /**
     * The admin dashboard.
     */
    public String dashboard(Admin a, String flash){
        int admins = mm.getAccounts().getAccounts().size() - count("Teen") - count("Adult") - count("Senior");
        ArrayList<Appeal> appeals = mm.getAppeals().getAppeals();
        ArrayList<Event> wait = waiting();
        String c = Html.head("admin", "dashboard", "Review new events, handle reports, and manage accounts.", Html.character("brand_cupid", "width:96px;height:96px"));
        c += "<div class=\"grid grid-4\">" + Html.stat("Accounts", "" + mm.getAccounts().getAccounts().size(), (count("Teen") + count("Adult") + count("Senior")) + " users · " + admins + " admins")
            + Html.stat("Open reports", "" + appeals.size(), appeals.size() == 0 ? "All clear" : "Needs a decision")
            + Html.stat("Waiting for review", "" + wait.size(), "Pending events")
            + Html.stat("Approved events", "" + mm.getEvents().getNumApprovedEvents(), "Of " + EventDatabase.getMaxApprovedEvents() + " max") + "</div>";
        String rows = "";
        for(int i = 0; i < wait.size() && i < 5; i++){
            Event e = wait.get(i);
            rows += "<div class=\"list-row\"><div class=\"grow\"><strong>" + Html.esc(e.getTitle()) + "</strong><p class=\"muted small\">" + Html.eventKind(e)
                + " · hosted by " + (e.getCurrentUser1() == null ? "—" : "@" + Html.esc(e.getCurrentUser1().getUsername())) + " · " + Html.day(e.getDate()) + " · " + Html.money(e.getOriginalCost())
                + "</p></div><a class=\"btn btn-primary\" href=\"/admin/review?t=" + Html.url(e.getTitle()) + "\">Review</a></div>";
        }
        c += "<div class=\"row\" style=\"align-items:flex-start;gap:20px\"><div class=\"card stack\" style=\"flex:1\"><h2>events waiting for review</h2>"
            + (rows.isEmpty() ? "<p class=\"muted\">Nothing to review.</p>" : "<div>" + rows + "</div>") + "</div>";
        String reps = "";
        for(int i = 0; i < appeals.size() && i < 3; i++){
            Appeal ap = appeals.get(i);
            if(ap.getUser() == null) continue;
            reps += "<div class=\"card soft stack\" style=\"gap:4px;padding:16px\"><strong>#" + (i + 1) + " · @" + Html.esc(ap.getUser().getUsername()) + "</strong><span class=\"muted small\">"
                + Html.esc(ap.getReason().isEmpty() ? "No reason provided" : ap.getReason()) + "</span></div>";
        }
        c += "<div class=\"card stack\" style=\"width:360px\"><h2>reports</h2>" + (reps.isEmpty() ? "<p class=\"muted\">No open reports.</p>" : reps)
            + "<a class=\"btn btn-secondary\" href=\"/admin/reports\">See all reports</a></div></div>";
        return layout(a, "Dashboard", flash, c);
    }

    /**
     * The list of users, filtered by age group or searched by name.
     */
    public String users(Admin a, String flash, Map<String, String> q){
        String group = UserPages.get(q, "group");
        String search = UserPages.get(q, "q").trim();
        AccountsDatabase db = mm.getAccounts();
        ArrayList<User> list = new ArrayList<User>();
        if(!search.isEmpty()){
            // use the database's own searches: username, first name and last name
            User[] found = {db.searchByUsername(search), db.searchByFirstName(search), db.searchByLastName(search)};
            for(int i = 0; i < found.length; i++){
                if(found[i] != null && !list.contains(found[i])){
                    list.add(found[i]);
                }
            }
        }else{
            User[] sorted = db.sortByUserType(db.getAccounts());
            for(int i = 0; i < sorted.length; i++){
                if(sorted[i] != null && (group.isEmpty() || Html.ageGroup(sorted[i]).equalsIgnoreCase(group))){
                    list.add(sorted[i]);
                }
            }
        }
        int admins = db.getAccounts().size() - count("Teen") - count("Adult") - count("Senior");
        String c = Html.head("admin", "all users", db.getAccounts().size() + " accounts. Filter by age group, look someone up, promote or remove them.", null);
        c += "<div class=\"grid grid-4\">" + Html.stat("Teens", "" + count("Teen"), "Ages 13–17") + Html.stat("Adults", "" + count("Adult"), "Ages 18–64")
            + Html.stat("Seniors", "" + count("Senior"), "Ages 65+") + Html.stat("Admins", "" + admins, "Can review events and reports") + "</div>";
        c += "<div class=\"row between\"><form class=\"row\" method=\"get\" action=\"/admin/users\" style=\"flex:1\"><input type=\"text\" name=\"q\" value=\"" + Html.esc(search)
            + "\" placeholder=\"Search by username, first or last name\" style=\"max-width:380px\"><button class=\"btn btn-secondary\">Search</button></form><div class=\"tabs\">"
            + tab("", "All", group) + tab("teen", "Teen", group) + tab("adult", "Adult", group) + tab("senior", "Senior", group) + "</div></div>";
        c += "<div class=\"card\"><table><tr><th>User</th><th>Age group</th><th>Age</th><th>City</th><th>Preferred age</th><th>Mobility limits</th><th></th></tr>";
        for(int i = 0; i < list.size(); i++){
            User u = list.get(i);
            String pref = "Teens only";
            String mob = "—";
            if(u instanceof Adult){
                pref = ((Adult)u).getPreferredMinAge() + " – " + ((Adult)u).getPreferredMaxAge();
            }else if(u instanceof Senior){
                pref = ((Senior)u).getPreferredMinAge() + " – " + ((Senior)u).getPreferredMaxAge();
                mob = ((Senior)u).hasMobilityLimitations() ? "Yes" : "No";
            }
            c += "<tr><td><div class=\"user\">" + Html.avatar(prefs.getAvatar(u.getUsername()), "s36") + "<div><strong>" + Html.esc(u.getName()) + "</strong><br><span class=\"muted small\">@"
                + Html.esc(u.getUsername()) + "</span></div></div></td><td>" + Html.groupPill(u, false) + "</td><td class=\"muted\">" + Html.age(u) + "</td><td class=\"muted\">" + Html.esc(u.getCity())
                + "</td><td class=\"muted\">" + pref + "</td><td class=\"muted\">" + mob + "</td><td style=\"text-align:right\"><a class=\"btn btn-ghost\" href=\"/admin/user?u=" + Html.url(u.getUsername()) + "\">View</a>"
                + Html.postButton("/admin/promote", "btn-ghost", "Promote", Html.hidden("username", u.getUsername())) + "</td></tr>";
        }
        if(list.isEmpty()){
            c += "<tr><td colspan=\"7\" class=\"muted\">No users found.</td></tr>";
        }
        c += "</table></div>";
        return layout(a, "Users", flash, c);
    }

    private String tab(String value, String label, String current){
        return "<a href=\"/admin/users" + (value.isEmpty() ? "" : "?group=" + value) + "\"" + (value.equalsIgnoreCase(current) ? " class=\"on\"" : "") + ">" + label + "</a>";
    }

    /**
     * Shows everything about one user.
     */
    public String userView(Admin a, String flash, User u){
        String c = "<div class=\"hero\">" + Html.avatar(prefs.getAvatar(u.getUsername()), "s132") + "<div class=\"grow stack\" style=\"gap:6px\"><div class=\"row\"><h1>"
            + Html.esc(u.getName().toLowerCase()) + "</h1>" + Html.groupPill(u, true) + "</div><p class=\"muted\">@" + Html.esc(u.getUsername()) + " · " + Html.esc(u.getCity()) + "</p></div>"
            + "<div class=\"row\"><a class=\"btn btn-secondary\" href=\"/admin/users\">Back</a>" + Html.postButton("/admin/promote", "btn-primary", "Promote to admin", Html.hidden("username", u.getUsername())) + "</div></div>";
        c += "<div class=\"card\">" + UserPages.row("Email", u.getEmail() + (u.isEmailVerified() ? " (verified)" : " (not verified)")) + UserPages.row("Birthday", "" + u.getBirthday())
            + UserPages.row("Postal code", u.getPostalCode()) + UserPages.row("Bio", u.getBio()) + UserPages.row("Gender", UserPages.gender(u.getGender()))
            + UserPages.row("Interested in", UserPages.gender(u.getPreferredGender())) + UserPages.row("Active matches", u.getNumMatches() + " / 5")
            + UserPages.row("Past matches", "" + (u.getMatchHistory() == null ? 0 : u.getMatchHistory().size()));
        if(u instanceof Adult){
            c += UserPages.row("Occupation", ((Adult)u).getOccupation()) + UserPages.row("Marital status", ((Adult)u).getMaritalStatus());
        }else if(u instanceof Senior){
            c += UserPages.row("Occupation", ((Senior)u).getOccupation()) + UserPages.row("Marital status", ((Senior)u).getMaritalStatus())
                + UserPages.row("Mobility limitations", ((Senior)u).hasMobilityLimitations() ? "Yes" : "No");
        }
        c += "</div>";
        return layout(a, "Users", flash, c);
    }

    /**
     * The list of reports users have sent.
     */
    public String reports(Admin a, String flash){
        ArrayList<Appeal> list = mm.getAppeals().getAppeals();
        String c = Html.head("admin · " + list.size() + " open", "reports", "Reports users send about each other. Resolving a report deletes the reported account.", null);
        if(list.isEmpty()){
            c += "<div class=\"card soft row\">" + Html.character("avatar_bunny", "width:90px;height:90px") + "<p class=\"muted\">No reports to review. Everyone is behaving!</p></div>";
        }
        for(int i = 0; i < list.size(); i++){
            Appeal ap = list.get(i);
            User u = ap.getUser();
            if(u == null) continue;
            c += "<div class=\"card stack\"><div class=\"row\">" + Html.avatar(prefs.getAvatar(u.getUsername()), "s48") + "<div class=\"stack\" style=\"gap:3px;flex:1\"><div class=\"row\"><strong style=\"font-size:18px\">#"
                + (i + 1) + " · " + Html.esc(u.getName()) + "</strong>" + Html.groupPill(u, false) + "</div><span class=\"muted small\">@" + Html.esc(u.getUsername()) + "</span></div>" + Html.badge("pending", "Open") + "</div>"
                + "<div class=\"card soft\" style=\"padding:16px\"><span class=\"script\" style=\"color:var(--text)\">“" + Html.esc(ap.getReason().isEmpty() ? "No reason provided" : ap.getReason()) + "”</span></div>"
                + "<div class=\"row\" style=\"justify-content:flex-end\"><a class=\"btn btn-secondary\" href=\"/admin/user?u=" + Html.url(u.getUsername()) + "\">View profile</a>"
                + Html.postButton("/admin/resolve", "btn-primary", "Resolve · delete user", Html.hidden("i", i)) + "</div></div>";
        }
        return layout(a, "Reports", flash, c);
    }

    /**
     * The event review page: a queue on the left and the selected event on the right.
     *
     * @param selected The event being reviewed, or null
     */
    public String review(Admin a, String flash, Event selected){
        EventDatabase ev = mm.getEvents();
        ArrayList<Event> wait = waiting();
        String c = Html.head("admin · " + wait.size() + " waiting", "event review", "Each event is checked by the system first, then you approve it one detail at a time.", null);
        String queue = "<div class=\"stack\" style=\"width:320px;gap:10px\"><h2>queue</h2>";
        Event[] pend = ev.getPendingEventList();
        for(int i = 0; i < ev.getNumPendingEvents(); i++){
            Event e = pend[i];
            if(e == null) continue;
            boolean sel = e == selected;
            String st = e.getEventAdminFeedback() == null ? Html.badge("pending", "Waiting") : Html.badge("rejected", "Rejected");
            queue += "<a class=\"card row\" href=\"/admin/review?t=" + Html.url(e.getTitle()) + "\" style=\"padding:14px;gap:10px;color:var(--text);text-decoration:none;"
                + (sel ? "background:var(--soft);border:1.5px solid var(--primary-dark)" : "") + "\"><div class=\"stack\" style=\"gap:2px;flex:1\"><strong>" + Html.esc(e.getTitle())
                + "</strong><span class=\"muted small\">" + Html.eventKind(e) + " · " + (e.getCurrentUser1() == null ? "—" : Html.esc(e.getCurrentUser1().getUsername())) + "</span></div>" + st + "</a>";
        }
        queue += ev.getNumPendingEvents() == 0 ? "<p class=\"muted\">No pending events.</p>" : "";
        queue += "</div>";
        String detail;
        if(selected == null){
            detail = "<div class=\"card soft row\" style=\"flex:1\">" + Html.character("avatar_painter", "width:90px;height:90px") + "<p class=\"muted\">Pick an event from the queue to review it.</p></div>";
        }else if(selected.getEventAdminFeedback() != null){
            detail = "<div class=\"card stack\" style=\"flex:1\"><h2>" + Html.esc(selected.getTitle().toLowerCase()) + "</h2><div class=\"flash err\">Rejected: " + Html.esc(selected.getEventAdminFeedback())
                + "</div><p class=\"muted\">The host can edit the event from their Events page and it will come back here.</p></div>";
        }else{
            detail = "<form class=\"card stack\" method=\"post\" action=\"/admin/review\" style=\"flex:1\">" + Html.hidden("t", selected.getTitle())
                + "<h2>" + Html.esc(selected.getTitle().toLowerCase()) + "</h2><div class=\"flash ok\">Passed system checks. Admin review available.</div><strong class=\"small\">Approve each detail</strong>";
            String[][] rows = UserPages.specRows(selected);
            for(int i = 0; i < rows.length; i++){
                detail += "<div class=\"list-row\"><span class=\"muted\" style=\"width:120px\">" + rows[i][0] + "</span><strong class=\"grow\">" + Html.esc(rows[i][1]) + "</strong>"
                    + "<div style=\"width:150px\">" + UserPages.segmented("spec" + i, new String[]{"y", "n"}, new String[]{"Yes", "No"}, "y") + "</div></div>";
            }
            detail += "<label class=\"field\">If you said No, why was this event rejected?<textarea name=\"reason\" placeholder=\"Please use Zoom or Discord so everyone can join.\"></textarea></label>"
                + Html.hidden("count", rows.length)
                + "<div class=\"row\" style=\"justify-content:flex-end\"><a class=\"btn btn-secondary\" href=\"/admin/review\">Skip for now</a><button class=\"btn btn-primary\">Submit review</button></div></form>";
        }
        c += "<div class=\"row\" style=\"align-items:flex-start;gap:20px\">" + queue + detail + "</div>";
        return layout(a, "Event Review", flash, c);
    }

    /**
     * Lists all approved and pending events.
     */
    public String events(Admin a, String flash){
        EventDatabase ev = mm.getEvents();
        ev.sortEventByAlphaTitle();
        String c = Html.head("admin", "all events", ev.getNumApprovedEvents() + " approved · " + ev.getNumPendingEvents() + " pending", null);
        c += "<div class=\"card\"><h2 style=\"margin-bottom:8px\">approved</h2><table><tr><th>Title</th><th>Type</th><th>Date</th><th>Cost</th><th>Booked</th><th></th></tr>";
        Event[] appr = ev.getApprovedEventList();
        for(int i = 0; i < ev.getNumApprovedEvents(); i++){
            c += eventRow(appr[i]);
        }
        c += "</table></div><div class=\"card\"><h2 style=\"margin-bottom:8px\">pending</h2><table><tr><th>Title</th><th>Type</th><th>Date</th><th>Cost</th><th>Booked</th><th></th></tr>";
        Event[] pend = ev.getPendingEventList();
        for(int i = 0; i < ev.getNumPendingEvents(); i++){
            c += eventRow(pend[i]);
        }
        c += "</table></div>";
        return layout(a, "All Events", flash, c);
    }

    private String eventRow(Event e){
        if(e == null){
            return "";
        }
        return "<tr><td><strong>" + Html.esc(e.getTitle()) + "</strong></td><td class=\"muted\">" + Html.eventKind(e) + "</td><td class=\"muted\">" + Html.day(e.getDate())
            + "</td><td class=\"muted\">" + Html.money(e.getOriginalCost()) + "</td><td class=\"muted\">" + Html.attendees(e) + " / " + Html.capacity(e)
            + "</td><td style=\"text-align:right\"><a class=\"btn btn-ghost\" href=\"/events/view?t=" + Html.url(e.getTitle()) + "\">View</a></td></tr>";
    }
}
