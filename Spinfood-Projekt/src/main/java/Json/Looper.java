package Json;


import Entity.Pair;
import Entity.Participant;
import Factory.*;
import Factory.Group.GroupFactory;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class Looper {
    private static List<ArrayList<Object>> pairCriteriaOrder;
    private static ParticipantFactory participantFactory;
    private static String[] filePaths;


    public Looper() {
        pairCriteriaOrder = pairCriteriaOrderInitializer();
        participantFactory = new ParticipantFactory(100);
        participantFactory.readCSV(new File("src/main/resources/teilnehmerliste.csv"));
        filePaths = new String[]{
                "src/main/resources/Json/looperOutput1.json",
                "src/main/resources/Json/looperOutput2.json",
                "src/main/resources/Json/looperOutput3.json",
                "src/main/resources/Json/looperOutput4.json",
                "src/main/resources/Json/looperOutput5.json",
                "src/main/resources/Json/looperOutput6.json",
        };


    }

    public void startLoop() {
        int index = 0;
        JacksonExport jacksonExport = new JacksonExport();
        for (ArrayList<Object> criteriaOrder : pairCriteriaOrder) {
            PairListFactory pairListFactory = pairListFactoryInitializer(criteriaOrder);
            GroupFactory groupFactory = groupFactoryInitializer(pairListFactory);
            groupFactory.startGroupAlgorithm();
            jacksonExport.exportToPath(groupFactory.getGroups(), groupFactory.getPairList(), groupFactory.getSuccessorPairs(), pairListFactory.getSuccessors(), filePaths[index]);
            index++;
        }

    }

    public static void main(String[] args) {
        Looper looper = new Looper();
        looper.startLoop();
    }

    /**
     * creates a List of ArrayLists containing all possible orders of the criteria
     *
     * @return
     */
    private List<ArrayList<Object>> pairCriteriaOrderInitializer() {
        List<ArrayList<Object>> pairCriteriaOrder = new ArrayList<>();
        ArrayList<Object> criteriaOrder1 = new ArrayList<>();
        criteriaOrder1.add("Essensvorlieben");
        criteriaOrder1.add("Altersdifferenz");
        criteriaOrder1.add("Geschlechterdiversität");
        pairCriteriaOrder.add(criteriaOrder1);

        ArrayList<Object> criteriaOrder2 = new ArrayList<>();
        criteriaOrder2.add("Altersdifferenz");
        criteriaOrder2.add("Essensvorlieben");
        criteriaOrder2.add("Geschlechterdiversität");
        pairCriteriaOrder.add(criteriaOrder2);

        ArrayList<Object> criteriaOrder3 = new ArrayList<>();
        criteriaOrder3.add("Geschlechterdiversität");
        criteriaOrder3.add("Essensvorlieben");
        criteriaOrder3.add("Altersdifferenz");
        pairCriteriaOrder.add(criteriaOrder3);

        ArrayList<Object> criteriaOrder4 = new ArrayList<>();
        criteriaOrder4.add("Essensvorlieben");
        criteriaOrder4.add("Geschlechterdiversität");
        criteriaOrder4.add("Altersdifferenz");
        pairCriteriaOrder.add(criteriaOrder4);

        ArrayList<Object> criteriaOrder5 = new ArrayList<>();
        criteriaOrder5.add("Altersdifferenz");
        criteriaOrder5.add("Geschlechterdiversität");
        criteriaOrder5.add("Essensvorlieben");
        pairCriteriaOrder.add(criteriaOrder5);

        ArrayList<Object> criteriaOrder6 = new ArrayList<>();
        criteriaOrder6.add("Geschlechterdiversität");
        criteriaOrder6.add("Altersdifferenz");
        criteriaOrder6.add("Essensvorlieben");
        pairCriteriaOrder.add(criteriaOrder6);
        return pairCriteriaOrder;
    }


    private PairListFactory pairListFactoryInitializer(ArrayList<Object> criteriaOrder) {
        return new PairListFactory((ArrayList<Participant>) participantFactory.getParticipantList(), (ArrayList<Pair>) participantFactory.getRegisteredPairs(), criteriaOrder);
    }

    private GroupFactory groupFactoryInitializer(PairListFactory pairListFactory) {
        return new GroupFactory(pairListFactory.getPairListAsArrayList(), new Double[] {8.6746166676233,50.5909317660173});
    }
}
