package Entity;

public class Pair {
    private final Participant participant1;
    private final Participant participant2;
    private byte course;
    private byte ageDifference;
    private byte preferenceDeviation;
    private double genderDiversityScore;
    private Double[][] route;
    private Double[][] placeOfCooking;
    private String foodPreference;
    private int pathLength;


    public Pair(Participant participant1, Participant participant2) {
        this.participant1 = participant1;
        this.participant2 = participant2;

        this.foodPreference = decideFoodPreference();

        calculateAgeDifference();
        calculateGenderDiversityScore();
        calculatePreferenceDeviation();
    }

    private String decideFoodPreference() {
        return "";
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

    public byte getAgeDifference() {
        return ageDifference;
    }

    public byte getPreferenceDeviation() {
        return preferenceDeviation;
    }

    public double getGenderDiversityScore() {
        return genderDiversityScore;
    }

    public Double[][] getPlaceOfCooking() {
        return placeOfCooking;
    }

    public void setPlaceOfCooking(Double[][] placeOfCooking) {
        this.placeOfCooking = placeOfCooking;
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
}
