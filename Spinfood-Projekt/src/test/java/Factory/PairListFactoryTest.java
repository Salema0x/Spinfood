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

        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/PairListFactoryTestlists/testliste0.csv")).toURI()));
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

            Assertions.assertEquals(5, pairListFactory.pairList.size());
            List<Pair> actualPairs = pairListFactory.pairList;
            //Fixme Algorithmus ordnet Participants mehrfach zu, test failed

            for (Pair pair : actualPairs) {
                for(Pair expectedPair : expectedPairs) {
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
        expectedPairLists.add(new ArrayList<>() {{add(new Pair(participantList.get(0), participantList.get(1))); add(new Pair(participantList.get(2), participantList.get(3))); add(new Pair(participantList.get(4), participantList.get(5)));}});
        expectedPairLists.add(new ArrayList<>() {{add(new Pair(participantList.get(0), participantList.get(1))); add(new Pair(participantList.get(2), participantList.get(3))); add(new Pair(participantList.get(4), participantList.get(5)));}});
        expectedPairLists.add(new ArrayList<>() {{add(new Pair(participantList.get(0), participantList.get(1))); add(new Pair(participantList.get(2), participantList.get(3))); add(new Pair(participantList.get(4), participantList.get(5)));}});
        expectedPairLists.add(new ArrayList<>() {{add(new Pair(participantList.get(0), participantList.get(1))); add(new Pair(participantList.get(2), participantList.get(3))); add(new Pair(participantList.get(4), participantList.get(5)));}});
        expectedPairLists.add(new ArrayList<>() {{add(new Pair(participantList.get(0), participantList.get(1))); add(new Pair(participantList.get(2), participantList.get(3))); add(new Pair(participantList.get(4), participantList.get(5)));}});
        expectedPairLists.add(new ArrayList<>() {{add(new Pair(participantList.get(0), participantList.get(1))); add(new Pair(participantList.get(2), participantList.get(3))); add(new Pair(participantList.get(4), participantList.get(5)));}});
    }

    private void initializeCriteria() {
        criteriaOrder = new ArrayList<>();
        criteriaOrder.add(new ArrayList<>() {{add("Essensvorlieben"); add("Altersdifferenz"); add("Geschlechterdiversität");}});
        criteriaOrder.add(new ArrayList<>() {{add("Essensvorlieben"); add("Geschlechterdiversität"); add("Altersdifferenz");}});
        criteriaOrder.add(new ArrayList<>() {{add("Altersdifferenz"); add("Essensvorlieben"); add("Geschlechterdiversität");}});
        criteriaOrder.add(new ArrayList<>() {{add("Altersdifferenz"); add("Geschlechterdiversität"); add("Essensvorlieben");}});
        criteriaOrder.add(new ArrayList<>() {{add("Geschlechterdiversität"); add("Essensvorlieben"); add("Altersdifferenz");}});
        criteriaOrder.add(new ArrayList<>() {{add("Geschlechterdiversität"); add("Altersdifferenz"); add("Essensvorlieben");}});
    }





}