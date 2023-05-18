package Factory;

import Entity.Pair;
import Entity.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PairListFactory {
    public List<Pair> registeredPairs = new ArrayList<>();
    public List<Pair> pairList = new ArrayList<>();
    private final List<Participant> participantList;
    private final List<Object> criteriaOrder;

    private final List<Participant> yesKitchenParticipants;
    private final List<Participant> maybeKitchenParticipants;
    private final List<Participant> noKitchenParticipants;


    public PairListFactory(List<Participant> participantList, List<Object> criteriaOrder) {
        this.participantList = participantList;
        this.criteriaOrder = criteriaOrder;

        yesKitchenParticipants = participantList.stream()
                .filter(p -> p.getHasKitchen().equals("yes"))
                .collect(Collectors.toList());

        maybeKitchenParticipants = participantList.stream()
                .filter(p -> p.getHasKitchen().equals("maybe"))
                .collect(Collectors.toList());

        noKitchenParticipants = participantList.stream()
                .filter(p -> p.getHasKitchen().equals("no"))
                .collect(Collectors.toList());

        decideAlgorithm();
    }



    private void decideAlgorithm() {
        int indexCriteria5 = criteriaOrder.indexOf("Kriterium 5");
        int indexCriteria6 = criteriaOrder.indexOf("Kriterium 6");
        int indexCriteria7 = criteriaOrder.indexOf("Kriterium 7");

        if (indexCriteria5 > indexCriteria6 && indexCriteria5 > indexCriteria7) {
            if (indexCriteria6 > indexCriteria7) {
                sorter567();
            } else {
                sorter576();
            }
        } else if (indexCriteria6 > indexCriteria5 && indexCriteria6 > indexCriteria7) {
            if (indexCriteria5 > indexCriteria7) {
                sorter657();
            } else {
                sorter675();
            }
        } else {
            if (indexCriteria5 > indexCriteria6) {
                sorter756();
            } else {
                sorter765();
            }
        }
    }

    private void sorter567() {
        yesKitchenParticipants.sort((a, b) -> {
            if (a.getFoodPreferenceNumber() != b.getFoodPreferenceNumber()) {
                return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
            } else {
                return a.getAge() - b.getAge();
            }
        });

        noKitchenParticipants.sort((a, b) -> {
            if (a.getFoodPreferenceNumber() != b.getFoodPreferenceNumber()) {
                return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
            } else {
                return a.getAge() - b.getAge();
            }
        });

        maybeKitchenParticipants.sort((a, b) -> {
            if (a.getFoodPreferenceNumber() != b.getFoodPreferenceNumber()) {
                return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
            } else {
                return a.getAge() - b.getAge();
            }
        });
    }

    private void sorter576() {
        yesKitchenParticipants.sort((a, b) -> {
            if (a.getFoodPreferenceNumber() != b.getFoodPreferenceNumber()) {
                return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
            } else {
                return a.getSex().compareTo(b.getSex());
            }
        });

        noKitchenParticipants.sort((a, b) -> {
            if (a.getFoodPreferenceNumber() != b.getFoodPreferenceNumber()) {
                return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
            } else {
                return a.getSex().compareTo(b.getSex());
            }
        });

        maybeKitchenParticipants.sort((a, b) -> {
            if (a.getFoodPreferenceNumber() != b.getFoodPreferenceNumber()) {
                return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
            } else {
                return a.getSex().compareTo(b.getSex());
            }
        });
    }

    private void sorter657() {
        yesKitchenParticipants.sort((a, b) -> {
            if (a.getAge() != b.getAge()) {
                return a.getAge() - b.getAge();
            } else {
                return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
            }
        });

        noKitchenParticipants.sort((a, b) -> {
            if (a.getAge() != b.getAge()) {
                return a.getAge() - b.getAge();
            } else {
                return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
            }
        });

        maybeKitchenParticipants.sort((a, b) -> {
            if (a.getAge() != b.getAge()) {
                return a.getAge() - b.getAge();
            } else {
                return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
            }
        });
    }

    private void sorter675() {
        yesKitchenParticipants.sort((a, b) -> {
            if (a.getAge() != b.getAge()) {
                return a.getAge() - b.getAge();
            } else {
                return a.getSex().compareTo(b.getSex());
            }
        });

        noKitchenParticipants.sort((a, b) -> {
            if (a.getAge() != b.getAge()) {
                return a.getAge() - b.getAge();
            } else {
                return a.getSex().compareTo(b.getSex());
            }
        });

        maybeKitchenParticipants.sort((a, b) -> {
            if (a.getAge() != b.getAge()) {
                return a.getAge() - b.getAge();
            } else {
                return a.getSex().compareTo(b.getSex());
            }
        });
    }

    private void sorter756() {
        yesKitchenParticipants.sort((a, b) -> {
            if (!a.getSex().equals(b.getSex())) {
                return a.getSex().compareTo(b.getSex());
            } else {
                return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
            }
        });

        noKitchenParticipants.sort((a, b) -> {
            if (!a.getSex().equals(b.getSex())) {
                return a.getSex().compareTo(b.getSex());
            } else {
                return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
            }
        });

        maybeKitchenParticipants.sort((a, b) -> {
            if (!a.getSex().equals(b.getSex())) {
                return a.getSex().compareTo(b.getSex());
            } else {
                return a.getFoodPreferenceNumber() - b.getFoodPreferenceNumber();
            }
        });
    }

    private void sorter765() {
        yesKitchenParticipants.sort((a, b) -> {
            if (!a.getSex().equals(b.getSex())) {
                return a.getSex().compareTo(b.getSex());
            } else {
                return a.getAge() - b.getAge();
            }
        });

        maybeKitchenParticipants.sort((a, b) -> {
            if (!a.getSex().equals(b.getSex())) {
                return a.getSex().compareTo(b.getSex());
            } else {
                return a.getAge() - b.getAge();
            }
        });

        noKitchenParticipants.sort((a, b) -> {
            if (!a.getSex().equals(b.getSex())) {
                return a.getSex().compareTo(b.getSex());
            } else {
                return a.getAge() - b.getAge();
            }
        });
    }

    public void createPairs() {
        // First, pair participants who have a kitchen with those who might not have one
        for (int i = 0; i < yesKitchenParticipants.size() && !maybeKitchenParticipants.isEmpty(); i++) {
            Pair pair = new Pair(yesKitchenParticipants.get(i), maybeKitchenParticipants.remove(0));
            pairList.add(pair);
        }

        // Then, pair the remaining participants who have a kitchen with those who do not have one
        for (int i = 0; i < yesKitchenParticipants.size() && !noKitchenParticipants.isEmpty(); i++) {
            Pair pair = new Pair(yesKitchenParticipants.get(i), noKitchenParticipants.remove(0));
            pairList.add(pair);
        }

        // Pair remaining participants with kitchen among themselves
        for (int i = 0; i < yesKitchenParticipants.size() - 1; i += 2) {
            Pair pair = new Pair(yesKitchenParticipants.get(i), yesKitchenParticipants.get(i + 1));
            pairList.add(pair);
        }

        // If there is still an unpaired participant with kitchen, leave them unpaired for now
        if (yesKitchenParticipants.size() % 2 != 0) {
            // Handle unpaired participant with kitchen
        }

        // Finally, pair the remaining participants without a kitchen or with 'maybe' kitchen
        List<Participant> remainingParticipants = new ArrayList<>();
        remainingParticipants.addAll(maybeKitchenParticipants);
        remainingParticipants.addAll(noKitchenParticipants);

        for (int i = 0; i < remainingParticipants.size() - 1; i += 2) {
            Pair pair = new Pair(remainingParticipants.get(i), remainingParticipants.get(i + 1));
            pairList.add(pair);
        }

        // If we have an odd number of remaining participants, the last one cannot be paired
        if (remainingParticipants.size() % 2 != 0) {
            Participant unpairedParticipant = remainingParticipants.get(remainingParticipants.size() - 1);
            // The unpaired participant is left unpaired
        }
    }


}
