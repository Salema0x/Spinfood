package Entity;

import Entity.Enum.FoodPreference;
import Entity.Enum.Gender;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;

@JsonPropertyOrder({"premade", "foodPreference", "firstParticipant", "secondParticipant"})
public class Pair implements Comparable<Pair> {


    private final Participant participant1;
    private final Participant participant2;
    private double ageDifference;
    private double preferenceDeviation;
    private double genderDiversityScore;
    private final Double[] placeOfCooking = new Double[2];
    private FoodPreference foodPreference;
    private boolean preMade = false;
    private Kitchen kitchen;

    private final Double[] coordinatesFirstRound = new Double[2];
    private final Double[] coordinatesSecondRound = new Double[2];
    private final Double[] coordinatesThirdRound = new Double[2];
    private final String id;
    private double distanceToPartyLocation;
    private Gender gender;
    public Double age;
    public ArrayList<Pair> seen = new ArrayList<>();

    /**
     * Constructur without preMade option
     *
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
     *
     * @param participant1
     * @param participant2
     * @param preMade
     */
    public Pair(Participant participant1, Participant participant2, boolean preMade) {
        this(participant1, participant2);
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
            case vegan -> {
                switch (part2Pref) {
                    case vegan, veggie, none -> this.foodPreference = FoodPreference.vegan;
                }
            }
            case veggie -> {
                switch (part2Pref) {
                    case vegan -> this.foodPreference = FoodPreference.vegan;
                    case veggie, none -> this.foodPreference = FoodPreference.veggie;
                }
            }
            case meat -> {
                switch (part2Pref) {
                    case meat, none -> this.foodPreference = FoodPreference.meat;
                }
            }
            case none -> {
                switch (part2Pref) {
                    case vegan -> this.foodPreference = FoodPreference.vegan;
                    case veggie -> this.foodPreference = FoodPreference.veggie;
                    case meat, none -> this.foodPreference = FoodPreference.meat;
                }
            }
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
        if (!participant1.getGender().equals(participant2.getGender()) || !participant2.getGender().equals(participant1.getGender())) {
            this.genderDiversityScore = 0.5;
            this.gender = Gender.mixed;
        } else if (participant1.getGender().equals(participant2.getGender()) && participant1.getGender().equals(Gender.female)) {
            this.genderDiversityScore = 1;
            this.gender = Gender.female;
        } else {
            this.genderDiversityScore = 0;
            this.gender = Gender.male;
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

