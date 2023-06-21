package Factory;

import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import java.util.ArrayList;
import java.util.List;

public class Cancelers {
    private final List<Participant> absences;
    private List<Participant> successorsInWaitingList;

    public Cancelers(List<Participant> absences, List<Participant> successorsInWaitingList, List<Group> groupList, List<Group> backupGroupList, List<Participant> backupWaitingList, PairListFactory pairListFactory) {
        this.absences = absences;
        this.successorsInWaitingList = successorsInWaitingList;
        this.groupList = groupList;
        this.backupGroupList = backupGroupList;
        this.backupWaitingList = backupWaitingList;

    }
    private final List<Group> groupList;
    private final List<Group> backupGroupList;
    private final List<Participant> backupWaitingList;
    private       PairListFactory pairListFactory;
    private final List<Participant> participantList = new ArrayList<>();
    private final List<Pair> successorPairList = new ArrayList<>();



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
            if (!participant.getHasPartner() && absences.contains(participant)) {
                System.out.println("The participant signed up alone and has canceled.");
                Participant successor = successorsInWaitingList.remove(0);
                participantList.add(successor);
            }
        }
        for (Group group : groupList) {
            List<Participant> participants = group.getParticipants();
            Participant participant1 = participants.get(0);
            Participant participant2 = participants.get(1);

            if (!participant1.getHasPartner() || !participant2.getHasPartner() && absences.contains(participant1) || absences.contains(participant2)) {
                groupList.remove(group);
                backupGroupList.add(group);
                if (absences.contains(participant1)) {
                    successorsInWaitingList.add(participant2);

                } else {
                    successorsInWaitingList.add(participant1);

                }
                // if both has signed up together
                if (group.isPair() && participants.size() == 2) {
                    if (absences.contains(participant1) && absences.contains(participant2)) {
                        groupList.remove(group);
                        backupGroupList.add(group);
                        // Check if there are successors available in the waiting list and replace them
                        if (!successorsInWaitingList.isEmpty()) {
                            Participant successor1 = successorsInWaitingList.remove(0);
                            Participant successor2 = successorsInWaitingList.remove(0);
                            successorPairList.add(new Pair(successor1,successor2, true));
                            backupWaitingList.add((Participant) successorPairList);
                        }
                    }
                }
            }
            backupWaitingList.add((Participant) successorsInWaitingList) ;
        }
    }


    /**
     * Completes a group by adding a pair of participants from the waiting list.
     * If there are at least two participants in the waiting list and there is at least one group in the group list,
     * a pair of participants is selected from the waiting list and added to the first group in the group list.
     * The completed group is added to the backup group list, and the group is removed from the group list.
     */
    void completeGroups() {
        if (backupWaitingList.size() >= 2 && groupList.size() > 0) {
            List<Participant> pair = findPairFromWaitingList();
            if (pair != null) {
                Group group = groupList.get(0);
                group.addParticipant(pair.get(0));
                group.addParticipant(pair.get(1));
                backupGroupList.add(group);
                groupList.remove(group);
            }
        }
    }
    // pairsFromPairAlgorithm should be called aswell
    public List<Participant> findPairFromWaitingList() {
        List<Participant> successorsFromPairAlgorithm = pairListFactory.getParticipantSuccessorList();
        List<Pair> pairsFromPairAlgorithm = pairListFactory.pairList;
        return successorsFromPairAlgorithm;
    }

}
