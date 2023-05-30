package Data;

import Entity.Participant;
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


    }

    @Test
    void getGenderDiversityScore() {
    }

    @Test
    void getCountPairs() {
    }

    @Test
    void getPreferenceDeviation() {
    }

    @Test
    void getCountSuccessors() {
    }
}