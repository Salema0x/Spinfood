package Factory;

import Entity.Pair;
import Entity.Participant;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class PairListFactoryTest {
    PairListFactory pairListFactory;
    ParticipantFactory participantFactory;

    List<Participant> participantList1;
    List<Participant> participantList2;
    List<Pair> pairList = new ArrayList<>() {{
        add(new Pair)
    }};
    List<File> testLists = new ArrayList<>() {{
        try {
            add(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlists/testliste0.csv")).toURI()));
            add(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlists/testliste1.csv")).toURI()));
            add(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlists/testliste2.csv")).toURI()));
            add(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlists/testliste3.csv")).toURI()));
            add(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlists/testliste4.csv")).toURI()));
            add(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlists/testliste5.csv")).toURI()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }};
    List<List<Integer>> criteriaOrder = new ArrayList<>() {{
        add(criteriaOrder1);
        add(criteriaOrder2);
        add(criteriaOrder3);
        add(criteriaOrder4);
        add(criteriaOrder5);
        add(criteriaOrder6);
    }};
    List<Integer> criteriaOrder1 = new ArrayList<>() {{
        add(5);
        add(6);
        add(7);
    }};
    List<Integer> criteriaOrder2 = new ArrayList<>() {{
        add(6);
        add(5);
        add(7);
    }};
    List<Integer> criteriaOrder3 = new ArrayList<>() {{
        add(7);
        add(6);
        add(5);
    }};
    List<Integer> criteriaOrder4 = new ArrayList<>() {{
        add(5);
        add(7);
        add(6);
    }};
    List<Integer> criteriaOrder5 = new ArrayList<>() {{
        add(6);
        add(7);
        add(5);
    }};
    List<Integer> criteriaOrder6 = new ArrayList<>() {{
        add(7);
        add(5);
        add(6);
    }};



    @BeforeEach
    void setUp() throws URISyntaxException {
        participantFactory = new ParticipantFactory();
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlist/testliste0.csv")).toURI()));
        participantList2 = participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlist/testliste1.csv")).toURI()));
    }

    @org.junit.jupiter.api.Test
    void makePairs() throws URISyntaxException {
        //testing if pairs are correctly matched in criteria Orders 1-7
        for (List<Integer> criteria : criteriaOrder) {

        }
             ) {

        }
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/pairfactorytestlists/testliste0.csv")).toURI()));
        pairListFactory.makePairs(participantFactory.getParticipantList(), participantFactory.getRegisteredPairs(), );
    }

}