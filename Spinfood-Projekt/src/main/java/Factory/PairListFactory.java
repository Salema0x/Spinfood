package Factory;

import Data.PairList;
import Entity.Pair;
import Entity.Participant;
import Entity.PairSwap;
import Misc.ParticipantComparator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PairListFactory {
    public ArrayList<Pair> registeredPairs;
    public ArrayList<Pair> pairList = new ArrayList<>();
    private final ArrayList<Object> criteriaOrder;
    private final ArrayList<Participant> participantList;
    private final ArrayList<ArrayList<Participant>> yesKitchenParticipants = new ArrayList<>();
    private final ArrayList<ArrayList<Participant>> maybeKitchenParticipants = new ArrayList<>();
    private final ArrayList<ArrayList<Participant>> noKitchenParticipants = new ArrayList<>();
    private int sexFunctionIndex;
    private Function<Participant, Integer> firstMethod;
    private Function<Participant, Integer> secondMethod;
    private Function<Participant, Integer> thirdMethod;
    private final ArrayList<Participant> removements = new ArrayList<>();
    private final ArrayList<Participant> upperRemovements = new ArrayList<>();
    private ArrayList<Participant> successors = new ArrayList<>();
    private final PairList pairListObject;

    private LinkedList<PairSwap> swapList = new LinkedList<>();
    private LinkedList<PairSwap> swapListFuture = new LinkedList<>();

    public PairListFactory(ArrayList<Participant> participantList, ArrayList<Pair> registeredPairs, ArrayList<Object> criteriaOrder) {
        this.participantList = participantList;
        this.registeredPairs = registeredPairs;
        this.criteriaOrder = criteriaOrder;

        cleanParticipantListFromRegisteredPairs();
        cleanParticipantListFromSuccessors();

        yesKitchenParticipants.add(createList("yes", "none"));
        yesKitchenParticipants.add(createList("yes", "meat"));

        ArrayList<Participant> veggieList = createList("yes", "veggie");
        ArrayList<Participant> veganList = createList("yes", "vegan");
        ArrayList<Participant> veggieVeganList = new ArrayList<>(Stream.concat(veggieList.stream(), veganList.stream()).toList());

        yesKitchenParticipants.add(veggieVeganList);

        maybeKitchenParticipants.add(createList("maybe", "none"));
        maybeKitchenParticipants.add(createList("maybe", "meat"));

        veggieList = createList("maybe", "veggie");
        veganList = createList("maybe", "vegan");
        veggieVeganList = new ArrayList<>(Stream.concat(veggieList.stream(), veganList.stream()).toList());

        maybeKitchenParticipants.add(veggieVeganList);

        noKitchenParticipants.add(createList("no", "none"));
        noKitchenParticipants.add(createList("no", "meat"));

        veggieList = createList("no", "veggie");
        veganList = createList("no", "vegan");
        veggieVeganList = new ArrayList<>(Stream.concat(veggieList.stream(), veganList.stream()).toList());

        noKitchenParticipants.add(veggieVeganList);

        decideAlgorithm();
        makePairs();
        concatWithRegisteredPairs();
        showPairs();
        identifySuccessors();
        pairListObject = new PairList(pairList, successors);
        System.out.println(pairListObject.getCountPairs() + " " + pairListObject.getCountSuccessors() + " " + pairListObject.getPreferenceDeviation() + " " + pairListObject.getAgeDifference() + " " + pairListObject.getGenderDiversityScore());
    }


    public void undoLatestSwapPairDialog(Runnable runnable) {
        if (swapList.isEmpty()) {
            return;
        }

        PairSwap last = swapList.getLast();
        Pair pair = last.getPair();
        pair.removeParticipant(last.getNewParticipant());
        pair.addParticipant(last.getSwappedParticipant());
        successors.add(last.getNewParticipant());
        successors.remove(last.getSwappedParticipant());

        swapList.removeLast();
        swapListFuture.add(last);
        pair.updateCalculations();
        runnable.run();
    }


    public void redoLatestSwapPairDialog(Runnable runnable) {
        if (swapListFuture.isEmpty()) {
            return;
        }


        PairSwap last = swapListFuture.getLast();
        Pair pair = last.getPair();
        pair.removeParticipant(last.getSwappedParticipant());
        pair.addParticipant(last.getNewParticipant());
        pair.updateCalculations();
        successors.add(last.getSwappedParticipant());
        successors.remove(last.getNewParticipant());
        swapListFuture.removeLast();
        System.out.println(last);
        swapList.add(last);
        runnable.run();
    }

    public void clearRedoAndUndoList() {
        swapListFuture.clear();
        swapList.clear();
    }

    //DAVID TO-DONE
    public void swapParticipants(Pair pair, Participant participantInPair, Participant participantInSuccessorList) {
        // Checking if pair exists in pairList and participantInPair is in the pair
        if (!pairList.contains(pair) || !pair.containsParticipant(participantInPair)) {
            System.out.println("The pair does not exist in the pair list or the participant is not in the given pair.");
            return;
        }

        // Checking if participantInSuccessorList exists in participantSuccessorList
        if (!successors.contains(participantInSuccessorList)) {
            System.out.println("The participant you are trying to swap in is not in the successor list.");
            return;
        }

        // Swap the participants
        // Remove the participantInPair from the pair
        pair.removeParticipant(participantInPair);

        // Add participantInSuccessorList to the pair
        pair.addParticipant(participantInSuccessorList);

        // Remove the participantInSuccessorList from participantSuccessorList
        successors.remove(participantInSuccessorList);

        // Add participantInPair to participantSuccessorList
        successors.add(participantInPair);
        pair.updateCalculations();
        swapList.add(new PairSwap(pair, participantInPair, participantInSuccessorList));

    }

    /**
     * Concatenates the pair list after the pair algorithm with pairs from the registration.
     */
    private void concatWithRegisteredPairs() {
        pairList = new ArrayList<>(Stream.concat(pairList.stream(), registeredPairs.stream()).toList());
    }

    /**
     * Removes participants which are already in a pair from the participant List.
     */
    private void cleanParticipantListFromRegisteredPairs() {
        for (Pair pair : registeredPairs) {
            participantList.remove(pair.getParticipant1());
            participantList.remove(pair.getParticipant2());
        }
    }

    /**
     * Removes Participants which are already a successor from the participant List.
     */
    private void cleanParticipantListFromSuccessors() {
        successors = new ArrayList<>(participantList.stream()
                .filter(Participant::isSuccessor)
                .toList());

        for (Participant participant : successors) {
            participantList.remove(participant);
        }
    }

    /**
     * Creates a List of Participants with specific attributes.
     *
     * @param kitchenIdentification Indicates the kitchen situation of the Participants which should be in the list.
     * @param foodIdentification    Indicates the food preference of the Participants which should be in the list.
     * @return a List of participants with the specified attributes.
     */
    private ArrayList<Participant> createList(String kitchenIdentification, String foodIdentification) {
        return participantList
                .stream()
                .filter(p -> p.getHasKitchen().equals(kitchenIdentification))
                .toList()
                .stream()
                .filter(p -> p.getFoodPreference().equals(foodIdentification)).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Decides after criteria which has been set before which algorithm is chosen.
     */
    private void decideAlgorithm() {
        int indexCriteria5 = criteriaOrder.indexOf("Essensvorlieben");
        int indexCriteria6 = criteriaOrder.indexOf("Altersdifferenz");
        int indexCriteria7 = criteriaOrder.indexOf("Geschlechterdiversität");

        Function<Participant, Integer> getFoodPreferenceNumber = Participant::getFoodPreferenceNumber;
        Function<Participant, Integer> getAgeRange = Participant::getAgeRange;
        Function<Participant, Integer> getSex = Participant::getSexNumber;

        if (indexCriteria5 < indexCriteria6 && indexCriteria5 < indexCriteria7) {
            assignFields(indexCriteria6, indexCriteria7, getFoodPreferenceNumber, getAgeRange, getSex);
        } else if (indexCriteria6 < indexCriteria5 && indexCriteria6 < indexCriteria7) {
            assignFields(indexCriteria5, indexCriteria7, getAgeRange, getFoodPreferenceNumber, getSex);
        } else {
            if (indexCriteria5 < indexCriteria6) {
                this.sexFunctionIndex = 0;
                firstMethod = getSex;
                secondMethod = getFoodPreferenceNumber;
                thirdMethod = getAgeRange;
                sorterStarter(0, getSex, getFoodPreferenceNumber, getAgeRange);
            } else {
                firstMethod = getSex;
                secondMethod = getFoodPreferenceNumber;
                thirdMethod = getAgeRange;
                this.sexFunctionIndex = 0;
                sorterStarter(0, getSex, getAgeRange, getFoodPreferenceNumber);
            }
        }
    }

    /**
     * Assigns the fields, and starts the sorters.
     *
     * @param indexCriteria6          the index of criteria6 in the criteriaOrder list
     * @param indexCriteria7          the index of criteria7 in the criteriaOrder list
     * @param getFoodPreferenceNumber the first method with which the sorter gets started
     * @param getAgeRange             the second method with which the sorter gets started
     * @param getSex                  the third method with which the sorter gets started
     */
    private void assignFields(int indexCriteria6, int indexCriteria7, Function<Participant, Integer> getFoodPreferenceNumber, Function<Participant, Integer> getAgeRange, Function<Participant, Integer> getSex) {
        if (indexCriteria6 < indexCriteria7) {
            sexFunctionIndex = 2;
            firstMethod = getFoodPreferenceNumber;
            secondMethod = getAgeRange;
            thirdMethod = getSex;
            sorterStarter(2, getFoodPreferenceNumber, getAgeRange, getSex);
        } else {
            sexFunctionIndex = 1;
            firstMethod = getFoodPreferenceNumber;
            secondMethod = getSex;
            thirdMethod = getAgeRange;
            sorterStarter(1, getFoodPreferenceNumber, getSex, getAgeRange);

        }
    }

    /**
     * Starts the sorter Method. With the three main lists.
     *
     * @param sexFunctionIndex the importance of the get Sex method.
     * @param methods          A Function Interface showing which functions should be used for comparing.
     */
    @SafeVarargs
    private void sorterStarter(int sexFunctionIndex, Function<Participant, Integer>... methods) {
        sorter(yesKitchenParticipants, sexFunctionIndex, methods[0], methods[1], methods[2]);
        sorter(noKitchenParticipants, sexFunctionIndex, methods[0], methods[1], methods[2]);
        sorter(maybeKitchenParticipants, sexFunctionIndex, methods[0], methods[1], methods[2]);
    }

    /**
     * Sorts the Lists contained in the given list with the given functions.
     *
     * @param kitchenParticipants the list of lists which should get sorted.
     * @param sexFunctionIndex    the importance of the get Sex method.
     * @param methods             The functions which should be used for sorting.
     */
    @SafeVarargs
    private void sorter(ArrayList<ArrayList<Participant>> kitchenParticipants, int sexFunctionIndex, Function<Participant, Integer>... methods) {
        Function<Participant, Integer> firstMethod = methods[0];
        Function<Participant, Integer> secondMethod = methods[1];
        Function<Participant, Integer> thirdMethod = methods[2];

        for (ArrayList<Participant> participants : kitchenParticipants) {
            int index = kitchenParticipants.indexOf(participants);
            participants.sort(new ParticipantComparator(true, sexFunctionIndex, firstMethod, secondMethod, thirdMethod));
            kitchenParticipants.set(index, participants);
        }
    }

    /**
     * Starts making pairs by calling methods for making meatPairs, making veggie/vegan Pairs, and for the nonePairs
     */
    void makePairs() {
        makePairsMeat();
        makePairsOther();
        makePairsStarter(yesKitchenParticipants.get(0), maybeKitchenParticipants.get(0), noKitchenParticipants.get(0));

        for (Participant participant : upperRemovements) {
            yesKitchenParticipants.get(0).remove(participant);
            maybeKitchenParticipants.get(0).remove(participant);
            noKitchenParticipants.get(0).remove(participant);
        }
    }

    /**
     * Starts with making Pairs for meat. First only meat eaters are getting paired, then if meat eaters are left over they are getting paired with none preference eaters.
     */
    private void makePairsMeat() {
        ArrayList<Participant> noneParticipantsYesKitchen = yesKitchenParticipants.get(0);
        ArrayList<Participant> meatParticipantsYesKitchen = yesKitchenParticipants.get(1);
        ArrayList<Participant> noneParticipantsNoKitchen = noKitchenParticipants.get(0);
        ArrayList<Participant> meatParticipantsNoKitchen = noKitchenParticipants.get(1);
        ArrayList<Participant> noneParticipantsMaybeKitchen = maybeKitchenParticipants.get(0);
        ArrayList<Participant> meatParticipantsMaybeKitchen = maybeKitchenParticipants.get(1);

        makePairsStarter(meatParticipantsYesKitchen, meatParticipantsMaybeKitchen, meatParticipantsNoKitchen);

        removeFromUpperLists(new ArrayList<>(List.of(noneParticipantsYesKitchen, meatParticipantsYesKitchen, noneParticipantsNoKitchen, meatParticipantsNoKitchen, noneParticipantsMaybeKitchen, meatParticipantsMaybeKitchen)));
    }

    /**
     * Makes pairs out of veggie and vegan participants.
     */
    private void makePairsOther() {
        ArrayList<Participant> noneParticipantsYesKitchen = yesKitchenParticipants.get(0);
        ArrayList<Participant> noneParticipantsMaybeKitchen = maybeKitchenParticipants.get(0);
        ArrayList<Participant> noneParticipantsNoKitchen = noKitchenParticipants.get(0);
        ArrayList<Participant> otherParticipantsYesKitchen = yesKitchenParticipants.get(2);
        ArrayList<Participant> otherParticipantsMaybeKitchen = maybeKitchenParticipants.get(2);
        ArrayList<Participant> otherParticipantsNoKitchen = noKitchenParticipants.get(2);

        makePairsStarter(otherParticipantsYesKitchen, otherParticipantsMaybeKitchen, otherParticipantsNoKitchen);

        removeFromUpperLists(new ArrayList<>(List.of(noneParticipantsYesKitchen, otherParticipantsYesKitchen, noneParticipantsNoKitchen, otherParticipantsNoKitchen, noneParticipantsMaybeKitchen, otherParticipantsMaybeKitchen)));
    }

    /**
     * Removes already used participants from all lists and starts pairing the remaining ones
     *
     * @param lists a list of lists containing those lists from which the participants should get removed from
     */
    private void removeFromUpperLists(ArrayList<ArrayList<Participant>> lists) {
        for (ArrayList<Participant> participants : lists) {
            for (Participant participant : upperRemovements) {
                participants.remove(participant);
            }
        }

        startPairRest(lists.get(0), lists.get(1), lists.get(2), lists.get(3), lists.get(4), lists.get(5));
    }

    /**
     * Starts with making restPairs. If one category is not fully paired it will get paired with the none eaters.
     *
     * @param noneParticipantsYesKitchen   The participants with no preference and yes kitchen.
     * @param meatParticipantsYesKitchen   The participants with meat preference and yes kitchen.
     * @param noneParticipantsNoKitchen    The participants with no preference and no kitchen.
     * @param meatParticipantsNoKitchen    The participants with meat preference and no kitchen.
     * @param noneParticipantsMaybeKitchen The participants with no preference and maybe kitchen.
     * @param meatParticipantsMaybeKitchen The participants with meat preference and maybe kitchen.
     */
    private void startPairRest(ArrayList<Participant> noneParticipantsYesKitchen, ArrayList<Participant> meatParticipantsYesKitchen, ArrayList<Participant> noneParticipantsNoKitchen, ArrayList<Participant> meatParticipantsNoKitchen, ArrayList<Participant> noneParticipantsMaybeKitchen, ArrayList<Participant> meatParticipantsMaybeKitchen) {

        ArrayList<ArrayList<Participant>> noneParticipants = new ArrayList<>(List.of(noneParticipantsYesKitchen, noneParticipantsNoKitchen, noneParticipantsMaybeKitchen));
        ArrayList<ArrayList<Participant>> meatParticipants = new ArrayList<>(List.of(meatParticipantsYesKitchen, meatParticipantsNoKitchen, meatParticipantsMaybeKitchen));

        pairRest(noneParticipants, meatParticipants);

        for (Participant participant : upperRemovements) {
            meatParticipantsYesKitchen.remove(participant);
            meatParticipantsMaybeKitchen.remove(participant);
            meatParticipantsNoKitchen.remove(participant);
            noneParticipantsNoKitchen.remove(participant);
            noneParticipantsMaybeKitchen.remove(participant);
            noneParticipantsYesKitchen.remove(participant);
        }
    }

    /**
     * Makes pairs out of the leftover participants from other categories (meat or veggie/vegan) with the no preference eaters
     *
     * @param noneParticipants List of Lists with participants with no preference
     * @param restParticipants List of Lists with the leftover participants
     */
    private void pairRest(ArrayList<ArrayList<Participant>> noneParticipants, ArrayList<ArrayList<Participant>> restParticipants) {
        ParticipantComparator comparator = new ParticipantComparator(false, sexFunctionIndex, firstMethod, secondMethod, thirdMethod);

        ArrayList<Participant> tempList = noneParticipants.get(1);
        tempList.sort(comparator);
        makePairs(restParticipants.get(0), tempList);
        noneParticipants.set(1, tempList);

        tempList = noneParticipants.get(2);
        tempList.sort(comparator);
        makePairs(restParticipants.get(0), tempList);
        noneParticipants.set(2, tempList);

        tempList = noneParticipants.get(0);
        makePairs(restParticipants.get(2), tempList);
        noneParticipants.set(0, tempList);

        tempList = noneParticipants.get(1);
        makePairs(restParticipants.get(2), tempList);
        noneParticipants.set(1, tempList);

        tempList = noneParticipants.get(0);
        makePairs(restParticipants.get(1), tempList);
        noneParticipants.set(0, tempList);

        tempList = noneParticipants.get(2);
        makePairs(restParticipants.get(1), tempList);
        noneParticipants.set(2, tempList);
    }

    /**
     * Starts the makePairs() Method which makes the actual pairs.
     *
     * @param yesKitchen   Participants which have a kitchen.
     * @param maybeKitchen Participants which maybe have a kitchen.
     * @param noKitchen    Participants which have no kitchen.
     */
    private void makePairsStarter(ArrayList<Participant> yesKitchen, ArrayList<Participant> maybeKitchen, ArrayList<Participant> noKitchen) {
        ParticipantComparator comparator = new ParticipantComparator(false, sexFunctionIndex, firstMethod, secondMethod, thirdMethod);

        ArrayList<Participant> tempList = noKitchen;
        tempList.sort(comparator);

        makePairs(yesKitchen, tempList);

        tempList = maybeKitchen;
        tempList.sort(comparator);

        makePairs(yesKitchen, tempList);

        tempList = noKitchen;
        makePairs(maybeKitchen, tempList);

        ArrayList<Participant> females = splitListForSex(maybeKitchen).get(0);
        ArrayList<Participant> males = splitListForSex(maybeKitchen).get(1);

        makePairs(females, males);

        females = splitListForSex(yesKitchen).get(0);
        males = splitListForSex(yesKitchen).get(1);

        makePairs(females, males);

    }

    /**
     * Splits a list in two lists according to the sex.
     *
     * @param participants The list of participants which should get split.
     * @return A List of Lists containing the both lists.
     */
    private ArrayList<ArrayList<Participant>> splitListForSex(ArrayList<Participant> participants) {
        ArrayList<Participant> females = new ArrayList<>(participants
                .stream()
                .filter(p -> p.getSex().equals("female"))
                .toList());

        ArrayList<Participant> males = new ArrayList<>(participants
                .stream()
                .filter(p -> p.getSex().equals("male") || p.getSex().equals("other"))
                .toList());

        return new ArrayList<>(List.of(females, males));
    }

    /**
     * Generates the actual pairs and adds them to the pairList.
     *
     * @param participantList1 The firs list of participants.
     * @param participantList2 An optional second participant List. Is optional because we can also form pairs from one single list.
     */
    private void makePairs(ArrayList<Participant> participantList1, ArrayList<Participant> participantList2) {
        while (!participantList1.isEmpty() && !participantList2.isEmpty()) {
            getParticipants(participantList1, participantList2);

            for (Participant participant : removements) {
                upperRemovements.add(participant);
                participantList1.remove(participant);
            }

            for (Participant participant : removements) {
                upperRemovements.add(participant);
                participantList2.remove(participant);
            }
        }
    }

    /**
     * Makes the list of second participants modifiable and generates the pairs and sets fields.
     *
     * @param participantList1 First participant list.
     * @param participantList2 Second participant list.
     */
    @SafeVarargs
    private void getParticipants(ArrayList<Participant> participantList1, ArrayList<Participant>... participantList2) {
        Participant participant1 = participantList1.remove(0);
        Participant participant2;

        if (participantList2.length == 1) {
            participant2 = participantList2[0].remove(0);
        } else if (participantList2.length == 0) {
            participant2 = participantList1.remove(0);
        } else {
            throw new NullPointerException();
        }

        removements.add(participant1);
        removements.add(participant2);

        participant1.setPartner(participant2);
        participant2.setPartner(participant1);
        participant1.setHasPartner(true);
        participant2.setHasPartner(true);
        pairList.add(new Pair(participant1, participant2));
    }

    /**
     * Prints the pairs onto the console
     *
     * @return
     */
    public List<Pair> showPairs() {
        String leftAlignFormat = "%-9s| %-36s | %-36s | %-20s | %-20s |%n";
        int pairNr = 0;

        System.out.format("+--------|--------------------------------------+--------------------------------------+----------------------+----------------------+%n");
        System.out.format("|Pair Nr.| ID1                                  | ID2                                  | Name1                | Name2                |%n");
        System.out.format("+--------|--------------------------------------+--------------------------------------+----------------------+----------------------+%n");

        for (Pair pair : pairList) {
            String id1 = pair.getParticipant1().getId();
            String id2 = pair.getParticipant2().getId();
            String name1 = pair.getParticipant1().getName();
            String name2 = pair.getParticipant2().getName();
            pairNr++;

            System.out.format(leftAlignFormat, pairNr, id1, id2, name1, name2);
        }

        System.out.format("+---------|--------------------------------------+--------------------------------------+----------------------+----------------------+%n");
        return null;
    }

    public List<Pair> getRegisteredPairs() {
        return registeredPairs;
    }

    /**
     * Identifies the successors after the pairs have been created.
     */
    private void identifySuccessors() {
        successors = new ArrayList<>(successors);

        for (int i = 0; i < 3; i++) {
            for (Participant participant : yesKitchenParticipants.get(i)) {
                participant.setSuccessor(true);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (Participant participant : maybeKitchenParticipants.get(i)) {
                participant.setSuccessor(true);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (Participant participant : noKitchenParticipants.get(i)) {
                participant.setSuccessor(true);
            }
        }


        successors.addAll(yesKitchenParticipants.get(0));
        successors.addAll(yesKitchenParticipants.get(1));
        successors.addAll(yesKitchenParticipants.get(2));

        successors.addAll(maybeKitchenParticipants.get(0));
        successors.addAll(maybeKitchenParticipants.get(1));
        successors.addAll(maybeKitchenParticipants.get(2));

        successors.addAll(noKitchenParticipants.get(0));
        successors.addAll(noKitchenParticipants.get(1));
        successors.addAll(noKitchenParticipants.get(2));
    }

    public PairList getPairListObject() {
        return pairListObject;
    }

    public ArrayList<Participant> getSuccessors() {
        return successors;
    }
}