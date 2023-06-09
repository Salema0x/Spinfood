package Json;

import Entity.Group;
import Entity.Pair;
import Misc.DinnerRound;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class JsonExport {
    private JSONObject root;
    private List<DinnerRound> dinnerRounds;
    private enum Course {
        first, main, dessert
    }
    private enum FoodType {
        meat, veggie, vegan
    }

    public JsonExport(List<DinnerRound> dinnerRounds, Path path) {
        this.dinnerRounds = dinnerRounds;
        root = new JSONObject();
        addRootProperties();
    }


    private void addRootProperties() {
        JSONArray groups = initializeGroups();
        JSONArray pairs = initializePairs();
        JSONArray successorPairs = initializeSuccessorPairs();
        JSONArray successorParticipants = initializeSuccessorParticipants();

        root.put("groups", groups);
        root.put("pairs", pairs);
        root.put("successorPairs", successorPairs);
        root.put("successorParticipants", successorParticipants);


    }

    /**
     * generates a JSONArray and fills it with the Group data from the Algorithm
     * @return
     */
    private JSONArray initializeGroups() {
        JSONArray groups = new JSONArray();
        for(DinnerRound dinnerRound : dinnerRounds) {
            Course course = getCourse(dinnerRound);
            for(Group group : dinnerRound.getGroups()) {
                JSONObject groupJson = new JSONObject();
                JSONObject[] pairs = generatePairs(group);
                groupJson.put("course", course);
                groupJson.put("foodType", getFoodType(group));
                groupJson.put("kitchen", generateKitchenObj(group));
                groupJson.put("cookingPair", pairs[0]);
                groupJson.put("secondPair", pairs[1]);
                groupJson.put("thirdPair", pairs[2]);




                groups.add(groups);
            }
        }
        return groups;
    }

    private JSONArray initializePairs() {
        JSONArray pairs = new JSONArray();




        return pairs;
    }

    private JSONArray initializeSuccessorPairs() {
        JSONArray successorPairs = new JSONArray();




        return successorPairs;
    }

    private JSONArray initializeSuccessorParticipants() {
        JSONArray successorParticipants = new JSONArray();




        return successorParticipants;
    }



    /**
     * Creates a JSON File from the
     */
    private void writeJsonFile(Path path) {
        File file = new File(path.toString());
        try(FileWriter fileWriter = new FileWriter("output.json")) {
            fileWriter.write(root.toJSONString());
            fileWriter.flush();

            System.out.println("Successfully generated JSON File...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //Helper Methods
    //TODO: Implement Field in Group for FoodType + Getter
    /**
     * Returns the FoodType of a Group
     * @param g
     * @return
     */
    private FoodType getFoodType(Group g) {
        String foodType = "puffer";  //g.getFoodType()
        switch (foodType) {
            case "meat":
                return FoodType.meat;
            case "veggie":
                return FoodType.veggie;
            case "vegan":
                return FoodType.vegan;
            default:
                return null;
        }
    }

    //TODO: Implement Field in DinnerRound + getter
    /**
     * Returns the Course of a DinnerRound
     */
    private Course getCourse(DinnerRound dinnerRound) {
        return null;                        //dinnerRound.getCourse()
    }



    //TODO: Implement Class Kitchen + Field in Group + getter
    /**
     * Returns a JSONObject with the Kitchen of a Group
     * @param g
     * @return
     */
    private JSONObject generateKitchenObj(Group g) {
        JSONObject kitchen = new JSONObject();
        kitchen.put("emergencyKitchen", "temp");      //g.getKitchen().isEmergencyKitchen()
        kitchen.put("story", "temp");   //g.getKitchen().getAddress()
        kitchen.put("longitude", "temp");   //g.getKitchen().getLongitude()
        kitchen.put("latitude", "temp");      //g.getKitchen().getLatitude()
        return kitchen;
    }

    //TODO: Implement Field in Group + getter
    /**
     * Generates a JSONObject Array with the Pairs of a Group {cookingPair, secondPair, thirdPair}
     */
    private JSONObject[] generatePairs(Group group) {
        List<Pair> pairs = group.getPairs();
        Pair cookingPair = null;  //pairs.getCookingPair();
        pairs.remove(cookingPair);

        JSONObject jsonCookingPair = new JSONObject();
        jsonCookingPair.put("premade", "temp");    //cookingPair.isPremade()
        jsonCookingPair.put("foodPreference", cookingPair.getFoodPreference());

        JSONObject jsonSecondPair = new JSONObject();
        JSONObject jsonThirdPair = new JSONObject();

        return new JSONObject[]{jsonCookingPair, jsonSecondPair, jsonThirdPair};
    }
}
