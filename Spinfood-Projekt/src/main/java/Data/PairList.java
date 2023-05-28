package Data;

import Entity.Pair;
import Entity.Participant;

import java.util.List;

public class PairList {
    private final List<Pair> pairList;
    private final int countPairs;
    private final int countSuccessors;
    private final double genderDiversityScore;
    private final double ageDifference;
    private final int preferenceDeviation;

    public PairList(List<Pair> pairList, List<Participant> successors) {
        this.pairList = pairList;
        this.countPairs = pairList.size();
        this.countSuccessors = successors.size();
        this.genderDiversityScore = calculateGenderDiversityScore();
        this.ageDifference = calculateAgeDifference();
        this.preferenceDeviation = calculatePreferenceDeviation();
    }

    private double calculateGenderDiversityScore() {
        double sumDiversityScores = 0.0d;

        for (Pair pair : pairList) {
            sumDiversityScores += pair.getGenderDiversityScore();
        }

        return sumDiversityScores/countPairs;
    }

    private double calculateAgeDifference() {
        double sumAgeDifference = 0.0d;

        for (Pair pair : pairList) {
            sumAgeDifference += pair.getAgeDifference();
        }

        return sumAgeDifference/countPairs;
    }

    private int calculatePreferenceDeviation() {
        return -1;
    }

    public double getAgeDifference() {
        return ageDifference;
    }

    public double getGenderDiversityScore() {
        return genderDiversityScore;
    }
}
