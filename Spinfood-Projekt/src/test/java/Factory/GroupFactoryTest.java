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
    private GroupFactory groupFactory;
    private PairListFactory pairListFactory;
    private ParticipantFactory participantFactory;
    private Double[] partyLocationCoordinates;
    private File partyLocation;
    private final double genderDiversityThreshold = 0.4;


    @BeforeEach
    void setUp() throws URISyntaxException {
        partyLocation = new File(Objects.requireNonNull(getClass().getResource("/partylocation.csv").toURI()));
        partyLocationCoordinates = partyLocationReader(partyLocation);
        participantFactory = new ParticipantFactory(100);
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/teilnehmerliste.csv").toURI())));
        pairListFactory = new PairListFactory(participantFactory.getParticipantList(), participantFactory.getRegisteredPairs(), new ArrayList<>());


    }


    @org.junit.jupiter.api.Test
    void GroupFactory() {
        groupFactory = new GroupFactory(pairListFactory, 3, partyLocationCoordinates);
        groupFactory.createGroups();
        Assertions.assertFalse(checkGroupsContainAllPairs(groupFactory));
        Assertions.assertFalse(checkFalseCooking(groupFactory));
        Assertions.assertTrue(checkNewPairsEachDinnerRound(groupFactory));
        Assertions.assertTrue(checkMixedGroupsBadFoodPref(groupFactory));
        Assertions.assertTrue(checkGenderDiversityScore(groupFactory, genderDiversityThreshold));
        Assertions.assertFalse(checkGenderDiversityScore(groupFactory, 0.5));
        Assertions.assertFalse(checkAgeDifferenceScore(groupFactory, 10));
        Assertions.assertFalse(checkPreferenceDeviationScore(groupFactory, 0.5));

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

    /**
     * checks if mixed groups with vegans/veggies contain max one pair with meat/no preference
     * @param groupFactory
     * @return
     */
    private boolean checkMixedGroupsBadFoodPref(GroupFactory groupFactory) {
        for(DinnerRound dinnerRound : groupFactory.getDinnerRounds()) {
            for(Group group : dinnerRound.getGroups()) {
                int noMeatPrefCount = 0;
                int noPrefCount = 0;
                for(Pair pair : group.getPairs()) {
                    String foodPreference = pair.getFoodPreference();
                    if(foodPreference.equals("vegetarisch") || foodPreference.equals("vegan")) {
                        noMeatPrefCount++;
                    }
                    if(foodPreference.equals("none") || foodPreference.equals("meat")) {
                        noPrefCount++;
                    }
                }
                if(noPrefCount > 1 || noMeatPrefCount > 0) {
                    System.out.println("Mixed Group: " + group.toString() + " has more than one meat preference");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * calculates the GenderDiversityScore of all generated Groups and checks if its higher than given threshold
     * @param groupFactory
     * @return
     */
    private boolean checkGenderDiversityScore(GroupFactory groupFactory, double genderDiversityThreshold) {
        double score = 0.0;
        for(DinnerRound dinnerRound : groupFactory.getDinnerRounds()) {
            for(Group group : dinnerRound.getGroups()) {
                score += group.getGenderDiversityScore();
            }
        }
        score = score / (groupFactory.getDinnerRounds().size() * groupFactory.getDinnerRounds().get(0).getGroups().size());
        System.out.println("GenderDiversity Score: " + score + " (should be > "+  genderDiversityThreshold +")");
        return score < genderDiversityThreshold;
    }

    /**
     * calculates the AgeDifferenceScore of all generated Groups and checks if its higher than given threshold
     */
    private boolean checkAgeDifferenceScore(GroupFactory groupFactory, double ageDifferenceThreshold) {
        double score = 0.0;
        for(DinnerRound dinnerRound : groupFactory.getDinnerRounds()) {
            for(Group group : dinnerRound.getGroups()) {
                score += group.getAgeRangeDeviationScore();
            }
        }
        score = score / (groupFactory.getDinnerRounds().size() * groupFactory.getDinnerRounds().get(0).getGroups().size());
        System.out.println("AgeDifference Score: " + score + " (should be > "+  ageDifferenceThreshold +")");
        return score > ageDifferenceThreshold;

    }

    /**
     * calculates the PreferenceDeviationScore of all generated Groups and checks if its higher than given threshold
     */
    private boolean checkPreferenceDeviationScore(GroupFactory groupFactory, double preferenceDeviationThreshold) {
        double score = 0.0;
        for(DinnerRound dinnerRound : groupFactory.getDinnerRounds()) {
            for(Group group : dinnerRound.getGroups()) {
                score += group.getFoodPreferenceDeviationScore();
            }
        }
        score = score / (groupFactory.getDinnerRounds().size() * groupFactory.getDinnerRounds().get(0).getGroups().size());
        System.out.println("PreferenceDeviation Score: " + score + " (should be > "+  preferenceDeviationThreshold +")");
        return score > preferenceDeviationThreshold;
    }





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
            pair.getPlaceOfCooking();

        }
        return false;
    }

    private boolean checkAgeDifference(Group group) {
        return true;
    }

    private boolean checkPreferenceDeviation(Group group) {
        return true;
    }
    //Helper Methods

    private double calculateGeographicalDistance(Double[] firstCoordinates, Double[] secondCoordinates) {
        Double distance = 0.0;
        double firstLatitude = firstCoordinates[0];
        double firstLongitude = firstCoordinates[1];
        double secondLatitude = secondCoordinates[0];
        double secondLongitude = secondCoordinates[1];

        double earthRadius = 6371.0;

        double latitudeDistance = Math.toRadians(secondLatitude - firstLatitude);
        double longitudeDistance = Math.toRadians(secondLongitude - firstLongitude);

        double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
                + Math.cos(Math.toRadians(firstLatitude)) * Math.cos(Math.toRadians(secondLatitude))
                * Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        distance = earthRadius * c;

        return distance;
    }

    private double distanceToPartyLocation(Double[] coordinates) {
        return calculateGeographicalDistance(coordinates, partyLocationCoordinates);
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