package Entity;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final List<Pair> pairs;

    public Group(Pair initialPair) {
        this.pairs = new ArrayList<>();
        pairs.add(initialPair);
    }

    public List<Pair> getPairs() {
        return pairs;
    }

    public void addPair(Pair pair) {
        this.pairs.add(pair);
    }
}
