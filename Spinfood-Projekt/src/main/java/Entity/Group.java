package Entity;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final List<Pair> pairs;
    private final List<Participant> participants = new ArrayList<>();
    private Pair cookingPair;


    public Group(Pair initialPair) {
        this.pairs = new ArrayList<>();
        pairs.add(initialPair);
        this.cookingPair = initialPair; // Setzen des initialPair als Kochpaar
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

    public Pair getCookingPair() {
        return this.cookingPair;
    }

    public void setCookingPair(Pair pair) {
        if (this.pairs.contains(pair)) {
            this.cookingPair = pair;
        } else {
            throw new IllegalArgumentException("The provided pair is not part of this group.");
        }
    }
}
