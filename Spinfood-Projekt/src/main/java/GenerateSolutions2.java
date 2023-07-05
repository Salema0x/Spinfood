import Factory.Group.GroupFactory;
import Factory.PairListFactory;
import Factory.ParticipantFactory;
import Json.JacksonExport;

import java.io.File;
import java.util.ArrayList;

public class GenerateSolutions2 {

    public static void main(String[] args) {
        generateSolutionsFoodAgeSex();
        generateSolutionsFoodSexAge();
        generateSolutionsSexAgeFood();
    }



    public static void generateSolutionsFoodAgeSex() {
        ParticipantFactory participantFactory = new ParticipantFactory(100);
        participantFactory.readCSV(new File("Spinfood-Projekt/src/main/resources/teilnehmerliste.csv"));
        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>() {{
            add("Essen");
            add("Alter");
            add("Geschlecht");
        }});
        GroupFactory groupFactory = new GroupFactory(new ArrayList<>(pairListFactory.getPairList()), new Double[]{8.6746166676233, 50.5909317660173});
        groupFactory.startGroupAlgorithm();

        JacksonExport jacksonExport = new JacksonExport();
        jacksonExport.exportToPath(groupFactory.getGroups(), pairListFactory.getPairList(), groupFactory.getSuccessorPairs(), pairListFactory.getParticipantSuccessorList(), "Loesungen/solutionForCriteriaOrder_a.json");
    }

    public static void generateSolutionsFoodSexAge() {
        ParticipantFactory participantFactory = new ParticipantFactory(100);
        participantFactory.readCSV(new File("Spinfood-Projekt/src/main/resources/teilnehmerliste.csv"));
        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>() {{
            add("Essen");
            add("Geschlecht");
            add("Alter");
        }});
        GroupFactory groupFactory = new GroupFactory(new ArrayList<>(pairListFactory.getPairList()), new Double[]{8.6746166676233, 50.5909317660173});
        groupFactory.startGroupAlgorithm();

        JacksonExport jacksonExport = new JacksonExport();
        jacksonExport.exportToPath(groupFactory.getGroups(), pairListFactory.getPairList(), groupFactory.getSuccessorPairs(), pairListFactory.getParticipantSuccessorList(), "Loesungen/solutionForCriteriaOrder_b.json");
    }

    public static void generateSolutionsSexAgeFood() {
        ParticipantFactory participantFactory = new ParticipantFactory(100);
        participantFactory.readCSV(new File("Spinfood-Projekt/src/main/resources/teilnehmerliste.csv"));
        PairListFactory pairListFactory = new PairListFactory(new ArrayList<>(participantFactory.getParticipantList()), new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<>() {{
            add("Geschlecht");
            add("Alter");
            add("Essen");
        }});
        GroupFactory groupFactory = new GroupFactory(new ArrayList<>(pairListFactory.getPairList()), new Double[]{8.6746166676233, 50.5909317660173});
        groupFactory.startGroupAlgorithm();

        JacksonExport jacksonExport = new JacksonExport();
        jacksonExport.exportToPath(groupFactory.getGroups(), pairListFactory.getPairList(), groupFactory.getSuccessorPairs(), pairListFactory.getParticipantSuccessorList(), "Loesungen/solutionForCriteriaOrder_c.json");

    }
}
