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

        PairListFactory pairListFactory;
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


            Assertions.assertFalse(checkNoGoPair(pairList));


            Assertions.assertFalse(checkWgNoGo(pairList));
        }

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListFactoryTestlists/testliste1.csv")).toURI()));
        participantList = participantFactory.getParticipantList();
        criteria = new ArrayList<>(List.of("Essensvorlieben", "Altersdifferenz", "Geschlechterdiversität"));
        pairListFactory = new PairListFactory(new ArrayList<>(participantList), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        //4 pairs possible but 4th pair would be cooking in the same kitchen as the other 3 pairs so 4th pairs cant exist
        Assertions.assertEquals(3, pairListFactory.pairList.size());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListFactoryTestlists/testliste2.csv")).toURI()));
        participantList = participantFactory.getParticipantList();
        pairListFactory = new PairListFactory(new ArrayList<>(participantList), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        //age difference of 99, but it's the only possible pair, so it will still be generated.
        Assertions.assertEquals(1, pairListFactory.pairList.size());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListFactoryTestlists/testliste3.csv")).toURI()));
        participantList = participantFactory.getParticipantList();
        pairListFactory = new PairListFactory(new ArrayList<>(participantList), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        //no pair should be generated
        Assertions.assertTrue(pairListFactory.pairList.isEmpty());

        //checking if correct pairs are generated
        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListFactoryTestlists/testliste4.csv")).toURI()));
        participantList = participantFactory.getParticipantList();
        pairListFactory = new PairListFactory(new ArrayList<>(participantList), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        Assertions.assertEquals(3, pairListFactory.pairList.size());
        Assertions.assertTrue(pairListFactory.pairList.get(0).isEqualTo(new Pair(participantList.get(1), participantList.get(0))));
        Assertions.assertTrue(pairListFactory.pairList.get(1).isEqualTo(new Pair(participantList.get(3), participantList.get(2))));
        Assertions.assertTrue(pairListFactory.pairList.get(2).isEqualTo(new Pair(participantList.get(5), participantList.get(4))));
    }

    /**
     * testet ob die AltersKennzahl richtig berechnet wird
     */
    @org.junit.jupiter.api.Test
    void calculateAgeDifference() throws URISyntaxException {
        int index = 0;
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlists/testliste0.csv")).toURI()));
        participantList = participantFactory.getParticipantList();
        ArrayList<Object> criteria = new ArrayList<>();
        criteria.add("Essensvorlieben");
        criteria.add("Altersdifferenz");
        criteria.add("Geschlechterdiversität");

        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantList), new ArrayList<>(participantFactory.getRegisteredPairs()), criteria);
        List<Pair> generatedPairs = pairListFactory.pairList;
        pairListFactory.showPairs();

        while (!generatedPairs.isEmpty()) {
            Pair p = generatedPairs.remove(0);
            double ageDifference = p.getAgeDifference();
            System.out.println("AgeDifference should be: " + expectedAgeDifference[index] + " and is: " + ageDifference);
            Assertions.assertTrue(ageDifference == expectedAgeDifference[index]);
            index++;
        }
    }

    /**
     * testet ob die EssensKennzahl richtig berechnet wird
     */
    @org.junit.jupiter.api.Test
    void calculatePreferenceDeviation() throws URISyntaxException {
        int index = 0;
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlists/testliste0.csv")).toURI()));
        participantList = participantFactory.getParticipantList();
        ArrayList<Object> criteria = new ArrayList<>();
        criteria.add("Essensvorlieben");
        criteria.add("Altersdifferenz");
        criteria.add("Geschlechterdiversität");

        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantList), new ArrayList<>(participantFactory.getRegisteredPairs()), criteria);
        List<Pair> generatedPairs = pairListFactory.pairList;
        pairListFactory.showPairs();

        while (!generatedPairs.isEmpty()) {
            Pair p = generatedPairs.remove(0);
            double preferenceDeviation = p.getPreferenceDeviation();
            System.out.println("PreferenceDeviation should be: " + expectedPreferenceDeviation[index] + " and is: " + preferenceDeviation);
            Assertions.assertTrue(preferenceDeviation == expectedPreferenceDeviation[index]);
            index++;
        }
    }

    /**
     * testet ob die GeschlechterKennzahl richtig berechnet wird
     */
    @org.junit.jupiter.api.Test
    void calculateGenderDiversityScore() throws URISyntaxException {
        int index = 0;
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlists/testliste0.csv")).toURI()));
        participantList = participantFactory.getParticipantList();
        ArrayList<Object> criteria = new ArrayList<>();
        criteria.add("Essensvorlieben");
        criteria.add("Altersdifferenz");
        criteria.add("Geschlechterdiversität");

        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantList), new ArrayList<>(participantFactory.getRegisteredPairs()), criteria);
        List<Pair> generatedPairs = pairListFactory.pairList;
        pairListFactory.showPairs();

        while (!generatedPairs.isEmpty()) {
            Pair p = generatedPairs.remove(0);
            double genderDiversityScore = p.getGenderDiversityScore();
            System.out.println("GenderDiversityScore should be: " + expectedGenderDiversityScore[index] + " and is: " + genderDiversityScore);
            Assertions.assertTrue(genderDiversityScore == expectedGenderDiversityScore[index]);
            index++;
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
     * Checks if a pairList contains illegal pairs
     *
     * @param pairList the pairList that should be checked for  noGoPairs
     * @return a boolean indicating if the pair is a noGoPair
     */
    private boolean checkNoGoPair(List<Pair> pairList) {
        for (Pair p : pairList) {
            if (checkKitchenNoGo(p.getParticipant1().getHasKitchen(), p.getParticipant2().getHasKitchen())) {
                System.out.println("Pair:" + p.getParticipant1().getName() + " " + p.getParticipant2().getName() + " has no kitchen");
                return true;
            }
            if (checkFoodNoGo(p.getParticipant1().getFoodPreference(), p.getParticipant2().getFoodPreference())) {
                System.out.println("Pair:" + p.getParticipant1().getName() + " " + p.getParticipant2().getName() + " has illegal food preferenceCombination");
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a Participant is in multiple Pairs.
     *
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
     *
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
     *
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
     *
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