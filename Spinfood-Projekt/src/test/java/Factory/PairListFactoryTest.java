package Factory;

import Entity.Pair;
import Entity.Participant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class PairListFactoryTest {

    PairListFactory pairListFactory;
    ParticipantFactory participantFactory;

    List<Participant> participantsList; //List of participants

    List<List<Pair>> expectedPairLists; //List of expected PairLists

    List<List<Object>> criteriaOrder; //List of criteria orders



    @BeforeEach
    void setUp() {
        participantFactory = new ParticipantFactory();

    }



    @org.junit.jupiter.api.Test
    void makePairs() throws URISyntaxException {

        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlists/testliste0.csv")).toURI()));
        participantsList = participantFactory.getParticipantList();
        initializeCriteria();
        initializeExpectedPairLists();
        List<Object> criteria = new ArrayList<>();
        List<Pair> expectedPairs = new ArrayList<>();


        //testing if pairs are correctly matched in criteria Orders 1-7
        for (Criteria c : Criteria.values()) {

            switch (c) {
                case CRITERIA_567:
                    criteria = criteriaOrder.get(0);
                    expectedPairs = expectedPairLists.get(0);
                    break;
                case CRITERIA_576:
                    criteria = criteriaOrder.get(1);
                    expectedPairs = expectedPairLists.get(1);
                    break;
                case CRITERIA_657:
                    criteria = criteriaOrder.get(2);
                    expectedPairs = expectedPairLists.get(2);
                    break;
                case CRITERIA_675:
                    criteria = criteriaOrder.get(3);
                    expectedPairs = expectedPairLists.get(3);
                    break;
                case CRITERIA_756:
                    criteria = criteriaOrder.get(4);
                    expectedPairs = expectedPairLists.get(4);
                    break;
                case CRITERIA_765:
                    criteria = criteriaOrder.get(5);
                    expectedPairs = expectedPairLists.get(5);
                    break;
            }
            pairListFactory = new PairListFactory(participantFactory.getParticipantList(), participantFactory.getRegisteredPairs(), criteria);
            pairListFactory.makePairs();
            List<Pair> actualPairs = pairListFactory.pairList;


            Assertions.assertEquals(expectedPairs, actualPairs);

        }
    }

    //Enum for criteria orders
    //5 = foodPreference, 6 = Altersdifferenz, 7 = Geschlecht
    private enum Criteria {
        CRITERIA_567,       //foodPreference > Altersdifferenz > Geschlecht
        CRITERIA_576,
        CRITERIA_657,
        CRITERIA_675,
        CRITERIA_756,
        CRITERIA_765,
    }
    void initializeExpectedPairLists() {
        expectedPairLists.add(new ArrayList<>() {{add(new Pair(participantsList.get(0), participantsList.get(1))); add(new Pair(participantsList.get(2), participantsList.get(3))); add(new Pair(participantsList.get(4), participantsList.get(5)));}});
        expectedPairLists.add(new ArrayList<>() {{add(new Pair(participantsList.get(0), participantsList.get(1))); add(new Pair(participantsList.get(2), participantsList.get(3))); add(new Pair(participantsList.get(4), participantsList.get(5)));}});
        expectedPairLists.add(new ArrayList<>() {{add(new Pair(participantsList.get(0), participantsList.get(1))); add(new Pair(participantsList.get(2), participantsList.get(3))); add(new Pair(participantsList.get(4), participantsList.get(5)));}});
        expectedPairLists.add(new ArrayList<>() {{add(new Pair(participantsList.get(0), participantsList.get(1))); add(new Pair(participantsList.get(2), participantsList.get(3))); add(new Pair(participantsList.get(4), participantsList.get(5)));}});
        expectedPairLists.add(new ArrayList<>() {{add(new Pair(participantsList.get(0), participantsList.get(1))); add(new Pair(participantsList.get(2), participantsList.get(3))); add(new Pair(participantsList.get(4), participantsList.get(5)));}});
        expectedPairLists.add(new ArrayList<>() {{add(new Pair(participantsList.get(0), participantsList.get(1))); add(new Pair(participantsList.get(2), participantsList.get(3))); add(new Pair(participantsList.get(4), participantsList.get(5)));}});
    }

    void initializeCriteria() {
        criteriaOrder = new ArrayList<>();
        criteriaOrder.add(new ArrayList<>() {{add(5); add(6); add(7);}});
        criteriaOrder.add(new ArrayList<>() {{add(5); add(7); add(6);}});
        criteriaOrder.add(new ArrayList<>() {{add(6); add(5); add(7);}});
        criteriaOrder.add(new ArrayList<>() {{add(6); add(7); add(5);}});
        criteriaOrder.add(new ArrayList<>() {{add(7); add(5); add(6);}});
        criteriaOrder.add(new ArrayList<>() {{add(7); add(6); add(5);}});
    }


}