import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DinnerParty {

    private List<String> guests; /*Wir brauchen eine Klasse für Einzelpersonen und dann wäre das hier eine List<Person>.
                                   Weil eine Person hier in diesem Fall ein Objekt mit verschiedenen Eigenschaften ist.
                                   Eine Person kann hier nicht einfach ein String sein.
                                   */
    private List<String> courses; /*Sinnvoll? Ist es sinnvoll eine List an Gängen zu haben, wenn es eh immer nur drei Gänge gibt?
                                    Vielleicht ist hier ein Enum besser?*/
    private List<String> kitchens; /*Sinnvoll? Informationen über die Küche kommen schon mit der Klasse der Einzelperson*/

    public DinnerParty(List<String> guests, List<String> courses, List<String> kitchens) {
        this.guests = guests;
        this.courses = courses;
        this.kitchens = kitchens;
    }

    public void start() {
        List<List<String>> teams = createTeams(); /*besserer Name wäre meiner Meinung pairs, weil in Anforderungsbeschreibung auch von
                                                    Paaren gesprochen wird.*/
        List<String> routes = createRoutes(teams);
        sendInfo(teams, routes);
    }

    private List<List<String>> createTeams() {
        List<List<String>> teams = new ArrayList<>(); // Vielleicht sollte es für Paare auch eine extra Klasse geben?
        Random rand = new Random();
        int teamSize = 2; /* two-person teams; kann meiner meinung als globale variable festgesetzt werden, da ein Paar ja immer
                            aus zwei Personen besteht.*/

        // shuffle the guest list to randomize teams
        List<String> shuffledGuests = new ArrayList<>(guests);
        Collections.shuffle(shuffledGuests,rand);

        // create teams of two
        /*
            Zeile 57 - 62 Zuteilung noch nicht wirklich sinnvoll, da ja auch bei der Erstellung der Pärchen auf Vorlieben,
            Ort (z.B. ob einer der beiden eine Küche hat), Altersdifferenz geachtet werden muss.

            Meiner Meinung sollte es vielleicht eine List der Veganer geben, eine Liste der Vegetarierer, eine Liste der Egalis
            und eine Liste der Fleischis. Dann werden erst aus der Liste der Veganer alle die keine Küche haben,
            mit jemanden zusammengetan der eine Küche hat. Und dies wird noch so getan, dass die Altersdifferenz zwischen
            den Partnern nicht zu groß ist. Dann werden die übrigen mit Beachtung der Altersdifferenz zugeteilt.

            Das Gleiche macht man mit der Liste der Fleischis, der Egalis und der Vegetarierer.

            Zum Ende hin werden die übrig gebliebenen Leute aus den Listen miteinander vermischt. Hier kommen auch erst die
            ohne Küche mit denjenigen zusammen, die eine Küche haben und dann der Rest. Jedes Paar hat dann entweder eine Küche,
            oder zwei, dies spielt dann eine Rolle beim Berechnen der Route.
         */
        for (int i = 0; i < shuffledGuests.size(); i += teamSize) {
            List<String> team = new ArrayList<>();
            team.add(shuffledGuests.get(i));
            if (i + 1 < shuffledGuests.size()) {
                team.add(shuffledGuests.get(i + 1));
            } else {
                team.add(""); // add an empty string if there's an odd number of guests; die übrig gebliebene Person muss eine Absage bekommen
            }
            teams.add(team);
        }

        // randomly assign teams to courses
        Collections.shuffle(courses,rand);
        for (int i = 0; i < teams.size(); i++) {
            List<String> team = teams.get(i); //wie gesagt, wahrscheinlich ist eine Klasse "Pairs" hier gut geeignet.
            team.add(courses.get(i));
        }

        // randomly assign teams to kitchens
        /*
            Es ist definitiv nicht sinnvoll Paare zufällig Küchen zuzuordnen. Mindestens einer Person aus dem Paar muss die
            Küche ja gehören. Kann schon in dem Berechnen der Paare erledigt werden, siehe Beschreibung oben.
         */
        Collections.shuffle(kitchens,rand);
        for (int i = 0; i < teams.size(); i++) {
            List<String> team = teams.get(i);
            team.add(kitchens.get(i));
        }

        return teams;
    }

    /*
        Hier muss dann ja erstmal mithilfe der Koordinaten berechnet werden, wie weit die Küchen auseinanderliegen
        und irgendwie der beste Weg ausgewählt werden (Pathfinding algorithmus). Das sollte am besten bei der Erstellung
        der Gruppen passieren, damit sichergestellt wird, dass die Gruppen einen möglichst angenehmen Weg zu laufen haben.

        Vielleicht ist es auch gut eine Klasse für Route zu haben. So, dass man jeder Route eine Kilometerzahl zuordnen kann
        um am Ende die Routen vergleichen zu können.
     */
    private List<String> createRoutes(List<List<String>> teams) {
        List<String> routes = new ArrayList<>();
        for (List<String> team : teams) {
            String route = String.format("%s and %s are cooking the %s in %s",
                    team.get(0), team.get(1), team.get(2), team.get(3));
            routes.add(route);
        }
        return routes;
    }

    /*
    Eh noch nicht relevant. Aber sendet ja auch nicht wirklich eine E-Mail? Funktionalität dieser Methode?
     */
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