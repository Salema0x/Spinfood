package Factory;

import Data.PairList;
import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import Misc.DinnerRound;
import org.junit.jupiter.api.Assertions;
import Enum.FoodPreference;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;

class CancelersTest {
    private List<Pair> absencePairs = new ArrayList<>();
    private List<Participant> absenceParticipants = new ArrayList<>();
    private List<Pair> successorList = new ArrayList<>();
    private List<Object> criteria = new ArrayList<>();
    private List<DinnerRound> dinnerRounds;
    private String participantFilePath = "/testlists/CancelersTestLists/testliste2.csv";
    private File participantFile;
    private ParticipantFactory participantFactory;
    private Cancelers cancelers;
    private int initialDinnerRoundSize;



    @BeforeEach
    void setUp() throws URISyntaxException {
        generateFileFromSource();

        ParticipantFactory participantFactory = new ParticipantFactory(100);
        participantFactory.readCSV(participantFile);
        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));

        GroupFactory groupFactory = new GroupFactory(pairListFactory, 3, new Double[]{8.6746166676233, 50.5909317660173});
        groupFactory.createGroups();

        initializeLists(groupFactory, pairListFactory);

        //TODO: cancelersConstructor takes absences, successorList, dinnerRounds
        //cancelers = new Cancelers(absenceParticipants, absencePairs, successorList, dinnerRounds);
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

        //TODO find Way to track updated groups and pairs
        //testIfUpdatedPairsAreFine();#
        testIfUpdatedGroupsAreFine();
    }



    /**
     * tests if absent participants were removed from the groups
     */
    public void testIfAbsentParticipantsAreRemoved() {
        for (Participant participant : absenceParticipants) {
            assertFalse(searchForParticipantInGroupList(participant));
        }

        for(Pair pair : absencePairs) {
            assertFalse(searchForParticipantInGroupList(pair.getParticipant1()));
            assertFalse(searchForParticipantInGroupList(pair.getParticipant2()));
        }
    }

    /**
     * tests if groups with absent participants/pairs were removed
     */
    private void testIfGroupsWithAbsentParticipantsAreRemoved() {
        for (DinnerRound dinnerRound : dinnerRounds) {
            for (Group group : dinnerRound.getGroups()) {
                for(Participant participant : absenceParticipants) {
                    Assertions.assertFalse(group.containsParticipant(participant));
                }

                for(Pair pair : absencePairs) {
                    Assertions.assertFalse(group.containsParticipant(pair.getParticipant1()));
                    Assertions.assertFalse(group.containsParticipant(pair.getParticipant2()));
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
    private void testIfUpdatedPairsAreFine(List<Pair> updatedPairs) {
        Assertions.assertFalse(checkNoGoPair(updatedPairs));
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


    /**
     * generates the ParticipantFile from the source
     *
     */
    private void generateFileFromSource() throws URISyntaxException {
        participantFile = new File(Objects.requireNonNull(getClass().getResource(participantFilePath)).toURI());
    }


    /**
     * initializes the lists for the test
     *
     * @param groupFactory
     * @param pairListFactory
     */
    public void initializeLists(GroupFactory groupFactory, PairListFactory pairListFactory) {
        dinnerRounds = groupFactory.getDinnerRounds();
        successorList = groupFactory.getSuccessorList();
        absencePairs = pairListFactory.registeredPairs.subList(0, 2);
        System.out.println("absencePairs: " + absencePairs);
        absenceParticipants.add(pairListFactory.getRegisteredPairs().get(14).getParticipant1());
        absenceParticipants.add(pairListFactory.getRegisteredPairs().get(22).getParticipant2());
        System.out.println("absenceParticipants: " + absenceParticipants);
    }


    //PairTestMethods to check updated pairs
    /**
     * Checks if a pairList contains illegal pairs
     *
     * @param pairList the pairList that should be checked for  noGoPairs
     * @return a boolean indicating if the pair is a noGoPair
     */
    private boolean checkNoGoPair(List<Pair> pairList) {
        for (Pair p : pairList) {
            if (checkKitchenNoGo(p.getParticipant1().getHasKitchen(), p.getParticipant2().getHasKitchen())) {
                System.out.println("Pair:" + p.getParticipant1().getName() + " " + p.getParticipant2().getName() + " has no kitchen");
                return true;
            }
            if (checkFoodNoGo(p.getParticipant1().getFoodPreference(), p.getParticipant2().getFoodPreference())) {
                System.out.println("Pair:" + p.getParticipant1().getName() + " " + p.getParticipant2().getName() + " has illegal food preferenceCombination");
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if a pair is a bad match in food preferences (vegan with meat/veggie with meat)
     *
     * @param foodPreference1 the food preference of the first participant of the pair.
     * @param foodPreference2 the food preference of the second participant of the pair.
     * @return a boolean indicating if the pair has valid food preferences or not.
     */
    private boolean checkFoodNoGo(FoodPreference foodPreference1, FoodPreference foodPreference2) {
        if (foodPreference1.equals(FoodPreference.VEGAN) || foodPreference1.equals(FoodPreference.VEGGIE)) {
            return foodPreference2.equals(FoodPreference.MEAT);
        }
        if (foodPreference2.equals(FoodPreference.VEGAN) || foodPreference2.equals(FoodPreference.VEGGIE)) {
            return foodPreference1.equals(FoodPreference.MEAT);
        }
        return false;
    }

    /**
     * Checks if a pair is a bad match in kitchen (no kitchen with no kitchen).
     *
     * @param kitchen1 the kitchen identification of the first participant.
     * @param kitchen2 the kitchen identification of the second participant.
     * @return a boolean indicating if both participants have no kitchen or not.
     */
    private boolean checkKitchenNoGo(String kitchen1, String kitchen2) {
        if (kitchen1.equals("no")) {
            return kitchen2.equals("no");
        }
        return false;
    }

    //GroupTestMethods to check updated groups



}