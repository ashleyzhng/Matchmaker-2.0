import java.util.*;
import java.time.*;
public class Outdoor extends Event {
    private Scanner sc = new Scanner(System.in);
    private String location; //note: This needs to be the postal code
    private int temperature;
    private int precipitation;

    public Outdoor(String title, LocalDate date, LocalTime startTime, LocalTime endTime, double originalCost, int r, int s, int m, int a, int t, int f, int g, int c, int an, int re, User initialUser, String location, int temperature, int precipitation) {
        super(title, date, startTime, endTime, originalCost, r, s, m, a, t, f, g, c, an, re, initialUser);
        this.location = location;
        this.temperature = temperature;
        this.precipitation = precipitation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(int precipitation) {
        this.precipitation = precipitation;
    }

    public double cancelEvent() {
        setApproved(false);
        setBooked(false);

        if (precipitation > 60) {
            return getOriginalCost() * 0.2; 
        }
        return getOriginalCost() * 0.7;
    }

    public boolean addUser(User user) {
        if (getCurrentUser1() == null) {
            setCurrentUser1(user);
            return true;
        } else if (getCurrentUser2() == null) {
            setCurrentUser2(user);
            setBooked(true);
            return true;
        } else {
            System.out.println("Event is already fully booked.");
            return false;
        }
    }

    public boolean autoApproveEvent() {
        if (temperature < -25 || precipitation > 80) {
        setApproved(false);
        setEventAdminFeedback("Rejected: unsafe weather conditions");
        return false;
    }
        return true;
    }

    public double calculateCost() {
        if (precipitation > 70) {
            return getOriginalCost() * 0.6; // bad weather discount
        } else if (precipitation < 20) {
            return getOriginalCost() * 1.2; // high demand / good weather
        }
        return getOriginalCost();
    }

    public String toString() {
        return "Outdoor Details:" +
                "Location:'" + location + '\'' +
                ", Temperature:" + temperature +
                ", Precipitation:" + precipitation +
                "} " + super.toString();
    }
}