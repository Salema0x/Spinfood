package Factory.Group;

import Entity.*;
import Entity.Enum.*;

import java.sql.SQLOutput;
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
    private final ArrayList<Group> firstCourseGroupList = new ArrayList<>();
    private final ArrayList<Group> mainCourseGroupList = new ArrayList<>();
    private final ArrayList<Group> dessertCourseGroupList = new ArrayList<>();
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
        System.out.println("Outer Ring: " + outerRing.pairsOnTheRing().size());
        System.out.println("middle Ring: " + middleRing.pairsOnTheRing().size());
        System.out.println("inner Ring: " + innerRing.pairsOnTheRing().size());

        removeSuccessorPairsFromPairList(ringFactory);

        generateGroups(outerRing, Course.first);
        generateGroups(middleRing, Course.main);
        generateGroups(innerRing, Course.dessert);

        printGroup(firstCourseGroupList, Course.first);
        printGroup(mainCourseGroupList, Course.main);
        printGroup(dessertCourseGroupList, Course.dessert);

        //TODO: implement pathlength for pairs, when Group algorithm is finished
        //setPathLengthForPairs();

    }



    /**
     * generates Groups for a given Course, The pairs on the ring are the pair who are cooking the appetizer.
     *
     * @param ring a Ring Record indicating the ring with which the groups should be generated.
     * @param course    the course for which the groups should be generated.
     */
    private void generateGroups(Ring ring, Course course) {
        ArrayList<Pair> pairsOnTheRing = ring.pairsOnTheRing();
        LinkedList<Pair> possibleMatchingPairs = new LinkedList<>(pairList);
        possibleMatchingPairs.removeAll(pairsOnTheRing);

        System.out.println("PossibleMatching Pairs: " + possibleMatchingPairs.size());

        System.out.println("PairList: " + pairList.size());
        System.out.println("successorPairs: " + successorPairs.size());

        Map<PairAttributes, List<Pair>> pairsByAttributes = possibleMatchingPairs
                .stream()
                .collect(Collectors.groupingBy(PairAttributes::new));

        for (Map.Entry<?, ?> entry : pairsByAttributes.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        for (Pair cookingPair : pairsOnTheRing) {
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

            Group group = new Group(groupMembers, course, cookingPair);
            group.setSeen();

            switch (course) {
                case first -> firstCourseGroupList.add(group);
                case main -> mainCourseGroupList.add(group);
                case dessert -> dessertCourseGroupList.add(group);
            }
            //TODO: Wenn weniger Nachrücker gebildet werden sollen, dann kann man die Gruppen regroupen mit einer entsprechend anderen Reihenfolge von FoodPreferences
        }
    }

    /**
     * removes the successor pairs from the pairList and adds them to the successorPairs list.
     * @param ringFactory
     */
    public void removeSuccessorPairsFromPairList(RingFactory ringFactory) {
        System.out.println("ringFactorySuccessors: " + ringFactory.getSuccessorPairs().size());
        ringFactory.getSuccessorPairs().forEach(pair -> {
            pairList.remove(pair);
            successorPairs.add(pair);
            System.out.println("PairName" + pair.getParticipant1().getName());
        });

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
    public void swapPairs(Group group, Pair pairInGroup, Pair pairInSuccessorList) {
        ArrayList<Group> targetGroupList = null;
        if (firstCourseGroupList.contains(group)) {
            targetGroupList = firstCourseGroupList;
        } else if (mainCourseGroupList.contains(group)) {
            targetGroupList = mainCourseGroupList;
        } else if (dessertCourseGroupList.contains(group)) {
            targetGroupList = dessertCourseGroupList;
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

        if (firstCourseGroupList.contains(group)) {
            targetGroupList = firstCourseGroupList;
            course = "appetizerGroups";
        } else if (mainCourseGroupList.contains(group)) {
            targetGroupList = mainCourseGroupList;
            course = "mainDishGroups";
        } else if (dessertCourseGroupList.contains(group)) {
            targetGroupList = dessertCourseGroupList;
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

        groupMembers = findTwoPairsForFoodPreference(firstFoodPreference, firstFoodPreference, genderFromCookingPair,cookingPair, possibleMatchingPairs, course);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        if (foodPreferenceFromCookingPair != FoodPreference.meat) {
            groupMembers = findTwoPairsForFoodPreference(firstFoodPreference, secondFoodPreference, genderFromCookingPair, cookingPair, possibleMatchingPairs, course);

            if (!groupMembers.isEmpty()) {
                return groupMembers;
            }

            groupMembers = findTwoPairsForFoodPreference(firstFoodPreference, thirdFoodPreference, genderFromCookingPair,cookingPair, possibleMatchingPairs, course);

            if (!groupMembers.isEmpty()) {
                return groupMembers;
            }
        }

        groupMembers = findTwoPairsForFoodPreference(secondFoodPreference, secondFoodPreference, genderFromCookingPair, cookingPair, possibleMatchingPairs, course);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        groupMembers = findTwoPairsForFoodPreference(secondFoodPreference, thirdFoodPreference, genderFromCookingPair, cookingPair, possibleMatchingPairs, course);

        if (!groupMembers.isEmpty()) {
            return groupMembers;
        }

        if (foodPreferenceFromCookingPair == FoodPreference.meat) {
            groupMembers = findTwoPairsForFoodPreference(thirdFoodPreference, thirdFoodPreference, genderFromCookingPair, cookingPair, possibleMatchingPairs, course);
        }

        return groupMembers;
    }

    private ArrayList<Pair> findTwoPairsForFoodPreference(FoodPreference firstFoodPreference,
                                                          FoodPreference secondFoodPreference,
                                                          Gender genderFromCookingPair,
                                                          Pair cookingPair,
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

            ArrayList<Pair> pairs = selectLegalPair(cookingPair, selectedListOne, selectedListTwo);
            if(!pairs.isEmpty()) {
                groupMembers.add(pairs.get(0));
                groupMembers.add(pairs.get(1));
            }
            if(pairs.isEmpty()) {
                System.out.println(cookingPair.toString() + "no legal pair found");
            }
        }

        else {
            System.out.println(cookingPair.toString() + "selectedList Empty: " + (selectedListOne == null) + " " + (selectedListTwo == null) + "Course" + course.toString());
        }


        return groupMembers;
    }

    /**
     * selects one Pair from each List, so the group is legal
     * @param cookingPair
     * @param firstPairList
     * @param secondPairList
     * @return
     */
    private ArrayList<Pair> selectLegalPair(Pair cookingPair, List<Pair> firstPairList, List<Pair> secondPairList) {
        ArrayList<Pair> legalPairs = new ArrayList<>();
        for (Pair firstPair : firstPairList) {
            for (Pair secondPair : secondPairList) {
                if (!checkIllegalGroup(List.of(cookingPair, firstPair, secondPair))) {
                    if(!checkDoublePair(cookingPair, firstPair, secondPair)) {
                        legalPairs.add(firstPair);
                        legalPairs.add(secondPair);
                        return legalPairs;
                    }
                }
            }

        }
        return legalPairs;

    }

    private boolean checkDoublePair(Pair cookingPair, Pair firstPair, Pair secondPair) {
        return cookingPair.getId().equals(firstPair.getId()) || cookingPair.getId().equals(secondPair.getId()) || firstPair.getId().equals(secondPair.getId());
    }

    /**
     * checks if the Group that would be created is illegal concerning the food preferences (vegan and veggie group -> only one meat lover allowed)
     *
     * @param pairs
     * @return
     */
    private boolean checkIllegalGroup(List<Pair> pairs) {

        double foodPreferenceNumber = 0;
        for (Pair pair : pairs) {
            foodPreferenceNumber += pair.getFoodPreference().asNumber();
        }
        foodPreferenceNumber /= pairs.size();

        FoodPreference groupFoodPreference;

        if (foodPreferenceNumber >= 0 && foodPreferenceNumber < 1.5) {
            groupFoodPreference = FoodPreference.meat;
        } else if (foodPreferenceNumber >= 0.5 && foodPreferenceNumber < 1.5) {
            groupFoodPreference = FoodPreference.veggie;
        } else {
            groupFoodPreference = FoodPreference.vegan;
        }

        if ((groupFoodPreference == FoodPreference.vegan) || (groupFoodPreference == FoodPreference.veggie)) {
            int countMeatLovers = 0;
            for(Pair pair : pairs) {
                if(pair.getParticipant1().getFoodPreference() == FoodPreference.meat) {
                    countMeatLovers++;
                }
                if(pair.getParticipant2().getFoodPreference() == FoodPreference.meat) {
                    countMeatLovers++;
                }
            }
            return countMeatLovers > 1;
        }
        return false;
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

    private void printGroup(ArrayList<Group> list, Course course) {
        System.out.println("Course:  " + course);
        String leftAlignFormat = "| %-11s | %-11s |%-14s | %-11s | %-11s | %-36s | %-11s | %-11s |  %-36s | %-11s | %-11s | %-36s | %-14s | %-14s | %-14s | %-14s | %-14s | %-14s | %n";

        System.out.format("+-------------+-------------+----------------+----------------+---------------+--------------------------------------+---------------+---------------+--------------------------------------+---------------+---------------+--------------------------------------+----------------+-----------------+----------------+------------------+-----------------+------------------%n");
        System.out.format("| Group Nr.   | GroupPref   | BadGroup       | CookingPairN1  + CookingPairN2 | Pair1 ID                             | Pair2 N1      | Pair2 N2      | Pair2 ID                             | Pair3 N1      | Pair3 N2      | Pair3 ID                             + Pair1Pref      + PrefComb        + Pair2Pref      + PrefComb         + Pair3Pref       + PrefComb         %n");
        System.out.format("+-------------+-------------+----------------+----------------+---------------+--------------------------------------+---------------+---------------+--------------------------------------+---------------+---------------+--------------------------------------+----------------+-----------------+----------------+------------------+-----------------+------------------%n");

        int counter = 0;

        for (Group group : list) {
            FoodPreference groupPref = group.getFoodPreference();
            Pair cookingPair = group.getCookingPair();
            Pair pair1 = group.getPairs().get(0);
            Pair pair2 = group.getPairs().get(1);


            counter++;
            String cookingPairId = cookingPair.getId();
            String id1 = pair1.getId();
            String id2 = pair2.getId();

            FoodPreference cookingPairFoodPreference = cookingPair.getFoodPreference();
            FoodPreference pref1 = pair1.getFoodPreference();
            FoodPreference pref2 = pair2.getFoodPreference();

            String cookingPairParFoodPref = cookingPair.getParticipant1().getFoodPreference().toString() + " " + cookingPair.getParticipant2().getFoodPreference().toString();
            String pair1ParFoodPref = pair1.getParticipant1().getFoodPreference().toString() + " " + pair1.getParticipant2().getFoodPreference().toString();
            String pair2ParFoodPref = pair2.getParticipant1().getFoodPreference().toString() + " " + pair2.getParticipant2().getFoodPreference().toString();


            String cookingPairN1 = group.getCookingPair().getParticipant1().getName();
            String cookingPairN2 = group.getCookingPair().getParticipant2().getName();
            String pair2N1 = pair2.getParticipant1().getName();
            String pair2N2 = pair2.getParticipant2().getName();
            String pair3N1 = cookingPair.getParticipant1().getName();
            String pair3N2 = cookingPair.getParticipant2().getName();

            Boolean badGroup = false;

            if(groupPref == FoodPreference.veggie || groupPref == FoodPreference.vegan) {
                int meatLovers = 0;
                for (Pair pair : group.getPairs()) {
                    if (pair.getParticipant1().getFoodPreference() == FoodPreference.meat) {
                        meatLovers++;
                    }
                    if (pair.getParticipant2().getFoodPreference() == FoodPreference.meat) {
                        meatLovers++;
                    }
                }
                badGroup = meatLovers > 1;
            }


            System.out.format(leftAlignFormat, counter, groupPref,badGroup, cookingPairN1, cookingPairN2, cookingPairId, pair2N1, pair2N2, id1, pair3N1, pair3N2, id2, cookingPairFoodPreference, cookingPairParFoodPref, pref1,pair1ParFoodPref, pref2,pair2ParFoodPref);
        }
        System.out.format("+-------------+------------+-----------------+------------*--------------------------------------+--------------------------------------+--------------------------------------+----------------+-----------------+----------------+-----------------+----------------+-----------------+%n");
    }







    /**
     * Sets the path length for all pairs
     */
    private void setPathLengthForPairs() {
        for(Group group : firstCourseGroupList) {
            for(Pair pair : group.getPairs()) {
                pair.setPathLength(getPARTY_LOCATION());
            }
        }
    }

    //Getter

    public ArrayList<Pair> getPairList() {
        return pairList;
    }

    public ArrayList<Pair> getSuccessorPairs() {
        return successorPairs;
    }

    public ArrayList<Group> getFirstCourseGroupList() {
        return firstCourseGroupList;
    }

    public ArrayList<Group> getMainCourseGroupList() {
        return mainCourseGroupList;
    }

    public ArrayList<Group> getDessertCourseGroupList() {
        return dessertCourseGroupList;
    }

    public ArrayList<Group> getSuccessorGroups() {
        return successorGroups;
    }

    public ArrayList<Group> getGroups() {
        ArrayList<Group> result = new ArrayList<>(firstCourseGroupList);
        result.addAll(mainCourseGroupList);
        result.addAll(dessertCourseGroupList);
        return result;
    }


    public Double[] getPARTY_LOCATION() {
        return PARTY_LOCATION;
    }


}