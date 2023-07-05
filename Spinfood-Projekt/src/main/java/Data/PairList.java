package Data;

import Entity.Pair;
import Entity.Participant;

import java.util.List;
import java.util.function.Function;

public class PairList {
    private final List<Pair> pairList;
    private final int countPairs;
    private final int countSuccessors;
    private final double genderDiversityScore;
    private final double ageDifference;
    private final double preferenceDeviation;





    public PairList(List<Pair> pairList, List<Participant> successors) {
        this.pairList = pairList;
        this.countPairs = pairList.size();
        this.countSuccessors = successors.size();
        this.genderDiversityScore = calculateGenderDiversity();
        this.ageDifference = calculateAverageScores(Pair::getAgeDifference);
        this.preferenceDeviation = calculateAverageScores(Pair::getPreferenceDeviation);

    }

    /**
     * Calculates the average scores of the PairList.
     * @param method the method the gather the data from the pairs
     * @return the average key identification of the list.
     */
    private double calculateAverageScores(Function<Pair, Double> method) {
        double sumDiversityScores = 0.0d;

        for (Pair pair : pairList) {
            sumDiversityScores += method.apply(pair);
        }

        if (countPairs != 0) {
            return sumDiversityScores / countPairs;
        }

        return 0;
    }

    /**
     * Calculates the gender diversity score of the pair.
     * @return a double representing the gender diversity score.
     */
    private double calculateGenderDiversity() {
        double sumDeviationFromIdeal = 0.0d;

        for (Pair pair : pairList) {
            sumDeviationFromIdeal += Math.abs(0.5 - pair.getGenderDiversityScore());
        }

        if (countPairs != 0) {
            return sumDeviationFromIdeal / countPairs;
        }

        return 0;
    }



    public double getAgeDifference() {
        return ageDifference;
    }

    public double getGenderDiversityScore() {
        return genderDiversityScore;
    }

    public int getCountPairs() {
        return countPairs;
    }

    public double getPreferenceDeviation() {
        return preferenceDeviation;
    }

    public int getCountSuccessors() {
        return countSuccessors;
    }

}



