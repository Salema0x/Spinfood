package Data;

import Entity.Pair;

import java.util.ArrayList;
import java.util.List;

public class PairList {
    private final List<Pair> pairList = new ArrayList<>();
    private int countPairs;
    private int countSuccessors;
    private int genderDiversityScore;
    private int ageDifference;
    private int preferenceDeviation;
}
