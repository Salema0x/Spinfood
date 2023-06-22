package Factory.Group;

import Entity.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is holding methods to generate a list of groups out of the list of pairs.
 * @author David Krell
 */
public class GroupFactory {
    private final ArrayList<Pair> pairList;
    private final ArrayList<Pair> successorPairs = new ArrayList<>();
    private final Double[] PARTY_LOCATION = new Double[2];
    private final ArrayList<Group> appetizerGroups = new ArrayList<>();
    private final ArrayList<Group> mainDishGroups = new ArrayList<>();
    private final ArrayList<Group> dessertGroups = new ArrayList<>();
    private final ArrayList<Group> successorGroups = new ArrayList<>();


    public GroupFactory(ArrayList<Pair> pairList, Double[] partyLocation) {
        this.pairList = pairList;
        PARTY_LOCATION[0] = partyLocation[0];
        PARTY_LOCATION[1] = partyLocation[1];
    }

    /**
     * Initiates the building of groups.
     */
    public void startGroupAlgorithm() {
        RingFactory ringFactory = new RingFactory(pairList, PARTY_LOCATION);

        Ring outerRing = new Ring(ringFactory.getOuterRing());
        Ring middleRing = new Ring(ringFactory.getMiddleRing());
        Ring innerRing = new Ring(ringFactory.getInnerRing());

        makeAppetizerGroups(outerRing);
        testGroups();
        printAppetizerGroups(appetizerGroups);
    }

    private void testGroups() {
        ArrayList<Pair> pairs = new ArrayList<>();
        for (Group group : appetizerGroups) {
            pairs.addAll(group.getPairs());
        }

        Set<Pair> set = new HashSet<Pair>(pairs);

        if (set.size() < pairs.size()) {
            System.out.println("Duplikate!");
        }
    }

    /**
     * Makes groups for the appetizer. The pairs on the outerRing are the pair who are cooking the appetizer.
     * @param outerRing a Ring Record indicating the ring with which the groups should be generated.
     */
    private void makeAppetizerGroups(Ring outerRing) {
        ArrayList<Pair> outerRingPairs = outerRing.pairsOnTheRing();
        LinkedList<Pair> possibleMatchingPairs = new LinkedList<>(pairList);

        possibleMatchingPairs.removeAll(outerRingPairs);

        Map<PairAttributes, List<Pair>> pairsByAttributes = possibleMatchingPairs
                .stream()
                .collect(Collectors.groupingBy(PairAttributes::new));

        for (Pair cookingPair : outerRingPairs) {
            FoodPreference foodPreferenceFromCookingPair = cookingPair.getFoodPreference();

            ArrayList<Pair> groupMembers = switch (foodPreferenceFromCookingPair) {
                case MEAT -> findPairsForMeatPair(cookingPair, pairsByAttributes);
                case VEGGIE -> findPairsForVeggiePair(cookingPair, pairsByAttributes);
                case VEGAN -> findPairsForVeganPair(cookingPair, pairsByAttributes);
            };

            for(Pair pair : groupMembers) {
                PairAttributes attributes = new PairAttributes(pair);
                pairsByAttributes.get(attributes).remove(pair)
            }

            groupMembers.add(cookingPair);
            Group group = new Group(groupMembers);


            appetizerGroups.add(group);
        }
    }

    /**
     * Will start the according algorithm to find two matching pairs for a given Pair. It's decided after gender which algorithm is chosen.
     * @param cookingPair the pair for which the two matching pairs should get found.
     * @param possibleMatchingPairs all the pairs which are possible matching pairs organized in a map.
     * @return a List containing the best matching pairs.
     */
    private ArrayList<Pair> findPairsForMeatPair(Pair cookingPair, Map<PairAttributes, List<Pair>> possibleMatchingPairs) {
        Gender genderFromCookingPair = cookingPair.getGender();

        if (possibleMatchingPairs.values().size() < 2) {
            return new ArrayList<>();
        }

        ArrayList<Pair> groupMembers;

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.MEAT, FoodPreference.MEAT, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGGIE, FoodPreference.VEGGIE, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGGIE, FoodPreference.VEGAN, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGAN, FoodPreference.VEGGIE, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGAN, FoodPreference.VEGAN, genderFromCookingPair, possibleMatchingPairs);

