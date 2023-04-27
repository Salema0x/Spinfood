package Entity;

public class Pair {
    private Participant participant1;
    private Participant participant2;
    private int course;
    private Double[][] route;
    private String foodPreference;
    private int ageDifference;
    private Double[][] placeOfCooking;
    private double genderDiversityScore;

    public Pair(Participant participant1, Participant participant2) {
        this.participant1 = participant1;
        this.participant2 = participant2;
    }

}
