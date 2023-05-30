package Data;

import Factory.PairListFactory;
import Factory.ParticipantFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PairListTest {

    private List<Object> criteria;
    private PairList pairList;

    @BeforeEach
    public void setUp() {
         criteria = new ArrayList<>(List.of("Essensvorlieben", "Altersdifferenz", "Geschlechterdiversität"));
    }

    @Test
    void getAgeDifference() throws URISyntaxException {
        ParticipantFactory participantFactory = new ParticipantFactory(10);

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste0.csv")).toURI()));
        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(0, pairList.getAgeDifference());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste1.csv")).toURI()));
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(1, pairList.getAgeDifference());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste2.csv")).toURI()));
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(0, pairList.getAgeDifference());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste3.csv")).toURI()));
        participantFactory.showCSV();
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals((double) 10 / 3, pairList.getAgeDifference());
    }

    @Test
    void getGenderDiversityScore() throws URISyntaxException {
        ParticipantFactory participantFactory = new ParticipantFactory(10);
        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste0.csv")).toURI()));
        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(0, pairList.getGenderDiversityScore());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste1.csv")).toURI()));
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(0.5, pairList.getGenderDiversityScore());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste2.csv")).toURI()));
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(0, pairList.getGenderDiversityScore());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste3.csv")).toURI()));
        participantFactory.showCSV();
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals((double) 1 / 3, pairList.getGenderDiversityScore());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste4.csv")).toURI()));
        participantFactory.showCSV();
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(1, pairList.getGenderDiversityScore());
    }

    @Test
    void getCountPairs() throws URISyntaxException {
        ParticipantFactory participantFactory = new ParticipantFactory(10);
        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste0.csv")).toURI()));
        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(1, pairList.getCountPairs());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste3.csv")).toURI()));
        participantFactory.showCSV();
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(3, pairList.getCountPairs());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste5.csv")).toURI()));
        participantFactory.showCSV();
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(0, pairList.getCountPairs());

    }

    @Test
    void getPreferenceDeviation() throws URISyntaxException {
        ParticipantFactory participantFactory = new ParticipantFactory(10);
        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste0.csv")).toURI()));
        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(0, pairList.getPreferenceDeviation());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste1.csv")).toURI()));
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(1, pairList.getPreferenceDeviation());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste2.csv")).toURI()));
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(0, pairList.getPreferenceDeviation());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste3.csv")).toURI()));
        participantFactory.showCSV();
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals((double) 1 / 3, pairList.getPreferenceDeviation());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste4.csv")).toURI()));
        participantFactory.showCSV();
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(0, pairList.getPreferenceDeviation());
    }

    @Test
    void getCountSuccessors() throws URISyntaxException {
        ParticipantFactory participantFactory = new ParticipantFactory(10);
        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste0.csv")).toURI()));
        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(0, pairList.getCountSuccessors());

        participantFactory.readCSV(new File(Objects.requireNonNull(ClassLoader.getSystemResource("testlists/PairListTestlists/testliste5.csv")).toURI()));
        participantFactory.showCSV();
        pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>(criteria));
        pairList = pairListFactory.getPairListObject();
        Assertions.assertEquals(2, pairList.getCountSuccessors());

    }
}