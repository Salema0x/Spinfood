package Entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import Enum.FoodPreference;

import static Enum.FoodPreference.*;

@JsonPropertyOrder({"premade", "foodPreference", "firstParticipant", "secondParticipant"})
public class Pair {
import java.util.ArrayList;

public class Pair implements Comparable<Pair> {
    private final Participant participant1;
    private final Participant participant2;
    private double ageDifference;
    private double preferenceDeviation;
    private double genderDiversityScore;
    private final Double[] placeOfCooking = new Double[2];
    private FoodPreference foodPreference;
    private final boolean preMade;
    private Kitchen kitchen;

    private final Double[] coordinatesFirstRound = new Double[2];
    private final Double[] coordinatesSecondRound = new Double[2];
    private final Double[] coordinatesThirdRound = new Double[2];
    private FoodPreference foodPreference;
    private final String id;
    private double distanceToPartyLocation;
    private Gender gender;
    public Double age;
    public ArrayList<Pair> seen = new ArrayList<>();
    /**
     * Constructur without preMade option
     * @param participant1
     * @param participant2
     */

    public Pair(Participant participant1, Participant participant2) {
        this.participant1 = participant1;
        this.participant2 = participant2;

        this.id = participant1.getId();

        decideFoodPreference();
        decidePlaceOfCooking();
        calculateAgeDifference();
        calculateGenderDiversityScore();
        calculatePreferenceDeviation();
        calculateAge();
    }
    /**
     * Constructor with preMade option
     * @param participant1
     * @param participant2
     * @param preMade
     */
    public Pair(Participant participant1, Participant participant2, boolean preMade) {
        this();
        this.preMade = preMade;
    }



    private void decidePlaceOfCooking() {
        if (participant1.getHasKitchen().equals("yes")) {
            placeOfCooking[0] = participant1.getKitchenLatitude();
            placeOfCooking[1] = participant1.getKitchenLongitude();
            kitchen = participant1.getKitchen();
        } else if (participant2.getHasKitchen().equals("yes")) {
            placeOfCooking[0] = participant2.getKitchenLatitude();
            placeOfCooking[1] = participant2.getKitchenLongitude();
            kitchen = participant2.getKitchen();
        } else if (participant1.getHasKitchen().equals("maybe")) {
            placeOfCooking[0] = participant1.getKitchenLatitude();
            placeOfCooking[1] = participant1.getKitchenLongitude();
            kitchen = participant1.getKitchen();
        } else if (participant2.getHasKitchen().equals("maybe")) {
            placeOfCooking[0] = participant2.getKitchenLatitude();
            placeOfCooking[1] = participant2.getKitchenLongitude();
            kitchen = participant2.getKitchen();
        }
    }

    public Double[] getCoordinatesFirstRound() {
        return coordinatesFirstRound;
    }

    public Double[] getCoordinatesSecondRound() {
        return coordinatesSecondRound;
    }

    public void setCoordinatesFirstRound(Double[] coordinatesFirstRound) {
        this.coordinatesFirstRound[0] = coordinatesFirstRound[0];
        this.coordinatesFirstRound[1] = coordinatesFirstRound[1];
    }

    public void setCoordinatesSecondRound(Double[] coordinatesSecondRound) {
        this.coordinatesSecondRound[0] = coordinatesSecondRound[0];
        this.coordinatesSecondRound[1] = coordinatesSecondRound[1];
    }

    public void setCoordinatesThirdRound(Double[] coordinatesThirdRound) {
        this.coordinatesThirdRound[0] = coordinatesThirdRound[0];
        this.coordinatesThirdRound[1] = coordinatesThirdRound[1];
    }

