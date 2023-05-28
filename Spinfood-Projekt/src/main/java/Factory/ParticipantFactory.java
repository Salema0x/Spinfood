package Factory;

import Entity.Pair;
import Entity.Participant;

import java.io.*;
import java.util.*;

public class ParticipantFactory {
    private static final List<Participant> PARTICIPANT_LIST = new ArrayList<>();
    private static final List<Pair> REGISTERED_PAIRS = new ArrayList<>();
    private static final HashSet<String> IDS = new HashSet<>();
    private static final int MAX_PARTICIPANTS = 300;
    private static final HashMap<String, List<Participant>> ADDRESS_PARTICIPANT_MAP = new HashMap<>();
    private static final HashMap<String, Participant> ID_PARTICIPANT_MAP = new HashMap<>();
    private int participantCounter = 0;
    private boolean isSuccessor = false;
    private boolean pairParticipant1Exists = false;
    private boolean pairParticipant2Exists = false;
    private byte sizeWGMembers = 0;
    private Double[] partyLocation = new Double[2];

    /**
     * Will extract all participants from the .csv file.
     * Will split up pairs in two participants and add them as a pair.
     *
     * @param csvFile the .csv File from which the participants should be extracted
     */
    public void readCSV(File csvFile) {
        if (!PARTICIPANT_LIST.isEmpty()) {
            PARTICIPANT_LIST.clear();
            IDS.clear();
            ADDRESS_PARTICIPANT_MAP.clear();
            ID_PARTICIPANT_MAP.clear();
            sizeWGMembers = 0;
            REGISTERED_PAIRS.clear();
            participantCounter = 0;
            isSuccessor = false;
            pairParticipant1Exists = false;
            pairParticipant2Exists = false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null && !line.equals("")) {

                String[] values = line.split(",");

                if (checkIDS(values)) {
                    continue;
                }

                if (participantCounter > MAX_PARTICIPANTS) {
                    isSuccessor = true;
                }

                if (values.length <= 10) {
                    Participant participant = new Participant(values, isSuccessor);
                    calculateWGCountSingle(participant);
                    ID_PARTICIPANT_MAP.put(values[1], participant);
                    PARTICIPANT_LIST.add(participant);
                } else if (values.length == 14) {
                    if (pairParticipant1Exists) {
                        Participant participant1 = ID_PARTICIPANT_MAP.get(values[1]);
                        Participant participant2 = createParticipant2(values, participant1);
                        participant1.setPartner(participant2);
                        participant2.setPartner(participant1);
                        participant1.setHasPartner(true);
                        participant2.setHasPartner(true);
                        calculateWGCountPair(participant1, participant2);
                    } else if (pairParticipant2Exists) {
                        Participant participant2 = ID_PARTICIPANT_MAP.get(values[10]);
                        Participant participant1 = new Participant(createSubArray(values), isSuccessor);
                        participant1.setPartner(participant2);
                        participant2.setPartner(participant1);
                        participant1.setHasPartner(true);
                        participant2.setHasPartner(true);
                        calculateWGCountPair(participant1, participant2);
                        PARTICIPANT_LIST.add(participant1);
                        REGISTERED_PAIRS.add(new Pair(participant1, participant2));
                    } else {
                        Participant participant1 = new Participant(createSubArray(values), isSuccessor);
                        PARTICIPANT_LIST.add(participant1);
                        Participant participant2 = createParticipant2(values, participant1);
                        participant1.setPartner(participant2);
                        participant2.setPartner(participant1);
                        participant1.setHasPartner(true);
                        participant2.setHasPartner(true);
                        calculateWGCountPair(participant1, participant2);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Will create a subarray of the first 10 elements from the given array.
     * @param values The array from which the first 10 elements should be picked.
     * @return an array with the first 10 elements of the given array.
     */
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
     * Checks if a participant has already registered himself before. Increases the participantCount accordingly.
     * @param values The currently read in values from the .csv File.
     * @return a boolean indicating if the current participant was already registered or not.
     */
    private boolean checkIDS(String[] values) {
        boolean firstIDcontained = IDS.contains(values[1]);

        if (values.length == 14) {
            boolean secondIDcontained = IDS.contains(values[10]);
            if (firstIDcontained) {
                pairParticipant1Exists = true;
                if (!secondIDcontained) {
                    IDS.add(values[10]);
                    participantCounter++;
                    pairParticipant2Exists = false;
                    return false;
                }
                pairParticipant2Exists = true;
                return true;
            } else {
                pairParticipant1Exists = false;
                if (secondIDcontained) {
                    pairParticipant2Exists = true;
                    IDS.add(values[1]);
                    return false;
                }

                IDS.add(values[1]);
                IDS.add(values[10]);
                pairParticipant2Exists = false;
                participantCounter = participantCounter + 2;
                return false;
            }
        } else {
            if (!firstIDcontained) {
                IDS.add(values[1]);
                participantCounter++;
                return false;
            } else {
                return true;
            }
        }
    }


    /**
     * Checks if there are persons from the wg of the given participant which have already registered.
     * If the there are more than 3 registrations from one wg every following registration will be a successor.
     * @param participant the participant who's wgMembers should be checked.
     */
    private void calculateWGCountSingle(Participant participant) {
        String addressString = participant.getKitchenLatitude()
                + String.valueOf(participant.getKitchenLongitude())
                + participant.getKitchenStory();

        if (!ADDRESS_PARTICIPANT_MAP.containsKey(addressString) && participant.getKitchenLongitude() != -1.0) {
            ADDRESS_PARTICIPANT_MAP.put(addressString, new ArrayList<>(List.of(participant)));
        } else if (participant.getKitchenLongitude() != -1.0) {
            List<Participant> oldMembers = ADDRESS_PARTICIPANT_MAP.get(addressString);
            List<Participant> memberStoring = calculateWGSize(addressString);

            if (sizeWGMembers == 3) {
                participant.setSuccessor(true);
            } else if (sizeWGMembers < 3) {
                participant.setCountWg(sizeWGMembers);

                for (Participant member : memberStoring) {
                    member.increaseCountWG();
                }

                memberStoring.add(participant);
                ADDRESS_PARTICIPANT_MAP.replace(addressString, oldMembers, memberStoring);
            }
        }
    }

    /**
     * Doing the same as calculateWGCountSingle but for Pairs.
     * The difference is that for pairs the wgCounter of both has to be incremented and both have to get a successor
     * if the wgCount gets to high.
     * @param participant1 First participant of the pair.
     * @param participant2 Second participant of the pair.
     */
    private void calculateWGCountPair(Participant participant1, Participant participant2) {
        String addressString = participant1.getKitchenLatitude()
                + String.valueOf(participant1.getKitchenLongitude())
                + participant1.getKitchenStory();

        boolean hasAddress = participant1.getKitchenLongitude() != -1.0;

        if (!ADDRESS_PARTICIPANT_MAP.containsKey(addressString) && hasAddress) {
            ADDRESS_PARTICIPANT_MAP.put(addressString, new ArrayList<>(List.of(participant1, participant2)));
        } else if (hasAddress) {
            List<Participant> memberStoring = calculateWGSize(addressString);
            List<Participant> oldMembers = ADDRESS_PARTICIPANT_MAP.get(addressString);

            if (sizeWGMembers == 3) {
                participant1.setSuccessor(true);
                participant2.setSuccessor(true);
            } else if (sizeWGMembers < 3) {
                participant1.setCountWg(sizeWGMembers);
                participant2.setCountWg(sizeWGMembers);

                for (Participant member : memberStoring) {
                    member.increaseCountWG();
                }

                memberStoring.add(participant1);
                memberStoring.add(participant2);
                ADDRESS_PARTICIPANT_MAP.replace(addressString, oldMembers, memberStoring);
            }
        }
    }

    /**
     * Calculates the amount of other WG_Members in the WG and calculates all the wg Members.
     * @param addressString A String representing the WG.
     * @return as List with all the wg Members
     */
    private List<Participant> calculateWGSize(String addressString) {
        List<Participant> wgMembers = ADDRESS_PARTICIPANT_MAP.get(addressString);

        List<Participant> memberStoring = new ArrayList<>();

        sizeWGMembers = 0;

        while (!wgMembers.isEmpty()) {
            Participant member = wgMembers.get(0);

            if (member.hasPartner() && wgMembers.contains(member.getPartner())) {
                sizeWGMembers++;
                wgMembers.remove(member);
                wgMembers.remove(member.getPartner());
                memberStoring.add(member);
                memberStoring.add(member.getPartner());
            } else {
                sizeWGMembers++;
                wgMembers.remove(member);
                memberStoring.add(member);
            }
        }

        return memberStoring;
    }

    /**
     * Creates the second participant from a registration as Pairs.
     * @param values The currently read in values from the .csv File.
     * @param participant1 The first participant of the pair.
     */
    private Participant createParticipant2(String[] values, Participant participant1) {
        values[1] = values[10];
        values[2] = values[11];
        values[4] = String.valueOf((int) Double.parseDouble(values[12]));
        values[5] = values[13];

        Participant participant2 = new Participant(createSubArray(values), isSuccessor);

        PARTICIPANT_LIST.add(participant2);
        REGISTERED_PAIRS.add(new Pair(participant1, participant2));
        return participant2;
    }

    public void readPartyLocation(File csvFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();
            String line = br.readLine();
            String[] values = line.split(",");
            partyLocation[0] = Double.parseDouble(values[0]);
            partyLocation[1] = Double.parseDouble(values[1]);
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

        for (Participant participant : PARTICIPANT_LIST) {
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
        if (PARTICIPANT_LIST.size() != participantList2.size()) {
            return false;
        }

        for (int i = 0; i < PARTICIPANT_LIST.size(); i++) {
            if (!PARTICIPANT_LIST.get(i).equals(participantList2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public List<Participant> getParticipantList() {
        return PARTICIPANT_LIST;
    }

    public List<Pair> getRegisteredPairs() {
        return REGISTERED_PAIRS;
    }
}