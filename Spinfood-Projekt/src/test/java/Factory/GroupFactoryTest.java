package Factory;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Entity.Enum.FoodPreference;
import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import Factory.Group.GroupFactory;
import org.opentest4j.AssertionFailedError;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GroupFactoryTest {

    @org.junit.jupiter.api.Test
    void PairListFactory() {
        //Setup
        ParticipantFactory participantFactory = new ParticipantFactory(100);
        participantFactory.readCSV(new File("src/main/resources/teilnehmerliste.csv"));
        PairListFactory pairListFactory = new PairListFactory(new ArrayList<Participant> (participantFactory.getParticipantList()),new ArrayList<>(participantFactory.getRegisteredPairs()), new ArrayList<Object>(Arrays.asList("Essensvorlieben", "Altersdifferenz", "Geschlechterdiversität", "Weglänge", "Minimale Nachrücker")));
        GroupFactory groupFactory = new GroupFactory(new ArrayList<>(pairListFactory.getPairList()), new Double[] {8.6746166676233, 50.5909317660173});

        //Act
        groupFactory.startGroupAlgorithm();


        //Assert
        ArrayList<Pair> firstCoursePairList = new ArrayList<>();
        for(Group group : groupFactory.getFirstCourseGroupList()) {
            firstCoursePairList.addAll(group.getPairs());
        }
        ArrayList<Pair> mainCoursePairList = new ArrayList<>();
        for (Group group : groupFactory.getMainCourseGroupList()) {
            mainCoursePairList.addAll(group.getPairs());
        }
        ArrayList<Pair> dessertCoursePairList = new ArrayList<>();
        for (Group group : groupFactory.getDessertCourseGroupList()) {
            dessertCoursePairList.addAll(group.getPairs());
        }

        for (Pair pair : firstCoursePairList) {
            try {
                assertTrue(mainCoursePairList.contains(pair));
                assertTrue(dessertCoursePairList.contains(pair));
            }
            catch (AssertionFailedError e) {
                System.out.println(pair.getParticipant1().getName() + " " + pair.getParticipant2().getName() + "is not in the main course group");
            }
        }

        for(Pair pair : mainCoursePairList) {
            assertTrue(dessertCoursePairList.contains(pair));
        }


        for (Group group : groupFactory.getGroups()){
            if(group.getFoodPreference().equals(FoodPreference.vegan) || group.getFoodPreference().equals(FoodPreference.veggie)) {
                int meatLoverCounter = 0;
                for (Pair pair : group.getPairs()) {
                    
                    if (pair.getParticipant1().getFoodPreference().equals(FoodPreference.meat) || pair.getParticipant2().getFoodPreference().equals(FoodPreference.meat)) {
                        meatLoverCounter++;
                    }
                }
                assertTrue(meatLoverCounter <= 1);

            }

        }


    }


}