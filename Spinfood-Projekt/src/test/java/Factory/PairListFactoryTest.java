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

    PairListFactory pairListFactory;
    ParticipantFactory participantFactory;
    List<Participant> participantList;
    List<List<Pair>> expectedPairLists;
    List<List<Object>> criteriaOrder;

    @BeforeEach
    void setUp() {
        participantFactory = new ParticipantFactory();
    }

    @org.junit.jupiter.api.Test
    void PairListFactory() throws URISyntaxException {

        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlists/testliste0.csv")).toURI()));
        participantList = participantFactory.getParticipantList();

        expectedPairLists = new ArrayList<>();
        List<Object> criteria = new ArrayList<>();
        List<Pair> expectedPairs = new ArrayList<>();

        initializeCriteria();
        initializeExpectedPairLists();

        for (Criteria c : Criteria.values()) {
            switch (c) {
                case CRITERIA_FOOD_AGE_SEX -> {
                    criteria = criteriaOrder.get(0);
                    expectedPairs = expectedPairLists.get(0);
                }
                case CRITERIA_FOOD_SEX_AGE -> {
                    criteria = criteriaOrder.get(1);
                    expectedPairs = expectedPairLists.get(1);
                }
                case CRITERIA_AGE_FOOD_SEX -> {
                    criteria = criteriaOrder.get(2);
                    expectedPairs = expectedPairLists.get(2);
                }
                case CRITERIA_AGE_SEX_FOOD -> {
                    criteria = criteriaOrder.get(3);
                    expectedPairs = expectedPairLists.get(3);
                }
                case CRITERIA_SEX_FOOD_AGE -> {
                    criteria = criteriaOrder.get(4);
                    expectedPairs = expectedPairLists.get(4);
                }
                case CRITERIA_SEX_AGE_FOOD -> {
                    criteria = criteriaOrder.get(5);
                    expectedPairs = expectedPairLists.get(5);
                }
            }

            pairListFactory = new PairListFactory(participantFactory.getParticipantList(), participantFactory.getRegisteredPairs(), criteria);

            //testing if correct amount of pairs is created
            Assertions.assertEquals(participantFactory.getParticipantList().size()/2, pairListFactory.pairList.size());
            pairListFactory.showPairs();


            //testing if bad pair is created(Bsp.: match vegan with meat)
            //Assertions.assertFalse(checkBadPair(c));

            //testing if created pairs match predicted pairs
            List<Pair> actualPairs = pairListFactory.pairList;
            for (Pair pair : actualPairs) {
                for (Pair expectedPair : expectedPairs) {
                    if (pair.getParticipant1().getId().equals(expectedPair.getParticipant1().getId()) || pair.getParticipant2().getId().equals(expectedPair.getParticipant2().getId()) || pair.getParticipant1().getId().equals(expectedPair.getParticipant2().getId()) || pair.getParticipant2().getId().equals(expectedPair.getParticipant1().getId())) {
                        Assertions.assertTrue(pair.isEqualTo(expectedPair));
                        break;
                    }
                }
            }

        }


    }

    private void initializeExpectedPairLists() {
        //TODO: Pärchen manuell so zuordnen wie der Algorithmus es tun sollte und dann hier einfügen
        expectedPairLists.add(new ArrayList<>() {{
            add(new Pair(participantList.get(0), participantList.get(1)));
            add(new Pair(participantList.get(2), participantList.get(3)));
            add(new Pair(participantList.get(4), participantList.get(5)));
            add(new Pair(participantList.get(4), participantList.get(5)));
            add(new Pair(participantList.get(4), participantList.get(5)));
        }});
        expectedPairLists.add(new ArrayList<>() {{
            add(new Pair(participantList.get(0), participantList.get(1)));
            add(new Pair(participantList.get(2), participantList.get(3)));
            add(new Pair(participantList.get(4), participantList.get(5)));
            add(new Pair(participantList.get(4), participantList.get(5)));
            add(new Pair(participantList.get(4), participantList.get(5)));
        }});
        expectedPairLists.add(new ArrayList<>() {{
            add(new Pair(participantList.get(0), participantList.get(1)));
            add(new Pair(participantList.get(2), participantList.get(3)));
            add(new Pair(participantList.get(4), participantList.get(5)));
            add(new Pair(participantList.get(4), participantList.get(5)));
            add(new Pair(participantList.get(4), participantList.get(5)));
        }});
        expectedPairLists.add(new ArrayList<>() {{
            add(new Pair(participantList.get(0), participantList.get(1)));
            add(new Pair(participantList.get(2), participantList.get(3)));
            add(new Pair(participantList.get(4), participantList.get(5)));
            add(new Pair(participantList.get(4), participantList.get(5)));
            add(new Pair(participantList.get(4), participantList.get(5)));
        }});
        expectedPairLists.add(new ArrayList<>() {{
            add(new Pair(participantList.get(0), participantList.get(1)));
            add(new Pair(participantList.get(2), participantList.get(3)));
            add(new Pair(participantList.get(4), participantList.get(5)));
            add(new Pair(participantList.get(4), participantList.get(5)));
            add(new Pair(participantList.get(4), participantList.get(5)));
        }});
        expectedPairLists.add(new ArrayList<>() {{
            add(new Pair(participantList.get(0), participantList.get(1)));
            add(new Pair(participantList.get(2), participantList.get(3)));
            add(new Pair(participantList.get(4), participantList.get(5)));
            add(new Pair(participantList.get(4), participantList.get(5)));
            add(new Pair(participantList.get(4), participantList.get(5)));
        }});
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

    private boolean checkBadPair(Criteria c) {
        for (Pair p : pairListFactory.pairList) {
            if(noKitchenAvailable(p)) {
                return true;
            }

            switch (c) {
                case CRITERIA_FOOD_AGE_SEX -> {
                    if (foodDifference(p, 3) || ageDifference(p, 2) || sexDifference(p, 1)) {
                        return true;
                    }
                    return false;
                }
                case CRITERIA_FOOD_SEX_AGE -> {
                    if (foodDifference(p, 3) || sexDifference(p, 2) || ageDifference(p, 1)) {
                        return true;
                    }
                    return false;
                }
                case CRITERIA_AGE_FOOD_SEX -> {
                    if (ageDifference(p, 3) || foodDifference(p, 2) || sexDifference(p, 1)) {
                        return true;
                    }
                    return false;
                }
                case CRITERIA_AGE_SEX_FOOD -> {
                    if (ageDifference(p, 3) || sexDifference(p, 2) || foodDifference(p, 1)) {
                        return true;
                    }
                    return false;
                }
                case CRITERIA_SEX_AGE_FOOD -> {
                    if (sexDifference(p, 3) || ageDifference(p, 2) || foodDifference(p, 1)) {
                        return true;
                    }
                    return false;
                }
                case CRITERIA_SEX_FOOD_AGE -> {
                    if (sexDifference(p, 3) || foodDifference(p, 2) || ageDifference(p, 1)) {
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }

    private boolean foodDifference(Pair p, int criteriaWeight) {
        String foodPreference1 = p.getParticipant1().getFoodPreference();
        String foodPreference2 = p.getParticipant2().getFoodPreference();

        //lowest criteria weight -> accepts every combination except: vegan&meat
        if (foodPreference1.equals("vegan") && foodPreference2.equals("meat")) {
            System.out.println("Bad Pair: " + p.getParticipant1().getName() + " and " + p.getParticipant2().getName() + "Food Preference: " + foodPreference1 + " and " + foodPreference2);
            return true;
        }

        //medium criteria weight -> accepts every combination except:  vegetarian&meat + vegan&meat
        if (criteriaWeight > 1 && foodPreference1.equals("vegetarian") && foodPreference2.equals("meat")) {
            System.out.println("Bad Pair: " + p.getParticipant1().getName() + " and " + p.getParticipant2().getName() + "Food Preference: " + foodPreference1 + " and " + foodPreference2);
            return true;
        }
        //highest criteria weight -> accepts only the following combinations:  vegan&vegan/veggie&veggie/meat&meat or any combination with none
        if (criteriaWeight > 2 && foodPreference1.equals("vegetarian") && foodPreference2.equals("vegan")) {
            System.out.println("Bad Pair: " + p.getParticipant1().getName() + " and " + p.getParticipant2().getName() + "Food Preference: " + foodPreference1 + " and " + foodPreference2);
            return true;
        }
        return false;
    }

    //TODO: check if i used the right age differences
    private boolean ageDifference(Pair p, int criteriaWeight) {
        int ageDifference = Math.abs(p.getParticipant1().getAge() - p.getParticipant2().getAge());
        switch (criteriaWeight) {

            //lowest criteria weight -> accepts ageDiff up to 8 years
            case (1):
                if (ageDifference > 8) {
                    System.out.println("Bad Pair: " + p.getParticipant1().getName() + " and " + p.getParticipant2().getName() + "Age Difference: " + ageDifference);
                    return true;
                }

            //medium criteria weight -> accepts ageDiff up to 7 years
            case (2):
                if (ageDifference > 7) {
                    System.out.println("Bad Pair: " + p.getParticipant1().getName() + " and " + p.getParticipant2().getName() + "Age Difference: " + ageDifference);
                    return true;
                }

            //highest criteria weight -> accepts ageDiff up to 5 years
            case (3):
                if (ageDifference > 5) {
                    System.out.println("Bad Pair: " + p.getParticipant1().getName() + " and " + p.getParticipant2().getName() + "Age Difference: " + ageDifference);
                    return true;
                }
        }
        return false;
    }

    private boolean sexDifference(Pair p, int criteriaWeight) {

        //lowest criteria weight -> accepts any combination of sex (female&female, male&male, female&male)
        if(criteriaWeight ==1) {
            return false;
        }
        //medium/high criteria weight -> accepts only combinations of male&female
        if ((p.getParticipant1().getSex().equals("male") && p.getParticipant2().getSex().equals("female")) || ((p.getParticipant1().getSex().equals("female") && p.getParticipant2().getSex().equals("male")))) {
            return false;
        }
        return true;
    }

    private boolean noKitchenAvailable(Pair p) {
        String p1Kitchen = p.getParticipant1().getHasKitchen();
        String p2Kitchen = p.getParticipant2().getHasKitchen();

        if(p1Kitchen.equals("no")){
            if(p2Kitchen.equals("no")){
                System.out.println("Pair has no kitchen");
                return true;
            }
        }
       return false;
    }


}