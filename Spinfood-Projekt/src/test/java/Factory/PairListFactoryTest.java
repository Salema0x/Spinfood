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
    void PairListFactory() throws URISyntaxException {

        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlists/testliste0.csv")).toURI()));
        participantFactory.showCSV();
        participantsList = participantFactory.getParticipantList();
        expectedPairLists = new ArrayList<>();
        List<Object> criteria = new ArrayList<>();
        List<Pair> expectedPairs = new ArrayList<>();
        initializeCriteria();
        initializeExpectedPairLists();


        //testing if participants are correctly matched into pairs by criteria Orders 1-6
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
            pairListFactory.showPairs();

            //testing if the correct amount of pairs is created
            Assertions.assertEquals(5, pairListFactory.pairList.size());
            List<Pair> actualPairs = pairListFactory.pairList;
            //Fixme Algorithmus ordnet Participants mehrfach zu, test failed


            //testing, if the expected pairs are created
            for (Pair pair : actualPairs) {
                for(Pair expectedPair : expectedPairs) {
                    //searching for a compatible pair in expectedPairs (at least one matching participantId)
                    if (pair.getParticipant1().getId() == expectedPair.getParticipant1().getId() || pair.getParticipant2().getId() == expectedPair.getParticipant2().getId() || pair.getParticipant1().getId() == expectedPair.getParticipant2().getId() || pair.getParticipant2().getId() == expectedPair.getParticipant1().getId()){
                        //testing if the pairs are equal
                        Assertions.assertEquals(0, pair.compareTo(expectedPair));
                        break;
                    }
                }
            }

        }






    }

    //Helper methods and classes
    void initializeExpectedPairLists() {
    //TODO: Pärchen manuell so zuordnen wie der Algorithmus es tun sollte und dann hier einfügen
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




}