package Factory;

import Entity.Group;
import Entity.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupFactory2 {
    private final List<Pair> registeredPairs;
    private final int maxGroupSize;
    private double[][] partyLocation;
    private final List<Group> groups;



    public GroupFactory2(int maxGroupSize, String[] criteriaOrder, String partyLocationFilePath, String participantListFilePath) throws URISyntaxException {
        this.maxGroupSize = maxGroupSize;
        this.groups = new ArrayList<>();
        ParticipantFactory participantFactory = new ParticipantFactory();
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource(participantListFilePath)).toURI()));
        partyLocation = partyLocationReader(new File(Objects.requireNonNull(getClass().getResource(partyLocationFilePath)).toURI()));
        PairListFactory pairListFactory = new PairListFactory(participantFactory.getParticipantList(), participantFactory.getRegisteredPairs(), criteriaOrder);


    }



    private void createGroups(String[] criteriaOrder) {
        //TODO
    }





    //Helper Methods
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
