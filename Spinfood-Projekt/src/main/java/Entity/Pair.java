package Entity;

public class Pair {
    private final Participant participant1;
    private final Participant participant2;
    private byte course;
    private byte ageDifference;
    private byte preferenceDeviation;
    private double genderDiversityScore;
    private Double[][] route;
    private final Double[] placeOfCooking = new Double[2];
    private String foodPreference;
    private int pathLength;


    public Pair(Participant participant1, Participant participant2) {
        this.participant1 = participant1;
        this.participant2 = participant2;

        decideFoodPreference();
        decidePlaceOfCooking();
        calculateAgeDifference();
        calculateGenderDiversityScore();
        calculatePreferenceDeviation();
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

    private void decideFoodPreference() {
        String part1Pref = participant1.getFoodPreference();
        String part2Pref = participant2.getFoodPreference();

        if (part1Pref.equals(part2Pref)) {
            this.foodPreference = part1Pref;
        } else if ((part1Pref.equals("meat") && part2Pref.equals("none")) || (part1Pref.equals("none") && part2Pref.equals("meat"))) {
            this.foodPreference = part1Pref;
        } else if (part1Pref.equals("meat") || part2Pref.equals("meat")) {
            if (part2Pref.equals("veggie") || part2Pref.equals("vegan")) {
                this.foodPreference = part2Pref;
            } else if (part1Pref.equals("veggie") || part1Pref.equals("vegan")) {
                this.foodPreference = part1Pref;
            }
        } else if (part1Pref.equals("veggie") && part2Pref.equals("vegan")) {
            this.foodPreference = part2Pref;
        } else if (part1Pref.equals("vegan") && part2Pref.equals("veggie")) {
            this.foodPreference = part1Pref;
        }
    }

    /**
     * Calculates the ageDifference of a pair.
     */
    private void calculateAgeDifference() {
       this.ageDifference = (byte) Math.abs(participant1.getAgeRange() - participant2.getAgeRange());
    }

    /**
     * Calculates the genderDiversityScore of a pair.
     */
    private void calculateGenderDiversityScore() {
        if (participant1.getSex().equals("female") || participant2.getSex().equals("female")) {
            this.genderDiversityScore = 0.5;
        } else {
            this.genderDiversityScore = 0;
        }
    }

    /**
     * Calculates the preferenceDeviation of a pair.
     */
    private void calculatePreferenceDeviation() {
        this.preferenceDeviation = (byte) Math.abs(participant1.getFoodPreferenceNumber() - participant2.getFoodPreferenceNumber());
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

    public byte getAgeDifference() {
        return ageDifference;
    }

    public byte getPreferenceDeviation() {
        return preferenceDeviation;
    }

    public int getPathLength() {
        return pathLength;
    }

    public double getGenderDiversityScore() {
        return genderDiversityScore;
    }

    public Double[] getPlaceOfCooking() {
        return placeOfCooking;
    }
}
