import java.util.Scanner;

public class EventMenu {
    Scanner sc = new Scanner(System.in);
    public void getEventScreen() {
        System.out.println("EVENT OPTIONS:");
        System.out.println("1. Create Event");
        System.out.println("2. Choose Pre-existing Event");
        int choice = sc.nextInt();

        while (choice != 1 && choice != 2) {
            System.out.println("That is not a valid choice");
            System.out.println("1. Create Event");
            System.out.println("2. Choose Pre-existing Event");
            choice = sc.nextInt();
        }

        if (choice == 1) {
            System.out.println("Which of the following types of events do you wanna create?");
            System.out.println("1. Virtual");
            System.out.println("2. Outdoor");
            System.out.println("3. Indoor");
            choice = sc.nextInt();

            while (choice < 1 || choice > 3){
                System.out.println("That is an invalid choice");
                System.out.println("Which of the following types of events do you wanna create?");
                System.out.println("1. Virtual");
                System.out.println("2. Outdoor");
                System.out.println("3. Indoor");
                choice = sc.nextInt();
            }
            if (choice == 1) {
                sc.nextLine();
                System.out.println("Enter title: ");
                String title = sc.nextLine();
                System.out.println("Enter starting time (HH:MM): ");
                String startTime = sc.nextLine();
                System.out.println("Enter duration (in hours): ");
                double duration = sc.nextDouble();
                System.out.println("Enter base cost $: ");
                double originalCost = sc.nextDouble();
                System.out.println("For the following categories, enter 0 for N/A, enter 1 for applied: ");
                System.out.print("Religion (0/1): ")
                int r = sc.nextInt();
                System.out.print("Sports(0/1): ")
                int s = sc.nextInt();
                System.out.print("Music (0/1): ")
                int m = sc.nextInt();
                System.out.print("Art (0/1): ")
                int a = sc.nextInt();
                System.out.print("Traveling (0/1): ")
                int t = sc.nextInt();
                System.out.print("Religion (0/1): ")
                int r = sc.nextInt();
                System.out.print("Religion (0/1): ")
                int r = sc.nextInt();
                System.out.print("Religion (0/1): ")
                int r = sc.nextInt();
                System.out.print("Religion (0/1): ")
                int r = sc.nextInt();
                System.out.print("Religion (0/1): ")
                int r = sc.nextInt();
                System.out.print("Religion (0/1): ")
                int r = sc.nextInt();
        fitness = f;
        gaming = g;
        cooking = c;
        animal = an;
        reading = re;
                System.out.println("Enter title: ");
                System.out.println("Enter title: ");
                private String title;
    private String date;
    private String startTime;
    private double duration;
    private double originalCost;


    private Tag categories;
            } else if (choice == 2) {

            } else {

            }
        }
    }
}
