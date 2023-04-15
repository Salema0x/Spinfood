import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DinnerParty {

    private List<String> guests;
    private List<String> courses;
    private List<String> kitchens;

    public DinnerParty(List<String> guests, List<String> courses, List<String> kitchens) {
        this.guests = guests;
        this.courses = courses;
        this.kitchens = kitchens;
    }

    public void start() {
        List<List<String>> teams = createTeams();
        List<String> routes = createRoutes(teams);
        sendInfo(teams, routes);
    }

    private List<List<String>> createTeams() {
        List<List<String>> teams = new ArrayList<>();
        Random rand = new Random();
        int teamSize = 2; // two-person teams

        // shuffle the guest list to randomize teams
        List<String> shuffledGuests = new ArrayList<>(guests);
        Collections.shuffle(shuffledGuests,rand);

        // create teams of two
        for (int i = 0; i < shuffledGuests.size(); i += teamSize) {
            List<String> team = new ArrayList<>();
            team.add(shuffledGuests.get(i));
            if (i + 1 < shuffledGuests.size()) {
                team.add(shuffledGuests.get(i + 1));
            } else {
                team.add(""); // add an empty string if there's an odd number of guests
            }
            teams.add(team);
        }

        // randomly assign teams to courses
        Collections.shuffle(courses,rand);
        for (int i = 0; i < teams.size(); i++) {
            List<String> team = teams.get(i);
            team.add(courses.get(i));
        }

        // randomly assign teams to kitchens
        Collections.shuffle(kitchens,rand);
        for (int i = 0; i < teams.size(); i++) {
            List<String> team = teams.get(i);
            team.add(kitchens.get(i));
        }

        return teams;
    }

    private List<String> createRoutes(List<List<String>> teams) {
        List<String> routes = new ArrayList<>();
        for (List<String> team : teams) {
            String route = String.format("%s and %s are cooking the %s in %s",
                    team.get(0), team.get(1), team.get(2), team.get(3));
            routes.add(route);
        }
        return routes;
    }

    private void sendInfo(List<List<String>> teams, List<String> routes) {
        // send routes and other relevant information to each team's email address
        for (int i = 0; i < teams.size(); i++) {
            List<String> team = teams.get(i);
            String email = String.format("irgendwas@example.com", team.get(0).toLowerCase());
            String subject = String.format("Your Dinner Party Route for irgendwas", team.get(2));
            String body = String.format("Dear bla and bla,\n\n"
                            + "Your dinner party route is as follows:\n\n"
                            + "Your kitchen is located at %s.\n"
                            + "Please arrive by 6:00pm to start preparing your meal.\n\n"
                            + "Best,\n"
                            + "The Spinfood Team",
                    team.get(0), team.get(1), routes.get(i), team.get(3), team.get(2));
            System.out.printf("Sending email to XY with subject XZ");
        }
    }
}