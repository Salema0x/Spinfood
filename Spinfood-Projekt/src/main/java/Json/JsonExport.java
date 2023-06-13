package Json;

import Entity.Group;
import Entity.Kitchen;
import Entity.Pair;
import Entity.Participant;
import Misc.DinnerRound;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonExport {

    private JSONObject root;
    private List<DinnerRound> dinnerRoundsList;
    private List<Pair> registeredPairsList;
    private List<Pair> successorPairsList;
    private List<Participant> successorParticipantsList;
    private enum Course {
        first, main, dessert
    }
    private enum FoodType {
        meat, veggie, vegan
    }

    /**
     * Constructor, generates the JSON Root with all its Properties and writes it to a file
     * @param dinnerRoundsList
     * @param registeredPairsList
     * @param successorPairsList
     * @param successorParticipantsList
     * @param path
     */
    public JsonExport(List<DinnerRound> dinnerRoundsList, List<Pair> registeredPairsList, List<Pair> successorPairsList, List<Participant> successorParticipantsList, Path path) {
        this.dinnerRoundsList = dinnerRoundsList;
        this.registeredPairsList = registeredPairsList;
        this.successorPairsList = successorPairsList;
        this.successorParticipantsList = successorParticipantsList;
        root = new JSONObject();
        addRootProperties();
        writeJsonFile();

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
        for(DinnerRound dinnerRound : dinnerRoundsList) {
            Course course = getCourse(dinnerRound);
            for(Group group : dinnerRound.getGroups()) {
                JSONObject groupJson = new JSONObject();
                List<JSONObject> pairs = generateJsonPairs(group);
                groupJson.put("course", course);
                groupJson.put("foodType", getFoodType(group));
                groupJson.put("kitchen", pairs.get(0).get("kitchen"));
                groupJson.put("cookingPair", pairs.get(0));
                groupJson.put("secondPair", pairs.get(1));
                groupJson.put("thirdPair", pairs.get(2));


                groups.add(groups);
            }
        }
        return groups;
    }

    /**
     * generates a JSONArray containing all registered Pairs as JsonObj
     * @return
     */
    private JSONArray initializePairs() {
        JSONArray result = new JSONArray();
        result.addAll(generatePairList(registeredPairsList));
        return result;
    }

    /**
     * generates a JSONArray containing all Pairs marked as Successors as JsonObj
     * @return
     */
    private JSONArray initializeSuccessorPairs() {
        JSONArray result = new JSONArray();
        result.addAll(generatePairList(successorPairsList));
        return result;
    }

    /**
     * generates a JSONArray containing all Participants marked as Successors as JsonObj
     * @return
     */
    private JSONArray initializeSuccessorParticipants() {
        JSONArray result = new JSONArray();
        result.addAll(generateJsonParticipantList(successorParticipantsList));
        return result;
    }


    /**
     * Creates a JSON File from the root
     */
    private void writeJsonFile() {
        try(FileWriter fileWriter = new FileWriter("Spinfood-Projekt/src/main/resources/output.json")) {
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



    /**
     * generates a JSONObject from a Kitchen
     * @param kitchen
     * @return
     */
    private JSONObject generateJsonKitchenObj(Kitchen kitchen) {
JSONObject result = new JSONObject();
        result.put("emergencyKitchen", kitchen.isEmergencyKitchen());
        result.put("story", kitchen.getStory());
        result.put("longitude", kitchen.getLongitude());
        result.put("latitude", kitchen.getLatitude());
        return result;

    }

    //TODO: Implement Field in Group + getter
    /**
     * Generates a JSONObject Array with the Pairs of a Group {cookingPair, secondPair, thirdPair}
     */
    private List<JSONObject> generateJsonPairs(Group group) {
        List<Pair> pairList = group.getPairs();
        List<JSONObject> jsonPairList = new ArrayList<>();
        jsonPairList.add(generateJsonPair(pairList.remove(pairList.indexOf(group.getCookingPair()))));
        jsonPairList.add(generateJsonPair(pairList.remove(0)));
        jsonPairList.add(generateJsonPair(pairList.remove(0)));
        return jsonPairList;
    }

    /**
     * Helper Method to generate a List of JsonObj containing all registered Pairs
     * @param pairList
     * @return
     */
    private List<JSONObject> generatePairList(List<Pair> pairList) {
        List<JSONObject> result = new ArrayList<>();
        for (Pair p : pairList) {
            result.add(generateJsonPair(p));
        }
        return result;
    }

    /**
     * method to generate a JSON Pair
     * @param p given Pair
     * @return
     */

    private JSONObject generateJsonPair(Pair p) {
        JSONObject result = new JSONObject();
        result.put("premade", "temp"); //p.isPremade()
        result.put("foodPreference", p.getFoodPreference());
        result.put("firstParticipant", generateJsonParticipant(p.getParticipant1()));
        result.put("secondParticipant", generateJsonParticipant(p.getParticipant2()));
        return result;
    }

    /**
     * Helpermethod to generate List containing all Participants as JSONobj
     * @return
     */
    private List<JSONObject> generateJsonParticipantList(List<Participant> participantList) {
        List<JSONObject> result = new ArrayList<>();
        for (Participant participant : participantList) {
            result.add(generateJsonParticipant(participant));
        }
        return result;

    }

    /**
     * generates a JsonParticipant Obj from a given Participant
     * @param participant
     * @return
     */
    private JSONObject generateJsonParticipant(Participant participant) {
        JSONObject result = new JSONObject();
        result.put("id", participant.getId());
        result.put("fullName", participant.getName());
        result.put("foodPreference", participant.getFoodPreference());
        result.put("age", participant.getAge());
        result.put("gender", participant.getSex());
        result.put("kitchen", generateJsonKitchenObj(participant.getKitchen()));
        return result;
    }
}
