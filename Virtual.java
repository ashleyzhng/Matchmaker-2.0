public class Virtual extends Event {
    private String platform;
    private int meetingID;
    private int password;

    public Virtual(String title, String date, String startTime, double duration, double originalCost, Tag[] categories, String platform, int meetingID, int password) {
        super(title, date, startTime, duration, originalCost, categories);
        this.platform = platform;
        this.meetingID = meetingID;
        this.password = password;
    }

    public void changeEventDetails() {
        // Implementation for changing event details specific to virtual events
    }

    public String approveEvent() {
        // Implementation for approving the virtual event
        return "Virtual event approved.";
    }

    public boolean attendEvent(User user) {
        // Implementation for attending the virtual event
        return true; // Placeholder return value
    }

    public boolean cancelEvent(Event event) {
        // Implementation for canceling the virtual event
        return true; // Placeholder return value
    }

    public double calculateCost() {
        // Implementation for calculating the cost of the virtual event
        return getOriginalCost(); // Placeholder return value
    }
    private String[] getCategoryNames() {
        Tag[] categories = getCategories();
        String[] categoryNames = new String[categories.length];
        for (int i = 0; i < categories.length; i++) {
            categoryNames[i] = categories[i].getName();
        }
        return categoryNames;
    }
    public String toString() {
        return "Virtual Details:" +
                "Platform:'" + platform + '\'' +
                ", Meeting ID:" + meetingID +
                ", Password:" + password +
                "} " + super.toString();
    }
}