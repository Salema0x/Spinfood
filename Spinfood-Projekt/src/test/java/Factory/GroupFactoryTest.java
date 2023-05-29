package Factory;

import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import Misc.DinnerRound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GroupFactoryTest {
    GroupFactory groupFactory;
    PairListFactory pairListFactory;
    ParticipantFactory participantFactory;
    Double[] partyLocationCoordinates;
    File partyLocation;


    @BeforeEach
    void setUp() throws URISyntaxException {
        partyLocation = new File(Objects.requireNonNull(getClass().getResource("/partylocation.csv").toURI()));
        partyLocationCoordinates = partyLocationReader(partyLocation);
        participantFactory = new ParticipantFactory();
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/teilnehmerliste.csv").toURI())));
        pairListFactory = new PairListFactory(participantFactory.getParticipantList(), participantFactory.getRegisteredPairs(), new ArrayList<>());
        groupFactory = new GroupFactory(pairListFactory, 3, partyLocationCoordinates);
        groupFactory.createGroups();


        Assertions.assertFalse(checkGroupsContainAllPairs(groupFactory));
        Assertions.assertFalse(checkFalseCooking(groupFactory));
        Assertions.assertTrue(checkNewPairsEachDinnerRound(groupFactory));



    }


    @org.junit.jupiter.api.Test
    void GroupFactory() {

    }
    //Testmethoden


    /**
     * Test if each Pair cooks exactly once
     *
     * @return
     */
    private boolean checkFalseCooking(GroupFactory g) {
        List<Pair> cookingPairs = new ArrayList<>();
        for (DinnerRound dinnerRound : g.getDinnerRounds()) {
            for (Group group : dinnerRound.getGroups()) {
                cookingPairs.add(group.getCookingPair());
            }
        }

        if (cookingPairs.size() != g.getRegisteredPairs().size()) {
            System.out.println("cookingPairs size =  " + cookingPairs.size() + "!= g.getRegisteredPairs size  "
                    + g.getRegisteredPairs().size());
            return false;
        }

        //checks if all registered Pairs are cooking
        for (Pair pair : g.getRegisteredPairs()) {
            if (!cookingPairs.contains(pair)) {
                System.out.println("cookingPairs does not contain " + pair);
                return false;
            }
        }


        //checks if a Pair cooks twice
        while (!cookingPairs.isEmpty()) {
            Pair currentPair = cookingPairs.remove(0);
            for (Pair pair : cookingPairs) {
                if (currentPair.equals(pair)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checks if each Pair meets new Pairs each DinnerRound
     * @param groupFactory
     * @return
     */
    private boolean checkNewPairsEachDinnerRound(GroupFactory groupFactory) {
        List<Pair> alreadyMetPairs = new ArrayList<>();
        List<Group> pairGroups = new ArrayList<>();
        for (Pair pair : groupFactory.getRegisteredPairs()) {
            for (DinnerRound dinnerRound : groupFactory.getDinnerRounds()) {
                for(Group group : dinnerRound.getGroups()) {
                    if(group.getPairs().contains(pair)) {
                        pairGroups.add(group);
                        for(Pair pairInGroup : group.getPairs()) {
                            if(!pairInGroup.isEqualTo(pair)) {
                                if(alreadyMetPairs.contains(pairInGroup)) {
                                    System.out.println("Group1: " + pairGroups.get(0).toString());
                                    if(pairGroups.size() > 1) {
                                        System.out.println("Group2: " + pairGroups.get(1).toString());
                                    }
                                    if(pairGroups.size() > 2) {
                                        System.out.println("Group3: " + pairGroups.get(2).toString());
                                    }
                                    System.out.println(pair + "meets" + pairInGroup + " twice");
                                    return false;
                                }
                                alreadyMetPairs.add(pairInGroup);
                            }
                        }
                        break;
                    }
                }
            }
            alreadyMetPairs.clear();
            pairGroups.clear();
        }
        return true;
    }

    /**
     * checks if all registered Pairs are in a Group each DinnerRound
     * @param groupFactory
     * @return
     */
    private boolean checkGroupsContainAllPairs(GroupFactory groupFactory) {
        List<Pair> pairsInGroups = new ArrayList<>();
        for (DinnerRound dinnerRound : groupFactory.getDinnerRounds()) {
            for(Group group : dinnerRound.getGroups()) {
                pairsInGroups.addAll(group.getPairs());
            }
            for(Pair pair : groupFactory.getRegisteredPairs()) {
                if(!pairsInGroups.contains(pair)) {
                    return false;
                }
            }
        }
        return true;
    }


    //helper Methods

    /**
     * checks if all Pairs in group have access to a kitchen
     */
    private boolean checkPairWithoutKitchen(Group group) {
        for (Pair pair : group.getPairs()) {
            if ((pair.getParticipant1().getHasKitchen().equals("false")) && (pair.getParticipant2().getHasKitchen().equals("false"))) {
                return true;
            }
        }
        return false;
    }


    private boolean checkGeographicalDistance(Group group) {
        for (Pair pair : group.getPairs()) {

        }
        return false;
    }

    private boolean checkAgeDifference(Group group) {
        return true;
    }

    private boolean checkPreferenceDeviation(Group group) {
        return true;
    }

    /**
     * Method to read the party location from a csv file
     *
     * @param file given csv file
     * @return partyLocation
     */
    private Double[] partyLocationReader(File file) {
        Double[] coordinates = new Double[2];

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.readLine() != null) {
                String line = br.readLine();
                String[] values = line.split(",");
                coordinates[0] = Double.parseDouble(values[0]);
                coordinates[1] = Double.parseDouble(values[1]);
            }
            return coordinates;

        } catch (IOException e) {
            throw new RuntimeException("Error while reading file");
        }
    }


}