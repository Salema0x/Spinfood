package Factory;

import Entity.Group;
import Entity.Participant;
import Misc.DinnerRound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;

class CancelersTest {
    private List<Participant> absences;
    private List<Object> criteria = new ArrayList<>();
    private List<DinnerRound> dinnerRounds;
    private String participantFilePath = "teilnehmerliste.csv";
    private File participantFile;
    private ParticipantFactory participantFactory;
    private Cancelers cancelers;
    private int initialDinnerRoundSize;


    @BeforeEach
    void setUp() throws URISyntaxException {
        generateFileFromSource();

        ParticipantFactory participantFactory = new ParticipantFactory(100);
        participantFactory.readCSV(participantFile);
        initializeAbsencesList(participantFactory.getParticipantList());
        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));

        GroupFactory groupFactory = new GroupFactory(pairListFactory, 3, new Double[]{8.6746166676233, 50.5909317660173});
        groupFactory.createGroups();
        dinnerRounds = groupFactory.getDinnerRounds();
        //absences = groupFactory.getSuccessorList();
        //TODO cancelers only works with groups, should work with dinnerRounds
        //TODO cancelers only works with participants, should also work with pairs
        //cancelers = new Cancelers(absences, dinnerRounds, pairListFactory);
        initialDinnerRoundSize = dinnerRounds.size();

    }

    @org.junit.jupiter.api.Test

    void performAdjustment() {
        cancelers.performAdjustment();
        testIfAbsentParticipantsAreRemoved();
    }

    /**
     * tests if all Groups no longer contain absent participants
     */
    @org.junit.jupiter.api.Test
    void updateGroupList() {
        cancelers.updateGroupList();
        testIfAbsentParticipantsAreRemoved();

    }

    /**
     * tests if all dinnerRounds no longer contain groups with absent participants
     */
    @org.junit.jupiter.api.Test
    void updateWaitingList() {
        cancelers.updateGroupList();
        cancelers.updateWaitingList();
        testIfGroupsWithAbsentParticipantsAreRemoved();
    }

    /**
     * tests if all groups are refilled with matching participants
     */
    @org.junit.jupiter.api.Test
    void completeGroups() {
        cancelers.updateGroupList();
        cancelers.updateWaitingList();
        cancelers.completeGroups();
        testIfGroupsAreRefilled();
        testIfUpdatedGroupsAreFine();
        testIfUpdatedPairsAreFine();
    }



    /**
     * tests if absent participants were removed from the groups
     */
    public void testIfAbsentParticipantsAreRemoved() {
        for (Participant participant : absences) {
            assertFalse(searchForParticipantInGroupList(participant));
        }
    }

    /**
     * tests if groups with absent participants/pairs were removed
     */
    private void testIfGroupsWithAbsentParticipantsAreRemoved() {
        for (DinnerRound dinnerRound : dinnerRounds) {
            for (Group group : dinnerRound.getGroups()) {
                for(Participant participant : absences) {
                    Assertions.assertFalse(group.containsParticipant(participant));
                }
            }

        }
    }

    /**
     * tests if groups are refilled with matching pairs
     */
    private void testIfUpdatedGroupsAreFine() {
        for(DinnerRound dinnerRound : dinnerRounds) {
            for(Group group : dinnerRound.getGroups()) {
                //Assertions.assertTrue();
            }
        }
    }

    /**
     * tests if mew pairs have matching participants
     */
    private void testIfUpdatedPairsAreFine() {
    }

    public void testIfGroupsAreRefilled() {
        for(DinnerRound dinnerRound : dinnerRounds) {
            Assertions.assertTrue(dinnerRound.getGroups().size() == initialDinnerRoundSize);
            for(Group group : dinnerRound.getGroups()) {
                Assertions.assertEquals(3, group.getParticipants().size());
            }
        }

    }

    /**
     * searches for a participant in the group list of the dinner rounds
     *
     * @param participant
     * @return
     */
    public boolean searchForParticipantInGroupList(Participant participant) {
        for (DinnerRound dinnerRound : dinnerRounds) {
            for (Group group : dinnerRound.getGroups()) {
                if (group.containsParticipant(participant)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void initializeAbsencesList(List<Participant> allParticipants) {
        absences = new ArrayList<>();
        absences.add(allParticipants.get(2));
        absences.add(allParticipants.get(5));
    }


    private void generateFileFromSource() throws URISyntaxException {
        participantFile = new File(Objects.requireNonNull(getClass().getResource(participantFilePath)).toURI());
    }

}