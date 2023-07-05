package Factory.Group;

import Entity.*;
import Enum.*;

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
    private final LinkedList<Object> undoRedoList = new LinkedList<>();
    private final LinkedList<Object> undoRedoListFuture = new LinkedList<>();


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

        makeAppetizerGroups(outerRing, Course.APPETIZER);
        makeAppetizerGroups(middleRing, Course.MAIN);
        makeAppetizerGroups(innerRing, Course.DESSERT);

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
                case MEAT -> findPairsForCookingPair(cookingPair, pairsByAttributes, FoodPreference.MEAT, FoodPreference.VEGGIE, FoodPreference.VEGAN, course);
                case VEGGIE -> findPairsForCookingPair(cookingPair, pairsByAttributes, FoodPreference.VEGGIE, FoodPreference.VEGAN, FoodPreference.MEAT, course);
                case VEGAN -> findPairsForCookingPair(cookingPair, pairsByAttributes, FoodPreference.VEGAN, FoodPreference.VEGGIE, FoodPreference.MEAT, course);
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
            Group group = new Group(groupMembers);
            group.setSeen();

            switch (course) {
                case APPETIZER -> appetizerGroups.add(group);
                case MAIN -> mainDishGroups.add(group);
                case DESSERT -> dessertGroups.add(group);
            }
            //TODO: Wenn weniger Nachrücker gebildet werden sollen, dann kann man die Gruppen regroupen mit einer entsprechend anderen Reihenfolge von FoodPreferences
        }
    }

    //7.2.6

    /**
     * Clears the swapListFuture and swapList, removing all stored group swap operations.
     * This method is used to reset the undo/redo functionality for group swaps.
     */
    public void clearRedoAndUndoList() {
        undoRedoListFuture.clear();
        undoRedoList.clear();
    }

    /**
     * Swaps pairs between a group and the successor list. Replaces a pair in the group with another pair from the successor list.
     *
     * @param group              The group in which the pairs should be swapped.
     * @param pairInGroup        The pair currently in the group to be replaced.
     * @param pairInSuccessorList The pair from the successor list to be placed in the group.
     */
    public void swapGroups(Group group, Pair pairInGroup, Pair pairInSuccessorList) {
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

        group.removePair(pairInGroup);
        successorPairs.add(pairInGroup);
        successorPairs.remove(pairInSuccessorList);
        group.addPair(pairInSuccessorList);

        // Updating the target group list
        targetGroupList.remove(group);
        targetGroupList.add(group);
        //TODO update values
        undoRedoList.add(new GroupSwap(group, pairInGroup, pairInSuccessorList));
    }

    public void dissolveGroup(Group group){
        ArrayList<Group> targetGroupList = null;
        ArrayList<Pair> pairs = group.getPairs();
        Pair pair1;
        Pair pair2;
        Pair pair3;
        String course;

        if (appetizerGroups.contains(group)) {
            targetGroupList = appetizerGroups;
            course = "appetizerGroups";
        } else if (mainDishGroups.contains(group)) {
            targetGroupList = mainDishGroups;
            course = "mainDishGroups";
        } else if (dessertGroups.contains(group)) {
            targetGroupList = dessertGroups;
            course = "dessertGroups";
        } else {
            System.out.println("Group does not exist in any group list.");
            return;
        }

        successorPairs.addAll(group.getPairs());
        pair1 = pairs.get(0);
        pair2 = pairs.get(1);
        pair3 = pairs.get(2);

        targetGroupList.remove(group);
        undoRedoList.add(new GroupDissolve(group, pair1, pair2, pair3, course));
    }

    /**
     * Undoes the latest swap pair dialog operation for groups. Reverts the pairs in the group and successor list to their previous state.
     * This method removes the latest group swap operation from the swapList and adds it to the swapListFuture for potential redo.
     * After reverting the swap, the provided runnable is executed.
     *
     * @param runnable The runnable to be executed after undoing the group swap.
     */
    public void undoLatestGroupDialog(Runnable runnable) {
        if (undoRedoList.isEmpty()) {
            return;
        } else if (undoRedoList.getLast() instanceof GroupSwap) {
            GroupSwap last = (GroupSwap) undoRedoList.getLast();
            Group group = last.getGroup();
            group.removePair(last.getNewPair());
            group.addPair(last.getSwappedPair());
            successorPairs.add(last.getNewPair());
            successorPairs.remove(last.getSwappedPair());

            undoRedoList.removeLast();
            undoRedoListFuture.add(last);
            //TODO group.updateCalculations();
            runnable.run();
        } else if (undoRedoList.getLast() instanceof GroupDissolve) {
            GroupDissolve last = (GroupDissolve) undoRedoList.getLast();


        }

    }

    /**
     * Redoes the latest pair dialog operation for groups. Reapplies the swap of pairs in the group and successor list.
     * This method removes the latest group swap operation from the swapListFuture and adds it back to the swapList.
     * After applying the swap, the provided runnable is executed.
     *
     * @param runnable The runnable to be executed after redoing the group swap.
     */
    public void redoLatestGroupDialog(Runnable runnable) {
        if (undoRedoListFuture.isEmpty()) {
            return;
        } else if (undoRedoListFuture.getLast() instanceof GroupSwap){
            GroupSwap last = (GroupSwap) undoRedoListFuture.getLast();
            Group group = last.getGroup();
            group.removePair(last.getSwappedPair());
            group.addPair(last.getNewPair());
            //TODO group.updateCalculations();
            successorPairs.add(last.getSwappedPair());
            successorPairs.remove(last.getNewPair());
            undoRedoListFuture.removeLast();
            System.out.println(last);
            undoRedoList.add(last);
            runnable.run();
        }  else if (undoRedoListFuture.getLast() instanceof GroupDissolve) {
            GroupDissolve last = (GroupDissolve) undoRedoList.getLast();


        }
    }

    //7.2.6 End

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

        if (foodPreferenceFromCookingPair != FoodPreference.MEAT) {
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

        if (foodPreferenceFromCookingPair == FoodPreference.MEAT) {
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
                new PairAttributes(firstFoodPreference, Gender.FEMALE),
                new PairAttributes(firstFoodPreference, Gender.MIXED),
                new PairAttributes(firstFoodPreference, Gender.MALE),
                new PairAttributes(secondFoodPreference, Gender.FEMALE),
                new PairAttributes(secondFoodPreference, Gender.MIXED),
                new PairAttributes(secondFoodPreference, Gender.MALE)
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
            case MALE -> {
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
            case FEMALE -> {
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
            case MIXED -> {
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
        if (course == Course.APPETIZER) {
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
                    if (!pairOne.seen.contains(pairTwo) && !pairTwo.seen.contains(pairOne) && pairOne != pairTwo) {
                        return new int[]{i, j};
                    }
                }
            }
            return new int[]{};
        }
    }

    private boolean checkLists(ArrayList<Pair> listOne, ArrayList<Pair> listTwo, Course course) {
        if (course == Course.APPETIZER) {
            if (listOne.equals(listTwo)) {
                return listOne.size() >= 2;
            } else {
                return !listOne.isEmpty() && !listTwo.isEmpty();
            }
        } else {
            for (Pair pairOne : listOne) {
                for (Pair pairTwo : listTwo) {
                    if (!pairOne.seen.contains(pairTwo) && !pairTwo.seen.contains(pairOne) && pairOne != pairTwo) {
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
}