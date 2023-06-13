package Entity;

import java.util.ArrayList;

public class Pair implements Comparable<Pair> {
    private final Participant participant1;
    private final Participant participant2;
    private double ageDifference;
    private double preferenceDeviation;
    private double genderDiversityScore;
    private final Double[] placeOfCooking = new Double[2];
    private Double[] coordinatesFirstRound;
    private Double[] coordinatesSecondRound;
    private Double[] coordinatesThirdRound;
    private String foodPreference;
    private final String id;
    private double distanceToPartyLocation;
    private String gender;
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

    public Double[] getCoordinatesThirdRound() {
        return coordinatesThirdRound;
    }

    public void setCoordinatesFirstRound(Double[] coordinatesFirstRound) {
        this.coordinatesFirstRound = coordinatesFirstRound;
    }

    public void setCoordinatesSecondRound(Double[] coordinatesSecondRound) {
        this.coordinatesSecondRound = coordinatesSecondRound;
    }

    public void setCoordinatesThirdRound(Double[] coordinatesThirdRound) {
        this.coordinatesThirdRound = coordinatesThirdRound;
    }

    private void decideFoodPreference() {
        String part1Pref = participant1.getFoodPreference();
        String part2Pref = participant2.getFoodPreference();

        switch (part1Pref) {
            case "vegan" -> {
                switch (part2Pref) {
                    case "vegan", "veggie", "none" -> this.foodPreference = "vegan";
                }
            }
            case "veggie" -> {
                switch (part2Pref) {
                    case "vegan" -> this.foodPreference = "vegan";
                    case "veggie", "none" -> this.foodPreference = "veggie";
                }
            }
            case "meat" -> {
                switch (part2Pref) {
                    case "meat", "none" -> this.foodPreference = "meat";
                }
            }
            case "none" -> {
                switch (part2Pref) {
                    case "vegan" -> this.foodPreference = "vegan";
                    case "veggie" -> this.foodPreference = "veggie";
                    case "meat", "none" -> this.foodPreference = "meat";
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
            this.gender = "mixed";
        } else if (participant1.getSex().equals(participant2.getSex()) && participant1.getSex().equals("female")) {
            this.genderDiversityScore = 1;
            this.gender = "female";
        } else {
            this.genderDiversityScore = 0;
            this.gender = "male";
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


    public boolean isEqualTo(Pair pair) {
        if (participant1.getId().equals(pair.getParticipant1().getId()) || participant1.getId().equals(pair.getParticipant2().getId())) {
            return true;
        } else {
            return participant2.getId().equals(pair.getParticipant1().getId()) || participant2.getId().equals(pair.getParticipant2().getId());
        }
    }


    public Participant getParticipant1() {
        return participant1;
    }

    public Participant getParticipant2() {
        return participant2;
    }

    public String getFoodPreference() {
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

    public String getGender() {
        return gender;
    }

    @Override
    public int compareTo(Pair o) {
        return age.compareTo(o.age);
    }
}