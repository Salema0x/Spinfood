package Factory;

import Entity.Pair;
import Entity.Participant;

import java.io.*;
import java.util.*;

public class ParticipantFactory {
    public List<Participant> participantList = new ArrayList<>();
    private final List<Pair> registeredPairs = new ArrayList<>();
    private final HashSet<String> ids = new HashSet<>();
    private static final int MAX_PARTICIPANTS = 100; //TODO: Settings Fenster in der GUI über die die Maximalanzahl der Teilnehmer eingelesen werden kann
    HashMap<String, List<Participant>> addressParticipantMap = new HashMap<>();
    /**
     * Will extract all participants from the .csv file.
     * Will split up pairs in two participants and add them as a pair.
     *
     * @param csvFile the .csv File from which the participants should be extracted
     */
    //TODO: include Method to check that the .csv File is correctly formatted
    //TODO: include calculating age range
    //TODO: include extracting party location
    /*
        Calculation of WG_Count: HashSet with Strings identifying the address. For every new Participant this String is created.
        If HashSet contains the String then WG_count of the new Participant goes one up.
        HashMap with address Strings as keys, and Participant as value.
        If HashSet contains the String then get the Participant of that String via the HashMap.
        WG_count of that Participant goes one up.
        If WG_count of the participants = 3 then new participant will be a successor (only for Einzelanmeldungen), with message "Zu viele Anmeldungen aus einer WG"
     */
    public void readCSV(File csvFile) {
        int participantCounter = 0;
        boolean isSuccessor = false;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            if (!participantList.isEmpty()) {
                participantList.clear();
                ids.clear();
                addressParticipantMap.clear();
            }

            String line = br.readLine(); /* skip the headers */

            while ((line = br.readLine()) != null && !line.equals("")) {

                String addressString;
                String[] values = line.split(",");

                if (ids.contains(values[1])) {
                    continue;
                }

                ids.add(values[1]);

                if (values.length == 14) {
                    participantCounter = participantCounter + 2;
                    if (ids.contains(values[10])) {
                        continue;
                    } else {
                        ids.add(values[10]);
                    }
                } else {
                    participantCounter++;
                }

                if (participantCounter > MAX_PARTICIPANTS) {
                    isSuccessor = true;
                }

                if (values.length <= 10) {
                    Participant participant = new Participant(values, isSuccessor);
                    participantList.add(participant);
                    addressString = participant.getKitchenLatitude() + String.valueOf(participant.getKitchenLongitude()) + participant.getKitchenStory();
                    if (!addressParticipantMap.containsKey(addressString) && participant.getKitchenLongitude() != -1.0) {
                        addressParticipantMap.put(addressString, new ArrayList<>(List.of(participant)));
                    } else if (participant.getKitchenLongitude() != -1.0){
                        participant.increaseCountWG();
                        if (participant.getCount_wg() > 3) {
                            isSuccessor = true;
                        }

                        List<Participant> wgMembers = addressParticipantMap.get(addressString);
                        for (Participant wgMember : wgMembers) {
                            wgMember.increaseCountWG();
                        }

                        wgMembers.add(participant);
                        addressParticipantMap.replace(addressString, wgMembers, wgMembers);
                    }
                } else if (values.length == 14) {
                    Participant participant1 = new Participant(createSubArray(values), isSuccessor);
                    participantList.add(participant1);
                    values[1] = values[10];
                    values[2] = values[11];
                    values[4] = String.valueOf((int) Double.parseDouble(values[12]));
                    values[5] = values[13];
                    Participant participant2 = new Participant(createSubArray(values), isSuccessor);
                    participantList.add(participant2);
                    registeredPairs.add(new Pair(participant1, participant2));
                    addressString = participant1.getKitchenLatitude() + String.valueOf(participant1.getKitchenLongitude()) + participant1.getKitchenStory();
                    if (!addressParticipantMap.containsKey(addressString) && participant1.getKitchenLongitude() != -1.0) {
                        addressParticipantMap.put(addressString, new ArrayList<>(List.of(participant1, participant2)));
                    } else {
                        participant1.increaseCountWG();
                        participant2.increaseCountWG();
                        if (participant1.getCount_wg() > 3) {
                            isSuccessor = true;
                        }

                        List<Participant> wgMembers = addressParticipantMap.get(addressString);
                        if (!wgMembers.isEmpty()) {
                            for (Participant wgMember : wgMembers) {
                                wgMember.increaseCountWG();
                            }
                        }

                        wgMembers.add(participant1);
                        wgMembers.add(participant2);
                        addressParticipantMap.replace(addressString, wgMembers, wgMembers);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] createSubArray(String[] values) {
        return Arrays
                .stream(values)
                .toList()
                .stream()
                .limit(10)
                .toList()
                .toArray(String[]::new);
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

    public List<Participant> getParticipantList() {
        return participantList;
    }

    public List<Pair> getRegisteredPairs() {
        return registeredPairs;
    }
}