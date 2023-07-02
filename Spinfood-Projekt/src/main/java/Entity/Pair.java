package Entity;

import Entity.Enum.FoodPreference;
import Entity.Enum.Gender;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;


@JsonPropertyOrder({"premade", "foodPreference", "firstParticipant", "secondParticipant"})
public class Pair implements Comparable<Pair> {

    //PairAttributes
    private final Participant participant1;
    private final Participant participant2;
    private final String id;
    private final boolean preMade;
    private Double age;
    private Gender gender;
    private FoodPreference foodPreference;
    private Kitchen kitchen;
    private final Double[] coordinatesFirstRound = new Double[2];
    private final Double[] coordinatesSecondRound = new Double[2];
    private final Double[] coordinatesThirdRound = new Double[2];
    private double distanceToPartyLocation;
    private ArrayList<Pair> seen = new ArrayList<>();

    //IndexNumbers
    private double ageDifference;
    private double preferenceDeviation;
    private double genderDiversityScore;





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
        this.preMade = false;


        initializePairAttributes();
        initializeIndexNumbers();
    }

    /**
     * Constructor with preMade option
     *
     * @param participant1
     * @param participant2
     * @param preMade
     */
    public Pair(Participant participant1, Participant participant2, boolean preMade) {
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.id = participant1.getId();
        this.preMade = preMade;


        initializePairAttributes();
        initializeIndexNumbers();
    }

    /**
     * Method to initialize the Attributes of the Pair
     */
    private void initializePairAttributes() {
        //decides witch Kitchen is used
        if (participant1.getHasKitchen().equals("yes")) {
            kitchen = participant1.getKitchen();
        } else if (participant2.getHasKitchen().equals("yes")) {
            kitchen = participant2.getKitchen();
        } else if (participant1.getHasKitchen().equals("maybe")) {
            kitchen = participant1.getKitchen();
        } else if (participant2.getHasKitchen().equals("maybe")) {
            kitchen = participant2.getKitchen();
        }

        //calculates the foodPreference of the Pair
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

        //sets the Gender of the Pair
        if (!participant1.getGender().equals(participant2.getGender()) || !participant2.getGender().equals(participant1.getGender())) {
            this.gender = Gender.mixed;
        } else if (participant1.getGender().equals(participant2.getGender()) && participant1.getGender().equals(Gender.female)) {
            this.gender = Gender.female;
        } else {
            this.gender = Gender.male;
        }

        //calculates the Age of the pair
        this.age = (double) (participant1.getAge() + participant2.getAge()) / 2;

    }

    /**
     * Method to initialize the IndexNumbers of the Pair
     */
    private void initializeIndexNumbers() {
        //sets the ageDifference IndexNumber
        this.ageDifference = Math.abs(participant1.getAgeRange() - participant2.getAgeRange());

        //sets the genderDiversityScore IndexNumber
        switch(gender) {
            case mixed -> genderDiversityScore = 0.5;
            case female -> genderDiversityScore = 1;
            case male -> genderDiversityScore = 0;
            default -> genderDiversityScore = -1;
        }

        //sets the preferenceDeviation IndexNumber
        preferenceDeviation = Math.abs(participant1.getFoodPreferenceNumber() - participant2.getFoodPreferenceNumber());

    }


    //Utility-Methods
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
    public Double[] getCoordinatesFirstRound() {
        return coordinatesFirstRound;
    }
    @JsonIgnore
    public Double[] getCoordinatesSecondRound() {
        return coordinatesSecondRound;
    }
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
    public ArrayList<Pair> getSeen() {
        return seen;
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
}

