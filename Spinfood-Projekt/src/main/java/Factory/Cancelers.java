package Factory;


import Entity.Group;
import Entity.Pair;
import Entity.Participant;

import java.util.ArrayList;
import java.util.List;



/**
 * The Cancelers class handles adjustments when participants cancel their attendance.
 * It updates the group list and waiting list accordingly.
 */
public class Cancelers {
    private final List<Participant> absences;
    private final List<Participant> successorsInWaitingList;
    private final List<Group> groupList;
    private final List<Group> backupGroupList;
    private final List<Participant> participantList = new ArrayList<>();
    private final List<Pair> successorPairList = new ArrayList<>();
    List<Pair> registeredPairs;
    List<Object> criteria;
    private static final int maxCapacity = 400;
    private int participantCounter = 0;
    private static PairListFactory pairListFactory;
    private static final ParticipantFactory PARTICIPANT_FACTORY = new ParticipantFactory(1000);
    private static final List<Object> CRITERIA_ORDER = new ArrayList<>();
    /**
     * Constructs a Cancelers object with the provided parameters.
     *
     * @param absences                the list of participants who have canceled their attendance
     * @param successorsInWaitingList the list of successors in the waiting list
     * @param groupList               the list of groups
     * @param backupGroupList         the backup list for groups
     */
    public Cancelers(List<Participant> absences, List<Participant> successorsInWaitingList, List<Group> groupList, List<Group> backupGroupList) {
        this.absences = absences;
        this.successorsInWaitingList = successorsInWaitingList;
        this.groupList = groupList;
        this.backupGroupList = backupGroupList;
    }
    /**
     * Performs the necessary adjustments when participants cancel their attendance.
     * It updates the group list, waiting list, and completes groups.
     */
    public void performAdjustment() {
        updateGroupList();
        updateWaitingList();
        completeGroups();
    }
    /**
     * Updates the group list based on the absences of participants.
     * If a participant is absent, the corresponding group is removed from the group list and added to the backup group list.
     */
    void updateGroupList() {
        for (Participant absence : absences) {
            Group groupToRemove = null;
            for (Group group : groupList) {
                if (group.containsParticipant(absence)) {
                    groupToRemove = group;
                    break;
                }
            }
            if (groupToRemove != null) {
                groupList.remove(groupToRemove);
                backupGroupList.add(groupToRemove);
            }
        }
    }
    /**
     * Updates the waiting list based on the absences of participants in the group list.
     * If a participant is absent and belongs to a pair group, the group is removed from the group list and added to the backup group list.
     * If a participant is absent and belongs to a non-pair group, the group is removed from the group list and added to the backup group list.
     */
    void updateWaitingList() {
        for (Participant participant : participantList) {
            if (!participant.hasPartner() && absences.contains(participant)) {
                System.out.println("The participant signed up alone and has canceled.");
                Participant successor = successorsInWaitingList.remove(0);
                participantList.remove(participant);
                participantList.add(successor);
                participantCounter++;
            }
        }
        for (Group group : groupList) {
            if (group.getPairs().size() == 3) {
                List<Participant> participants = group.getParticipants();
                Participant participant1 = participants.get(0);
                Participant participant2 = participants.get(1);
                // Wenn sich eine einzelne absagende Teilnehmende im
                // Pärchen angemeldet hat, wird die zweite Person dieses Pärchens in die Nachrückerliste
                // für Teilnehmende aufgenommen
                if (!participant1.hasPartner() || !participant2.hasPartner() && (absences.contains(participant1) || absences.contains(participant2))) {
                    groupList.remove(group);
                    backupGroupList.add(group);
                    // Check if there are successors available in the waiting list and replace them
                    if (findParticipantFromSuccessorsList()) {
                        System.out.println("A Valid Pair has been found and added to the SuccessorPairList");
                    }
                }
                // if both have signed up together
                if (group.isPair() && participants.size() == 2) {
                    if (absences.contains(participant1) && absences.contains(participant2)) {
                        participantList.remove(participant1);
                        participantList.remove(participant2);
                        if(successorPairList!=null){
                            Pair replacedPairs = successorPairList.remove(0);
                            group.getPairs().add(replacedPairs);
                        }else{
                            // Should an algorithm be applied here ?
                            Participant replacement1 = participantList.remove(0);
                            Participant replacement2 =participantList.remove(1);
                            group.addParticipant(replacement1);
                            group.addParticipant(replacement2);
                        }
                    }
                }
            } else {
                backupGroupList.add(group);
            }
        }
    }
    /**
     * Completes a group by adding a pair of participants from the waiting list.
     * If there is at least one pair in the waiting list and there is at least one incomplete group in the group list,
     * a pair of participants is selected from the waiting list and added to the first group in the group list.
     * The completed group is added to the backup group list, and the group is removed from the group list.
     * @return
     */
    List<Group> completeGroups() {
        while (!successorPairList.isEmpty() && backupGroupList.size() < maxCapacity) {
            if (groupList.isEmpty()) break;

            Group group = groupList.get(0);
            Pair pair = findPairFromSuccessorPairList();

            if (pair != null) {
                group.addParticipant(pair.getParticipant1());
                group.addParticipant(pair.getParticipant2());
                backupGroupList.add(group);
                groupList.remove(group);
            }
        }

        return backupGroupList;
    }
    /**
     * Finds a valid pair from the successorPairList for the provided participants and successors.
     * It uses the PairListFactory to generate pairs and identify successors.
     * @return true if a valid pair is found, false otherwise
     */
    private boolean findParticipantFromSuccessorsList() {
        for (Group group : groupList) {
            List<Participant> participants = group.getParticipants();
            Participant participant1 = participants.get(0);
            Participant participant2 = participants.get(1);
            for (int i = 0; i < successorsInWaitingList.size(); i++) {
                if (absences.contains(participant1)) {
                    // remove the canceled Participant from the ParticipantList
                    participantList.remove(participant1);
                    List<Pair> validPairs = applyPairAlgorithm();
                    if (validPairs != null) {
                        successorPairList.add((Pair) validPairs);
                        successorsInWaitingList.remove(i);
                    }
                } else {
                    // remove the canceled Participant from the ParticipantList
                    participantList.remove(participant2);
                    List<Pair> validPairs = applyPairAlgorithm();
                    if (validPairs != null) {
                        successorPairList.add((Pair) validPairs);
                        successorsInWaitingList.remove(i);
                    }
                }
            }
        }
        return false;
    }

    /**
     * Applies the pair algorithm to find a valid pair for the provided participant and successor.
     * It uses the PairListFactory to generate pairs and apply the algorithm.
     * @return a list of participants representing a valid pair if found, null otherwise
     */
    public List<Pair> applyPairAlgorithm() {
        pairListFactory = new PairListFactory(
                new ArrayList<>(PARTICIPANT_FACTORY.getParticipantList()),
                new ArrayList<>(PARTICIPANT_FACTORY.getRegisteredPairs()),
                new ArrayList<>(CRITERIA_ORDER));
        return null;
    }

    /**
     * Finds a pair from the successorPairList.
     * If a pair is found where neither participant is absent, it is removed from the list and returned.
     * @return the pair if found, null otherwise
     */
    private Pair findPairFromSuccessorPairList() {
        for (int i = 0; i < successorPairList.size(); i++) {
            Pair pair = successorPairList.get(i);
            Participant participant1 = pair.getParticipant1();
            Participant participant2 = pair.getParticipant2();

            if (!absences.contains(participant1) && !absences.contains(participant2)) {
                successorPairList.remove(i);
                return pair;
            }
        }
        return null;
    }
}