package Json;

import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;


@JsonPropertyOrder({"groups", "pairs", "successorPairs", "successorParticipants"})
public class Root {
    private final List<Group> groupList;
    private final List<Pair> pairList;
    private final List<Pair> successorPairList;
    private final List<Participant> successorParticipantList;


    public Root(List<Group> groupList, List<Pair> pairList, List<Pair> successorPairList, List<Participant> successorParticipantList) {
        this.groupList = groupList;
        this.pairList = pairList;
        this.successorPairList = successorPairList;
        this.successorParticipantList = successorParticipantList;
    }

    //Getter

    @JsonGetter("groups")
    public List<Group> getGroupList() {
        return groupList;
    }

    @JsonGetter("pairs")
    public List<Pair> getPairList() {
        return pairList;
    }

    @JsonGetter("successorParticipants")
    public List<Participant> getSuccessorParticipantList() {
        return successorParticipantList;
    }

    @JsonGetter("successorPairs")
    public List<Pair> getSuccessorPairList() {
        return successorPairList;
    }

}