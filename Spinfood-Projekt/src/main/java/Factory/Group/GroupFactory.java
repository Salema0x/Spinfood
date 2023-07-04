package Factory.Group;

import Entity.*;
import Entity.Enum.*;

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
    private LinkedList<GroupSwap> swapList = new LinkedList<>();
    private LinkedList<GroupSwap> swapListFuture = new LinkedList<>();


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

        makeAppetizerGroups(outerRing, Course.first);
        makeAppetizerGroups(middleRing, Course.main);
        makeAppetizerGroups(innerRing, Course.dessert);

        printAppetizerGroups(appetizerGroups);
        printAppetizerGroups(mainDishGroups);
        printAppetizerGroups(dessertGroups);
    }

    /**
     * Makes groups for the appetizer. The pairs on the outerRing are the pair who are cooking the appetizer.
     * @param outerRing a Ring Record indicating the ring with which the groups should be generated.
     */
    private void makeAppetizerGroups(Ring outerRing, Course course) {
        ArrayList<Pair> outerRingPairs = outerRing.pairsOnTheRing();
        LinkedList<Pair> possibleMatchingPairs = new LinkedList<>(pairList);

        possibleMatchingPairs.removeAll(outerRingPairs);

        Map<PairAttributes, List<Pair>> pairsByAttributes = possibleMatchingPairs
                .stream()
                .collect(Collectors.groupingBy(PairAttributes::new));

        for (Map.Entry<?, ?> entry : pairsByAttributes.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        for (Pair cookingPair : outerRingPairs) {
            FoodPreference foodPreferenceFromCookingPair = cookingPair.getFoodPreference();

            ArrayList<Pair> groupMembers = switch (foodPreferenceFromCookingPair) {
                case meat ->
                        findPairsForCookingPair(cookingPair, pairsByAttributes, FoodPreference.meat, FoodPreference.veggie, FoodPreference.vegan, course);
                case veggie ->
                        findPairsForCookingPair(cookingPair, pairsByAttributes, FoodPreference.veggie, FoodPreference.vegan, FoodPreference.meat, course);
                case vegan ->
                        findPairsForCookingPair(cookingPair, pairsByAttributes, FoodPreference.vegan, FoodPreference.veggie, FoodPreference.meat, course);
                default -> new ArrayList<>();
            };

            if (groupMembers.size() != 2) {
                for (Map.Entry<?, ?> entry : pairsByAttributes.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                System.out.println(cookingPair.getFoodPreference());
                System.out.println(groupMembers.size());
                continue;
            }


            for (Pair pair : groupMembers) {
                PairAttributes attributes = new PairAttributes(pair);
                pairsByAttributes.get(attributes).remove(pair);
            }

            groupMembers.add(cookingPair);
            Group group = new Group(groupMembers, course);
            group.setSeen();

            switch (course) {
                case first -> appetizerGroups.add(group);
                case main -> mainDishGroups.add(group);
                case dessert -> dessertGroups.add(group);
            }
            //TODO: Wenn weniger Nachrücker gebildet werden sollen, dann kann man die Gruppen regroupen mit einer entsprechend anderen Reihenfolge von FoodPreferences
        }
    }


    /**
     * Will start the according algorithm to find two matching pairs for a given Pair. It's decided after gender which algorithm is chosen.
     * @param cookingPair the pair for which the two matching pairs should get found.
     * @param possibleMatchingPairs all the pairs which are possible matching pairs organized in a map.
     *
     * @return a List containing the best matching pairs.
     */
    private ArrayList<Pair> findPairsForCookingPair(Pair cookingPair,
                                                    Map<PairAttributes, List<Pair>> possibleMatchingPairs,
                                                    FoodPreference firstFoodPreference,
                                                    FoodPreference secondFoodPreference,
                                                    FoodPreference thirdFoodPreference,
                                                    Course course) {
        Gender genderFromCookingPair = cookingPair.getGender();
        FoodPreference foodPreferenceFromCookingPair = cookingPair.getFoodPreference();

        if (possibleMatchingPairs.values().size() < 2) {
            return new ArrayList<>();
        }

        ArrayList<Pair> groupMembers;

        groupMembers = findTwoPairsForFoodPreference(firstFoodPreference, firstFoodPreference, genderFromCookingPair, possibleMatchingPairs, course);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        if (foodPreferenceFromCookingPair != FoodPreference.meat) {
            groupMembers = findTwoPairsForFoodPreference(firstFoodPreference, secondFoodPreference, genderFromCookingPair, possibleMatchingPairs, course);

            if (!groupMembers.isEmpty()) {
                return groupMembers;
            }

            groupMembers = findTwoPairsForFoodPreference(firstFoodPreference, thirdFoodPreference, genderFromCookingPair, possibleMatchingPairs, course);

            if (!groupMembers.isEmpty()) {
                return groupMembers;
            }
        }

        groupMembers = findTwoPairsForFoodPreference(secondFoodPreference, secondFoodPreference, genderFromCookingPair, possibleMatchingPairs, course);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(secondFoodPreference, thirdFoodPreference, genderFromCookingPair, possibleMatchingPairs, course);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        if (foodPreferenceFromCookingPair == FoodPreference.meat) {
            groupMembers = findTwoPairsForFoodPreference(thirdFoodPreference, thirdFoodPreference, genderFromCookingPair, possibleMatchingPairs, course);
        }

        return groupMembers;
    }

    private ArrayList<Pair> findTwoPairsForFoodPreference(FoodPreference firstFoodPreference,
                                                          FoodPreference secondFoodPreference,
                                                          Gender genderFromCookingPair,
                                                          Map<PairAttributes, List<Pair>> possibleMatchingPairs,
                                                          Course course) {
        ArrayList<Pair> groupMembers = new ArrayList<>();
        ArrayList<ArrayList<Pair>> pairLists = new ArrayList<>();

        PairAttributes[] attributes = {
                new PairAttributes(firstFoodPreference, Gender.female),
                new PairAttributes(firstFoodPreference, Gender.mixed),
                new PairAttributes(firstFoodPreference, Gender.male),
                new PairAttributes(secondFoodPreference, Gender.female),
                new PairAttributes(secondFoodPreference, Gender.mixed),
                new PairAttributes(secondFoodPreference, Gender.male)
        };

        for (PairAttributes attribute : attributes) {
            if (possibleMatchingPairs.get(attribute) != null) {
                pairLists.add(new ArrayList<>(possibleMatchingPairs.get(attribute)));
            } else {
                pairLists.add(new ArrayList<>());
            }
        }

        ArrayList<Pair> firstFemaleList = pairLists.get(0);
        ArrayList<Pair> firstMixedList = pairLists.get(1);
        ArrayList<Pair> firstMaleList = pairLists.get(2);

        ArrayList<Pair> secondFemaleList = pairLists.get(3);
        ArrayList<Pair> secondMixedList = pairLists.get(4);
        ArrayList<Pair> secondMaleList = pairLists.get(5);

        ArrayList<Pair> selectedListOne = null;
        ArrayList<Pair> selectedListTwo = null;

        switch (genderFromCookingPair) {
            case male -> {
                if (checkLists(firstMixedList, secondFemaleList, course)) {
                    selectedListOne = firstMixedList;
                    selectedListTwo = secondFemaleList;
                } else if (checkLists(firstFemaleList, secondFemaleList, course)) {
                    selectedListOne = firstFemaleList;
                    selectedListTwo = secondFemaleList;
                } else if (checkLists(firstMixedList, secondMixedList, course)) {
                    selectedListOne = firstMixedList;
                    selectedListTwo = secondMixedList;
                } else if (checkLists(firstFemaleList, secondMaleList, course)) {
                    selectedListOne = firstFemaleList;
                    selectedListTwo = secondMaleList;
                } else if (checkLists(firstMixedList, secondMaleList, course)) {
                    selectedListOne = firstMixedList;
                    selectedListTwo = secondMaleList;
                } else if (checkLists(firstMaleList, secondMaleList, course)) {
                    selectedListOne = firstMaleList;
                    selectedListTwo = secondMaleList;
                }
            }
            case female -> {
                if (checkLists(firstMixedList, secondMaleList, course)) {
                    selectedListOne = firstMixedList;
                    selectedListTwo = secondMaleList;
                } else if (checkLists(firstMixedList, secondMixedList, course)) {
                    selectedListOne = firstMixedList;
                    selectedListTwo = secondMixedList;
                } else if (checkLists(firstFemaleList, secondMaleList, course)) {
                    selectedListOne = firstFemaleList;
                    selectedListTwo = secondMaleList;
                } else if (checkLists(firstMaleList, secondMaleList, course)) {
                    selectedListOne = firstMaleList;
                    selectedListTwo = secondMaleList;
                } else if (checkLists(firstMixedList, secondFemaleList, course)) {
                    selectedListOne = firstMixedList;
                    selectedListTwo = secondFemaleList;
                } else if (checkLists(firstFemaleList, secondFemaleList, course)) {
                    selectedListOne = firstFemaleList;
                    selectedListTwo = secondFemaleList;
                }
            }
            case mixed -> {
                if (checkLists(firstMixedList, secondMixedList, course)) {
                    selectedListOne = firstMixedList;
                    selectedListTwo = secondMixedList;
                } else if (checkLists(firstFemaleList, secondMaleList, course)) {
                    selectedListOne = firstFemaleList;
                    selectedListTwo = secondMaleList;
                } else if (checkLists(firstMixedList, secondFemaleList, course)) {
                    selectedListOne = firstMixedList;
                    selectedListTwo = secondFemaleList;
                } else if (checkLists(firstMixedList, secondMaleList, course)) {
                    selectedListOne = firstMixedList;
                    selectedListTwo = secondMaleList;
                } else if (checkLists(firstFemaleList, secondFemaleList, course)) {
                    selectedListOne = firstFemaleList;
                    selectedListTwo = secondFemaleList;
                } else if (checkLists(firstMaleList, secondMaleList, course)) {
                    selectedListOne = firstMaleList;
                    selectedListTwo = secondMaleList;
                }
            }
        }

        if (selectedListOne != null && selectedListTwo != null) {
            int[] indices = calculateIndices(selectedListOne, selectedListTwo, course);
            if (indices.length == 0) {
                System.out.println(checkLists(selectedListOne, selectedListTwo, course));
            }
            groupMembers.add(selectedListOne.remove(indices[0]));
            groupMembers.add(selectedListTwo.remove(indices[1]));
        }

        return groupMembers;
    }

    private int[] calculateIndices(ArrayList<Pair> listOne, ArrayList<Pair> listTwo, Course course) {
        if (course == Course.first) {
            if (listOne.equals(listTwo)) {
                return new int[]{0, 1};
            } else {
                return new int[]{0, 0};
            }
        } else {
            for (int i = 0; i < listOne.size(); i++) {
                Pair pairOne = listOne.get(i);
                for (int j = 0; j < listTwo.size(); j++) {
                    Pair pairTwo = listTwo.get(j);
                    if (!pairOne.getSeen().contains(pairTwo) && !pairTwo.getSeen().contains(pairOne) && pairOne != pairTwo) {
                        return new int[]{i, j};
                    }
                }
            }
            return new int[]{};
        }
    }

    private boolean checkLists(ArrayList<Pair> listOne, ArrayList<Pair> listTwo, Course course) {
        if (course == Course.first) {
            if (listOne.equals(listTwo)) {
                return listOne.size() >= 2;
            } else {
                return !listOne.isEmpty() && !listTwo.isEmpty();
            }
        } else {
            for (Pair pairOne : listOne) {
                for (Pair pairTwo : listTwo) {
                    if (!pairOne.getSeen().contains(pairTwo) && !pairTwo.getSeen().contains(pairOne) && pairOne != pairTwo) {
                        return true;
                    }
                }
            }
            return false;
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

    /**
     * Clears the swapListFuture and swapList, removing all stored group swap operations.
     * This method is used to reset the undo/redo functionality for group swaps.
     */
    public void clearRedoAndUndoList() {
        swapListFuture.clear();
        swapList.clear();
    }

    /**
     * Swaps pairs between a group and the successor list. Replaces a pair in the group with another pair from the successor list.
     *
     * @param group              The group in which the pairs should be swapped.
     * @param pairInGroup        The pair currently in the group to be replaced.
     * @param pairInSuccessorList The pair from the successor list to be placed in the group.
     */
    public void swapPairs(Group group, Pair pairInGroup, Pair pairInSuccessorList) {
        // Checking if the group exists in one of the three group lists
        ArrayList<Group> targetGroupList = null;
        if (appetizerGroups.contains(group)) {
            targetGroupList = appetizerGroups;
        } else if (mainDishGroups.contains(group)) {
            targetGroupList = mainDishGroups;
        } else if (dessertGroups.contains(group)) {
            targetGroupList = dessertGroups;
        } else {
            System.out.println("Group does not exist in any group list.");
            return;
        }

        // Checking if the pairInGroup is in the selected group
        if (!group.containsPair(pairInGroup)) {
            System.out.println("Pair does not exist in the selected group.");
            return;
        }

        // Checking if the pairInSuccessorList is actually in the successorList
        if (!successorPairs.contains(pairInSuccessorList)) {
            System.out.println("Pair does not exist in the successor list.");
            return;
        }

        // Removing the pair from the group
        group.removePair(pairInGroup);

        // Adding the removed pair to the successorList
        successorPairs.add(pairInGroup);

        // Removing pairInSuccessor from the successorList
        successorPairs.remove(pairInSuccessorList);

        // Adding pairInSuccessor to the group
        group.addPair(pairInSuccessorList);

        // Updating the target group list
        targetGroupList.remove(group);
        targetGroupList.add(group);
        //TODO update values
        swapList.add(new GroupSwap(group, pairInGroup, pairInSuccessorList));
    }

    /**
     * Undoes the latest swap pair dialog operation for groups. Reverts the pairs in the group and successor list to their previous state.
     * This method removes the latest group swap operation from the swapList and adds it to the swapListFuture for potential redo.
     * After reverting the swap, the provided runnable is executed.
     *
     * @param runnable The runnable to be executed after undoing the group swap.
     */
    public void undoLatestSwapPairDialog(Runnable runnable) {
        if (swapList.isEmpty()) {
            return;
        }

        GroupSwap last = swapList.getLast();
        Group group = last.getGroup();
        group.removePair(last.getNewPair());
        group.addPair(last.getSwappedPair());
        successorPairs.add(last.getNewPair());
        successorPairs.remove(last.getSwappedPair());

        swapList.removeLast();
        swapListFuture.add(last);
        //TODO group.updateCalculations();
        runnable.run();
    }

    /**
     * Redoes the latest swap pair dialog operation for groups. Reapplies the swap of pairs in the group and successor list.
     * This method removes the latest group swap operation from the swapListFuture and adds it back to the swapList.
     * After applying the swap, the provided runnable is executed.
     *
     * @param runnable The runnable to be executed after redoing the group swap.
     */
    public void redoLatestSwapPairDialog(Runnable runnable) {
        if (swapListFuture.isEmpty()) {
            return;
        }

        GroupSwap last = swapListFuture.getLast();
        Group group = last.getGroup();
        group.removePair(last.getSwappedPair());
        group.addPair(last.getNewPair());
        //TODO group.updateCalculations();
        successorPairs.add(last.getSwappedPair());
        successorPairs.remove(last.getNewPair());
        swapListFuture.removeLast();
        System.out.println(last);
        swapList.add(last);
        runnable.run();
    }


    //Getter

    public ArrayList<Pair> getPairList() {
        return pairList;
    }

    public ArrayList<Pair> getSuccessorPairs() {
        return successorPairs;
    }

    public ArrayList<Group> getAppetizerGroups() {
        return appetizerGroups;
    }

    public ArrayList<Group> getMainDishGroups() {
        return mainDishGroups;
    }

    public ArrayList<Group> getDessertGroups() {
        return dessertGroups;
    }

    public ArrayList<Group> getSuccessorGroups() {
        return successorGroups;
    }

    public ArrayList<Group> getGroups() {
        ArrayList<Group> result = new ArrayList<>(appetizerGroups);
        result.addAll(mainDishGroups);
        result.addAll(dessertGroups);
        return result;
    }


    public Double[] getPARTY_LOCATION() {
        return PARTY_LOCATION;
    }



}