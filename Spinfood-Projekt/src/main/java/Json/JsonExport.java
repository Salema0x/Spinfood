package Json;

import Enum.Course;
import Entity.Group;
import Entity.Kitchen;
import Entity.Pair;
import Entity.Participant;
import Misc.DinnerRound;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;

public class JsonExport {

    private JSONObject root;
    private List<DinnerRound> dinnerRoundsList;
    private List<Pair> registeredPairsList;
    private List<Pair> successorPairsList;
    private List<Participant> successorParticipantsList;

    private String filePath = "src/main/resources/Json/output.json";

    /**
     * Constructor, generates the JSON Root with all its Properties and writes it to a file
     *
     * @param dinnerRoundsList
     * @param registeredPairsList
     * @param successorPairsList
     * @param successorParticipantsList
     */
    public JsonExport(List<DinnerRound> dinnerRoundsList, List<Pair> registeredPairsList, List<Pair> successorPairsList, List<Participant> successorParticipantsList) {
        this.dinnerRoundsList = dinnerRoundsList;
        this.registeredPairsList = registeredPairsList;
        this.successorPairsList = successorPairsList;
        this.successorParticipantsList = successorParticipantsList;
        root = new JSONObject();

        addRootProperties();
        prettyPrintJson();

    }

    /**
     * Constructor, generates the JSON Root with all its Properties and writes it to a file under the given path
     * @param dinnerRoundsList
     * @param registeredPairsList
     * @param successorPairsList
     * @param successorParticipantsList
     * @param filePath
     */
    public JsonExport(List<DinnerRound> dinnerRoundsList, List<Pair> registeredPairsList, List<Pair> successorPairsList, List<Participant> successorParticipantsList, String filePath) {
        new JsonExport(dinnerRoundsList, registeredPairsList, successorPairsList, successorParticipantsList);
        this.filePath = filePath;
    }

    /**
     * adds given properties to the root JSONObject (4 JSONArrays: groups, pairs, successorPairs, successorParticipants)
     */
    private void addRootProperties() {

        root.put("groups", generateJsonGroupArray());
        root.put("pairs", generateJsonPairArray());
        root.put("successorPairs", generateJsonPairSuccessorArray());
        root.put("successorParticipants", generateJsonParticipantSuccessorArray());


    }

    /**
     * generates a JSONArray and fills it with the Group data from the Algorithm
     *
     * @return
     */
    private JSONArray generateJsonGroupArray() {
        JSONArray groups = new JSONArray();
        for (DinnerRound dinnerRound : dinnerRoundsList) {
            Course course = dinnerRound.getCourse();
            for (Group group : dinnerRound.getGroups()) {
                JSONObject groupJson = new JSONObject();
                List<JSONObject> pairs = generateJsonPairs(group);
                groupJson.put("course", course);
                groupJson.put("foodType", group.getFoodPreference());
                groupJson.put("kitchen", pairs.get(0).get("kitchen"));
                groupJson.put("cookingPair", pairs.get(0));
                groupJson.put("secondPair", pairs.get(1));
                groupJson.put("thirdPair", pairs.get(2));
                groups.add(groupJson);
            }
        }
        return groups;
    }

    /**
     * generates a JSONArray containing all registered Pairs as JsonObj
     *
     * @return
     */
    private JSONArray generateJsonPairArray() {
        JSONArray result = new JSONArray();
        result.addAll(generatePairList(registeredPairsList));
        return result;
    }

    /**
     * generates a JSONArray containing all Pairs marked as Successors as JsonObj
     *
     * @return
     */
    private JSONArray generateJsonPairSuccessorArray() {
        JSONArray result = new JSONArray();
        for (JSONObject obj : generatePairList(successorPairsList)) {
            result.add(obj);

        }

        return result;
    }

    /**
     * generates a JSONArray containing all Participants marked as Successors as JsonObj
     *
     * @return
     */
    private JSONArray generateJsonParticipantSuccessorArray() {
        JSONArray result = new JSONArray();
        result.addAll(generateJsonParticipantList(successorParticipantsList));
        return result;
    }


    private void prettyPrintJson() {
        if (root != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonData = gson.toJson(root);

            // Write JSON data to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(jsonData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Getter and Setter

    public JSONObject getRoot() {
        return root;
    }


    //Helper Methods


    /**
     * generates a JSONObject from a Kitchen
     *
     * @param kitchen
     * @return
     */
    private JSONObject generateJsonKitchenObj(Kitchen kitchen) {
        JSONObject result = new JSONObject();
        if (kitchen.isNoKitchen()) {
            result.put("emergencyKitchen", false);
            result.put("story", 0);
            result.put("longitude", -1);
            result.put("latitude", -1);
        } else {
            result.put("emergencyKitchen", kitchen.isEmergencyKitchen());
            result.put("story", kitchen.getStory());
            result.put("longitude", kitchen.getLongitude());
            result.put("latitude", kitchen.getLatitude());
        }
        return result;
    }


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
     *
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
     *
     * @param p given Pair
     * @return
     */

    private JSONObject generateJsonPair(Pair p) {
        JSONObject result = new JSONObject();
        result.put("premade", p.isPreMade());
        result.put("foodPreference", p.getFoodPreference());
        result.put("firstParticipant", generateJsonParticipant(p.getParticipant1()));
        result.put("secondParticipant", generateJsonParticipant(p.getParticipant2()));
        return result;
    }

    /**
     * Helpermethod to generate List containing all Participants as JSONobj
     *
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
     *
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
