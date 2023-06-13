package Factory;
/*
import Factory.Cancelers;
import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import Factory.PairListFactory;
import Factory.ParticipantFactory;
import Factory.GroupFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CancelersTestSalem {
    private List<Participant> absences;
    private List<Participant> successorsInWaitingList;
    private List<Group> groupList;
    private List<Group> backupGroupList;
    private List<Participant> backupWaitingList;
    private PairListFactory pairListFactory;
    private List<Participant> participantList;
    private List<Pair> successorPairList;
    private ParticipantFactory participantFactory;
    private GroupFactory groupFactory;
    private Cancelers cancelers;

    @BeforeEach
    public void setUp() {
        absences = new ArrayList<>();
        successorsInWaitingList = new ArrayList<>();
        groupList = new ArrayList<>();
        backupGroupList = new ArrayList<>();
        backupWaitingList = new ArrayList<>();

        pairListFactory = new PairListFactory();
        participantList = new ArrayList<>();
        successorPairList = new ArrayList<>();
        participantFactory = new ParticipantFactory();
        groupFactory = new GroupFactory();



        cancelers = new Cancelers(absences, successorsInWaitingList, groupList, backupGroupList, backupWaitingList, pairListFactory, pairListFactory, participantFactory, groupFactory);
    }

    @Test
    public void testUpdateGroupList() {
        // Prepare test data

        Participant participant1 = new Participant("");
        Participant participant2 = new Participant("");
        Participant participant3 = new Participant("");
        Group group1 = new Group();
        group1.addParticipant(participant1);
        group1.addParticipant(participant2);
        Group group2 = new Group();
        group2.addParticipant(participant3);
        groupList.add(group1);
        groupList.add(group2);
        absences.add(participant1);

        // Call the method to be tested
        cancelers.updateGroupList();

        // Assert the expected results
        assertEquals(1, groupList.size());
        assertEquals(group2, groupList.get(0));
        assertEquals(1, backupGroupList.size());
        assertEquals(group1, backupGroupList.get(0));
    }

    @Test
    public void testUpdateWaitingList() {
        // Prepare test data
        Participant participant1 = new Participant("");
        Participant participant2 = new Participant("");
        Participant successor = new Participant("");
        Group group = new Group();
        group.addParticipant(participant1);
        groupList.add(group);
        participantList.add(participant1);
        successorsInWaitingList.add(successor);
        absences.add(participant1);

        // Call the method to be tested
        cancelers.updateWaitingList();

        // Assert the expected results
        assertEquals(0, groupList.size());
        assertEquals(1, backupGroupList.size());
        assertEquals(group, backupGroupList.get(0));
        assertEquals(0, participantList.size());
        assertEquals(1, successorsInWaitingList.size());
        assertEquals(successor, successorsInWaitingList.get(0));
    }

    @Test
    public void testCompleteGroups() {
        // Prepare test data
        Participant participant1 = new Participant("");
        Participant participant2 = new Participant("");
        Participant successor1 = new Participant("");
        Participant successor2 = new Participant("");
        Group group = new Group();
        groupList.add(group);
        backupWaitingList.add(successor1);
        backupWaitingList.add(successor2);
        successorsInWaitingList.add(successor1);
        successorsInWaitingList.add(successor2);

        // Call the method to be tested
        cancelers.completeGroups();

        // Assert the expected results
        assertEquals(1, backupGroupList.size());
        assertEquals(group, backupGroupList.get(0));
        assertEquals(0, groupList.size());
        assertEquals(0, backupWaitingList.size());
        assertEquals(1, successorPairList.size());
        assertEquals(new Pair(successor1, successor2), successorPairList.get(0));
    }
}

 */
