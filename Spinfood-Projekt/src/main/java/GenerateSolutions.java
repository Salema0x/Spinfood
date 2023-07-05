import Entity.Group;
import Entity.Pair;
import Factory.Group.GroupFactory;
import Factory.PairListFactory;
import Factory.ParticipantFactory;
import Json.JacksonExport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class GenerateSolutions {
    private static String[] fileNames = new String[]{
            "solutionForCriteriaOrder58679.json",
            "solutionForCriteriaOrder95876.json",
            "solutionForCriteriaOrder76589.json",
    };
    private static String filePath = "Loesungen/";

    private enum CriteriaOrder {
        CRITERIA_FOOD_AGE_SEX,
        CRITERIA_FOOD_SEX_AGE,
        CRITERIA_SEX_AGE_FOOD;

        public ArrayList<Object> toList() {
            switch (this) {
                case CRITERIA_SEX_AGE_FOOD: {
                    ArrayList<Object> criteriaOrder = new ArrayList<>();
                    criteriaOrder.add("Geschlecht");
                    criteriaOrder.add("Alter");
                    criteriaOrder.add("Essen");
                    return criteriaOrder;
                }
                case CRITERIA_FOOD_SEX_AGE: {
                    ArrayList<Object> criteriaOrder = new ArrayList<>();
                    criteriaOrder.add("Essen");
                    criteriaOrder.add("Geschlecht");
                    criteriaOrder.add("Alter");
                    return criteriaOrder;
                }
                case CRITERIA_FOOD_AGE_SEX: {
                    ArrayList<Object> criteriaOrder = new ArrayList<>();
                    criteriaOrder.add("Essen");
                    criteriaOrder.add("Alter");
                    criteriaOrder.add("Geschlecht");
                    return criteriaOrder;
                }
                default:
                    throw new IllegalArgumentException("CriteriaOrder not found");
            }
        }

        public String toString() {
            switch (this) {
                case CRITERIA_FOOD_AGE_SEX: {
                    return "solutionForCriteriaOrder58679.json";
                }
                case CRITERIA_FOOD_SEX_AGE: {
                    return "solutionForCriteriaOrder95876.json";
                }
                case CRITERIA_SEX_AGE_FOOD: {
                    return "solutionForCriteriaOrder76589.json";
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

        //temp
        ArrayList<Pair> pairListFoodAgeSex = new ArrayList<>();
        ArrayList<Pair> pairListFoodSexAge = new ArrayList<>();
        ArrayList<Pair> pairListSexAgeFood = new ArrayList<>();
        ArrayList<Group> groupListFoodAgeSex = new ArrayList<>();
        ArrayList<Group> groupListFoodSexAge = new ArrayList<>();
        ArrayList<Group> groupListSexAgeFood = new ArrayList<>();

        for (CriteriaOrder criteriaOrder : CriteriaOrder.values()) {

            ArrayList<Object> criteriaOrderAsList = criteriaOrder.toList();
            System.out.println(criteriaOrderAsList);
            ParticipantFactory participantFactory = new ParticipantFactory(100);
            participantFactory.readCSV(new File("Spinfood-Projekt/src/main/resources/teilnehmerliste.csv"));
            PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), criteriaOrderAsList);
            ArrayList<Pair> pairList = new ArrayList<>(pairListFactory.getPairList());
            GroupFactory groupFactory = new GroupFactory(pairList, new Double[]{8.6746166676233, 50.5909317660173});
            groupFactory.startGroupAlgorithm();


            //Temp
            if(criteriaOrder.equals(CriteriaOrder.CRITERIA_FOOD_AGE_SEX)) {
                pairListFoodAgeSex = pairList;
                groupListFoodAgeSex = groupFactory.getGroups();


            } else if (criteriaOrder.equals(CriteriaOrder.CRITERIA_FOOD_SEX_AGE)) {
                pairListFoodSexAge = pairList;
                groupListFoodSexAge = groupFactory.getGroups();

            }
            else {
                pairListSexAgeFood = pairList;
                groupListSexAgeFood = groupFactory.getGroups();

            }

            JacksonExport jacksonExport = new JacksonExport();
            jacksonExport.exportToPath(groupFactory.getGroups(), groupFactory.getPairList(), groupFactory.getSuccessorPairs(), pairListFactory.getParticipantSuccessorList(), (filePath + criteriaOrder.toString()));
        }

        HashSet<Pair> pairListFoodAgeSexSet = new HashSet(pairListFoodAgeSex);
        HashSet<Pair> pairListFoodSexAgeSet = new HashSet(pairListFoodSexAge);
        HashSet<Pair> pairListSexAgeFoodSet = new HashSet(pairListSexAgeFood);

        HashSet<Group> groupSetFoodAGeSex = new HashSet<>(groupListFoodAgeSex);
        HashSet<Group> groupSetFoodSexAge = new HashSet<>(groupListFoodSexAge);
        HashSet<Group> groupSetSexAgeFood = new HashSet<>(groupListSexAgeFood);

        boolean equalPairs = pairListFoodAgeSexSet.equals(pairListFoodSexAgeSet) || pairListFoodAgeSexSet.equals(pairListSexAgeFoodSet) || pairListFoodSexAgeSet.equals(pairListSexAgeFoodSet);
        boolean equalGroups = groupSetFoodAGeSex.equals(groupSetFoodSexAge) || groupSetFoodAGeSex.equals(groupSetSexAgeFood) || groupSetFoodSexAge.equals(groupSetSexAgeFood);

        System.out.println("PairLists are equal: " + equalPairs);
        System.out.println("Groups are equal: " + equalGroups);
    }


}
