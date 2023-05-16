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
    }

    private void criteria5() {

    }

}
