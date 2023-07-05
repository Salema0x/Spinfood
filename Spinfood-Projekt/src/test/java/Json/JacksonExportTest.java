package Json;

import Entity.Enum.Course;
import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JacksonExportTest {

    @org.junit.jupiter.api.Test
    void export() {
        //init
        ArrayList<Group> groupList = initializeCustomGroupList();
        ArrayList<Pair> pairList = initializeCustomPairList();
        ArrayList<Pair> successorPairList = initializeCustomSuccessorPairList();
        ArrayList<Participant> successorParticipantList = initializeCustomSuccessorParticipantList();
        JacksonExport jacksonExport = new JacksonExport();

        //act
        jacksonExport.exportToPath(groupList, pairList, successorPairList, successorParticipantList, "src/main/resources/Json/Test/testOutput.json");

    }

    @org.junit.jupiter.api.Test
    void exportToPath() {
        ArrayList<Group> groupList = initializeCustomGroupList2();
        ArrayList<Pair> pairList = initializeCustomPairList();
        ArrayList<Pair> successorPairList = initializeCustomSuccessorPairList();
        ArrayList<Participant> successorParticipantList = initializeCustomSuccessorParticipantList();
        JacksonExport jacksonExport = new JacksonExport();

        //act
        jacksonExport.exportToPath(groupList, pairList, successorPairList, successorParticipantList, "src/main/resources/Json/Test/testOutputCustom.json");
    }

    public ArrayList<Group> initializeCustomGroupList2() {
        ArrayList<Group> groupList = new ArrayList<>();
        ArrayList<Pair> pairList = initializeCustomPairList();
        System.out.println(pairList.size());

        ArrayList<Pair> groupPairs1 = new ArrayList<>();
        groupPairs1.add(pairList.get(5));
        groupPairs1.add(pairList.get(1));
        groupPairs1.add(pairList.get(3));

        ArrayList<Pair> groupPairs2 = new ArrayList<>();
        groupPairs2.add(pairList.get(2));
        groupPairs2.add(pairList.get(4));
        groupPairs2.add(pairList.get(0));

        Group group1 = new Group(groupPairs1, Course.first);
        Group group2 = new Group(groupPairs2, Course.main);

        groupList.add(group1);
        groupList.add(group2);

        return groupList;
    }


    public ArrayList<Group> initializeCustomGroupList() {
        ArrayList<Group> groupList = new ArrayList<>();
        ArrayList<Pair> pairList = initializeCustomPairList();
        System.out.println(pairList.size());

        Group group1 = new Group(new ArrayList<> (pairList.subList(0, 3)), Course.first);
        Group group2 = new Group(new ArrayList<>(pairList.subList(3, 6)), Course.main);

        groupList.add(group1);
        groupList.add(group2);

        return groupList;
    }

    public ArrayList<Participant> initializeCustomParticipantList() {
        ArrayList<Participant> participantList = new ArrayList<>();

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


        participantList.add(participant1);
        participantList.add(participant2);
        participantList.add(participant3);
        participantList.add(participant4);
        participantList.add(participant5);
        participantList.add(participant6);
        participantList.add(participant7);
        participantList.add(participant8);
        participantList.add(participant9);
        participantList.add(participant10);
        participantList.add(participant11);
        participantList.add(participant12);


        return participantList;

    }

    public ArrayList<Pair> initializeCustomPairList() {
        ArrayList<Pair> pairList = new ArrayList<>();
        ArrayList<Participant> participantList = initializeCustomParticipantList();

        Pair pair1 = new Pair(participantList.get(0), participantList.get(1));
        Pair pair2 = new Pair(participantList.get(2), participantList.get(3));
        Pair pair3 = new Pair(participantList.get(4), participantList.get(5));
        Pair pair4 = new Pair(participantList.get(6), participantList.get(7));
        Pair pair5 = new Pair(participantList.get(8), participantList.get(9));
        Pair pair6 = new Pair(participantList.get(10), participantList.get(11));

        pairList.add(pair1);
        pairList.add(pair2);
        pairList.add(pair3);
        pairList.add(pair4);
        pairList.add(pair5);
        pairList.add(pair6);

        return pairList;

    }

    public ArrayList<Pair> initializeCustomSuccessorPairList() {
        ArrayList<Pair> successorPairList = new ArrayList<>();
        Participant participant1 = new Participant(new String[]{"1", "ffaec9d9-b787-492a-a63d-01ef80a9cd11", "Person19", "veggie", "21", "male", "no"}, false);
        Participant participant2 = new Participant(new String[]{"2", "fe47cfc7-d16a-49e6-ad3d-2284a2d85c71", "Person20", "veggie", "19", "female", "yes", "2.0", "8.68238010723739", "50.58709280723739"}, false);
        Participant participant3 = new Participant(new String[]{"3", "fcc4c645-dc03-4687-a0bd-18e3d3c610d3", "Person21", "vegan", "19", "female", "no"}, false);
        Participant participant4 = new Participant(new String[]{"4", "f6d28393-fb4a-4d9a-94af-78b35cd31b59", "Person22", "vegan", "18", "male", "maybe", "1.0", "8.68151506608149", "50.59463476608149"}, false);
        Participant participant5 = new Participant(new String[]{"5", "f6afb0b2-752f-4c4d-a419-14fd0cd27a8e", "Person23", "vegan", "22", "female", "yes", "0.0", "8.686014200624939", "50.59846340062495"}, false);
        Participant participant6 = new Participant(new String[]{"6", "f6afb0b2-752f-4c4d-a419-14fd0cd27a8e", "Person24", "vegan", "22", "female", "yes", "-1.0", "8.677227638993099", "50.5782781389931"}, false);

        //generate some pairs
        Pair pair1 = new Pair(participant1, participant2, false);
        Pair pair2 = new Pair(participant3, participant4, false);
        Pair pair3 = new Pair(participant5, participant6, false);


        //add pairs to a List
        successorPairList.add(pair1);
        successorPairList.add(pair2);
        successorPairList.add(pair3);

        return successorPairList;

    }

    public ArrayList<Participant> initializeCustomSuccessorParticipantList() {
        ArrayList<Participant> successorParticipantList = new ArrayList<>();
        //generate some custom participants
        Participant participant1 = new Participant(new String[]{"1", "b1fb61c9-1a7a-4418-9eea-9d91a8c9e0dc", "Person100", "veggie", "21", "male", "no"}, false);
        Participant participant2 = new Participant(new String[]{"2", "b3092fb5-4038-4095-b721-8294d3803717", "Person101", "veggie", "25", "male", "no"}, false);


        //add Participants to a List
        successorParticipantList.add(participant1);
        successorParticipantList.add(participant2);

        return successorParticipantList;

    }
}