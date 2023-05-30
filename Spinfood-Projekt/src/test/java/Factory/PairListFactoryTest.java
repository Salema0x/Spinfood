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

    double[] meanFoodDeviationMax = new double[] {7, 1.5, 0.7};
    double[] meanAgeDeviationMax = new double[] {5, 3, 2.5};
    double[] meanGenderDeviationMin = new double[] {0.3, 0.35, 0.39};

    PairListFactory pairListFactory;
    ParticipantFactory participantFactory;
    List<Participant> participantList;
    List<List<Object>> criteriaOrder;

    @BeforeEach
    void setUp() {
        participantFactory = new ParticipantFactory(1000);
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

            //searches for participants who are in multiple pairs
            Assertions.assertFalse(checkMultiplePairs(pairList));

            //searches for illegal pairs
            for (Pair p : pairList) {
                Assertions.assertFalse(checkNoGoPair(p));
            }

            //checks if generated pairs fulfill deviation restrictions, given by the criteria Order
            Assertions.assertFalse(checkDeviationToHigh(pairList,c));
        }
    }

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
     * checks if a pair is a noGoPair (no kitchen / bad food preference)
     *
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
     * checks if generated Pairs fulfill deviation restrictions, given by the criteria Order
     *
     * @param pairList
     * @param criteria
     * @return
     */
    private boolean checkDeviationToHigh(List<Pair> pairList, Criteria criteria) {
        double foodDeviation = calculateMeanFoodDeviation(pairList);
        double ageDeviation = calculateAgeDeviation(pairList);
        double genderDeviation = calculateGenderDeviation(pairList);

        switch (criteria) {
            case CRITERIA_FOOD_AGE_SEX -> {
                if (foodDeviation > meanFoodDeviationMax[2]) {
                    System.out.println("CriteriaOrder " + criteria.toString() + " MeanFoodDeviation is = " + foodDeviation + "but should be lower than " + meanFoodDeviationMax[2]);
                    return true;
                }
                else if (ageDeviation > meanAgeDeviationMax[1]) {
                    System.out.println("CriteriaOrder " + criteria.toString() + " MeanAgeDeviation is = " + ageDeviation + "but should be lower than " + meanAgeDeviationMax[1]);
                    return true;
                }
            }
            case CRITERIA_FOOD_SEX_AGE -> {
                if(foodDeviation > meanFoodDeviationMax[2]){
                    System.out.println("CriteriaOrder " + criteria.toString() + " MeanFoodDeviation is = " + foodDeviation + "but should be lower than " + meanFoodDeviationMax[2]);
                    return true;
                }
                else if(genderDeviation < meanGenderDeviationMin[1]){
                    System.out.println("CriteriaOrder " + criteria.toString() + " MeanGenderDeviation is = " + genderDeviation + "but should be higher than " + meanGenderDeviationMin[1]);
                    return true;
                }
            }
            case CRITERIA_AGE_FOOD_SEX -> {
                if(ageDeviation > meanAgeDeviationMax[2]){
                    System.out.println("CriteriaOrder " + criteria.toString() + " MeanAgeDeviation is = " + ageDeviation + "but should be lower than " + meanAgeDeviationMax[2]);
                    return true;
                }
                else if(foodDeviation > meanFoodDeviationMax[1]){
                    System.out.println("CriteriaOrder " + criteria.toString() + " MeanFoodDeviation is = " + foodDeviation + "but should be lower than " + meanFoodDeviationMax[1]);
                    return true;
                }
            }
            case CRITERIA_AGE_SEX_FOOD -> {
                if(ageDeviation > meanAgeDeviationMax[2]){
                    System.out.println("CriteriaOrder " + criteria.toString() + " MeanAgeDeviation is = " + ageDeviation + "but should be lower than " + meanAgeDeviationMax[2]);
                    return true;
                }
                else if(genderDeviation < meanGenderDeviationMin[1]){
                    System.out.println("CriteriaOrder " + criteria.toString() + " MeanGenderDeviation is = " + genderDeviation + "but should be higher than " + meanGenderDeviationMin[1]);
                    return true;
                }
            }
            case CRITERIA_SEX_AGE_FOOD -> {
                if (genderDeviation < meanGenderDeviationMin[2]) {
                    System.out.println("CriteriaOrder " + criteria.toString() + " MeanGenderDeviation is = "+ genderDeviation + "but should be higher than " + meanGenderDeviationMin[2]);
                    return true;
                }
                else if (ageDeviation > meanAgeDeviationMax[1]) {
                    System.out.println("CriteriaOrder " + criteria.toString() + " MeanAgeDeviation is = " + ageDeviation + "but should be lower than " + meanAgeDeviationMax[1]);
                    return true;
                }
            }
            case CRITERIA_SEX_FOOD_AGE -> {
                if(genderDeviation < meanGenderDeviationMin[2]){
                    System.out.println("CriteriaOrder " + criteria.toString() + " MeanGenderDeviation is = " + genderDeviation + "but should be lower than " + meanGenderDeviationMin[2]);
                    return true;
                }
                else if(foodDeviation > meanFoodDeviationMax[1]){
                    System.out.println("CriteriaOrder " + criteria.toString() + " MeanFoodDeviation is = " + foodDeviation + "but should be lower than " + meanFoodDeviationMax[1]);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checks if a Participant is in multiple Pairs
     *
     * @param pairList
     * @return
     */
    private boolean checkMultiplePairs(List<Pair> pairList) {
        for (int i = 0; i < pairList.size(); i++) {
            pairList = new ArrayList<>(pairList);
            Pair pair = pairList.remove(i);
            String[] pairIDs = new String[]{pair.getParticipant1().getId(), pair.getParticipant2().getId()};
            if (pairList.stream().anyMatch(p -> p.getParticipant1().getId().equals(pairIDs[0]) || p.getParticipant2().getId().equals(pairIDs[0]))) {
                System.out.println("Participant " + pair.getParticipant1().getName() + " is in multiple Pairs");
                return true;
            }
        }
        return false;
    }

    /**
     * checks if a pair is a bad match in food preferences (vegan with meat/veggie with meat)
     *
     * @param foodPreference1
     * @param foodPreference2
     * @return
     */
    private boolean checkFoodNoGo(String foodPreference1, String foodPreference2) {
        if (foodPreference1.equals("vegan") || foodPreference1.equals("vegetarian")) {
            return foodPreference2.equals("meat");
        }
        if (foodPreference2.equals("vegan") || foodPreference2.equals("vegetarian")) {
            return foodPreference1.equals("meat");
        }
        return false;
    }

    /**
     * checks if a pair is a bad match in kitchen (no kitchen with no kitchen)
     *
     * @param kitchen1
     * @param kitchen2
     * @return
     */
    private boolean checkKitchenNoGo(String kitchen1, String kitchen2) {
        if (kitchen1.equals("no")) {
            return kitchen2.equals("no");
        }
        return false;
    }


    /**
     * calculates foodDeviation of a pairList
     *
     * @param pairList
     * @return
     */
    private double calculateMeanFoodDeviation(List<Pair> pairList) {
        double foodDifference = 0;
        for (Pair p : pairList) {
            double pairFoodDifference = Math.abs((double) p.getParticipant1().getFoodPreferenceNumber() - (double) p.getParticipant2().getFoodPreferenceNumber());
            foodDifference += pairFoodDifference;
        }
        return foodDifference / (double) pairList.size();
    }

    /**
     * calculates the ageDeviation of a pairList
     *
     * @param pairList
     * @return
     */
    private double calculateAgeDeviation(List<Pair> pairList) {
        double ageDifference = 0;
        for (Pair p : pairList) {
            double pairAgeDifference = Math.abs((double) p.getParticipant1().getAgeRange() - (double) p.getParticipant2().getAgeRange());
            ageDifference += pairAgeDifference;
        }
        return ageDifference / (double) pairList.size();
    }

    /**
     * calculates the genderDeviation of a pairList
     *
     * @param pairList
     * @return
     */
    private double calculateGenderDeviation(List<Pair> pairList) {
        double genderDeviation = 0;
        for (Pair p : pairList) {
            genderDeviation += p.getGenderDiversityScore();
        }
        return genderDeviation / (double) pairList.size();
    }
}