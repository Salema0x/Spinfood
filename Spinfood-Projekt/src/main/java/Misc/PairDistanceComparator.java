package Misc;

import Entity.Pair;

import java.util.Comparator;

public class PairDistanceComparator implements Comparator<Pair> {

    @Override
    public int compare(Pair o1, Pair o2) {
        return o2.getDistanceToPartyLocation().compareTo(o1.getDistanceToPartyLocation());
    }
}