    private void decideFoodPreference() {
        FoodPreference part1Pref = participant1.getFoodPreference();
        FoodPreference part2Pref = participant2.getFoodPreference();

        switch (part1Pref) {
            case VEGAN -> {
                switch (part2Pref) {
                    case VEGAN , VEGGIE , NONE-> this.foodPreference = FoodPreference.VEGAN;
                }
            }
            case VEGGIE -> {
                switch (part2Pref) {
                    case VEGAN -> this.foodPreference = FoodPreference.VEGAN;
                    case VEGGIE, NONE -> this.foodPreference = FoodPreference.VEGGIE;
                }
            }
            case MEAT -> {
                switch (part2Pref) {
                    case MEAT , NONE -> this.foodPreference = FoodPreference.MEAT;
                }
            }
            case NONE -> {
                switch (part2Pref) {
                    case VEGAN -> this.foodPreference = FoodPreference.VEGAN;
                    case VEGGIE -> this.foodPreference = FoodPreference.VEGGIE;
                    case MEAT, NONE -> this.foodPreference = FoodPreference.MEAT;
                }
            }
        } else if (part1Pref.equals(VEGGIE) && part2Pref.equals(VEGAN)) {
            this.foodPreference = part2Pref;
        } else if (part1Pref.equals(VEGAN) && part2Pref.equals(VEGGIE)) {
            this.foodPreference = part1Pref;
        }

    }

    private void calculateAge() {
        this.age = (double) (participant1.getAge() + participant2.getAge()) / 2;
    }

    /**
     * Calculates the ageDifference of a pair.
     */
    private void calculateAgeDifference() {
        this.ageDifference = Math.abs(participant1.getAgeRange() - participant2.getAgeRange());
    }

    /**
     * Calculates the genderDiversityScore of a pair.
     */
    private void calculateGenderDiversityScore() {
        if (!participant1.getSex().equals(participant2.getSex()) || !participant2.getSex().equals(participant1.getSex())) {
            this.genderDiversityScore = 0.5;
            this.gender = Gender.MIXED;
        } else if (participant1.getSex().equals(participant2.getSex()) && participant1.getSex().equals("female")) {
            this.genderDiversityScore = 1;
            this.gender = Gender.FEMALE;
        } else {
            this.genderDiversityScore = 0;
            this.gender = Gender.MALE;
        }
    }

    /**
     * Calculates the preferenceDeviation of a pair.
     */
    private void calculatePreferenceDeviation() {
        this.preferenceDeviation = Math.abs(participant1.getFoodPreferenceNumber() - participant2.getFoodPreferenceNumber());
    }

    @Override
    public String toString() {
        return "Pair{" +
                "Participant Namen: " + participant1.getName() + " | " + participant2.getName() +
                ", Essensvorlieben: " + participant1.getFoodPreference() + " | " + participant2.getFoodPreference() +
                '}';
    }

    @Override
    public int compareTo(Pair o) {
        return age.compareTo(o.age);
    }


    public boolean isEqualTo(Pair pair) {
        if (participant1.getId().equals(pair.getParticipant1().getId()) || participant1.getId().equals(pair.getParticipant2().getId())) {
            return true;
        } else {
            return participant2.getId().equals(pair.getParticipant1().getId()) || participant2.getId().equals(pair.getParticipant2().getId());
        }
    }

    //Getters
    @JsonIgnore
    public String getId() {
        return id;
    }
    @JsonIgnore
    public Double getDistanceToPartyLocation() {
        return distanceToPartyLocation;
    }
    @JsonIgnore
    public Gender getGender() {
        return gender;
    }
    @JsonIgnore
    public double getAgeDifference() {
        return ageDifference;
    }
    @JsonIgnore
    public double getPreferenceDeviation() {
        return preferenceDeviation;
    }
    @JsonIgnore
    public double getGenderDiversityScore() {
        return genderDiversityScore;
    }
    @JsonIgnore
    public Double[] getPlaceOfCooking() {
        return placeOfCooking;
    }
    @JsonIgnore
    public Kitchen getKitchen() {
        return kitchen;
    }

    //JsonGetters
    @JsonGetter("premade")
    public boolean isPreMade() {
        return preMade;
    }
    @JsonGetter("foodPreference")
    public FoodPreference getFoodPreference() {
        return foodPreference;
    }
    @JsonGetter("firstParticipant")
    public Participant getParticipant1() {
        return participant1;
    }
    @JsonGetter("secondParticipant")
    public Participant getParticipant2() {
        return participant2;
    }

    //Setters
    public void setDistanceToPartyLocation(double distanceToPartyLocation) {
        this.distanceToPartyLocation = distanceToPartyLocation;
    }

}