        return groupMembers;
    }

    private ArrayList<Pair> findPairsForVeggiePair(Pair cookingPair, Map<PairAttributes, List<Pair>> possibleMatchingPairs) {
        Gender genderFromCookingPair = cookingPair.getGender();

        if (possibleMatchingPairs.values().size() < 2) {
            return new ArrayList<>();
        }

        ArrayList<Pair> groupMembers;

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGGIE, FoodPreference.VEGGIE, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGGIE, FoodPreference.VEGAN, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return  groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGGIE, FoodPreference.MEAT, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGAN, FoodPreference.VEGGIE, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGAN, FoodPreference.VEGAN, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGAN, FoodPreference.MEAT, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.MEAT, FoodPreference.VEGGIE, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.MEAT, FoodPreference.VEGAN, genderFromCookingPair, possibleMatchingPairs);

        return groupMembers;
    }

    private ArrayList<Pair> findPairsForVeganPair(Pair cookingPair, Map<PairAttributes, List<Pair>> possibleMatchingPairs) {
        Gender genderFromCookingPair = cookingPair.getGender();

        if (possibleMatchingPairs.values().size() < 2) {
            return new ArrayList<>();
        }

        ArrayList<Pair> groupMembers;

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGAN, FoodPreference.VEGAN, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGAN, FoodPreference.VEGGIE, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGAN, FoodPreference.MEAT, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGGIE, FoodPreference.VEGAN, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGGIE, FoodPreference.VEGGIE, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.VEGGIE, FoodPreference.MEAT, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.MEAT, FoodPreference.VEGAN, genderFromCookingPair, possibleMatchingPairs);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(FoodPreference.MEAT, FoodPreference.VEGGIE, genderFromCookingPair, possibleMatchingPairs);

        return groupMembers;
    }

    private ArrayList<Pair> findTwoPairsForFoodPreference(FoodPreference firstFoodPreference, FoodPreference secondFoodPreference, Gender genderFromCookingPair, Map<PairAttributes, List<Pair>> possibleMatchingPairs) {
        ArrayList<Pair> groupMembers = new ArrayList<>();

        PairAttributes firstFemale = new PairAttributes(firstFoodPreference, Gender.FEMALE);
        PairAttributes firstMixed = new PairAttributes(firstFoodPreference, Gender.MIXED);
        PairAttributes firstMale = new PairAttributes(firstFoodPreference, Gender.MALE);

        PairAttributes secondFemale = new PairAttributes(secondFoodPreference, Gender.FEMALE);
        PairAttributes secondMixed = new PairAttributes(secondFoodPreference, Gender.MIXED);
        PairAttributes secondMale = new PairAttributes(secondFoodPreference, Gender.MALE);

        ArrayList<Pair> firstFemaleList = new ArrayList<>(possibleMatchingPairs.get(firstFemale));
        ArrayList<Pair> firstMixedList = new ArrayList<>(possibleMatchingPairs.get(firstMixed));
        ArrayList<Pair> firstMaleList = new ArrayList<>(possibleMatchingPairs.get(firstMale));

        ArrayList<Pair> secondFemaleList = new ArrayList<>(possibleMatchingPairs.get(secondFemale));
        ArrayList<Pair> secondMixedList = new ArrayList<>(possibleMatchingPairs.get(secondMixed));
        ArrayList<Pair> secondMaleList = new ArrayList<>(possibleMatchingPairs.get(secondMale));

        if (genderFromCookingPair == Gender.MALE) {
            if (checkLists(firstMixedList, secondFemaleList)) {
                int[] indices = calculateIndices(firstMixedList, secondFemaleList);
                groupMembers.add(firstMixedList.remove(indices[0]));
                groupMembers.add(secondFemaleList.remove(indices[1]));
            } else if (checkLists(firstFemaleList, secondMixedList)) {
                int[] indices = calculateIndices(firstFemaleList, secondMixedList);
                groupMembers.add(firstFemaleList.remove(indices[0]));
                groupMembers.add(secondMixedList.remove(indices[1]));
            } else if (checkLists(firstFemaleList, secondFemaleList)) {
                int[] indices = calculateIndices(firstFemaleList, secondFemaleList);
                groupMembers.add(firstFemaleList.remove(indices[0]));
                groupMembers.add(secondFemaleList.remove(indices[1]));
            } else if (checkLists(firstMixedList, secondMixedList)) {
                int[] indices = calculateIndices(firstMixedList, secondMixedList);
                groupMembers.add(firstMixedList.remove(indices[0]));
                groupMembers.add(secondMixedList.remove(indices[1]));
            } else if (checkLists(firstFemaleList, secondMaleList)) {
                int[] indices = calculateIndices(firstFemaleList, secondMaleList);
                groupMembers.add(firstFemaleList.remove(indices[0]));
                groupMembers.add(secondMaleList.remove(indices[1]));
            } else if (checkLists(firstMaleList, secondFemaleList)) {
                int[] indices = calculateIndices(firstMaleList, secondFemaleList);
                groupMembers.add(firstMaleList.remove(indices[0]));
                groupMembers.add(secondFemaleList.remove(indices[1]));
            } else if (checkLists(firstMixedList, secondMaleList)) {
                int[] indices = calculateIndices(firstMixedList, secondMaleList);
                groupMembers.add(firstMixedList.remove(indices[0]));
                groupMembers.add(secondMaleList.remove(indices[1]));
            } else if (checkLists(firstMaleList, secondMixedList)) {
                int[] indices = calculateIndices(firstMaleList, secondMixedList);
                groupMembers.add(firstMaleList.remove(indices[0]));
                groupMembers.add(secondMixedList.remove(indices[1]));
            } else if (checkLists(firstMaleList, secondMaleList)) {
                int[] indices = calculateIndices(firstMaleList, secondMaleList);
                groupMembers.add(firstMaleList.remove(indices[0]));
                groupMembers.add(secondMaleList.remove(indices[1]));
            }
        } else if (genderFromCookingPair == Gender.FEMALE) {
            if (checkLists(firstMixedList, secondMaleList)) {
                int[] indices = calculateIndices(firstMixedList, secondMaleList);
                groupMembers.add(firstMixedList.remove(indices[0]));
                groupMembers.add(secondMaleList.remove(indices[1]));
            } else if (checkLists(firstMaleList, secondMixedList)) {
                int[] indices = calculateIndices(firstMaleList, secondMixedList);
                groupMembers.add(firstMaleList.remove(indices[0]));
                groupMembers.add(secondMixedList.remove(indices[1]));
            } else if (checkLists(firstMixedList, secondMixedList)) {
                int[] indices = calculateIndices(firstMixedList, secondMixedList);
                groupMembers.add(firstMixedList.remove(indices[0]));
                groupMembers.add(secondMixedList.remove(indices[1]));
            } else if (checkLists(firstFemaleList, secondMaleList)) {
                int[] indices = calculateIndices(firstFemaleList, secondMaleList);
                groupMembers.add(firstFemaleList.remove(indices[0]));
                groupMembers.add(secondMaleList.remove(indices[1]));
            } else if (checkLists(firstMaleList, secondFemaleList)) {
                int[] indices = calculateIndices(firstMaleList, secondFemaleList);
                groupMembers.add(firstMaleList.remove(indices[0]));
                groupMembers.add(secondFemaleList.remove(indices[1]));
            } else if (checkLists(firstMaleList, secondMaleList)) {
                int[] indices = calculateIndices(firstMaleList, secondMaleList);
                groupMembers.add(firstMaleList.remove(indices[0]));
                groupMembers.add(secondMaleList.remove(indices[1]));
            } else if (checkLists(firstMixedList, secondFemaleList)) {
                int[] indices = calculateIndices(firstMixedList, secondFemaleList);
                groupMembers.add(firstMixedList.remove(indices[0]));
                groupMembers.add(secondFemaleList.remove(indices[1]));
            } else if (checkLists(firstFemaleList, secondMixedList)) {
                int[] indices = calculateIndices(firstFemaleList, secondMixedList);
                groupMembers.add(firstFemaleList.remove(indices[0]));
                groupMembers.add(secondMixedList.remove(indices[1]));
            } else if (checkLists(firstFemaleList, secondFemaleList)) {
                int[] indices = calculateIndices(firstFemaleList, secondFemaleList);
                groupMembers.add(firstFemaleList.remove(indices[0]));
                groupMembers.add(secondFemaleList.remove(indices[1]));
            }
        } else if (genderFromCookingPair == Gender.MIXED) {
            if (checkLists(firstMixedList, secondMixedList)) {
                int[] indices = calculateIndices(firstMixedList, secondMixedList);
                groupMembers.add(firstMixedList.remove(indices[0]));
                groupMembers.add(secondMixedList.remove(indices[1]));
            } else if (checkLists(firstFemaleList, secondMaleList)) {
                int[] indices = calculateIndices(firstFemaleList, secondMaleList);
                groupMembers.add(firstFemaleList.remove(indices[0]));
                groupMembers.add(secondMaleList.remove(indices[1]));
            } else if (checkLists(firstMaleList, secondFemaleList)) {
                int[] indices = calculateIndices(firstMaleList, secondFemaleList);
                groupMembers.add(firstMaleList.remove(indices[0]));
                groupMembers.add(secondFemaleList.remove(indices[1]));
            } else if (checkLists(firstMixedList, secondFemaleList)) {
                int[] indices = calculateIndices(firstMixedList, secondFemaleList);
                groupMembers.add(firstMixedList.remove(indices[0]));
                groupMembers.add(secondFemaleList.remove(indices[1]));
            } else if (checkLists(firstFemaleList, secondMixedList)) {
                int[] indices = calculateIndices(firstFemaleList, secondMixedList);
                groupMembers.add(firstFemaleList.remove(indices[0]));
                groupMembers.add(secondMixedList.remove(indices[1]));
            } else if (checkLists(firstMixedList, secondMaleList)) {
                int[] indices = calculateIndices(firstMixedList, secondMaleList);
                groupMembers.add(firstMixedList.remove(indices[0]));
                groupMembers.add(secondMaleList.remove(indices[1]));
            } else if (checkLists(firstMaleList, secondMixedList)) {
                int[] indices = calculateIndices(firstMaleList, secondMixedList);
                groupMembers.add(firstMaleList.remove(indices[0]));
                groupMembers.add(secondMixedList.remove(indices[1]));
            } else if (checkLists(firstFemaleList, secondFemaleList)) {
                int[] indices = calculateIndices(firstFemaleList, secondFemaleList);
                groupMembers.add(firstFemaleList.remove(indices[0]));
                groupMembers.add(secondFemaleList.remove(indices[1]));
            } else if (checkLists(firstMaleList, secondMaleList)) {
                int[] indices = calculateIndices(firstMaleList, secondMaleList);
                groupMembers.add(firstMaleList.remove(indices[0]));
                groupMembers.add(secondMaleList.remove(indices[1]));
            }
        }

        return groupMembers;
    }

    private int[] calculateIndices(ArrayList<Pair> listOne, ArrayList<Pair> listTwo) {
        if (listOne.equals(listTwo)) {
            return new int[]{0, 1};
        } else {
            return new int[]{0, 0};
        }
    }

    private boolean checkLists(ArrayList<Pair> listOne, ArrayList<Pair> listTwo) {
        if (listOne.equals(listTwo)) {
            return listOne.size() >= 2;
        } else {
            return !listOne.isEmpty() && !listTwo.isEmpty();
        }
    }

    private void printAppetizerGroups(ArrayList<Group> list) {
        String leftAlignFormat = "| %-13s | %-36s | %-36s | %-36s | %-14s | %-14s | %-14s |%n";

        System.out.format("+--------------+--------------------------------------+--------------------------------------+--------------------------------------+--------------+--------------+--------------+%n");
        System.out.format("| Group Nr.    | Pair1 ID                             | Pair2 ID                             | Pair3 ID                             + Pair1Pref    + Pair2Pref    + Pair3Pref    +%n");
        System.out.format("+--------------+--------------------------------------+--------------------------------------+--------------------------------------+--------------+--------------+--------------+%n");

        int counter = 0;

        for (Group group : list) {
            counter++;
            String id1 = group.getPairs().get(0).getId();
            String id2 = group.getPairs().get(1).getId();
            String id3 = group.getPairs().get(2).getId();
            FoodPreference pref1 = group.getPairs().get(0).getFoodPreference();
            FoodPreference pref2 = group.getPairs().get(1).getFoodPreference();
            FoodPreference pref3 = group.getPairs().get(2).getFoodPreference();

            System.out.format(leftAlignFormat, counter, id1, id2, id3, pref1, pref2, pref3);
        }
        System.out.format("+--------------+--------------------------------------+--------------------------------------+--------------------------------------+%n");
    }





    private void genderFillerForDoubleGroups(String gender, Group group, ArrayList<ArrayList<Pair>> foodPreferenceList, ArrayList<ArrayList<ArrayList<Pair>>> pairsSplitUp, int listIndex, String courseIdentification) {
        ArrayList<Integer> maleFiller = new ArrayList<>(List.of(0, 2, 1));
        ArrayList<Integer> mixedFiller = new ArrayList<>(List.of(2, 1, 0));
        ArrayList<Integer> femaleFiller = new ArrayList<>(List.of(1, 2, 0));

        if (gender.equals("ff") || gender.equals("fmix")) {
            fillOne(group, foodPreferenceList, pairsSplitUp, listIndex, maleFiller, courseIdentification); //TODO
        } else if (gender.equals("fma") || gender.equals("mixmix")) {
            fillOne(group, foodPreferenceList, pairsSplitUp, listIndex, mixedFiller, courseIdentification);
        } else {
            fillOne(group, foodPreferenceList, pairsSplitUp, listIndex, femaleFiller, courseIdentification);
        }
    }

    private void fillOne(Group group, ArrayList<ArrayList<Pair>> foodPreferenceList, ArrayList<ArrayList<ArrayList<Pair>>> pairsSplitUp, int listIndex, ArrayList<Integer> maleFiller, String courseIdentification) {
        ArrayList<ArrayList<Pair>> splitUpPairs = pairsSplitUp.get(listIndex);
        ArrayList<Pair> males = splitUpPairs.get(maleFiller.get(0));
        ArrayList<Pair> females = splitUpPairs.get(maleFiller.get(1));
        ArrayList<Pair> mixed = splitUpPairs.get(maleFiller.get(2));

        if (courseIdentification.equals("appetizer")) {
            if (males.size() >= 1) {
                Pair p = males.remove(0);
                group.addPair(p);
                foodPreferenceList.get(listIndex).remove(p);
                pairsSplitUp.get(listIndex).get(0).remove(p);
            } else if (mixed.size() >= 1) {
                Pair p = mixed.remove(0);
                group.addPair(p);
                foodPreferenceList.get(listIndex).remove(p);
                pairsSplitUp.get(listIndex).get(2).remove(p);
            } else if (females.size() >= 1) {
                Pair p = females.remove(0);
                group.addPair(p);
                foodPreferenceList.get(listIndex).remove(p);
                pairsSplitUp.get(listIndex).get(1).remove(p);
            }
        } else {
            if (males.size() >= 1) {
                Pair initialPair = group.getCookingPair();
                Double[] coordinates = new Double[2];
                if (courseIdentification.equals("main")) {
                    coordinates = initialPair.getCoordinatesFirstRound();
                } else if (courseIdentification.equals("dessert")) {
                    coordinates = initialPair.getCoordinatesSecondRound();
                }

                double minimalDistance = Double.MAX_VALUE;
                Pair minimalPair = null;

                for (Pair pair : males) {
                    Double[] pairCoordinates = new Double[2];

                    if (courseIdentification.equals("main")) {
                        pairCoordinates = initialPair.getCoordinatesFirstRound();
                    } else if (courseIdentification.equals("dessert")) {
                        pairCoordinates = initialPair.getCoordinatesSecondRound();
                    }

                    double distance = RingFactory.calculateDistance(coordinates[0], coordinates[1], pairCoordinates[0], pairCoordinates[1]);

                    if (distance < minimalDistance && !pair.getId().equals(initialPair.getId())) {
                        minimalPair = pair;
                        minimalDistance = distance;
                    }
                }
                males.remove(minimalPair);
                Pair p = minimalPair;
                group.addPair(p);
                foodPreferenceList.get(listIndex).remove(p);
                pairsSplitUp.get(listIndex).get(0).remove(p);
            } else if (mixed.size() >= 1) {
                Pair initialPair = group.getCookingPair();
                Double[] coordinates = new Double[2];
                if (courseIdentification.equals("main")) {
                    coordinates = initialPair.getCoordinatesFirstRound();
                } else if (courseIdentification.equals("dessert")) {
                    coordinates = initialPair.getCoordinatesSecondRound();
                }

                double minimalDistance = Double.MAX_VALUE;
                Pair minimalPair = null;

                for (Pair pair : mixed) {
                    Double[] pairCoordinates = new Double[2];

                    if (courseIdentification.equals("main")) {
                        pairCoordinates = initialPair.getCoordinatesFirstRound();
                    } else if (courseIdentification.equals("dessert")) {
                        pairCoordinates = initialPair.getCoordinatesSecondRound();
                    }

                    double distance = RingFactory.calculateDistance(coordinates[0], coordinates[1], pairCoordinates[0], pairCoordinates[1]);

                    if (distance < minimalDistance && !pair.getId().equals(initialPair.getId())) {
                        minimalPair = pair;
                        minimalDistance = distance;
                    }
                }
                mixed.remove(minimalPair);
                Pair p = minimalPair;
                group.addPair(p);
                foodPreferenceList.get(listIndex).remove(p);
                pairsSplitUp.get(listIndex).get(0).remove(p);
            } else if (females.size() >= 1) {
                Pair initialPair = group.getCookingPair();
                Double[] coordinates = new Double[2];
                if (courseIdentification.equals("main")) {
                    coordinates = initialPair.getCoordinatesFirstRound();
                } else if (courseIdentification.equals("dessert")) {
                    coordinates = initialPair.getCoordinatesSecondRound();
                }

                double minimalDistance = Double.MAX_VALUE;
                Pair minimalPair = null;

                for (Pair pair : females) {
                    Double[] pairCoordinates = new Double[2];

                    if (courseIdentification.equals("main")) {
                        pairCoordinates = initialPair.getCoordinatesFirstRound();
                    } else if (courseIdentification.equals("dessert")) {
                        pairCoordinates = initialPair.getCoordinatesSecondRound();
                    }

                    double distance = RingFactory.calculateDistance(coordinates[0], coordinates[1], pairCoordinates[0], pairCoordinates[1]);

                    if (distance < minimalDistance && !pair.getId().equals(initialPair.getId())) {
                        minimalPair = pair;
                        minimalDistance = distance;
                    }
                }
                females.remove(minimalPair);
                Pair p = minimalPair;
                group.addPair(p);
                foodPreferenceList.get(listIndex).remove(p);
                pairsSplitUp.get(listIndex).get(0).remove(p);
            }
        }
    }


    private void fillMixedPair(Group group, ArrayList<ArrayList<ArrayList<Pair>>> pairsSplitUp, int listIndicator, ArrayList<ArrayList<Pair>> foodPreferenceList, String courseIdentification) {
        ArrayList<ArrayList<Pair>> splitUpVegans = pairsSplitUp.get(listIndicator);
        ArrayList<Pair> males = splitUpVegans.get(0);
        ArrayList<Pair> females = splitUpVegans.get(1);
        ArrayList<Pair> mixed = splitUpVegans.get(2);

        if (mixed.size() >= 2) {
            remover(2, 2, pairsSplitUp, listIndicator, foodPreferenceList, group, courseIdentification, mixed);
        } else if (males.size() >= 1 && females.size() >= 1) {
            remover(0, 1, pairsSplitUp, listIndicator, foodPreferenceList, group, courseIdentification, males, females);
        } else if (mixed.size() == 1 && males.size() >= 1) {
            remover(2, 0, pairsSplitUp, listIndicator, foodPreferenceList, group, courseIdentification, mixed, males);
        } else if (mixed.size() == 1 && females.size() >= 1) {
            remover(2, 1, pairsSplitUp, listIndicator, foodPreferenceList, group, courseIdentification, mixed, females);
        } else if (females.size() >= 2) {
            remover(1, 1, pairsSplitUp, listIndicator, foodPreferenceList, group, courseIdentification, females);
        } else if (males.size() >= 2) {
            remover(0, 0, pairsSplitUp, listIndicator, foodPreferenceList, group, courseIdentification, males);
        } else {
            successorGroups.add(group);
        }
    }

    @SafeVarargs
    private static void remover(int firstIndex, int secondIndex,
                         ArrayList<ArrayList<ArrayList<Pair>>> pairsSplitUp,
                         int listIndicator, ArrayList<ArrayList<Pair>> foodPreferenceList,
                         Group group, String courseIdentification, ArrayList<Pair>... pairs) {
        Pair p1 = null;
        Pair p2 = null;
        if (courseIdentification.equals("appetizer")) {

            if (pairs.length == 2) {
                p1 = pairs[0].remove(0);
                p2 = pairs[1].remove(0);
            } else if (pairs.length == 1) {
                p1 = pairs[0].remove(0);
                p2 = pairs[0].remove(0);
            }

            group.addPair(p1);
            group.addPair(p2);

            pairsSplitUp.get(listIndicator).get(firstIndex).remove(p1);
            pairsSplitUp.get(listIndicator).get(secondIndex).remove(p2);

            foodPreferenceList.get(listIndicator).remove(p1);
            foodPreferenceList.get(listIndicator).remove(p2);
        } else {
            Pair initialPair = group.getPairs().get(0);
            Double[] coordinates = new Double[2];

            if (courseIdentification.equals("main")) {
                coordinates = initialPair.getCoordinatesFirstRound();
            } else if (courseIdentification.equals("dessert")) {
                coordinates = initialPair.getCoordinatesSecondRound();
            }

            if (pairs.length == 2) {
                double minimalDistance = Double.MAX_VALUE;
                Pair minimalPair = null;

                for (Pair pair : pairs[0]) {
                    Double[] pairCoordinates = new Double[2];

                    if (courseIdentification.equals("main")) {
                        pairCoordinates = initialPair.getCoordinatesFirstRound();
                    } else if (courseIdentification.equals("dessert")) {
                        pairCoordinates = initialPair.getCoordinatesSecondRound();
                    }

                    double distance = RingFactory.calculateDistance(coordinates[0], coordinates[1], pairCoordinates[0], pairCoordinates[1]);
                    if (distance < minimalDistance && !Objects.equals(pair.getId(), initialPair.getId())) {
                        minimalPair = pair;
                        minimalDistance = distance;
                    }
                }

                pairs[0].remove(minimalPair);
                p1 = minimalPair;
                group.addPair(p1);
                pairsSplitUp.get(listIndicator).get(firstIndex).remove(minimalPair);
                foodPreferenceList.get(listIndicator).remove(minimalPair);

                minimalDistance = Double.MAX_VALUE;
                for (Pair pair : pairs[1]) {
                    Double[] pairCoordinates = new Double[2];

                    if (courseIdentification.equals("main")) {
                        pairCoordinates = initialPair.getCoordinatesFirstRound();
                    } else if (courseIdentification.equals("dessert")) {
                        pairCoordinates = initialPair.getCoordinatesSecondRound();
                    }

                    double distance = RingFactory.calculateDistance(coordinates[0], coordinates[1], pairCoordinates[0], pairCoordinates[1]);
                    if (distance < minimalDistance && !Objects.equals(pair.getId(), initialPair.getId())) {
                        minimalPair = pair;
                        minimalDistance = distance;
                    }
                }

                pairs[1].remove(minimalPair);
                p1 = minimalPair;
                group.addPair(p1);
                pairsSplitUp.get(listIndicator).get(firstIndex).remove(minimalPair);
                foodPreferenceList.get(listIndicator).remove(minimalPair);
            } else if (pairs.length == 1) {
                double minimalDistance = Double.MAX_VALUE;
                Pair minimalPair = null;

                for (Pair pair : pairs[0]) {
                    Double[] pairCoordinates = new Double[2];

                    if (courseIdentification.equals("main")) {
                        pairCoordinates = initialPair.getCoordinatesFirstRound();
                    } else if (courseIdentification.equals("dessert")) {
                        pairCoordinates = initialPair.getCoordinatesSecondRound();
                    }

                    double distance = RingFactory.calculateDistance(coordinates[0], coordinates[1], pairCoordinates[0], pairCoordinates[1]);
                    if (distance < minimalDistance && !Objects.equals(pair.getId(), initialPair.getId())) {
                        minimalPair = pair;
                        minimalDistance = distance;
                    }
                }

                pairs[0].remove(minimalPair);
                p1 = minimalPair;
                group.addPair(p1);
                pairsSplitUp.get(listIndicator).get(firstIndex).remove(minimalPair);
                foodPreferenceList.get(listIndicator).remove(minimalPair);

                minimalDistance = Double.MAX_VALUE;
                for (Pair pair : pairs[0]) {
                    Double[] pairCoordinates = new Double[2];

                    if (courseIdentification.equals("main")) {
                        pairCoordinates = initialPair.getCoordinatesFirstRound();
                    } else if (courseIdentification.equals("dessert")) {
                        pairCoordinates = initialPair.getCoordinatesSecondRound();
                    }

                    double distance = RingFactory.calculateDistance(coordinates[0], coordinates[1], pairCoordinates[0], pairCoordinates[1]);
                    if (distance < minimalDistance && !Objects.equals(pair.getId(), initialPair.getId())) {
                        minimalPair = pair;
                        minimalDistance = distance;
                    }
                }

                pairs[0].remove(minimalPair);
                p1 = minimalPair;
                group.addPair(p1);
                pairsSplitUp.get(listIndicator).get(firstIndex).remove(minimalPair);
                foodPreferenceList.get(listIndicator).remove(minimalPair);
            }
        }

    }

    private void fillMaleOnlyPair(Group group, ArrayList<ArrayList<ArrayList<Pair>>> splitUp, boolean fillFemales, int listIndicator, ArrayList<ArrayList<Pair>> foodPreferenceList, String courseIdentification) {
        ArrayList<ArrayList<Pair>> splitUpVegans = splitUp.get(listIndicator);
        ArrayList<Pair> males = splitUpVegans.get(0);
        ArrayList<Pair> females = splitUpVegans.get(1);
        ArrayList<Pair> mixed = splitUpVegans.get(2);

        if (fillFemales) {
            if (males.size() >= 1 && mixed.size() >= 1) {
                remover(0, 2, splitUp, listIndicator, foodPreferenceList, group, courseIdentification, males, mixed);
            } else if (mixed.size() >= 2) {
                remover(2, 2, splitUp, listIndicator, foodPreferenceList, group, courseIdentification, mixed);
            } else if (males.size() >= 2) {
                remover(0, 0, splitUp, listIndicator, foodPreferenceList, group, courseIdentification, males);
            } else if (males.size() == 1 && females.size() >= 1) {
                remover(0, 1, splitUp, listIndicator, foodPreferenceList, group, courseIdentification, males, females);
            } else if (females.size() >= 1 && mixed.size() == 1) {
                remover(1, 2, splitUp, listIndicator, foodPreferenceList, group, courseIdentification, females, mixed);
            } else if (females.size() >= 2) {
                remover(1, 1, splitUp, listIndicator, foodPreferenceList, group, courseIdentification, females);
            } else {
                successorGroups.add(group);
            }
        } else {
            if (females.size() >= 1 && mixed.size() >= 1) {
                remover(1, 2, splitUp, listIndicator, foodPreferenceList, group, courseIdentification, females, mixed);
            } else if (mixed.size() >= 2) {
                remover(2, 2, splitUp, listIndicator, foodPreferenceList, group, courseIdentification, mixed);
            } else if (females.size() >= 2) {
                remover(1, 1, splitUp, listIndicator, foodPreferenceList, group, courseIdentification, females);
            } else if (females.size() == 1 && males.size() >= 1) {
                remover(1, 0, splitUp, listIndicator, foodPreferenceList, group, courseIdentification, females, males);
            } else if (males.size() >= 1 && mixed.size() == 1) {
                remover(0, 2, splitUp, listIndicator, foodPreferenceList, group, courseIdentification, males, mixed);
            } else if (males.size() >= 2) {
                remover(0, 0, splitUp, listIndicator, foodPreferenceList, group, courseIdentification, males);
            } else {
                successorGroups.add(group);
            }
        }
    }
}