package Factory;

import Entity.Pair;
import Entity.Participant;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ParticipantFactory {
    public List<Participant> participantList = new ArrayList<>();
    public PairListFactory pairListFactory = new PairListFactory();
    private final HashSet<String> ids = new HashSet<>();
    private static final int MAX_PARTICIPANTS = 100; //TODO: Settings Fenster in der GUI über die die Maximalanzahl der Teilnehmer eingelesen werden kann

    /**
     * Will extract all participants from the .csv file.
     * Will split up pairs in two participants and add them as a pair.
     *
     * @param csvFile the .csv File from which the participants should be extracted
     */
    //TODO: include WG-Count
    //TODO: include Method to check that the .csv File is correctly formatted
    public void readCSV(File csvFile) {
        boolean isSuccessor = false;
        int participantCounter = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            if (!participantList.isEmpty()) {
                participantList.clear();
                ids.clear();
            }

            String line = br.readLine(); /* skip the headers */

            while ((line = br.readLine()) != null && !line.equals("")) {
                String[] values = line.split(",");

                if (ids.contains(values[1])) {
                    continue;
                }

                ids.add(values[1]);
                participantCounter++;

                if (participantCounter > MAX_PARTICIPANTS) {
                    isSuccessor = true;
                }

                if (values.length <= 10) {
                    participantList.add(new Participant(values, isSuccessor));
                } else if (values.length == 14) {
                    Participant participant1 = new Participant(values, isSuccessor);
                    participantList.add(participant1);
                    values[1] = values[10];
                    values[2] = values[11];
                    values[4] = String.valueOf((int) Double.parseDouble(values[12]));
                    values[5] = values[13];
                    Participant participant2 = new Participant(values, isSuccessor);
                    participantList.add(participant2);
                    pairListFactory.pairList.add(new Pair(participant1, participant2));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Will display all participants in a table on the console.
     */
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

        System.out.format("+--------------------------------------+------------+--------+-----+---------+----------+---------------+-------------------+---------------------+%n");
    }

    /**
     * Checks if the participantList from this class equals another participant list.
     * @param participantList2 the other participantList to which we should compare.
     * @return a boolean indicating if the participantLists equals or not.
     */
    public boolean participantListEquals(List<Participant> participantList2) {
        if (participantList.size() != participantList2.size()) {
            return false;
        }

        for (int i = 0; i < participantList.size(); i++) {
            if (!participantList.get(i).equals(participantList2.get(i))) {
                return false;
            }
        }
        return true;
    }
}