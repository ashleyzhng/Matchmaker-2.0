public class Indoor extends Event {
    private String location; //Note: This needs to be the postal code
    private int floorNumber;
    private String room;
    private String user3;
    private String user4;

    public Indoor(String title, String date, String startTime, double duration, double originalCost, Tag categories, String location, int floorNumber, String room) {
        super(title, date, startTime, duration, originalCost, categories);
        this.location = location;
        this.floorNumber = floorNumber;
        this.room = room;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public int getFloorNumber(){
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber){
        this.floorNumber = floorNumber;
    }

    public String getRoom(){
        return room;
    }

    public void setRoom(String room){
        this.room = room;
    }

    public String getUser3() {
        return user3;
    }

    public void setUser3(String user3) {
        this.user3 = user3;
    }

    public String getUser4() {
        return user4;
    }

    public void setUser4(String user4) {
        this.user4 = user4;
    }

    public void changeEventDetails() {
        // Implementation for changing event details specific to indoor events
    }

    public String approveEvent() {
        // Implementation for approving the indoor event
        return "Indoor event approved.";
    }

    public boolean attendEvent(User user) {
        // Implementation for attending the indoor event
        return true; // Placeholder return value
    }

    public boolean cancelEvent(Event event) {
        // Implementation for canceling the indoor event
        return true; // Placeholder return value
    }

    public double calculateCost() {
        // Implementation for calculating the cost of the indoor event
        return getOriginalCost(); // Placeholder return value
    }

    /*private String[] getCategoryNames() {
        Tag[] categories = getCategories();
        String[] categoryNames = new String[categories.length];
        for (int i = 0; i < categories.length; i++) {
            categoryNames[i] = categories[i].getName();
        }
        return categoryNames;
    }
    */

    public String toString() {
        return "Indoor Details:" +
                "Location:'" + location + '\'' +
                ", Floor Number:" + floorNumber +
                ", Room:'" + room + '\'' +
                "} " + super.toString();
    }
    
}