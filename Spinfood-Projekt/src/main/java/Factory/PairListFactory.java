package Factory;

import Entity.Pair;
import Entity.Participant;
import Misc.ParticipantComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PairListFactory {
    public List<Pair> registeredPairs;
    public List<Pair> pairList = new ArrayList<>();
    private final List<Object> criteriaOrder;
    private final List<Participant> participantList;
    private final List<List<Participant>> yesKitchenParticipants = new ArrayList<>();
    private final List<List<Participant>> maybeKitchenParticipants = new ArrayList<>();
    private final List<List<Participant>> noKitchenParticipants = new ArrayList<>();
    private int sexFunctionIndex;
    private Function<Participant, Integer> firstMethod;
    private Function<Participant, Integer> secondMethod;
    private Function<Participant, Integer> thirdMethod;
    private final List<Participant> removements = new ArrayList<>();
    private final List<Participant> upperRemovements = new ArrayList<>();

    public PairListFactory(List<Participant> participantList, List<Pair> registeredPairs, List<Object> criteriaOrder) {
        this.registeredPairs = registeredPairs;
        this.criteriaOrder = criteriaOrder;
        this.participantList = participantList;

        cleanParticipantListFromRegisteredPairs();
        cleanParticipantListFromSuccessors();

        yesKitchenParticipants.add(createList("yes", "none"));
        yesKitchenParticipants.add(createList("yes", "meat"));

        List<Participant> veggieList = createList("yes", "veggie");
        List<Participant> veganList = createList("yes", "vegan");
        List<Participant> veggieVeganList = Stream.concat(veggieList.stream(), veganList.stream()).toList();

        yesKitchenParticipants.add(veggieVeganList);

        maybeKitchenParticipants.add(createList("maybe", "none"));
        maybeKitchenParticipants.add(createList("maybe", "meat"));

        veggieList = createList("maybe", "veggie");
        veganList = createList("maybe", "vegan");
        veggieVeganList = Stream.concat(veggieList.stream(), veganList.stream()).toList();

        maybeKitchenParticipants.add(veggieVeganList);

        noKitchenParticipants.add(createList("no", "none"));
        noKitchenParticipants.add(createList("no", "meat"));

        veggieList = createList("no", "veggie");
        veganList = createList("no", "vegan");
        veggieVeganList = Stream.concat(veggieList.stream(), veganList.stream()).toList();

        noKitchenParticipants.add(veggieVeganList);

        decideAlgorithm();
        makePairs();
        showPairs();
        System.out.println("Done!");
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
        List<Participant> successors = participantList.stream()
                .filter(Participant::isSuccessor)
                .toList();

        for (Participant participant : successors) {
            participantList.remove(participant);
        }
    }

    /**
     * Creates a List of Participants with specific attributes.
     * @param kitchenIdentification Indicates the kitchen situation of the Participants which should be in the list.
     * @param foodIdentification Indicates the food preference of the Participants which should be in the list.
     * @return a List of participants with the specified attributes.
     */
    private List<Participant> createList(String kitchenIdentification, String foodIdentification) {
        return participantList
                .stream()
                .filter(p -> p.getHasKitchen().equals(kitchenIdentification))
                .toList()
                .stream()
                .filter(p -> p.getFoodPreference().equals(foodIdentification))
                .collect(Collectors.toList());
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
     * @param indexCriteria6 the index of criteria6 in the criteriaOrder list
     * @param indexCriteria7 the index of criteria7 in the criteriaOrder list
     * @param getFoodPreferenceNumber the first method with which the sorter gets started
     * @param getAgeRange the second method with which the sorter gets started
     * @param getSex the third method with which the sorter gets started
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
     * @param methods A Function Interface showing which functions should be used for comparing.
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
     * @param methods The functions which should be used for sorting.
     */
    @SafeVarargs
    private void sorter(List<List<Participant>> kitchenParticipants, int sexFunctionIndex, Function<Participant, Integer>... methods) {
        Function<Participant, Integer> firstMethod = methods[0];
        Function<Participant, Integer> secondMethod = methods[1];
        Function<Participant, Integer> thirdMethod = methods[2];

        for (List<Participant> participants : kitchenParticipants) {
            int index = kitchenParticipants.indexOf(participants);
            participants = new ArrayList<>(participants);
            participants.sort(new ParticipantComparator(true, sexFunctionIndex, firstMethod, secondMethod, thirdMethod));
            kitchenParticipants.set(index, participants);
        }
    }

    /**
     * Starts making pairs by calling methods for making meatPairs, making veggie/vegan Pairs, and for the nonePairs
     */
    private void makePairs() {
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
        List<Participant> noneParticipantsYesKitchen = yesKitchenParticipants.get(0);
        List<Participant> meatParticipantsYesKitchen = yesKitchenParticipants.get(1);
        List<Participant> noneParticipantsNoKitchen = noKitchenParticipants.get(0);
        List<Participant> meatParticipantsNoKitchen = noKitchenParticipants.get(1);
        List<Participant> noneParticipantsMaybeKitchen = maybeKitchenParticipants.get(0);
        List<Participant> meatParticipantsMaybeKitchen = maybeKitchenParticipants.get(1);

        makePairsStarter(meatParticipantsYesKitchen, meatParticipantsMaybeKitchen, meatParticipantsNoKitchen);

        removeFromUpperLists(List.of(noneParticipantsYesKitchen, meatParticipantsYesKitchen, noneParticipantsNoKitchen, meatParticipantsNoKitchen, noneParticipantsMaybeKitchen, meatParticipantsMaybeKitchen));
    }

    /**
     * Makes pairs out of veggie and vegan participants.
     */
    private void makePairsOther() {
        List<Participant> noneParticipantsYesKitchen = yesKitchenParticipants.get(0);
        List<Participant> noneParticipantsMaybeKitchen = maybeKitchenParticipants.get(0);
        List<Participant> noneParticipantsNoKitchen = noKitchenParticipants.get(0);
        List<Participant> otherParticipantsYesKitchen = yesKitchenParticipants.get(2);
        List<Participant> otherParticipantsMaybeKitchen = maybeKitchenParticipants.get(2);
        List<Participant> otherParticipantsNoKitchen = noKitchenParticipants.get(2);

        makePairsStarter(otherParticipantsYesKitchen, otherParticipantsMaybeKitchen, otherParticipantsNoKitchen);

        otherParticipantsNoKitchen = new ArrayList<>(otherParticipantsNoKitchen);

        removeFromUpperLists(List.of(noneParticipantsYesKitchen, otherParticipantsYesKitchen, noneParticipantsNoKitchen, otherParticipantsNoKitchen, noneParticipantsMaybeKitchen, otherParticipantsMaybeKitchen));
    }

    /**
     * Removes already used participants from all lists and starts pairing the remaining ones
     * @param lists a list of lists containing those lists from which the participants should get removed from
     */
    private void removeFromUpperLists(List<List<Participant>> lists) {
        for (List<Participant> participants : lists) {
            for (Participant participant : upperRemovements) {
                participants.remove(participant);
            }
        }

        startPairRest(lists.get(0), lists.get(1), lists.get(2), lists.get(3), lists.get(4), lists.get(5));
    }

    /**
     * Starts with making restPairs. If one category is not fully paired it will get paired with the none eaters.
     * @param noneParticipantsYesKitchen The participants with no preference and yes kitchen.
     * @param meatParticipantsYesKitchen The participants with meat preference and yes kitchen.
     * @param noneParticipantsNoKitchen The participants with no preference and no kitchen.
     * @param meatParticipantsNoKitchen The participants with meat preference and no kitchen.
     * @param noneParticipantsMaybeKitchen The participants with no preference and maybe kitchen.
     * @param meatParticipantsMaybeKitchen The participants with meat preference and maybe kitchen.
     */
    private void startPairRest(List<Participant> noneParticipantsYesKitchen, List<Participant> meatParticipantsYesKitchen, List<Participant> noneParticipantsNoKitchen, List<Participant> meatParticipantsNoKitchen, List<Participant> noneParticipantsMaybeKitchen, List<Participant> meatParticipantsMaybeKitchen) {

        List<List<Participant>> noneParticipants = new ArrayList<>(List.of(noneParticipantsYesKitchen, noneParticipantsNoKitchen, noneParticipantsMaybeKitchen));
        List<List<Participant>> meatParticipants = new ArrayList<>(List.of(meatParticipantsYesKitchen, meatParticipantsNoKitchen, meatParticipantsMaybeKitchen));

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
     * @param noneParticipants List of Lists with participants with no preference
     * @param restParticipants List of Lists with the leftover participants
     */
    private void pairRest(List<List<Participant>> noneParticipants, List<List<Participant>> restParticipants) {
        ParticipantComparator comparator = new ParticipantComparator(false, sexFunctionIndex, firstMethod, secondMethod, thirdMethod);

        List<Participant> tempList = new ArrayList<>(noneParticipants.get(1));
        tempList.sort(comparator);

        makePairs(restParticipants.get(0), tempList);

        tempList = new ArrayList<>(noneParticipants.get(2));
        tempList.sort(comparator);

        makePairs(restParticipants.get(0), tempList);

        noneParticipants.set(2, tempList);

        tempList = new ArrayList<>(noneParticipants.get(1));
        makePairs(restParticipants.get(2), tempList);
    }

    /**
     * Starts the makePairs() Method which makes the actual pairs.
     * @param yesKitchen Participants which have a kitchen.
     * @param maybeKitchen Participants which maybe have a kitchen.
     * @param noKitchen Participants which have no kitchen.
     */
    private void makePairsStarter(List<Participant> yesKitchen, List<Participant> maybeKitchen, List<Participant> noKitchen) {
        ParticipantComparator comparator = new ParticipantComparator(false, sexFunctionIndex, firstMethod, secondMethod, thirdMethod);

        List<Participant> tempList = new ArrayList<>(noKitchen);
        tempList.sort(comparator);

        makePairs(yesKitchen, tempList);

        tempList = new ArrayList<>(maybeKitchen);
        tempList.sort(comparator);

        makePairs(yesKitchen, tempList);

        maybeKitchen = tempList;

        tempList = new ArrayList<>(noKitchen);
        makePairs(maybeKitchen, tempList);

        List<Participant> females = splitListForSex(maybeKitchen).get(0);
        List<Participant> males = splitListForSex(maybeKitchen).get(1);

        makePairs(females, males);

        females = splitListForSex(yesKitchen).get(0);
        males = splitListForSex(yesKitchen).get(1);

        makePairs(females, males);
    }

    /**
     * Splits a list in two lists according to the sex.
     * @param participants The list of participants which should get split.
     * @return A List of Lists containing the both lists.
     */
    private List<List<Participant>> splitListForSex(List<Participant> participants) {
        List<Participant> females = participants
                .stream()
                .filter(p -> p.getSex().equals("female"))
                .toList();

        List<Participant> males = participants
                .stream()
                .filter(p -> p.getSex().equals("male") || p.getSex().equals("other"))
                .toList();

        return new ArrayList<>(List.of(females, males));
    }

    /**
     * Generates the actual pairs and adds them to the pairList.
     * @param participantList1 The firs list of participants.
     * @param participantList2 An optional second participant List. Is optional because we can also form pairs from one single list.
     */
    private void makePairs(List<Participant> participantList1, List<Participant> participantList2) {
        while (!participantList1.isEmpty() && !participantList2.isEmpty()) {
            getParticipants(participantList1, participantList2);

            for (Participant participant : removements) {
                participantList1.remove(participant);
            }

            for (Participant participant : removements)  {
                upperRemovements.add(participant);
                participantList2.remove(participant);
            }
        }
    }

    /**
     * Makes the list of second participants modifiable and generates the pairs and sets fields.
     * @param participantList1 First participant list.
     * @param participantList2 Second participant list.
     */
    private void getParticipants(List<Participant> participantList1, List<Participant> participantList2) {
        participantList2 = new ArrayList<>(participantList2);
        participantList1 = new ArrayList<>(participantList1);
        Participant participant1 = participantList1.remove(0);
        Participant participant2 = participantList2.remove(0);

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
     */
    public void showPairs() {
        String leftAlignFormat = "%-9s| %-36s | %-36s | %-20s | %-20s |%n";
        int pairNr = 0;

        System.out.format("+--------|--------------------------------------+--------------------------------------+----------------------+----------------------+%n");
        System.out.format("|Pair Nr.| ID1                                  | ID2                                  | Name1                | Name2                |%n");
        System.out.format("+--------|--------------------------------------+--------------------------------------+----------------------+----------------------+%n");

        for (Pair pair: pairList ) {
            String id1 = pair.getParticipant1().getId();
            String id2 = pair.getParticipant2().getId();
            String name1 = pair.getParticipant1().getName();
            String name2 = pair.getParticipant2().getName();
            pairNr++;

            System.out.format(leftAlignFormat,pairNr, id1, id2, name1, name2);
        }

        System.out.format("+---------|--------------------------------------+--------------------------------------+----------------------+----------------------+%n");
    }

    public List<Pair> getRegisteredPairs() {
        return registeredPairs;
    }
}