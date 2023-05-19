package Factory;

import Entity.Pair;
import Entity.Participant;

import java.util.ArrayList;
import java.util.List;
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
    }

    private List<Participant> createList(String kitchenIdentification, String foodIdentification) {
        return participantList
                .stream()
                .filter(p -> p.getHasKitchen().equals(kitchenIdentification))
                .toList()
                .stream()
                .filter(p -> p.getFoodPreference().equals(foodIdentification))
                .collect(Collectors.toList());
    }

    private void decideAlgorithm() {
        int indexCriteria5 = criteriaOrder.indexOf("Kriterium 5");
        int indexCriteria6 = criteriaOrder.indexOf("Kriterium 6");
        int indexCriteria7 = criteriaOrder.indexOf("Kriterium 7");

        if (indexCriteria5 > indexCriteria6 && indexCriteria5 > indexCriteria7) {
            if (indexCriteria6 > indexCriteria7) {
                sorter567Starter();
            } else {
                sorter576Starter();
            }
        } else if (indexCriteria6 > indexCriteria5 && indexCriteria6 > indexCriteria7) {
            if (indexCriteria5 > indexCriteria7) {
                sorter657Starter();
            } else {
                sorter675Starter();
            }
        } else {
            if (indexCriteria5 > indexCriteria6) {
                sorter756Starter();
            } else {
                sorter765Starter();
            }
        }
    }

    private void sorter567Starter() {
        sorter567(yesKitchenParticipants);
        sorter567(noKitchenParticipants);
        sorter567(maybeKitchenParticipants);
    }

    private void sorter567(List<List<Participant>> kitchenParticipants) {
        for (List<Participant> participants : kitchenParticipants) {
            participants
                    .sort((a, b) -> {
                        if (a.getFoodPreferenceNumber() != b.getFoodPreferenceNumber()) {
                            return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
                        } else {
                            return a.getAgeRange() - b.getAgeRange();
                        }
                    });
        }
    }

    private void sorter576Starter() {
        sorter576(yesKitchenParticipants);
        sorter576(noKitchenParticipants);
        sorter576(maybeKitchenParticipants);
    }

    private void sorter576(List<List<Participant>> kitchenParticipants) {
        for (List<Participant> participants : kitchenParticipants) {
            participants
                    .sort((a, b) -> {
                        if (a.getFoodPreferenceNumber() != b.getFoodPreferenceNumber()) {
                            return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
                        } else {
                            return a.getSex().compareTo(b.getSex());
                        }
                    });
        }
    }

    private void sorter657Starter() {
        sorter657(yesKitchenParticipants);
        sorter657(noKitchenParticipants);
        sorter657(maybeKitchenParticipants);
    }

    private void sorter657(List<List<Participant>> kitchenParticipants) {
        for (List<Participant> participants : kitchenParticipants) {
            participants.sort((a, b) -> {
                if (a.getAgeRange() != b.getAgeRange()) {
                    return a.getAgeRange() - b.getAgeRange();
                } else {
                    return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
                }
            });
        }
    }

    private void sorter675Starter() {
        sorter675(yesKitchenParticipants);
        sorter675(noKitchenParticipants);
        sorter675(maybeKitchenParticipants);
    }

    private void sorter675(List<List<Participant>> kitchenParticipants) {
        for (List<Participant> participants : kitchenParticipants) {
            participants.sort((a, b) -> {
                if (a.getAgeRange() != b.getAgeRange()) {
                    return a.getAgeRange() - b.getAgeRange();
                } else {
                    return a.getSex().compareTo(b.getSex());
                }
            });
        }
    }

    private void sorter756Starter() {
        sorter756(yesKitchenParticipants);
        sorter756(noKitchenParticipants);
        sorter756(maybeKitchenParticipants);
    }

    private void sorter756(List<List<Participant>> kitchenParticipants) {
        for (List<Participant> participants : kitchenParticipants) {
            participants.sort((a, b) -> {
                if (!a.getSex().equals(b.getSex())) {
                    return a.getSex().compareTo(b.getSex());
                } else {
                    return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
                }
            });
        }
    }

    private void sorter765Starter() {
        sorter765(yesKitchenParticipants);
        sorter765(noKitchenParticipants);
        sorter765(maybeKitchenParticipants);
    }

    private void sorter765(List<List<Participant>> kitchenParticipants) {
        for (List<Participant> participants : kitchenParticipants) {
            participants.sort((a, b) -> {
                if (!a.getSex().equals(b.getSex())) {
                    return a.getSex().compareTo(b.getSex());
                } else {
                    return a.getAgeRange() - b.getAgeRange();
                }
            });
        }
    }

    private void makePairs() {

        makePairsMeat();
        makePairsOther();

        /*
        if (!yesKitchenParticipants.isEmpty() && !noKitchenParticipants.isEmpty()) {
            while (!yesKitchenParticipants.isEmpty() && !noKitchenParticipants.isEmpty()) {
                Participant participant1 = yesKitchenParticipants.remove(0);
                Participant participant2 = noKitchenParticipants.remove(0);
                pairList.add(new Pair(participant1, participant2));
            }
        } else if (!yesKitchenParticipants.isEmpty() && !maybeKitchenParticipants.isEmpty()) {
            while (!yesKitchenParticipants.isEmpty() && !maybeKitchenParticipants.isEmpty()) {
                Participant participant1 = yesKitchenParticipants.remove(0);
                Participant participant2 = maybeKitchenParticipants.remove(0);
                pairList.add(new Pair(participant1, participant2));
            }
        } else if (!yesKitchenParticipants.isEmpty()) {
            while (yesKitchenParticipants.size() > 1) {
                Participant participant1 = yesKitchenParticipants.remove(0);
                Participant participant2 = yesKitchenParticipants.remove(0);
                pairList.add(new Pair(participant1, participant2));
            }
        } else if (!maybeKitchenParticipants.isEmpty() && !noKitchenParticipants.isEmpty()) {
            while (!maybeKitchenParticipants.isEmpty() && !noKitchenParticipants.isEmpty()) {
                Participant participant1 = maybeKitchenParticipants.remove(0);
                Participant participant2 = noKitchenParticipants.remove(0);
                pairList.add(new Pair(participant1, participant2));
            }
        } else if (!maybeKitchenParticipants.isEmpty()) {
            while (maybeKitchenParticipants.size() > 1) {
                Participant participant1 = maybeKitchenParticipants.remove(0);
                Participant participant2 = maybeKitchenParticipants.remove(0);
                pairList.add(new Pair(participant1, participant2));
            }
        } */

    }

    private void makePairsMeat() {
        List<Participant> noneParticipantsYesKitchen = yesKitchenParticipants.get(0);
        List<Participant> meatParticipantsYesKitchen = yesKitchenParticipants.get(1);
        List<Participant> noneParticipantsNoKitchen = noKitchenParticipants.get(0);
        List<Participant> meatParticipantsNoKitchen = noKitchenParticipants.get(1);
        List<Participant> noneParticipantsMaybeKitchen = maybeKitchenParticipants.get(0);
        List<Participant> meatParticipantsMaybeKitchen = maybeKitchenParticipants.get(1);

        makePairsStarter(meatParticipantsYesKitchen, meatParticipantsMaybeKitchen, meatParticipantsNoKitchen);

        /*
            List<List<Participant>> noneParticipants = new ArrayList<>(List.of(noneParticipantsYesKitchen, noneParticipantsNoKitchen, noneParticipantsMaybeKitchen));
            List<List<Participant>> meatParticipants = new ArrayList<>(List.of(meatParticipantsYesKitchen, meatParticipantsNoKitchen, meatParticipantsMaybeKitchen));

            identifySuccessors();
         */
    }

    private void makePairsOther() {
        List<Participant> noneParticipantsYesKitchen = yesKitchenParticipants.get(0);
        List<Participant> noneParticipantsMaybeKitchen = maybeKitchenParticipants.get(0);
        List<Participant> noneParticipantsNoKitchen = noKitchenParticipants.get(0);
        List<Participant> otherParticipantsYesKitchen = yesKitchenParticipants.get(2);
        List<Participant> otherParticipantsMaybeKitchen = maybeKitchenParticipants.get(2);
        List<Participant> otherParticipantsNoKitchen = noKitchenParticipants.get(2);

        makePairsStarter(otherParticipantsYesKitchen, otherParticipantsMaybeKitchen, otherParticipantsNoKitchen);

        List<List<Participant>> noneParticipants = new ArrayList<>(List.of(noneParticipantsYesKitchen, noneParticipantsNoKitchen, noneParticipantsMaybeKitchen));
        List<List<Participant>> otherParticipants = new ArrayList<>(List.of(otherParticipantsYesKitchen, otherParticipantsNoKitchen, otherParticipantsMaybeKitchen));

        identifySuccessors(noneParticipants, otherParticipants);
    }

    private void makePairsStarter(List<Participant> yesKitchen, List<Participant> maybeKitchen, List<Participant> noKitchen) {
        makePairs(yesKitchen, noKitchen);
        makePairs(yesKitchen, maybeKitchen);
        makePairs(yesKitchen);
        makePairs(maybeKitchen, noKitchen);
        makePairs(maybeKitchen);
    }

    @SafeVarargs
    private void makePairs(List<Participant> participantList1, List<Participant>... optionalParticipantList) {
        List<Participant> participantList2;
        if (optionalParticipantList.length > 0) {
            participantList2 = optionalParticipantList[0];

            if (!participantList1.isEmpty() && !participantList2.isEmpty()) {
                while (!participantList1.isEmpty() && !participantList2.isEmpty()) {
                    Participant participant1 = participantList1.remove(0);
                    Participant participant2 = participantList2.remove(0);
                    pairList.add(new Pair(participant1, participant2));
                }
            }
        } else {
            if (!participantList1.isEmpty()) {
                while (participantList1.size() > 1) {
                    Participant participant1 = participantList1.remove(0);
                    Participant participant2 = participantList1.remove(0);
                    pairList.add(new Pair(participant1, participant2));
                }
            }
        }
    }

    private void identifySuccessors(List<List<Participant>> noneParticipants, List<List<Participant>> restParticipants) {


    }

}
