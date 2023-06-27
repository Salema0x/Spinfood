package Json;

import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"groups", "pairs", "successorPairs", "successorParticipants"})
public record Root(@JsonGetter("groups") List<Group> groupList, @JsonGetter("pairs") List<Pair> pairList,
                   @JsonGetter("successorPairs") List<Pair> successorPairList,
                   @JsonGetter("successorParticipants") List<Participant> successorParticipantList) {


}
