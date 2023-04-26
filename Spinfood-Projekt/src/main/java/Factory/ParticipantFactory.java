package Factory;

import Entity.Pair;
import Entity.Participant;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantFactory {
    List<Participant> participantList = new ArrayList<>();
    PairListFactory pairListFactory = new PairListFactory();

    /**
     * Will extract all participants from the .csv file.
     * Will split up pairs in two participants and add them as a pair.
     *
     * @param csvFile the .csv File from which the participants should be extracted
     */
    public void readCSV(File csvFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length <= 10) {
                    participantList.add(new Participant(values));
                } else if (values.length == 14) {
                    Participant participant1 = new Participant(values);
                    participantList.add(participant1);
                    values[1] = values[10];
                    values[2] = values[11];
                    values[4] = String.valueOf((int) Double.parseDouble(values[12]));
                    values[5] = values[13];
                    Participant participant2 = new Participant(values);
                    pairListFactory.pairList.add(new Pair(participant1, participant2));

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showCSV() {
        String leftAlignFormat = "| %-36s | %-10s | %-6s | %-3d | %-7s | %-8s | %-13d | %-17s | %-19s |%n";

        System.out.format("+--------------------------------------+------------+--------+-----+---------+----------+---------------+-------------------+---------------------+%n");
        System.out.format("| ID                                   | Name       | Food   | Age | Sex     | Kitchen  | Kitchen Story | Longitude         | Latitude            |%n");
        System.out.format("+--------------------------------------+------------+--------+-----+---------+----------+---------------+-------------------+---------------------+%n");

        for (Participant participant : participantList) {
            String id = participant.getId();
            String name = participant.getName();
            String foodPreference = participant.getFoodPreference();
            String sex = participant.getSex();
            String hasKitchen = participant.getHasKitchen();
            String longitude = String.valueOf(participant.getKitchenLongitude());
            String latitude = String.valueOf(participant.getKitchenLatitude());

            byte age = participant.getAge();
            byte kitchenStory = participant.getKitchenStory();

            System.out.format(leftAlignFormat, id, name, foodPreference, age, sex, hasKitchen, kitchenStory, longitude, latitude);
        }

        System.out.format("\"+--------------------------------------+------------+--------+-----+---------+----------+---------------+-------------------+---------------------+%n");
    }
}