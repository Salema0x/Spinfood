package Factory;

import Entity.Course;
import Entity.Criteria;
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
    private final List<Group> starter;
    private final List<Group> main;
    private final List<Group> dessert;



    public GroupFactory2(int maxGroupSize, Criteria[] criteriaOrder, String partyLocationFilePath, String participantListFilePath) throws URISyntaxException {
        this.maxGroupSize = maxGroupSize;
        this.starter = new ArrayList<>();
        this.main = new ArrayList<>();
        this.dessert = new ArrayList<>();
        ParticipantFactory participantFactory = new ParticipantFactory();
        participantFactory.readCSV(new File(Objects.requireNonNull(getClass().getResource(participantListFilePath)).toURI()));
        partyLocation = partyLocationReader(new File(Objects.requireNonNull(getClass().getResource(partyLocationFilePath)).toURI()));

        PairListFactory pairListFactory = new PairListFactory(participantFactory.getParticipantList(), participantFactory.getRegisteredPairs(),  parseCriteriaForPairFactory(criteriaOrder));
        this.registeredPairs = pairListFactory.getRegisteredPairs();

        createGroups(criteriaOrder);


    }


    //Algorithm that creates three iterations of GroupLists for each course
    private void createGroups(Criteria[] criteriaOrder) {

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

    /**
     * Method to parse the criteria for the PairFactory (Enum Criteria -> Object[])
     * @param criteriaOrder
     * @return
     */
    private List<Object> parseCriteriaForPairFactory(Criteria[] criteriaOrder) {
        List<Object> criteria = new ArrayList<>();
        for (int i = 0; i < criteriaOrder.length; i++) {
            Criteria currentCriteria = criteriaOrder[i];
            if(currentCriteria.equals(Criteria.FOOD_PREFERENCE)) {
                criteria.add("Essensvorlieben");
            }
            if (currentCriteria.equals(Criteria.AGE_DIFFERENCE)) {
                criteria.add("Altersdifferenz");
            }
            if(currentCriteria.equals(Criteria.GENDER_DIVERSITY)) {
                criteria.add("Geschlechterdiversität");
            }
        }
        return criteria;
    }
}
