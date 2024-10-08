package Json;

import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JacksonExport {
    private String filePath = "Spinfood-Projekt/src/main/resources/Json/output.json";
    private ObjectMapper objectMapper;
    private ObjectWriter objectWriter;
    private Root root;


    /**
     * Constructor
     */
    public JacksonExport() {
        objectMapper = new ObjectMapper();
        objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

    }


    /**
     * generates a new Root Obj and then writes its Json-Repr into a file at default path
     * @param groupList List containing all Groups
     * @param registeredPairsList List containing all registered Pairs
     * @param successorPairsList List containing all successor Pairs
     * @param successorParticipantsList List containing all successor Participants
     */
    public void export(List<Group> groupList, List<Pair> registeredPairsList, List<Pair> successorPairsList, List<Participant> successorParticipantsList) {
        root = new Root(groupList, registeredPairsList, successorPairsList, successorParticipantsList);
        jsonWriter();
        System.out.println("Groups Saved at default Path: " + this.filePath);
    }

    /**
     * generates a new Root Obj and then writes its Json-Repr into a new file at given path
     * @param groupList List containing all Groups
     * @param registeredPairsList List containing all registered Pairs
     * @param successorPairsList List containing all successor Pairs
     * @param successorParticipantsList List containing all successor Participants
     * @param filePath path where the file should be written to
     */
    public void exportToPath(List<Group> groupList, List<Pair> registeredPairsList, List<Pair> successorPairsList, List<Participant> successorParticipantsList,String filePath) {
        this.filePath = filePath;
        root = new Root(groupList, registeredPairsList, successorPairsList, successorParticipantsList);
        jsonWriter();
    }


    /**
     * generates a new Root Obj and then writes its Json-Repr into a given File
     * @param groupList
     * @param registeredPairsList
     * @param successorPairsList
     * @param successorParticipantsList
     * @param file
     */
    public void exportToFile(List<Group> groupList, List<Pair> registeredPairsList, List<Pair> successorPairsList, List<Participant> successorParticipantsList, File file) {
        root = new Root(groupList, registeredPairsList, successorPairsList, successorParticipantsList);
        jsonFileWriter(file);
    }

    /**
     * generates new File at given path and writes the data into it
     */
    private void jsonWriter() {
        try {
            File output = new File(filePath);
            objectWriter.writeValue(output, root);
            System.out.println("Json saved at: " + output.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void jsonFileWriter(File file) {
        try {
            objectWriter.writeValue(file, root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
