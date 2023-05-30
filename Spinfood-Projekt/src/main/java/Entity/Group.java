package Entity;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Group {
    private final List<Pair> pairs;
    private final List<Participant> participants = new ArrayList<>();
    private Pair cookingPair;
    private double foodPreference;
    private double foodPreferenceDeviationScore;
    private double ageRange;
    private double ageRangeDeviationScore;

    public Group(Pair initialPair) {
        this.pairs = new ArrayList<>();
        pairs.add(initialPair);
        this.cookingPair = initialPair; // Setzen des initialPair als Kochpaar
        createParticipants();
        calculateFoodPreference();
        calculateAgeRange();
        calculateFoodPreferenceDeviationScore();
        calculateAgeRangeDeviationScore();
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

    public boolean containsPair(Pair pair) {
        return this.pairs.contains(pair);
    }

    /**
     * builds String of Pairs for printing
     * @return
     */
    public String toString() {
        String result = "";
        for (Pair pair : pairs) {
            result += pair.toString() + " ";
        }
        return result;
    }

    /**
     * calculates genderDiversityScore of a Group
     * @return
     */
    public double getGenderDiversityScore() {
        double score = 0.0;
        for(Pair pair : pairs) {
            score += pair.getGenderDiversityScore();

        }
        return score/pairs.size();
    }

    /**
     * calculates ageRange of a Group
     */
    public void calculateAgeRange() {
        for (Pair pair : pairs) {
            ageRange += pair.getMeanAgeRange();
        }
        ageRange = ageRange / pairs.size();
    }

    /**
     * calculates the mean deviation in ageRange of the pairs to the ageRange of the group
     */
    private void calculateAgeRangeDeviationScore() {
        double score = 0.0;
        for (Pair pair : pairs) {
            score += Math.abs(pair.getMeanAgeRange() - ageRange);
        }
        ageRangeDeviationScore = score / pairs.size();
    }

    /**
     * calculates the mean deviation of the pairs to the foodPreference of the group
     * @return
     */
    private void calculateFoodPreferenceDeviationScore() {
        double score = 0.0;
        for (Pair pair : pairs) {
            score += Math.abs(pair.getFoodPreferenceNumber() - foodPreference);
        }
        foodPreferenceDeviationScore = score / pairs.size();
    }

    /**
     * calculates foodPreference of a Group
     */
    private void calculateFoodPreference() {
        for(Pair pair : pairs) {
            foodPreference += pair.getFoodPreferenceNumber();
        }
        foodPreference = foodPreference/pairs.size();
    }

    public double getFoodPreferenceDeviationScore() {
        return foodPreferenceDeviationScore;
    }

    public double getAgeRangeDeviationScore() {
        return ageRangeDeviationScore;
    }
}
