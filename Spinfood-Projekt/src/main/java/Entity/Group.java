package Entity;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final List<Pair> pairs;
    private final List<Participant> participants = new ArrayList<>();

    public Group(Pair initialPair) {
        this.pairs = new ArrayList<>();
        pairs.add(initialPair);
        createParticipants();
    }

    private void createParticipants() {
        for (Pair pair : pairs) {
            participants.add(pair.getParticipant1());
            participants.add(pair.getParticipant2());
        }
    }

    public List<Pair> getPairs() {
        return pairs;
    }

    public void addPair(Pair pair) {
        this.pairs.add(pair);
    }

    public boolean containsParticipant(Participant participant) {
        boolean contains = false;

        for (Pair pair : pairs) {
            contains = pair.getParticipant1().equals(participant) || pair.getParticipant2().equals(participant);
        }

        return contains;
    }

    public boolean isPair() {
        return participants.size() == 2;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
    }
}
