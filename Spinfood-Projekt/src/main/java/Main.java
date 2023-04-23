import Entity.Participant;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //entry point

        String fileName = "testliste.csv"; // Name der CSV-Datei

        // Aufruf der readCSV-Funktion
        try {
            HashMap<String, Participant> participants = readCSV(fileName);
            //Ausgabe der HashMap mit den Personen auf der Konsole (TODO)
            printHashMap(participants);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, Participant> readCSV(String fileName) throws URISyntaxException {
        // Hash-Tabelle von Participants
        HashMap<String, Participant> participants = new HashMap<String, Participant>();

        URL resource = Main.class.getResource(fileName);

        // Datei öffnen
        File file = Paths.get(resource.toURI()).toFile();
        try {
            Scanner inputStream = new Scanner(file);
            inputStream.nextLine(); // Erste Zeile überspringen (Spaltenüberschriften)

            // Zeilenweises Lesen der Datei
            while(inputStream.hasNext()){
                String data = inputStream.nextLine();
                String[] values = data.split(","); // Trennzeichen ist ","
                // Erstellen eines neuen Participant-Objekts
                Participant participant = new Participant();
                participant.setId(values[1]);
                participant.setName(values[2]);
                participant.setFoodPreference(values[3]);
                participant.setAge(Integer.parseInt(values[4]));
                participant.setSex(values[5]);
                participant.setKitchen(values[6]);
                participant.setKitchenStory(values[7]);
                participant.setKitchenLongitude(Double.parseDouble(values[8]));
                participant.setKitchenLatitude(Double.parseDouble(values[9]));
                //Manche Werte der CSV sind nicht bei jedem Participant gefüllt, weshalb/wie damit umgehen?
                participant.setId2(values[10]);
                participant.setName2(values[11]);
                participant.setAge2(Integer.parseInt(values[12]));
                participant.setSex2(values[13]);

                // Participant zur Hash-Tabelle hinzufügen
                participants.put(participant.getId(), participant);
            }

            // Datei schließen
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Rückgabe der Hash-Tabelle von Participants
        return participants;
    }

    public static void printHashMap(HashMap<String, Participant> participants) {
        System.out.println("ID\tName\t\tAge\tSex\tFood Preference\tKitchen\t\t\t\tKitchen Story");
        System.out.println("----------------------------------------------------------------------------------------------");
        for(Map.Entry<String, Participant> entry : participants.entrySet()){
            Participant p = entry.getValue();
            System.out.printf("%s\t%-15s\t%d\t%s\t%-15s\t%-30s\t%-30s\n", p.getId(), p.getName(), p.getAge(), p.getSex(), p.getFoodPreference(), p.getKitchen(), p.getKitchenStory());
        }
    }
}
