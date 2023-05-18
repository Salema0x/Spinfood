package Factory;

import Entity.Pair;
import Entity.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PairListFactory {
    public List<Pair> registeredPairs;
    public List<Pair> pairList = new ArrayList<>();
    private final List<Object> criteriaOrder;
    private final List<Participant> yesKitchenParticipants;
    private final List<Participant> maybeKitchenParticipants;
    private final List<Participant> noKitchenParticipants;


    public PairListFactory(List<Participant> participantList, List<Pair> registeredPairs, List<Object> criteriaOrder) {
        this.registeredPairs = registeredPairs;
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
        makePairs();
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

    private void makePairs() {

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
        }

        identifySuccessors();
    }

    private void identifySuccessors() {
        if (yesKitchenParticipants.size() == 1) {
            yesKitchenParticipants.get(0).setSuccessor(true);
        }

        if (maybeKitchenParticipants.size() == 1) {
            maybeKitchenParticipants.get(0).setSuccessor(true);
        }

    }

}
