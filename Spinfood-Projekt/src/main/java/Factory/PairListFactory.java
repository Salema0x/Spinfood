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
    private final List<Participant> participantList;
    private final List<List<Participant>> yesKitchenParticipants = new ArrayList<>();
    private final List<List<Participant>> maybeKitchenParticipants = new ArrayList<>();
    private final List<List<Participant>> noKitchenParticipants = new ArrayList<>();


    public PairListFactory(List<Participant> participantList, List<Pair> registeredPairs, List<Object> criteriaOrder) {
        this.registeredPairs = registeredPairs;
        this.criteriaOrder = criteriaOrder;
        this.participantList = participantList;

        createKitchenPreferenceLists();
        decideAlgorithm();
        makePairs();
    }

    /**
     * Method to fill kitchenPreference Lists with foodPreference Lists of Participants.
     */
    private void createKitchenPreferenceLists() {
        String[] foodPreferences = {"none", "meat", "veggie", "vegan"};

        for (String foodPreference : foodPreferences) {
            yesKitchenParticipants.add(createList("yes", foodPreference));
            maybeKitchenParticipants.add(createList("maybe", foodPreference));
            noKitchenParticipants.add(createList("no", foodPreference));
        }
    }

    /**
     * Method to create a List of Participants with specific kitchen and food pref.
     * @param kitchenIdentification
     * @param foodIdentification
     * @return List of Participants
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

        identifySuccessors();
    }

    private void split() {

    }

    private void identifySuccessors() {
        if (yesKitchenParticipants.size() == 1) {
           // yesKitchenParticipants.get(0).setSuccessor(true);
        }

        if (maybeKitchenParticipants.size() == 1) {
           // maybeKitchenParticipants.get(0).setSuccessor(true);
        }

    }

}
