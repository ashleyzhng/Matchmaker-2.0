import java.util.*;

public class MatchmakerRunner{
    Matchmaker system = new Matchmaker();
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Matchmaker!");
        boolean running = true;

        while(running){
            System.out.println("Main menu");
            System.out.println("1. Find an Event Match");
            System.out.print("3. View Current Matches");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (number): ");
            int response = sc.nextInt();

            if(response == 4){
                running = false;
            }

        }
        
    }
}