import Entity.Pair;
import Factory.Group.GroupFactory;
import Factory.PairListFactory;
import Factory.ParticipantFactory;
import Json.JacksonExport;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class GenerateSolutions {
    private static String filePath = "Loesungen/";

    private enum CriteriaOrder {
        CRITERIA_FOOD_AGE_SEX,
        CRITERIA_FOOD_SEX_AGE,
        CRITERIA_SEX_AGE_FOOD;


        private ArrayList<Object> asArray() {
            switch (this) {
                case CRITERIA_SEX_AGE_FOOD: {
                    ArrayList<Object> criteriaOrder = new ArrayList<>(Arrays.asList("Geschlechterdiversität", "Altersdifferenz", "Essensvorlieben"));
                    return criteriaOrder;
                }
                case CRITERIA_FOOD_SEX_AGE: {
                    ArrayList<Object> criteriaOrder = new ArrayList<>(Arrays.asList("Essensvorlieben", "Geschlechterdiversität", "Altersdifferenz"));
                    return criteriaOrder;
                }
                case CRITERIA_FOOD_AGE_SEX: {
                    ArrayList<Object> criteriaOrder = new ArrayList<>(Arrays.asList("Essensvorlieben", "Altersdifferenz", "Geschlechterdiversität"));
                    return criteriaOrder;
                }
                default:
                    throw new IllegalArgumentException("CriteriaOrder not found");
            }
        }

        public String toString() {
            switch (this) {
                case CRITERIA_FOOD_AGE_SEX: {
                    return "solutionForCriteriaOrder_a.json";
                }
                case CRITERIA_FOOD_SEX_AGE: {
                    return "solutionForCriteriaOrder_b.json";
                }
                case CRITERIA_SEX_AGE_FOOD: {
                    return "solutionForCriteriaOrder_c.json";
                }
                default:
                    throw new IllegalArgumentException("CriteriaOrder not found");
            }
        }
    }


    public static void main(String[] args) {
        generateSolutions();
    }

    /**
     * generates all solutions for the given criteria orders
     */
    public static void generateSolutions() {

        for (CriteriaOrder criteriaOrder : CriteriaOrder.values()) {
            ParticipantFactory participantFactory = new ParticipantFactory(100);
            participantFactory.readCSV(new File("Spinfood-Projekt/src/main/resources/teilnehmerliste.csv"));
            PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), criteriaOrder.asArray());
            ArrayList<Pair> pairList = new ArrayList<>(pairListFactory.getPairList());
            GroupFactory groupFactory = new GroupFactory(pairList, new Double[]{8.6746166676233, 50.5909317660173});
            groupFactory.startGroupAlgorithm();
            JacksonExport jacksonExport = new JacksonExport();
            jacksonExport.exportToPath(groupFactory.getGroups(), groupFactory.getPairList(), groupFactory.getSuccessorPairs(), pairListFactory.getParticipantSuccessorList(), (filePath + criteriaOrder.toString()));
        }
    }


}
