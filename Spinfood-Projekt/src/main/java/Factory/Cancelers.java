package Factory;

import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import Misc.DinnerRound;

import java.util.ArrayList;
import java.util.List;
/**
 * The Cancelers class handles adjustments when participants cancel their attendance.
 * It updates the group list and waiting list accordingly.
 */
public class Cancelers {
    private final List<Participant> absences;
    private List<Participant> successorsInWaitingList;
    private final List<Group> groupList;
    private final List<Group> backupGroupList;
    private final List<Participant> backupWaitingList;
    private PairListFactory pairListFactory;
    private final List<Participant> participantList = new ArrayList<>();
    private final List<Pair> successorPairList = new ArrayList<>();
    private final ParticipantFactory participantFactory;
    private final GroupFactoryOld groupFactoryOld;
    List<Pair> registeredPairs;
    List<Object> criteria;

    /**
     * Constructs a Cancelers object with the provided parameters.
     *
     * @param absences                the list of participants who have canceled their attendance
     * @param successorsInWaitingList the list of successors in the waiting list
     * @param groupList               the list of groups
     * @param backupGroupList         the backup list for groups
     * @param backupWaitingList       the backup list for participants in the waiting list
     * @param pairListFactory         the PairListFactory object for generating pairs
     * @param participantFactory      the ParticipantFactory object for creating participants
     * @param groupFactoryOld            the GroupFactory object for creating groups
     */

    public Cancelers(List<Participant> absences, List<Participant> successorsInWaitingList, List<Group> groupList, List<Group> backupGroupList, List<Participant> backupWaitingList, PairListFactory pairListFactory, PairListFactory pairListFactory1, ParticipantFactory participantFactory, GroupFactoryOld groupFactoryOld) {
        this.absences = absences;
        this.successorsInWaitingList = successorsInWaitingList;
        this.groupList = groupList;
        this.backupGroupList = backupGroupList;
        this.backupWaitingList = backupWaitingList;
        this.pairListFactory = pairListFactory;
        this.participantFactory = participantFactory;
        this.groupFactoryOld = groupFactoryOld;
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
                    if (!successorsInWaitingList.isEmpty()) {
                        for (int i = 0; i < successorsInWaitingList.size(); i++) {
                            Participant successor = successorsInWaitingList.get(i);

                            if (absences.contains(participant1)) {
                                // remove the canceled Participant from the ParticipantList
                                participantList.remove(participant1);
                                /*
                                if (findPairFromWaitingList(participant2, successor)) {
                                    successorPairList.add(new Pair(participant2, successor));
                                    successorsInWaitingList.remove(i);
                                    break;
                                }
                                 */
                            } else {
                                // remove the canceled Participant from the ParticipantList
                                participantList.remove(participant2);
                                /*
                                if (findPairFromWaitingList(participant1, successor)) {
                                    successorPairList.add(new Pair(participant1, successor));
                                    successorsInWaitingList.remove(i);
                                    break;
                                }

                                 */
                            }
                        }
                    }
                }
                // if both have signed up together
                if (group.isPair() && participants.size() == 2) {
                    if (absences.contains(participant1) && absences.contains(participant2)) {
                        groupList.remove(group);
                        backupGroupList.add(group);
                        // remove the canceled Participant from the ParticipantList
                        participantList.remove(participant1);
                        participantList.remove(participant2);
                        // Check if there are successors available in the waiting list and replace them
                        if (!successorsInWaitingList.isEmpty()) {
                            Participant successor1 = successorsInWaitingList.remove(0);
                            Participant successor2 = successorsInWaitingList.remove(0);
                            // The Pairs will be added to the SuccessorsPairList
                            successorPairList.add(new Pair(successor1, successor2));
                            backupWaitingList.add((Participant) successorPairList);
                        }
                    }
                }

                backupWaitingList.add((Participant) successorsInWaitingList);
            } else {
                backupGroupList.add(group);
            }
        }
    }

    /**
     * Completes a group by adding a pair of participants from the waiting list.
     * If there are at least one Pair in the waiting list for Pairs and there is at least one incomplete group in the group list,
     * a pair of participants is selected from the waiting list for Pairs and added to the first group in the group list.
     * The completed group is added to the backup group list, and the group is removed from the group list.
     */
    void completeGroups() {
        if (successorPairList.size() >= 1 && groupList.size() > 0) {
            Group group = groupList.get(0);
            if (group.isPair() && group.getParticipants().size() == 1) {
                // Complete the group with a pair from successorPairList
                Pair pair = findPairFromSuccessorPairList();
                if (pair != null) {
                    group.addParticipant(pair.getParticipant1());
                    group.addParticipant(pair.getParticipant2());
                    backupGroupList.add(group);
                    groupList.remove(group);
                }
            }
        }
    }

    Pair findPairFromSuccessorPairList() {
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
    /**
     * Finds a pair from the successorPairList for the provided participant and successor.
     * It uses the PairListFactory to generate pairs and identify successors.
     *
     * @param participant the participant to find a pair for
     * @param successor   the successor participant
     * @return the list of successors from the PairListFactory
     */
    public List<Participant> findPairFromWaitingList(Participant participant, Participant successor) {

        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantList), new ArrayList<>(registeredPairs), new ArrayList<>(criteria));
        //pairListFactory.cleanParticipantListFromRegisteredPairs();
        //pairListFactory.cleanParticipantListFromSuccessors();
        //pairListFactory.decideAlgorithm();
        //pairListFactory.makePairs();
        //pairListFactory.concatWithRegisteredPairs();
        //pairListFactory.identifySuccessors();

        return pairListFactory.getSuccessors();
    }

    // DinnerRounds are unnecessary here right ?
    public List<DinnerRound> findGroupFromWaitingList() {
        return groupFactoryOld.createGroups();
    }
}
