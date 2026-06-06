public class Outdoor extends Event {
    private String location; //note: This needs to be the postal code
    private int temperature;
    private int precipitation;

    public Outdoor(String title, String date, String startTime, double duration, double originalCost, Tag[] categories, String location, int temperature, int precipitation) {
        super(title, date, startTime, duration, originalCost, categories);
        this.location = location;
        this.temperature = temperature;
        this.precipitation = precipitation;
    }

    public String getLocation(){
        return location;
    }

    public void changeEventDetails() {
        // Implementation for changing event details specific to outdoor events
    }

    public String approveEvent() {
        // Implementation for approving the outdoor event
        return "Outdoor event approved.";
    }

    public boolean attendEvent(User user) {
        // Implementation for attending the outdoor event
        return true; // Placeholder return value
    }

    public boolean cancelEvent(Event event) {
        // Implementation for canceling the outdoor event
        return true; // Placeholder return value
    }

    public double calculateCost() {
        // Implementation for calculating the cost of the outdoor event
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
        return "Outdoor Details:" +
                "Location:'" + location + '\'' +
                ", Temperature:" + temperature +
                ", Precipitation:" + precipitation +
                "} " + super.toString();
    }
}