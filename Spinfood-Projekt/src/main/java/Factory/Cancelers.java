package Factory;

import Entity.Group;
import Entity.Pair;
import Entity.Participant;

import java.util.ArrayList;
import java.util.List;

public class Cancelers {
    private List<Participant> absences;
    private List<Group> groupList;
    private List<Group> backupGroupList;
    private List<Participant> backupWaitingList;

    public Cancelers(List<Participant> absences, List<Group> groupList) {
        this.absences = absences;
        this.groupList = groupList;
        this.backupGroupList = new ArrayList<>(groupList);
        this.backupWaitingList = new ArrayList<>();
    }

    public void performAdjustment() {
        updateGroupList();
        updateWaitingList();
        completeGroups();
    }
    /**
     * Updates the group list based on the absences of participants.
     * If a participant is absent, the corresponding group is removed from the group list and added to the backup group list.
     */
    private void updateGroupList() {
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
     * Absent participants are added to the backup waiting list.
     */
    private void updateWaitingList() {
        for (Group group : groupList) {
            if (group.isPair() && group.getParticipants().size() == 2) {
                Participant participant1 = group.getParticipants().get(0);
                Participant participant2 = group.getParticipants().get(1);

                if (absences.contains(participant1) && absences.contains(participant2)) {
                    groupList.remove(group);
                    backupGroupList.add(group);

                    backupWaitingList.add(participant1);
                    backupWaitingList.add(participant2);
                }
            } else if (!group.isPair() && group.getParticipants().size() == 1) {
                Participant participant = group.getParticipants().get(0);
                if (absences.contains(participant)) {
                    groupList.remove(group);
                    backupGroupList.add(group);

                    backupWaitingList.add(participant);
                }
            }
        }
    }
    /**
     * Completes a group by adding a pair of participants from the waiting list.
     * If there are at least two participants in the waiting list and there is at least one group in the group list,
     * a pair of participants is selected from the waiting list and added to the first group in the group list.
     * The completed group is added to the backup group list, and the group is removed from the group list.
     */
    private void completeGroups() {
        if (backupWaitingList.size() >= 2 && groupList.size() > 0) {
            List<Participant> pair = findPairFromWaitingList();
            if (pair != null) {
                Group group = groupList.get(0);
                group.addParticipant(pair.get(0));
                group.addParticipant(pair.get(1));
                group.addPair(new Pair(pair.get(0), pair.get(1)));
                backupGroupList.add(group);
                groupList.remove(group);
            }
        }
    }
    // i cannot include the Algorithm from PairLestFactory
    public List<Participant> findPairFromWaitingList() {
        return null;
    }
}
