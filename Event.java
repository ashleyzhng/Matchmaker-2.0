public abstract class Event {
    private String title;
    private String date;
    private String startTime;
    private double duration;
    private double originalCost;
    private double avgRating;

    private Tag categories;
    private boolean approved;
    private String eventAdminFeedback;
    private boolean booked;


    private User currentUser1;
    private User currentUser2;

    private boolean attended;
    private User[] attendanceHistory;

    public Event(String title, String date, String startTime, double duration, double originalCost, int r, int s, int m, int a, int t, int f, int g, int c, int an, int re) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
        this.originalCost = originalCost;
        this.categories = new Tag(r, s, m, a, t, f, g, c, an, re);
        this.approved = false;
        this.attended = false;
        this.attendanceHistory = new User[100];
        this.eventAdminFeedback = null;
        this.booked = false;

    }

    public User getCurrentUser1() {
        return currentUser1;
    }

    public void setCurrentUser1(User currentUser1) {
        this.currentUser1 = currentUser1;
    }

    public User getCurrentUser2() {
        return currentUser2;
    }

    public void setCurrentUser2(User currentUser2) {
        this.currentUser2 = currentUser2;
    }

    public Tag getCategories(){
        return categories;
    }

    public void setCategories(Tag categories) {
        this.categories = categories;
    }
    public String getStartTime(){
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public double getDuration(){
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getOriginalCost() {
        return originalCost;
    }
    
    public void setOriginalCost(double originalCost) {
        this.originalCost = originalCost;
    }

    public boolean getApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getEventAdminFeedback() {
        return eventAdminFeedback;
    }

    public void setEventAdminFeedback(String eventAdminFeedback) {
        this.eventAdminFeedback = eventAdminFeedback;
    }
    
    public boolean getBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public Event compareToTitle(Event other) {
        if (this.title.compareToIgnoreCase(other.title) > 0) {
            return this;
        } else if (this.title.compareToIgnoreCase(other.title) < 0) {
            return other;
        } else {
            return this;
        }
    }

    public Event compareToTime(Event other) {
        int yearthis = Integer.parseInt(this.date.substring(0, 4));
        int yearother = Integer.parseInt(other.date.substring(0, 4));
        int monththis = Integer.parseInt(this.date.substring(5, 7));
        int monthother = Integer.parseInt(other.date.substring(5, 7));
        int daythis = Integer.parseInt(this.date.substring(8, 10));
        int dayother = Integer.parseInt(other.date.substring(8, 10));
        int hourthis = Integer.parseInt(this.startTime.substring(0, 2));
        int hourother = Integer.parseInt(other.startTime.substring(0, 2));
        int minthis = Integer.parseInt(this.startTime.substring(3, 5));
        int minother = Integer.parseInt(other.startTime.substring(3, 5));
        if (yearthis < yearother) {
            return this;
        } else if (yearthis > yearother) {
            return other;
        } else { 
            if (monththis < monthother) {
                return this;
            } else if (monththis > monthother) {
                return other;
            } else {
                if (daythis < dayother) {
                    return this;
                } else if (daythis > dayother) {
                    return other;
                } else { 
                    if (hourthis < hourother) {
                        return this;
                    } else if (hourthis > hourother) {
                        return other;
                    } else { 
                        if (minthis < minother) {
                            return this;
                        } else if (minthis > minother) {
                            return other;
                        } else { 
                            return this;
                        }
                    }
                }
            }
        }
    }

    public abstract boolean addUser(User user);

    public abstract void changeEventDetails();

    public abstract String approveEvent();

    public abstract double calculateCost();

    public abstract void attendEvent(User user);

    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration=" + duration +
                ", originalCost=" + originalCost +
                ", categories=" + getCategories() +
                ", approved=" + approved +
                '}';
    }
}