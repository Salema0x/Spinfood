package Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Group {
    private final ArrayList<Pair> pairs = new ArrayList<>();
    private final List<Participant> participants = new ArrayList<>();
    private Pair cookingPair;
    private final double genderDiversityScore;
    private final double ageDifference;
    private final double preferenceDeviation;
    private String gender;

    public Group(Pair initialPair) {
        pairs.add(initialPair);
        this.cookingPair = initialPair; // Setzen des initialPair als Kochpaar
        this.ageDifference = calculateAverageScores(Pair::getAgeDifference);
        this.preferenceDeviation = calculateAverageScores(Pair::getPreferenceDeviation);
        this.genderDiversityScore = calculateGenderDiversityScore();
        createParticipants();
    }

    public Group(ArrayList<Pair> pairs) {
        this.pairs.addAll(pairs);
        this.cookingPair = pairs.get(2);
        this.ageDifference = calculateAverageScores(Pair::getAgeDifference);
        this.preferenceDeviation = calculateAverageScores(Pair::getPreferenceDeviation);
        this.genderDiversityScore = calculateGenderDiversityScore();
        createParticipants();
        setSeen();
    }



    public void setSeen() {
        Pair p1 = pairs.get(0);
        Pair p2 = pairs.get(1);
        Pair p3 = pairs.get(2);

        p1.seen.add(p2);
        p1.seen.add(p3);

        p2.seen.add(p1);
        p2.seen.add(p3);

        p3.seen.add(p2);
        p3.seen.add(p1);
    }

    public void addPairs(ArrayList<Pair> pairs) {
        this.pairs.addAll(pairs);
    }

    public int getGroupSize() {
        return pairs.size();
    }

    /**
     * Extracts the participants from the pairs.
     */
    private void createParticipants() {
        for (Pair pair : pairs) {
            participants.add(pair.getParticipant1());
            participants.add(pair.getParticipant2());
        }
    }

    /**
     * Calculates the average scores of the Group.
     * @param method the method the gather the data from the pairs of the group.
     * @return the average key identification of the list.
     */
    private double calculateAverageScores(Function<Pair, Double> method) {
        double sumDiversityScores = 0.0d;

        for (Pair pair : pairs) {
            sumDiversityScores += method.apply(pair);
        }

        if (pairs.size() != 0) {
            return sumDiversityScores / pairs.size();
        }

        return 0;
    }

    /**
     * Calculates the gender diversity score of the group.
     * @return a double representing the gender diversity score.
     */
    private double calculateGenderDiversityScore() {
        double sumDeviationFromIdeal = 0.0d;

        for (Pair pair : pairs) {
            sumDeviationFromIdeal += Math.abs(0.5 - pair.getGenderDiversityScore());
        }

        if (pairs.size() != 0) {
            return sumDeviationFromIdeal / pairs.size();
        }

        return 0;
    }

    public ArrayList<Pair> getPairs() {
        return pairs;
    }

    /**
     * Checks if the group contains a specific participant.
     * @param participant The participant for which should be checked.
     * @return a boolean indicating if the specified participant is in the group or not.
     */
    public boolean containsParticipant(Participant participant) {
        boolean contains = false;

        for (Pair pair : pairs) {
            contains = pair.getParticipant1().equals(participant) || pair.getParticipant2().equals(participant);
        }

        return contains;
    }

    /**
     * Builds String of Pairs for printing
     * @return A string representing the Pairs of the group.
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Pair pair : pairs) {
            result.append(pair.toString()).append(" ");
        }
        return result.toString();
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

    public boolean containsPair(Pair pair) {
        return this.pairs.contains(pair);
    }

    public double getGenderDiversityScore() {
        return genderDiversityScore;
    }

    public double getPreferenceDeviation() {
        return preferenceDeviation;
    }

    public double getAgeDifference() {
        return ageDifference;
    }
}
