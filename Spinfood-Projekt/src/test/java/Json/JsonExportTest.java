package Json;

import Entity.*;

import Factory.GroupFactory;
import Factory.PairListFactory;
import Factory.ParticipantFactory;
import Misc.DinnerRound;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Enum.FoodPreference;
import Enum.Course;

public class JsonExportTest {
    private JsonExport jsonExport;

    /**
     * tests JsonExport with default input data(see csv file)
     */
    @Test
    public void testJsonExport() {
        ParticipantFactory participantFactory = new ParticipantFactory(100);
        participantFactory.readCSV(new File("src/main/resources/Teilnehmerliste.csv"));
        PairListFactory pairListFactory = new PairListFactory(
                new ArrayList<>(participantFactory.getParticipantList()),
                new ArrayList<>(participantFactory.getRegisteredPairs()),
                new ArrayList<Object>(Arrays.asList("Geschlechterdiversität", "Essensvorlieben", "Altersdifferenz")));
        GroupFactory groupFactory = new GroupFactory(pairListFactory, 3, new Double[]{8.6746166676233, 50.5909317660173});
        checkValidInputData(groupFactory.getDinnerRounds(), groupFactory.getRegisteredPairs(), groupFactory.getSuccessorList(), pairListFactory.getParticipantSuccessorList());
        JsonExport export = new JsonExport(groupFactory.getDinnerRounds(), groupFactory.getRegisteredPairs(), groupFactory.getSuccessorList(), pairListFactory.getParticipantSuccessorList());
        writePropertyData(export.getRoot());
    }

    /**
     * Test JsonExport with custom inputData
     */
    @Test
    public void testJsonExportCustomInput() {
        //Arrange
        List<DinnerRound> dinnerRoundsList = generateCustomDinnerRoundList();
        List<Pair> registeredPairsList = generatePairs();
        List<Pair> successorPairsList = generateSuccessorPairs();
        List<Participant> successorParticipantsList = generateSuccessorParticipants();

        //Act
        JsonExport jsonExport = new JsonExport(dinnerRoundsList, registeredPairsList, successorPairsList, successorParticipantsList, "src/main/resources/Json/Test/testOutputCustom.json");
        Assertions.assertTrue(Paths.get("src/main/resources/Json/Test/testOutputCustom.json").toFile().exists());

    }

    /**
     * SimpleJson test for the functionality of the JsonExport class
     * @throws IOException
     */

    @Test
    public void functionalityTest() throws IOException {

        JSONObject root = new JSONObject();

        JSONObject jsonObject1 = new JSONObject();

        jsonObject1.put("ID", "1521");
        jsonObject1.put("Position", "Manager");
        jsonObject1.put("Name", "Albert Ferenc");

        JSONObject jsonObject2 = new JSONObject();

        jsonObject2.put("ID", "1111");
        jsonObject2.put("Position", "Chef");
        jsonObject2.put("Name", "Rudi Ratlos");

        JSONObject jsonObject3 = new JSONObject();

        jsonObject3.put("ID", "2721");
        jsonObject3.put("Position", "Abteilungsleiterin");
        jsonObject3.put("Name", "Maria chen");

        JSONObject jsonObject4 = new JSONObject();

        jsonObject4.put("ID", "p111");
        jsonObject4.put("Position", "Praktikant");
        jsonObject4.put("Name", "Sepp Meier");

        JSONObject jsonObject5 = new JSONObject();

        jsonObject5.put("ID", "p222");
        jsonObject5.put("Position", "Praktikant");
        jsonObject5.put("Name", "Willy Astor");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject4);
        jsonArray.add(jsonObject5);


        root.put("Manager", jsonObject1);
        root.put("Chef", jsonObject2);
        root.put("Abteilungsleiterin", jsonObject3);
        root.put("Praktikanten", jsonArray);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonData = gson.toJson(root);
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/Json/Test/functionallityTest.json"));
        writer.write(jsonData);
        writer.close();
    }


