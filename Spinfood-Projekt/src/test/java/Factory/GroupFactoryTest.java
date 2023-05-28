package Factory;

import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GroupFactoryTest {
    GroupFactory groupFactory;
    PairListFactory pairListFactory;
    ParticipantFactory participantFactory;
    double[][] partyLocationCoordinates;
    File partyLocation;

    private enum Criteria {
        FOOD_PREFERENCE,
        AGE_DIFFERENCE,
        GENDER_DIVERSITY,
        GEOGRAPHICAL_DISTANCE,
        MAX_PARTICIPANTS
    }


    @BeforeEach
    void setUp() throws URISyntaxException {
        partyLocation = new File(Objects.requireNonNull(getClass().getResource("partylocation.csv").toURI()));
        partyLocationCoordinates = partyLocationReader(partyLocation);
        participantFactory = new ParticipantFactory();
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource("participants.csv").toURI())));
        pairListFactory = new PairListFactory(participantFactory.getParticipantList(), participantFactory.getRegisteredPairs(), );
        groupFactory = new GroupFactory(pairListFactory, 3, partyLocationCoordinates);


    }

    @org.junit.jupiter.api.Test
    void GroupFactory() {

    }
    //Testmethoden

    /**
     * Test if each Pair cooks exactly once
     *
     * @return
     */
    private boolean checkFalseCooking() {
        //TODO
        return false;
    }


    //helper Methods

    /**
     * checks if all Pairs in group have access to a kitchen
     */
    private boolean checkPairWithoutKitchen(Group group) {
        for (Pair pair : group.getPairs()) {
            if ((pair.getParticipant1().getHasKitchen().equals("false")) && (pair.getParticipant2().getHasKitchen().equals("false"))) {
                return true;
            }
        }
        return false;
    }


    private boolean checkGeographicalDistance(Group group) {
        for (Pair pair : group.getPairs()) {

        }
        return false;
    }

    private boolean checkAgeDifference(Group group) {
        return true;
    }

    private boolean checkPreferenceDeviation(Group group) {
        return true;
    }

    /**
     * Method to read the party location from a csv file
     *
     * @param file given csv file
     * @return partyLocation
     */
    private double[][] partyLocationReader(File file) {
        double[][] coordinates = new double[][]{new double[]{-1}, new double[]{-1}};

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.readLine() != null) {
                String line = br.readLine();
                String[] values = line.split(",");
                coordinates[0][0] = Double.parseDouble(values[0]);
                coordinates[1][0] = Double.parseDouble(values[1]);
            }
            return coordinates;

        } catch (IOException e) {
            throw new RuntimeException("Error while reading file");
        }
    }


}