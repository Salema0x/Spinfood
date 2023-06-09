import Entity.Group;
import Misc.DinnerRound;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class JsonExport {
    private JSONObject root;
    private enum course {
        first, main, dessert
    }
    private enum foodType {
        meat, veggie, vegan
    }

    public JsonExport(Path path) {
        root = new JSONObject();
    }


    private void addRootProperties() {
        JSONArray groups = initializeGroups();
        JSONArray pairs = intializedPairs();
        JSONArray successorPairs = intializedSuccessorPairs();
        JSONArray successorParticipants = intializedSuccessorParticipants();

        root.put("groups", groups);
        root.put("pairs", pairs);
        root.put("successorPairs", successorPairs);
        root.put("successorParticipants", successorParticipants);


    }

    JSONArray initializeGroups() {
        JSONArray groups = new JSONArray();
        for(DinnerRound dinnerRound : dinnerRounds) {
            for(Group group : dinnerRound.getGroups()) {
                JSONObject groupJson = new JSONObject();
                groupJson.put("course", group.getId());
                groupJson.put("foodType", dinnerRound.getId());
                groupJson.put("kitchen", group.getParticipants());
                groupJson.put("cookingPair", group.);
                groupJson.put("secondPair", group.g);
                groupJson.put("thirdPair", group.getParticipants());




                groups.add(groups);
            }
        }
        return groups;
    }


    private void addJsonObjects() {

    }

    /**
     * Creates a JSON File from the
     */
    public void writeJsonFile(Path path) {
        File file = new File(path.toString());
        try(FileWriter fileWriter = new FileWriter("output.json")) {
            fileWriter.write(root.toJSONString());
            fileWriter.flush();

            System.out.println("Successfully generated JSON File...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
