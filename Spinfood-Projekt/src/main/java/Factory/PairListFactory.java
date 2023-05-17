package Factory;

import Entity.Pair;
import Entity.Participant;
import GUI.Criteria;

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


    public PairListFactory() {
        ParticipantFactory participantFactory = new ParticipantFactory();
        Criteria criteria = new Criteria();

        participantList = participantFactory.getParticipantList();
        criteriaOrder = criteria.getCriteriaOrder();

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
                //Kriterium 5 > Kriterium 6 > Kriterium 7
            } else {
                //Kriterium 5 > Kriterium 7 > Kriterium 6
            }
        } else if (indexCriteria6 > indexCriteria5 && indexCriteria6 > indexCriteria7) {
            if (indexCriteria5 > indexCriteria7) {
                //Kriterium 6 > Kriterium 5 > Kriterium 7
            } else {
                //Kriterium 6 > Kriterium 7 > Kriterium 5
            }
        } else {
            if (indexCriteria5 > indexCriteria6) {
                //Kriterium 7 > Kriterium 5 > Kriterium 6
            } else {
                //Kriterium 7 > Kriterium 6 > Kriterium 5
            }
        }
    }

    /*
    List Sorters

    public void sortParticipants(List<Participant> participants) {
        participants.sort((a, b) -> {
            if (a.food_preference != b.food_preference) return a.food_preference - b.food_preference;
            return a.age - b.age;
        });
    }

    public void sortParticipants2(List<Participant> participants) {
        participants.sort((a, b) -> {
            if (a.age != b.age) return a.age - b.age;
            return a.sex.compareTo(b.sex);
        });
    }
     */


}
