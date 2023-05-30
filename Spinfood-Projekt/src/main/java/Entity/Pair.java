package Entity;

public class Pair {
    private final Participant participant1;
    private final Participant participant2;
    private byte meanAgeRange;
    private byte ageDifference;
    private byte preferenceDeviation;
    private double genderDiversityScore;
    private Double[][] route;
    private final Double[] placeOfCooking = new Double[2];
    private String foodPreference;
    private int foodPreferenceNumber;


    public Pair(Participant participant1, Participant participant2) {
        this.participant1 = participant1;
        this.participant2 = participant2;

        decideFoodPreference();
        parseFoodPreferenceToNumber();
        decidePlaceOfCooking();
        calculateAgeDifference();
        calculateGenderDiversityScore();
        calculatePreferenceDeviation();
        calculateMeanAge();

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

    /**
     * parses the food preference to a number ("for deviation calculation")
     */
    private void parseFoodPreferenceToNumber() {
        if (foodPreference.equals("none")) {
            foodPreferenceNumber = 0;
        } else if (foodPreference.equals("meat")) {
            foodPreferenceNumber = 1;
        } else if (foodPreference.equals("veggie")) {
            foodPreferenceNumber = 2;
        } else if (foodPreference.equals("vegan")) {
            foodPreferenceNumber = 3;
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


    /**
     * Calculates the meanAgeRange of a pair.
     */
    private void calculateMeanAge() {
        double meanAge = ((double) (participant1.getAgeRange() + participant2.getAgeRange()))/ 2;
        if (meanAge >= 0 && meanAge <= 17) {
            this.meanAgeRange = 0;
        } else if (meanAge >= 18 && meanAge <= 23) {
            this.meanAgeRange = 1;
        } else if (meanAge >= 24 && meanAge <= 27) {
            this.meanAgeRange = 2;
        } else if (meanAge >= 28 && meanAge <= 30) {
            this.meanAgeRange = 3;
        } else if (meanAge >= 31 && meanAge <= 35) {
            this.meanAgeRange = 4;
        } else if (meanAge >= 36 && meanAge <= 41) {
            this.meanAgeRange = 5;
        } else if (meanAge >= 42 && meanAge <= 46) {
            this.meanAgeRange = 6;
        } else if (meanAge >= 47 && meanAge <= 56) {
            this.meanAgeRange = 7;
        } else {
            this.meanAgeRange = 8;
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

    public double getGenderDiversityScore() {
        return genderDiversityScore;
    }

    public Double[] getPlaceOfCooking() {
        return placeOfCooking;
    }

    public int getFoodPreferenceNumber() {
        return foodPreferenceNumber;
    }

    public byte getMeanAgeRange() {
        return meanAgeRange;
}
