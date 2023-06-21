package Json;

import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import Misc.DinnerRound;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JacksonExport {
    private String filePath = "src/main/resources/Json/output.json";
    private ObjectMapper objectMapper;
    private ObjectWriter objectWriter;
    private Root root;



    /**
     * Constructor
     */
    public JacksonExport(List<DinnerRound> dinnerRoundList, List<Pair> registeredPairsList, List<Pair> successorPairsList, List<Participant> successorParticipantsList) {
        objectMapper = new ObjectMapper();
        objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        List<Group> groupList = new ArrayList<>();
        for(DinnerRound dinnerRound : dinnerRoundList) {
            groupList.addAll(dinnerRound.getGroups());
        }
        root = new Root(groupList, registeredPairsList, successorPairsList, successorParticipantsList);
        export();
    }

    /**
     * Constructor, with filePath
     *
     * @param filePath
     */
    public JacksonExport(List<DinnerRound> dinnerRoundList, List<Pair> registeredPairsList, List<Pair> successorPairsList, List<Participant> successorParticipantsList, String filePath) {
        this(dinnerRoundList, registeredPairsList, successorPairsList, successorParticipantsList);
        this.filePath = filePath;
    }

    /**
     * generates new File at given path and writes the data into it
     */
    public void export() {
        try {
            objectWriter.writeValue(new File(filePath), root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
