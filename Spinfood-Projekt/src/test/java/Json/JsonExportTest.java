package Json;

import Entity.Group;
import Misc.DinnerRound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import Enum.FoodPreference;
import Enum.Course;

public class JsonExportTest {

    @Test
    public void testInitializeGroups() {
        List<DinnerRound> dinnerRounds = new ArrayList<>();
        // Create some test data for dinnerRounds and groups
        DinnerRound dinnerRound1 = new DinnerRound();
        dinnerRound1.setCourse(Course.first);

        Group group1 = new Group();
        group1.setFoodPreference(FoodPreference.meat);
        // Set other properties for group1

        Group group2 = new Group();
        group2.setFoodPreference(FoodPreference.veggie);
        // Set other properties for group2

        dinnerRound1.addGroup(group1);
        dinnerRound1.addGroup(group2);

        dinnerRounds.add(dinnerRound1);

        JsonExport jsonExport = new JsonExport(dinnerRounds, Paths.get("test.json"));
        JSONArray groups = jsonExport.initializeGroups();

        // Assert the expected number of groups
        Assertions.assertEquals(2, groups.size());

        // Assert the properties of the first group
        JSONObject group1Json = (JSONObject) groups.get(0);
        Assertions.assertEquals("first", group1Json.get("course"));
        Assertions.assertEquals("meat", group1Json.get("foodType"));
        // Assert other properties of group1Json

        // Assert the properties of the second group
        JSONObject group2Json = (JSONObject) groups.get(1);
        Assertions.assertEquals("first", group2Json.get("course"));
        Assertions.assertEquals("veggie", group2Json.get("foodType"));
        // Assert other properties of group2Json
    }

    @Test
    public void testGeneratePairs() {
        Group group = new Group();
        // Set up the group and pairs for testing

        JsonExport jsonExport = new JsonExport(new ArrayList<>(), Paths.get("test.json"));
        JSONObject[] pairs = jsonExport.generatePairs(group);

        // Assert the properties of the cookingPair
        JSONObject cookingPairJson = pairs[0];
        Assertions.assertEquals("temp", cookingPairJson.get("premade"));
        Assertions.assertEquals("foodPreferenceValue", cookingPairJson.get("foodPreference"));
        // Assert other properties of cookingPairJson

        // Assert the properties of the secondPair
        JSONObject secondPairJson = pairs[1];
        Assertions.assertEquals(null, secondPairJson.get("premade"));
        // Assert other properties of secondPairJson

        // Assert the properties of the thirdPair
        JSONObject thirdPairJson = pairs[2];
        Assertions.assertEquals(null, thirdPairJson.get("premade"));
        // Assert other properties of thirdPairJson
    }

    // Add more test methods to cover other functionality of the JsonExport class
}