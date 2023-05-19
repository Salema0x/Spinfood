package Factory;

import Entity.Participant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ParticipantFactoryTest {

    ParticipantFactory participantFactory;
    @BeforeEach
    void setUp() {
        participantFactory = new ParticipantFactory();
    }

    @org.junit.jupiter.api.Test
    void readCSV() throws URISyntaxException {

        //testing if single participants are read in correctly
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/testliste0.csv")).toURI()));
        List<Participant> expectedParticipantList0 = new ArrayList<>();
        expectedParticipantList0.add(new Participant(new String[] {"0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "veggie", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"}, false));
        expectedParticipantList0.add(new Participant(new String[] {"1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "none", "26", "male", "yes", "1.0", "8.718914539788807", "50.590899839788804"}, false));
        Assertions.assertTrue(participantFactory.participantListEquals(expectedParticipantList0));

        //if empty .csv file is given the participant list is empty
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/testliste1.csv")).toURI()));
        Assertions.assertTrue(participantFactory.getParticipantList().isEmpty());

        //checking that if the same participant is in the .csv file he is only once in the participant list
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/testliste2.csv")).toURI()));
        Assertions.assertEquals(1, participantFactory.getParticipantList().size());

        //checking that if a pair is on the list, the participants get extracted and are both in the participant list
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/testliste3.csv")).toURI()));
        Assertions.assertEquals(2, participantFactory.getParticipantList().size());

        //testing if participant list is still correctly read in if he has no kitchen story, kitchen story will then be set on 0
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/testliste4.csv")).toURI()));
        List<Participant> expectedParticipantList1 = new ArrayList<>();
        expectedParticipantList1.add(new Participant(new String[] {"2", "01be5c1f-4aa1-458d-a530-b1c109ffbb55", "Person3", "vegan", "22", "male", "yes", "0", "8.681372017093311", "50.5820794170933"}, false));
        Assertions.assertTrue(participantFactory.participantListEquals(expectedParticipantList1));

        //testing if participant list is still correctly read in if he has no kitchen story and no coordinates, coordinates should be set to -1.0
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/testliste5.csv")).toURI()));
        List<Participant> expectedParticipantList2 = new ArrayList<>();
        expectedParticipantList2.add(new Participant(new String[] {"2", "01be5c1f-4aa1-458d-a530-b1c109ffbb55", "Person3", "vegan", "22", "male", "yes", "0", "-1.0", "-1.0"}, false));
        Assertions.assertTrue(participantFactory.participantListEquals(expectedParticipantList2));

        //testing if participant list is still correctly read in if he has kitchen story but does not have coordinates
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/testliste6.csv")).toURI()));
        List<Participant> expectedParticipantList3 = new ArrayList<>();
        expectedParticipantList3.add(new Participant(new String[] {"2", "01be5c1f-4aa1-458d-a530-b1c109ffbb55", "Person3", "vegan", "22", "male", "yes", "4.0", "-1.0", "-1.0"}, false));
        Assertions.assertTrue(participantFactory.participantListEquals(expectedParticipantList3));

        //testing if participant list is still correctly read in if it is a pair and has no kitchen_story
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/testliste7.csv")).toURI()));
        List<Participant> expectedParticipantList4 = new ArrayList<>();
        expectedParticipantList4.add(new Participant(new String[] {"0", "f2f848e8-96d0-40e2-a7f8-7cd3751769ed", "Person1", "veggie", "22", "female", "yes", "0", "8.675847472405028", "50.57733957240503"}, false));
        expectedParticipantList4.add(new Participant(new String[] {"0", "fdc3673e-210f-4938-a7ef-f31a99574b29", "Person1Partner", "veggie", "20", "female", "yes", "0", "8.675847472405028", "50.57733957240503"}, false));
        Assertions.assertTrue(participantFactory.participantListEquals(expectedParticipantList4));

        //testing if participant list is still correctly read in if it is pair and has no kitchen_story and no coordinates
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/testliste8.csv")).toURI()));
        List<Participant> expectedParticipantList5 = new ArrayList<>();
        expectedParticipantList5.add(new Participant(new String[]{"0", "f2f848e8-96d0-40e2-a7f8-7cd3751769ed", "Person1", "veggie", "22", "female", "yes", "0", "-1.0", "-1.0"}, false));
        expectedParticipantList5.add(new Participant(new String[] {"0", "fdc3673e-210f-4938-a7ef-f31a99574b29", "Person1Partner", "veggie", "20", "female", "yes", "0", "-1.0", "-1.0"}, false));
        Assertions.assertTrue(participantFactory.participantListEquals(expectedParticipantList5));
        //TODO: add Testcase for isSuccessor

        //testing if participant is correctly marked as Successor, if max registrations reached
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/testliste9.csv")).toURI()));
        participantFactory.showCSV();
        Assertions.assertFalse(participantFactory.getParticipantList().get(0).getIsSuccessor()); //First participant cant be Successor
        Assertions.assertTrue(participantFactory.getParticipantList().get(101).getIsSuccessor());  //MAX Participants = 100 -> 101 Participant = Successor

        //testing if participant is correctly marked as Successor, if WG count > 3
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("/testlists/testliste10.csv")).toURI()));
        participantFactory.showCSV();
        Assertions.assertFalse(participantFactory.getParticipantList().get(0).getIsSuccessor());
        Assertions.assertTrue(participantFactory.getParticipantList().get(4).getIsSuccessor());
        Assertions.assertTrue(participantFactory.getParticipantList().get(5).getIsSuccessor());
    }

}