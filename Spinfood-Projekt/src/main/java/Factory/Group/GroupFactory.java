package Factory.Group;

import Entity.FoodPreference;
import Entity.Group;
import Entity.Pair;
import Entity.PairAttributes;
import Misc.PairDistanceComparator;

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
    }

    /**
     * Makes groups for the appetizer. The pairs on the outerRing are the pair who are cooking the appetizer.
     * @param outerRing a Ring Record indicating the ring with which the groups should be generated.
     */
    private void makeAppetizerGroups(Ring outerRing) {
        ArrayList<Pair> outerRingPairs = outerRing.pairsOnTheRing();
        ArrayList<Pair> possibleMatchingPairs = new ArrayList<>(pairList);
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

            if (groupMembers.size() != 2) {
                continue;
            }

            possibleMatchingPairs.removeAll(groupMembers);

            pairsByAttributes = possibleMatchingPairs
                    .stream()
                    .collect(Collectors.groupingBy(PairAttributes::new));

            groupMembers.add(cookingPair);
            Group group = new Group(groupMembers);

            appetizerGroups.add(group);
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
            String pref1 = group.getPairs().get(0).getFoodPreference();
            String pref2 = group.getPairs().get(1).getFoodPreference();
            String pref3 = group.getPairs().get(2).getFoodPreference();

            System.out.format(leftAlignFormat, counter, id1, id2, id3, pref1, pref2, pref3);
        }
        System.out.format("+--------------+--------------------------------------+--------------------------------------+--------------------------------------+%n");
    }


    private void cleanPairsSplitUp(ArrayList<ArrayList<ArrayList<Pair>>> pairsSplitUp, ArrayList<Pair> outerRing) {
        for (ArrayList<ArrayList<Pair>> genderLists : pairsSplitUp) {
            for (ArrayList<Pair> pairs : genderLists) {
                pairs.removeAll(outerRing);
            }
        }
    }

    private void fillWithMeatNones(Group group, Pair cookingPair, ArrayList<ArrayList<ArrayList<Pair>>> pairsSplitUp, ArrayList<ArrayList<Pair>> foodPreferenceList, ArrayList<Integer> meatFiller, int listIndicator, String courseIdentification) {
        ArrayList<ArrayList<Pair>> foodPreferenceListCopy;
        ArrayList<ArrayList<ArrayList<Pair>>> pairsSplitUpCopy;
        if (courseIdentification.equals("main") || courseIdentification.equals("dessert")) {
            ArrayList<Pair> seen = cookingPair.seen;

            foodPreferenceListCopy = new ArrayList<>(foodPreferenceList);
            pairsSplitUpCopy = new ArrayList<>(pairsSplitUp);

            foodPreferenceListCopy.get(0).removeAll(seen);
            foodPreferenceListCopy.get(1).removeAll(seen);
            foodPreferenceListCopy.get(2).removeAll(seen);
            foodPreferenceListCopy.get(3).removeAll(seen);

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    pairsSplitUpCopy.get(i).get(j).removeAll(seen);
                }

            }
        } else {
            foodPreferenceListCopy = foodPreferenceList;
            pairsSplitUpCopy = pairsSplitUp;
        }


        ArrayList<Pair> foodPairs = foodPreferenceListCopy.get(listIndicator);

        int amountPairs = foodPairs.size();

        String gender = cookingPair.getGender();

        if (amountPairs > 2) {
            switch (gender) {
                case "male" -> fillMaleOnlyPair(group, pairsSplitUpCopy, false, listIndicator, foodPreferenceListCopy, courseIdentification);
                case "female" -> fillMaleOnlyPair(group, pairsSplitUpCopy, true, listIndicator, foodPreferenceListCopy, courseIdentification);
                case "mixed" -> fillMixedPair(group, pairsSplitUpCopy, listIndicator, foodPreferenceListCopy, courseIdentification);
            }
        } else if (amountPairs == 2) {
            group.addPairs(foodPairs);
            foodPairs.clear();
            foodPreferenceListCopy.get(listIndicator).clear();
            for (ArrayList<Pair> pairs : pairsSplitUpCopy.get(listIndicator)) {
                pairs.clear();
            }
        } else {
            //TODO wir können auch einen Veganer und einen Veggie adden
            if (filler(group, foodPreferenceListCopy, 2, pairsSplitUpCopy, 3, courseIdentification) == -2) {
                if (filler(group, foodPreferenceListCopy, 1, pairsSplitUpCopy, 2, courseIdentification) == -1) {
                    successorGroups.add(group);
                }
            } else if (filler(group, foodPreferenceListCopy, 2, pairsSplitUpCopy, 3, courseIdentification) == -1) {
                if (filler(group, foodPreferenceListCopy, 2, pairsSplitUpCopy, 2, courseIdentification) == -1) {
                    successorGroups.add(group);
                }
            }
        }
    }

    private void fillWithVegansVeggies(Group group, Pair initialPair, ArrayList<ArrayList<ArrayList<Pair>>> pairsSplitUp, ArrayList<ArrayList<Pair>> foodPreferenceList, ArrayList<Integer> veganFiller, int listIndicator, String courseIdentification) {
        ArrayList<ArrayList<Pair>> foodPreferenceListCopy;
        ArrayList<ArrayList<ArrayList<Pair>>> pairsSplitUpCopy;
        if (courseIdentification.equals("main") || courseIdentification.equals("dessert")) {
            ArrayList<Pair> seen = initialPair.seen;

            foodPreferenceListCopy = new ArrayList<>(foodPreferenceList);
            pairsSplitUpCopy = new ArrayList<>(pairsSplitUp);

            foodPreferenceListCopy.get(0).removeAll(seen);
            foodPreferenceListCopy.get(1).removeAll(seen);
            foodPreferenceListCopy.get(2).removeAll(seen);
            foodPreferenceListCopy.get(3).removeAll(seen);

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    pairsSplitUpCopy.get(i).get(j).removeAll(seen);
                }

            }
        } else {
            foodPreferenceListCopy = foodPreferenceList;
            pairsSplitUpCopy = pairsSplitUp;
        }

        ArrayList<Pair> vegans = foodPreferenceListCopy.get(listIndicator);

        int amountVegans = vegans.size();

        String gender = initialPair.getGender();


        if (amountVegans > 2) {
            switch (gender) {
                case "male" -> fillMaleOnlyPair(group, pairsSplitUpCopy, false, listIndicator, foodPreferenceListCopy, courseIdentification);
                case "female" -> fillMaleOnlyPair(group, pairsSplitUpCopy, true, listIndicator, foodPreferenceListCopy, courseIdentification);
                case "mixed" -> fillMixedPair(group, pairsSplitUpCopy, listIndicator, foodPreferenceListCopy, courseIdentification);
            }
        } else if (amountVegans == 2) {
            group.addPairs(vegans);
            vegans.clear();
            foodPreferenceListCopy.get(listIndicator).clear();
            for (ArrayList<Pair> pairs : pairsSplitUpCopy.get(listIndicator)) {
                    pairs.clear();
            }
        } else if (amountVegans == 1) {
            group.addPairs(vegans);
            vegans.clear();
            foodPreferenceListCopy.get(listIndicator).clear();
            for (ArrayList<Pair> pairs : pairsSplitUpCopy.get(listIndicator)) {
                    pairs.clear();
            }
            if (filler(group, foodPreferenceListCopy, 1, pairsSplitUpCopy, veganFiller.get(0), courseIdentification) == -1) {
                if (filler(group, foodPreferenceListCopy, 1, pairsSplitUpCopy, veganFiller.get(1), courseIdentification) == -1) {
                    if (filler(group, foodPreferenceListCopy, 1, pairsSplitUpCopy, veganFiller.get(2), courseIdentification) == -1 ){
                        successorGroups.add(group);
                    }
                }
            }
        } else {
            if (filler(group, foodPreferenceListCopy, 2, pairsSplitUpCopy, veganFiller.get(3), courseIdentification) == -1) {
                successorGroups.add(group);
            } else if (filler(group, foodPreferenceListCopy, 2, pairsSplitUpCopy, veganFiller.get(4), courseIdentification) == -2) {
                if (filler(group, foodPreferenceListCopy, 1, pairsSplitUpCopy, veganFiller.get(5), courseIdentification) == -1) {
                    if (filler(group, foodPreferenceListCopy, 1, pairsSplitUpCopy, veganFiller.get(6), courseIdentification) == -1 ) {
                        successorGroups.add(group);
                    }
                }
            }
        }
    }

    private int filler(Group group, ArrayList<ArrayList<Pair>> foodPreferenceList, int i, ArrayList<ArrayList<ArrayList<Pair>>> pairsSplitUp, int listIndex, String courseIdentification) {
        ArrayList<Pair> veggies = foodPreferenceList.get(listIndex);

        if (i == 1) {
            String gender = group.getGender();
            if (veggies.size() > 1) {
                genderFillerForDoubleGroups(gender, group, foodPreferenceList, pairsSplitUp, listIndex, courseIdentification); //TODO
                return 0;
            } else if (veggies.size() == 1) {
                group.addPairs(veggies);
                veggies.clear();
                foodPreferenceList.get(listIndex).clear();
                for (ArrayList<Pair> pairs : pairsSplitUp.get(listIndex)) {
                    pairs.clear();
                }
                return 0;
            } else {
                return -1;
            }
        } else if (i == 2) {
            Pair p = group.getPairs().get(0);
            String gender = p.getGender();

            if (veggies.size() > 2) {
                switch (gender) {
                    case "male" -> fillMaleOnlyPair(group, pairsSplitUp, false, listIndex, foodPreferenceList, courseIdentification);
                    case "female" -> fillMaleOnlyPair(group, pairsSplitUp, true, listIndex, foodPreferenceList, courseIdentification);
                    case "mixed" -> fillMixedPair(group, pairsSplitUp, listIndex, foodPreferenceList, courseIdentification);
                }
                return 0;
            } else if (veggies.size() == 2) {
                group.addPairs(veggies);
                veggies.clear();
                foodPreferenceList.get(listIndex).clear();
                for (ArrayList<Pair> pairs : pairsSplitUp.get(listIndex)) {
                    pairs.clear();
                }
                return 0;
            } else if (veggies.size() == 1) {
                group.addPairs(veggies);
                veggies.clear();
                foodPreferenceList.get(listIndex).clear();
                for (ArrayList<Pair> pairs : pairsSplitUp.get(listIndex)) {
                    pairs.clear();
                }
                return -2;
            } else {
                return -1;
            }
        } else return -100;
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


    /**
     * Sorts the pair List according to the distances to the party location.
     * @return a copy of the pair list sorted in descending order, according to the distances to the party location.
     */
    private ArrayList<Pair> sortPairListAccordingToDistances() {
        ArrayList<Pair> pairListCopy = new ArrayList<>(pairList);
        pairListCopy.sort(new PairDistanceComparator());
        return pairListCopy;
    }
}