//Helper methods

    public List<DinnerRound> generateCustomDinnerRoundList() {
        List<Pair> pairList = generatePairs();
        List<DinnerRound> dinnerRounds = new ArrayList<>();
        DinnerRound dinnerRound1 = new DinnerRound(Course.FIRST);
        DinnerRound dinnerRound2 = new DinnerRound(Course.MAIN);
        DinnerRound dinnerRound3 = new DinnerRound(Course.DESSERT);
        dinnerRound1.addGroupList(initializeCustomGroups(Course.FIRST, pairList));
        dinnerRound2.addGroupList(initializeCustomGroups(Course.MAIN, pairList));
        dinnerRound3.addGroupList(initializeCustomGroups(Course.DESSERT, pairList));
        dinnerRounds.add(dinnerRound1);
        dinnerRounds.add(dinnerRound2);
        dinnerRounds.add(dinnerRound3);

        return dinnerRounds;
    }


    public List<Group> initializeCustomGroups(Course course, List<Pair> pairs) {


        // Create groups
        if (course.equals(Course.FIRST)) {

            Group group1 = new Group(pairs.get(0));
            group1.addPair(pairs.get(1));
            group1.addPair(pairs.get(2));
            Group group2 = new Group(pairs.get(3));
            group2.addPair(pairs.get(4));
            group2.addPair(pairs.get(5));
            return new ArrayList<Group>() {{
                add(group1);
                add(group2);
            }};

        } else if (course.equals(Course.MAIN)) {

            Group group3 = new Group(pairs.get(1));
            group3.addPair(pairs.get(3));
            group3.addPair(pairs.get(4));
            Group group4 = new Group(pairs.get(2));
            group4.addPair(pairs.get(5));
            group4.addPair(pairs.get(0));
            return new ArrayList<Group>() {{
                add(group3);
                add(group4);
            }};

        }

        Group group5 = new Group(pairs.get(2));
        Group group6 = new Group(pairs.get(5));
        group5.addPair(pairs.get(0));
        group5.addPair(pairs.get(1));
        group6.addPair(pairs.get(3));
        group6.addPair(pairs.get(4));
        return new ArrayList<Group>() {{
            add(group5);
            add(group6);
        }};
    }

    public List<Pair> generatePairs() {
        //generate some custom participants and make pairs
        List<Participant> participants = new ArrayList<>();
        Participant participant1 = new Participant(new String[]{"1", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "veggie", "21", "male", "maybe", "1", "50.673368271555807", "50.5941282715558"}, false);
        Participant participant2 = new Participant(new String[]{"2", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "veggie", "25", "male", "yes", "2", "8.676057363906686", "8.67437965355096"}, false);
        Participant participant3 = new Participant(new String[]{"3", "01be5c1f-4aa1-458d-a530-b1c109ffbb55", "Person3", "vegan", "25", "female", "no"}, false);
        Participant participant4 = new Participant(new String[]{"4", "01c1372d-d120-4459-9b65-39d56d1ad430", "Person4", "vegan", "18", "male", "yes", "1", "8.690711820698867", "8.668840259833873"}, false);
        Participant participant5 = new Participant(new String[]{"5", "033d5f60-5853-4931-8b38-1d3da9910e6d", "Person5", "vegan", "22", "female", "yes", "0", "8.6884154438459", "50.5941282715558"}, false);
        Participant participant6 = new Participant(new String[]{"6", "03ec42b3-de71-424d-a51d-f8a3977dac38", "Person6", "vegan", "25", "female", "yes", "0", "50.673368271555807", "50.5780036438459"}, false);
        Participant participant7 = new Participant(new String[]{"7", "04b46884-c678-450a-a38e-9087bb67bf97", "Person7", "meat", "19", "male", "no", "1", "8.677676558333479", "50.589846758333486"}, false);
        Participant participant8 = new Participant(new String[]{"8", "05381170-b888-4457-a5f8-a528ac763236", "Person8", "none", "20", "female", "yes", "3", "8.698184706391327", "50.5941282715558"}, false);
        Participant participant9 = new Participant(new String[]{"9", "060800e6-8a64-4131-a292-22e5e1626b67", "Person9", "meat", "27", "male", "yes", "1", "50.673368271555807", "50.604206006391344"}, false);
        Participant participant10 = new Participant(new String[]{"10", "06d17797-9452-49e2-8e46-e1067a5fb901", "Person10", "meat", "25", "female", "yes", "2", "8.67073904401206", "50.57969724401207"}, false);
        Participant participant11 = new Participant(new String[]{"11", "07b46a18-a534-4c2c-b154-ec28c1aae8a7", "Person11", "meat", "19", "male", "yes", "1", "50.673368271555807", "50.57969724401207"}, false);
        Participant participant12 = new Participant(new String[]{"12", "07b7446a-9d8b-478b-b3e9-e95b992fcf50", "Person12", "meat", "19", "female", "no"}, false);
        Pair pair1 = new Pair(participant1, participant2);
        Pair pair2 = new Pair(participant3, participant4);
        Pair pair3 = new Pair(participant5, participant6);
        Pair pair4 = new Pair(participant7, participant8);
        Pair pair5 = new Pair(participant9, participant10);
        Pair pair6 = new Pair(participant11, participant12);

        //add pairs to a List
        List<Pair> pairs = new ArrayList<>();
        pairs.add(pair1);
        pairs.add(pair2);
        pairs.add(pair3);
        pairs.add(pair4);
        pairs.add(pair5);
        pairs.add(pair6);
        return pairs;
    }


    public List<Pair> generateSuccessorPairs() {
        //generate some custom participants and make pairs
        List<Participant> participants = new ArrayList<>();
        Participant participant1 = new Participant(new String[]{"1", "ffaec9d9-b787-492a-a63d-01ef80a9cd11", "Person19", "veggie", "21", "male", "no"}, false);
        Participant participant2 = new Participant(new String[]{"2", "fe47cfc7-d16a-49e6-ad3d-2284a2d85c71", "Person20", "veggie", "19", "female", "yes", "2.0", "8.68238010723739", "50.58709280723739"}, false);
        Participant participant3 = new Participant(new String[]{"3", "fcc4c645-dc03-4687-a0bd-18e3d3c610d3", "Person21", "vegan", "19", "female", "no"}, false);
        Participant participant4 = new Participant(new String[]{"4", "f6d28393-fb4a-4d9a-94af-78b35cd31b59", "Person22", "vegan", "18", "male", "maybe", "1.0", "8.68151506608149", "50.59463476608149"}, false);
        Participant participant5 = new Participant(new String[]{"5", "f6afb0b2-752f-4c4d-a419-14fd0cd27a8e", "Person23", "vegan", "22", "female", "yes", "0.0", "8.686014200624939", "50.59846340062495"}, false);
        Participant participant6 = new Participant(new String[]{"6", "f6afb0b2-752f-4c4d-a419-14fd0cd27a8e", "Person24", "vegan", "22", "female", "yes", "-1.0", "8.677227638993099", "50.5782781389931"}, false);

        //generate some pairs
        Pair pair1 = new Pair(participant1, participant2);
        Pair pair2 = new Pair(participant3, participant4);
        Pair pair3 = new Pair(participant5, participant6);


        //add pairs to a List
        List<Pair> pairs = new ArrayList<>();
        pairs.add(pair1);
        pairs.add(pair2);
        pairs.add(pair3);

        return pairs;
    }


    public List<Participant> generateSuccessorParticipants() {
        //generate some custom participants
        Participant participant1 = new Participant(new String[]{"1", "b1fb61c9-1a7a-4418-9eea-9d91a8c9e0dc", "Person100", "veggie", "21", "male", "no"}, false);
        Participant participant2 = new Participant(new String[]{"2", "b3092fb5-4038-4095-b721-8294d3803717", "Person101", "veggie", "25", "male", "no"}, false);


        //add Participants to a List
        List<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);
        return participants;
    }

    boolean checkValidInputData(List<DinnerRound> dinnerRoundsList, List<Pair> registeredPairsList, List<Pair> successorPairsList, List<Participant> successorParticipantsList) {
        if (dinnerRoundsList.isEmpty()) {
            System.out.println("DinnerRoundList is Empty?: " + dinnerRoundsList.isEmpty());
            return false;
        }
        if (dinnerRoundsList.get(0).getGroups().isEmpty()) {
            System.out.println("GroupList1 is Empty?: " + dinnerRoundsList.get(0).getGroups().isEmpty());
            return false;
        }
        if (dinnerRoundsList.get(1).getGroups().isEmpty()) {
            System.out.println("GroupList2 is Empty?: " + dinnerRoundsList.get(1).getGroups().isEmpty());
            return false;
        }
        if (dinnerRoundsList.get(2).getGroups().isEmpty()) {
            System.out.println("GroupList3 is Empty?: " + dinnerRoundsList.get(2).getGroups().isEmpty());
            return false;
        }
        if (registeredPairsList.isEmpty()) {
            System.out.println("RegisteredPairList is Empty?: " + registeredPairsList.isEmpty());
            return false;
        }
        if (successorPairsList.isEmpty()) {
            System.out.println("SuccessorPairList is Empty?: " + successorPairsList.isEmpty());
            return false;
        }
        if (successorParticipantsList.isEmpty()) {
            System.out.println("SuccessorParticipantList is Empty? :" + successorParticipantsList.isEmpty());
            return false;
        }
        System.out.println("All Lists contain data");
        return true;
    }

    /**
     * helper Method to check if Arrays contain data
     */
    private void writePropertyData(JSONObject root) {
        writeJsonData(root.get("groups"), "src/main/resources/Json/Test/groups.json");
        writeJsonData(root.get("pairs"), "src/main/resources/Json/Test/pairs.json");
        writeJsonData(root.get("successorPairs"), "src/main/resources/Json/Test/successorPairs.json");
        writeJsonData(root.get("successorParticipants"), "src/main/resources/Json/Test/successorParticipants.json");
    }

    public void writeJsonData(Object data, String fileName) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonData = gson.toJson(data);

        // Write JSON data to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}