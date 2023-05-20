package Factory;

import Entity.Pair;
import Entity.Participant;

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


    public PairListFactory(List<Participant> participantList, List<Pair> registeredPairs, List<Object> criteriaOrder) {
        this.registeredPairs = registeredPairs;
        this.criteriaOrder = criteriaOrder;
        this.participantList = participantList;

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
        System.out.println("Done!");
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
            if (indexCriteria6 < indexCriteria7) {
                sorterStarter(getFoodPreferenceNumber, getAgeRange);
            } else {
                sorterStarter(getFoodPreferenceNumber, getSex);
            }
        } else if (indexCriteria6 < indexCriteria5 && indexCriteria6 < indexCriteria7) {
            if (indexCriteria5 < indexCriteria7) {
                sorterStarter(getAgeRange, getFoodPreferenceNumber);
            } else {
                sorterStarter(getAgeRange, getSex);
            }
        } else {
            if (indexCriteria5 < indexCriteria6) {
                sorterStarter(getSex, getFoodPreferenceNumber);
            } else {
                sorterStarter(getSex, getAgeRange);
            }
        }
    }

    /**
     * Starts the sorter Method. With the three main lists.
     * @param methods A Function Interface showing which functions should be used for comparing.
     */
    @SafeVarargs
    private void sorterStarter(Function<Participant, Integer>... methods) {
        sorter(yesKitchenParticipants, methods[0], methods[1]);
        sorter(noKitchenParticipants, methods[0], methods[1]);
        sorter(maybeKitchenParticipants, methods[0], methods[1]);
    }

    /**
     * Sorts the Lists contained in the given list with the given functions.
     * @param kitchenParticipants the list of lists which should get sorted.
     * @param methods The functions which should be used for sorting.
     */
    @SafeVarargs
    private void sorter(List<List<Participant>> kitchenParticipants, Function<Participant, Integer>... methods) {
        Function<Participant, Integer> firstMethod = methods[0];
        Function<Participant, Integer> secondMethod = methods[1];

        for (List<Participant> participants : kitchenParticipants) {
            participants = new ArrayList<>(participants);
            participants
                    .sort((a, b) -> {
                        if (!firstMethod.apply(a).equals(firstMethod.apply(b))) {
                            return firstMethod.apply(a) - firstMethod.apply(b);
                        } else {
                            return secondMethod.apply(a) - secondMethod.apply(b);
                        }
                    });
        }
    }

    /**
     * Starts making pairs by calling methods for making meatPairs, making veggie/vegan Pairs, and for the nonePairs
     */
    private void makePairs() {
        makePairsMeat();
        makePairsOther();
        makePairsStarter(yesKitchenParticipants.get(0), maybeKitchenParticipants.get(0), noKitchenParticipants.get(0));

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

        startPairRest(
                noneParticipantsYesKitchen,
                meatParticipantsYesKitchen,
                noneParticipantsNoKitchen,
                meatParticipantsNoKitchen,
                noneParticipantsMaybeKitchen,
                meatParticipantsMaybeKitchen);
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
        makePairsStarter(meatParticipantsYesKitchen, meatParticipantsMaybeKitchen, meatParticipantsNoKitchen);

        List<List<Participant>> noneParticipants = new ArrayList<>(List.of(noneParticipantsYesKitchen, noneParticipantsNoKitchen, noneParticipantsMaybeKitchen));
        List<List<Participant>> meatParticipants = new ArrayList<>(List.of(meatParticipantsYesKitchen, meatParticipantsNoKitchen, meatParticipantsMaybeKitchen));

        pairRest(noneParticipants, meatParticipants);
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

        startPairRest(noneParticipantsYesKitchen, otherParticipantsYesKitchen, noneParticipantsNoKitchen, otherParticipantsNoKitchen, noneParticipantsMaybeKitchen, otherParticipantsMaybeKitchen);
    }

    /**
     * Makes pairs out of the leftover participants from other categories (meat or veggie/vegan) with the no preference eaters
     * @param noneParticipants List of Lists with participants with no preference
     * @param restParticipants List of Lists with the leftover participants
     */
    private void pairRest(List<List<Participant>> noneParticipants, List<List<Participant>> restParticipants) {
        makePairs(restParticipants.get(0), noneParticipants.get(1));
        makePairs(restParticipants.get(0), noneParticipants.get(2));
        makePairs(restParticipants.get(0));
        makePairs(restParticipants.get(1), noneParticipants.get(0));
        makePairs(restParticipants.get(1), noneParticipants.get(2));
        makePairs(restParticipants.get(2));
    }

    /**
     * Starts the makePairs() Method which makes the actual pairs.
     * @param yesKitchen Participants which have a kitchen.
     * @param maybeKitchen Participants which maybe have a kitchen.
     * @param noKitchen Participants which have no kitchen.
     */
    private void makePairsStarter(List<Participant> yesKitchen, List<Participant> maybeKitchen, List<Participant> noKitchen) {
        makePairs(yesKitchen, noKitchen);
        makePairs(yesKitchen, maybeKitchen);
        makePairs(yesKitchen);
        makePairs(maybeKitchen, noKitchen);
        makePairs(maybeKitchen);
    }

    /**
     * Generates the actual pairs and adds them to the pairList.
     * @param participantList1 The firs list of participants.
     * @param optionalParticipantList An optional second participant List. Is optional because we can also form pairs from one single list.
     */
    @SafeVarargs
    private void makePairs(List<Participant> participantList1, List<Participant>... optionalParticipantList) {
        List<Participant> participantList2;
        if (optionalParticipantList.length > 0) {
            participantList2 = optionalParticipantList[0];

            if (!participantList1.isEmpty() && !participantList2.isEmpty()) {
                while (!participantList1.isEmpty() && !participantList2.isEmpty()) {
                    participantList1 = new ArrayList<>(participantList1);
                    participantList2 = getParticipants(participantList1, participantList2);
                }
            }
        } else {
            if (!participantList1.isEmpty()) {
                while (participantList1.size() > 1) {
                    participantList1 = getParticipants(participantList1, participantList1);
                }
            }
        }
    }

    /**
     * Makes the list of second participants modifiable and generates the pairs and sets fields.
     * @param participantList1 First participant list.
     * @param participantList2 Second participant list.
     * @return a modifiable second participant list.
     */
    private List<Participant> getParticipants(List<Participant> participantList1, List<Participant> participantList2) {
        participantList2 = new ArrayList<>(participantList2);
        participantList1 = new ArrayList<>(participantList1);
        Participant participant1 = participantList1.remove(0);
        Participant participant2 = participantList2.remove(0);
        participant1.setPartner(participant2);
        participant2.setPartner(participant1);
        participant1.setHasPartner(true);
        participant2.setHasPartner(true);
        pairList.add(new Pair(participant1, participant2));
        return participantList2;
    }
}