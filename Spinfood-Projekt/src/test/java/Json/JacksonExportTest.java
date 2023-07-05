package Json;

import Entity.Group;
import Entity.Pair;
import Entity.Participant;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JacksonExportTest {

        @org.junit.jupiter.api.Test
        void export() {
            //init
        }

        @org.junit.jupiter.api.Test
        void exportToPath() {
        }

        public ArrayList<Group> initializeCustomGroupList() {
            ArrayList<Group> groupList = new ArrayList<>();
            return groupList;
        }

        public ArrayList<Participant> initializeCustomParticipantList() {
            ArrayList<Participant> participantList = new ArrayList<>();
            return participantList;

        }

        public ArrayList<Pair> initializeCustomPairList() {
            ArrayList<Pair> pairList = new ArrayList<>();
            return pairList;

        }

}