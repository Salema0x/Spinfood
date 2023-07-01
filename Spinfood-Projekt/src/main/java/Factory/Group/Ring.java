package Factory.Group;

import Entity.Group;
import Entity.Pair;

import java.util.ArrayList;

public record Ring(ArrayList<Pair> pairsOnTheRing) {

    /**
     * After appetizer groups have been generated, the rings can only always contain the pairs which are already in a group.
     * @param existingGroups all the groups that are existing at this point.
     */
    public void updateRing(ArrayList<Group> existingGroups) {
        ArrayList<Pair> existingPairs = new ArrayList<>();
        for (Group group : existingGroups) {
            ArrayList<Pair> pairsInTheGroup = group.getPairs();
            existingPairs.addAll(pairsInTheGroup);
        }

        pairsOnTheRing.retainAll(existingPairs);
    }
}
