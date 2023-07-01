package Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import Entity.Enum.*;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"course", "foodType", "kitchen", "cookingPair", "secondPair", "thirdPair"})
public class Group {
    private final ArrayList<Pair> pairs = new ArrayList<>();
    private final List<Participant> participants = new ArrayList<>();
    private Pair cookingPair;
    private final double genderDiversityScore;
    private final double ageDifference;
    private final double preferenceDeviation;
    private FoodPreference foodPreference;
    private Course course;

    public Group(Pair initialPair, Course course) {
        pairs.add(initialPair);
        this.cookingPair = initialPair; // Setzen des initialPair als Kochpaar
        this.ageDifference = calculateAverageScores(Pair::getAgeDifference);
        this.preferenceDeviation = calculateAverageScores(Pair::getPreferenceDeviation);
        this.genderDiversityScore = calculateGenderDiversityScore();
        this.course = course;
        createParticipants();
        calculateFoodPreference();
    }

    public Group(ArrayList<Pair> pairs) {
        this.pairs.addAll(pairs);
        this.cookingPair = pairs.get(2);
        this.ageDifference = calculateAverageScores(Pair::getAgeDifference);
        this.preferenceDeviation = calculateAverageScores(Pair::getPreferenceDeviation);
        this.genderDiversityScore = calculateGenderDiversityScore();
        this.course = course;
        calculateFoodPreference();
        createParticipants();
        setSeen();
    }

    public String getGender() {
        Pair pair1 = pairs.get(0);
        Pair pair2 = pairs.get(1);
        Gender genderPair1 = pair1.getGender();
        Gender genderPair2 = pair2.getGender();
        Gender female = Gender.female;
        Gender male = Gender.male;
        Gender mixed = Gender.mixed;

        if (genderPair1.equals(female) && genderPair2.equals(female)) {
            return "ff";
        } else if (genderPair1.equals(female) && genderPair2.equals(male)) {
            return "fma";
        } else if (genderPair1.equals(female) && genderPair2.equals(mixed)) {
            return "fmix";
        } else if (genderPair1.equals(male) && genderPair2.equals(male)) {
            return "mm";
        } else if (genderPair1.equals(male) && genderPair2.equals(mixed)) {
            return "mmix";
        } else if (genderPair1.equals(mixed) && genderPair2.equals(mixed)) {
            return "mixmix";
        }
        return "0";
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

    //TODO: Remove this method if useless?
    /**
     * Method to check if a group contains only one pair
     * @return
     */
    public boolean isPair() {
        return participants.size() == 2;
    }



    /**
     * Method to manually add a Participant to the group
     * @param participant
     */
    public void addParticipant(Participant participant) {
        this.participants.add(participant);
    }


    /**
     * Helper method to calculate the food preference of the group.
     */
    public void calculateFoodPreference() {
        int sum = 0;
        for (Pair pair : pairs) {
            sum += pair.getFoodPreference().asNumber();
        }
        double median = (double) sum / pairs.size();

        if (median < 1.5) {
            this.foodPreference = FoodPreference.meat;
        } else if (median >= 1.5 && median < 2.5) {
            this.foodPreference = FoodPreference.veggie;
        } else {
            this.foodPreference = FoodPreference.vegan;
        }
    }


    /**
     * Adds a pair to the group.
     *
     * @param pair
     */
    public void addPair(Pair pair) {
        this.pairs.add(pair);
    }


    // Getter

    @JsonIgnore
    public double getGenderDiversityScore() {
        return genderDiversityScore;
    }

    @JsonIgnore
    public double getPreferenceDeviation() {
        return preferenceDeviation;
    }

    @JsonIgnore
    public double getAgeDifference() {
        return ageDifference;
    }
    @JsonIgnore
    public ArrayList<Pair> getPairs() {
        return pairs;
    }

    @JsonIgnore
    public Pair getCookingPair() {
        return this.cookingPair;
    }

    @JsonIgnore
    public List<Participant> getParticipants() {
        return participants;
    }

    //JsonGetter
    @JsonGetter("course")
    public Course getCourse() {
        return course;
    }

    @JsonGetter("foodType")
    public FoodPreference getFoodPreference() {
        return foodPreference;
    }

    @JsonGetter("kitchen")
    public Kitchen getKitchen() {
        return cookingPair.getKitchen();
    }

    @JsonGetter("cookingPair")
    public Pair getCookingPairJson() {
        return cookingPair;
    }

    @JsonGetter("secondPair")
    public Pair getSecondPair() {
        if (pairs.size() > 1) {
            return pairs.get(1);
        }
        return null;
    }

    @JsonGetter("thirdPair")
    public Pair getThirdPair() {
        if (pairs.size() > 2) {
            return pairs.get(2);
        }
        return null;
    }

    //Setter
    public void setFoodPreference(FoodPreference foodPreference) {
        this.foodPreference = foodPreference;
    }

    public void setCookingPair(Pair pair) {
        if (this.pairs.contains(pair)) {
            this.cookingPair = pair;
        } else {
            throw new IllegalArgumentException("The provided pair is not part of this group.");
        }
    }
}
