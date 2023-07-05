package Entity;

import Enum.*;
import java.util.ArrayList;

public class Pair implements Comparable<Pair> {
    private Participant participant1;
    private Participant participant2;
    private double ageDifference;
    private double preferenceDeviation;
    private double genderDiversityScore;
    private final Double[] placeOfCooking = new Double[2];
    private final Double[] coordinatesFirstRound = new Double[2];
    private final Double[] coordinatesSecondRound = new Double[2];
    private final Double[] coordinatesThirdRound = new Double[2];
    private FoodPreference foodPreference;
    private final String id;
    private double distanceToPartyLocation;
    private Gender gender;
    public Double age;
    public ArrayList<Pair> seen = new ArrayList<>();

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

    private void decidePlaceOfCooking() {
        if (participant1.getHasKitchen().equals("yes")) {
            placeOfCooking[0] = participant1.getKitchenLatitude();
            placeOfCooking[1] = participant1.getKitchenLongitude();
        } else if (participant2.getHasKitchen().equals("yes")) {
            placeOfCooking[0] = participant2.getKitchenLatitude();
            placeOfCooking[1] = participant2.getKitchenLongitude();
        } else if (participant1.getHasKitchen().equals("maybe")) {
            placeOfCooking[0] = participant1.getKitchenLatitude();
            placeOfCooking[1] = participant1.getKitchenLongitude();
        } else if (participant2.getHasKitchen().equals("maybe")) {
            placeOfCooking[0] = participant2.getKitchenLatitude();
            placeOfCooking[1] = participant2.getKitchenLongitude();
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
        String part1Pref = participant1.getFoodPreference();
        String part2Pref = participant2.getFoodPreference();

        switch (part1Pref) {
            case "vegan" -> {
                switch (part2Pref) {
                    case "vegan", "veggie", "none" -> this.foodPreference = FoodPreference.VEGAN;
                }
            }
            case "veggie" -> {
                switch (part2Pref) {
                    case "vegan" -> this.foodPreference = FoodPreference.VEGAN;
                    case "veggie", "none" -> this.foodPreference = FoodPreference.VEGGIE;
                }
            }
            case "meat" -> {
                switch (part2Pref) {
                    case "meat", "none" -> this.foodPreference = FoodPreference.MEAT;
                }
            }
            case "none" -> {
                switch (part2Pref) {
                    case "vegan" -> this.foodPreference = FoodPreference.VEGAN;
                    case "veggie" -> this.foodPreference = FoodPreference.VEGGIE;
                    case "meat", "none" -> this.foodPreference = FoodPreference.MEAT;
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

    public void updateCalculations() {
        calculateAge();
        calculateAgeDifference();
        calculatePreferenceDeviation();
        calculateGenderDiversityScore();
    }

    @Override
    public String toString() {
        return "Pair{" +
                "Participants: " + participant1.getName() + " | " + participant2.getName() + '}';
    }


    public boolean isEqualTo(Pair pair) {
        if (participant1.getId().equals(pair.getParticipant1().getId()) || participant1.getId().equals(pair.getParticipant2().getId())) {
            return true;
        } else {
            return participant2.getId().equals(pair.getParticipant1().getId()) || participant2.getId().equals(pair.getParticipant2().getId());
        }
    }

    public boolean containsParticipant(Participant participant) {
        return this.participant1.equals(participant) || this.participant2.equals(participant);
    }

    public void addParticipant(Participant participant) {
        // Checking if there is an open spot in the pair
        if (this.participant1 == null) {
            this.participant1 = participant;
        } else if (this.participant2 == null) {
            this.participant2 = participant;
        } else {
            System.out.println("The pair is already full. Cannot add another participant.");
        }
    }

    public void removeParticipant(Participant participant) {
        // Checking if the participant is in the pair
        if (this.participant1 != null && this.participant1.equals(participant)) {
            this.participant1 = null;
        } else if (this.participant2 != null && this.participant2.equals(participant)) {
            this.participant2 = null;
        } else {
            System.out.println("The participant is not in the pair. Cannot remove the participant.");
        }
    }


    public Participant getParticipant1() {
        return participant1;
    }

    public Participant getParticipant2() {
        return participant2;
    }

    public FoodPreference getFoodPreference() {
        return foodPreference;
    }

    public double getAgeDifference() {
        return ageDifference;
    }

    public double getPreferenceDeviation() {
        return preferenceDeviation;
    }

    public double getGenderDiversityScore() {
        return genderDiversityScore;
    }

    public Double[] getPlaceOfCooking() {
        return placeOfCooking;
    }

    public String getId() {
        return id;
    }

    public void setDistanceToPartyLocation(double distanceToPartyLocation) {
        this.distanceToPartyLocation = distanceToPartyLocation;
    }

    public Double getDistanceToPartyLocation() {
        return distanceToPartyLocation;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public int compareTo(Pair o) {
        return age.compareTo(o.age);
    }
}