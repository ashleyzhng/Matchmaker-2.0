public abstract class Event {
    private String title;
    private String date;
    private String startTime;
    private double duration;
    private double originalCost;
    //changed from object array to singular object
    private Tag categories;
    private boolean approved;

    //I changed this to User object - ASHLEY
    private User currentUser1;
    private User currentUser2;

    private boolean attended;
    private User[] attendanceHistory;

    public Event(String title, String date, String startTime, double duration, double originalCost, Tag categories) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
        this.originalCost = originalCost;
        this.categories = categories;
        this.approved = false; // Default to not approved
        this.attended = false; // Default to not attended
    }

    //ADDED THIS-ASHLEY
    public void setUser(User u){
        if(currentUser1 == null){
            currentUser1 = u;
        }else{
            currentUser2 = u;
        }
    }
    //ADDED THIS-ASHLEY
    public Tag getCategories(){
        return categories;
    }
    public String getStartTime(){
        return startTime;
    }
    public double getDuration(){
        return duration;
    }
    public String getDate(){
        return date;
    }
    public String getTitle(){
        return title;
    }

    public abstract void changeEventDetails();
    public abstract String approveEvent();
    public abstract boolean attendEvent(User user);
    public abstract boolean cancelEvent(Event event);
    public abstract double calculateCost();
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration=" + duration +
                ", originalCost=" + originalCost +
                ", categories=" + String.join(", ", getCategoryNames()) +
                ", approved=" + approved +
                '}';
    }
}