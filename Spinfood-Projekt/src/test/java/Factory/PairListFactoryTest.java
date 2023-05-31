package Factory;

import Entity.Pair;
import Entity.Participant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class PairListFactoryTest {

    private enum Criteria {
        CRITERIA_FOOD_AGE_SEX,
        CRITERIA_FOOD_SEX_AGE,
        CRITERIA_AGE_FOOD_SEX,
        CRITERIA_AGE_SEX_FOOD,
        CRITERIA_SEX_FOOD_AGE,
        CRITERIA_SEX_AGE_FOOD,
    }

    private PairListFactory pairListFactory;
    private ParticipantFactory participantFactory;
    List<Participant> participantList;
    List<List<Object>> criteriaOrder;

    @BeforeEach
    void setUp() {
        participantFactory = new ParticipantFactory(500);
    }

    @org.junit.jupiter.api.Test
    void PairListFactory() throws URISyntaxException {

        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/teilnehmerliste.csv")).toURI()));
        participantList = participantFactory.getParticipantList();
        List<Object> criteria = new ArrayList<>();
        initializeCriteria();

        for (Criteria c : Criteria.values()) {
            switch (c) {
                case CRITERIA_FOOD_AGE_SEX -> criteria = criteriaOrder.get(0);
                case CRITERIA_FOOD_SEX_AGE -> criteria = criteriaOrder.get(1);
                case CRITERIA_AGE_FOOD_SEX -> criteria = criteriaOrder.get(2);
                case CRITERIA_AGE_SEX_FOOD -> criteria = criteriaOrder.get(3);
                case CRITERIA_SEX_FOOD_AGE -> criteria = criteriaOrder.get(4);
                case CRITERIA_SEX_AGE_FOOD -> criteria = criteriaOrder.get(5);
            }

            pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
            List<Pair> pairList = pairListFactory.pairList;

            Assertions.assertFalse(checkMultiplePairs(pairList));

            for (Pair p : pairList) {
                Assertions.assertFalse(checkNoGoPair(p));
            }

            Assertions.assertFalse(checkWgNoGo(pairList));
        }
    }

    /**
     * Initializes the criteria list.
     */
    private void initializeCriteria() {
        criteriaOrder = new ArrayList<>();
        criteriaOrder.add(new ArrayList<>() {{
            add("Essensvorlieben");
            add("Altersdifferenz");
            add("Geschlechterdiversität");
        }});
        criteriaOrder.add(new ArrayList<>() {{
            add("Essensvorlieben");
            add("Geschlechterdiversität");
            add("Altersdifferenz");
        }});
        criteriaOrder.add(new ArrayList<>() {{
            add("Altersdifferenz");
            add("Essensvorlieben");
            add("Geschlechterdiversität");
        }});
        criteriaOrder.add(new ArrayList<>() {{
            add("Altersdifferenz");
            add("Geschlechterdiversität");
            add("Essensvorlieben");
        }});
        criteriaOrder.add(new ArrayList<>() {{
            add("Geschlechterdiversität");
            add("Essensvorlieben");
            add("Altersdifferenz");
        }});
        criteriaOrder.add(new ArrayList<>() {{
            add("Geschlechterdiversität");
            add("Altersdifferenz");
            add("Essensvorlieben");
        }});
    }

    /**
     * Checks if a pair is a noGoPair (no kitchen / bad food preference).
     * @param p the pair that should be checked for a noGoPair
     * @return a boolean indicating if the pair is a noGoPair
     */
    private boolean checkNoGoPair(Pair p) {
        if (checkKitchenNoGo(p.getParticipant1().getHasKitchen(), p.getParticipant2().getHasKitchen())) {
            System.out.println("Pair:" + p.getParticipant1().getName() + " " + p.getParticipant2().getName() + " has no kitchen");
            return true;
        }
        if (checkFoodNoGo(p.getParticipant1().getFoodPreference(), p.getParticipant2().getFoodPreference())) {
            System.out.println("Pair:" + p.getParticipant1().getName() + " " + p.getParticipant2().getName() + " has illegal food preferenceCombination");
            return true;
        }
        return false;
    }

    /**
     * Checks if a Participant is in multiple Pairs.
     * @param pairList list of pairs from the pair algorithm.
     * @return a boolean indication if a participant is occurring in multiple pairs.
     */
    private boolean checkMultiplePairs(List<Pair> pairList) {
        for (int i = 0; i < pairList.size(); i++) {
            Pair pair = pairList.remove(i);
            String[] pairIDs = new String[]{pair.getParticipant1().getId(), pair.getParticipant2().getId()};
            if (pairList.stream().anyMatch(p -> p.getParticipant1().getId().equals(pairIDs[0]) || p.getParticipant2().getId().equals(pairIDs[0]) || p.getParticipant1().getId().equals(pairIDs[1]) || p.getParticipant2().getId().equals(pairIDs[1]))) {
                System.out.println("Participant " + pair.getParticipant1().getName() + " is in multiple Pairs");
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a pair is a bad match in food preferences (vegan with meat/veggie with meat)
     * @param foodPreference1 the food preference of the first participant of the pair.
     * @param foodPreference2 the food preference of the second participant of the pair.
     * @return a boolean indicating if the pair has valid food preferences or not.
     */
    private boolean checkFoodNoGo(String foodPreference1, String foodPreference2) {
        if (foodPreference1.equals("vegan") || foodPreference1.equals("veggie")) {
            return foodPreference2.equals("meat");
        }
        if (foodPreference2.equals("vegan") || foodPreference2.equals("veggie")) {
            return foodPreference1.equals("meat");
        }
        return false;
    }

    /**
     * Checks if a pair is a bad match in kitchen (no kitchen with no kitchen).
     * @param kitchen1 the kitchen identification of the first participant.
     * @param kitchen2 the kitchen identification of the second participant.
     * @return a boolean indicating if both participants have no kitchen or not.
     */
    private boolean checkKitchenNoGo(String kitchen1, String kitchen2) {
        if (kitchen1.equals("no")) {
            return kitchen2.equals("no");
        }
        return false;
    }

    /**
     * Checks if the wgCount of a participant in the pairs is too high
     * @param pairs the list of pairs from the pair algorithm.
     * @return a boolean indicating if the wg count is too high.
     */
    private boolean checkWgNoGo(List<Pair> pairs) {
        for (Pair p : pairs) {
            Participant p1 = p.getParticipant1();
            Participant p2 = p.getParticipant2();
            if (p1.getCountWg() > 3 || p2.getCountWg() > 3) {
                return true;
            }
        }
        return false;
    }

}