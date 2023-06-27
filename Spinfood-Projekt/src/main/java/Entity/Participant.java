package Entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import Entity.Enum.FoodPreference;

@JsonPropertyOrder({"id", "name", "foodPreference", "age", "gender", "kitchen"})
public class Participant {
    private final String id;
    private final String name;
    private final FoodPreference foodPreference;
    private final String sex;
    private final String hasKitchen;
    private final byte age;
    private byte kitchenStory;
    private int ageRange;
    private double kitchenLongitude;
    private double kitchenLatitude;
    private int countWg = 1;
    private int foodPreferenceNumber;
    private int sexNumber;
    private boolean isSuccessor;
    private Participant partner = null;
    private boolean hasPartner = false;
    private Kitchen kitchen;

    public Participant(String[] values, boolean isSuccessor) {
        this.id = values[1];
        this.name = values[2];

        //Initialize food preference
        if(values[3].equals("meat")) {
            this.foodPreference = FoodPreference.MEAT;
        }
        else if(values[3].equals("veggie")) {
            this.foodPreference = FoodPreference.VEGGIE;
        }
        else if(values[3].equals("vegan")) {
            this.foodPreference = FoodPreference.VEGAN;
        }
        else {
            this.foodPreference = FoodPreference.NONE;
        }

        this.age = Byte.parseByte(values[4]);
        this.sex = values[5];
        this.hasKitchen = values[6];

        //avoid index out of bound exception when no kitchen is given
        if (values.length <= 8) {
            initializeKitchenValues();
        } else {
            initializeKitchenValues(values);
        }


        if (values.length <= 8) {
            handleSmallKitchenValues(values);
        } else {
            handleFullKitchenValues(values);
        }

        this.isSuccessor = isSuccessor;

        calculateFoodPreferenceNumber(foodPreference);

        calculateAgeRangeNumber(age);

        calculateSexNumber(sex);

    }


    /**
     * initializes the kitchen Filed of the participant
     *
     * @param values String array representing all the values of the participant which have been read in. (hasKitchen, story, longitude, latitude)
     */
    private void initializeKitchenValues(String[] values) {
        int story;
        double longitude;
        double latitude;

        //set story to 0 if no story is given
        if (values[7].isEmpty()) {
            story = 0;
        } else {
            story = (int) Double.parseDouble(values[7]);
        }

        //set coordinates to -1 if no coordinates are given
        if (values[8].isEmpty() || values[9].isEmpty()) {
            longitude = -1;
            latitude = -1;
        } else {
            longitude = Double.parseDouble(values[8]);
            latitude = Double.parseDouble(values[9]);
        }

        //set maybeKitchen and generate the kitchenObj
        if (values[0].equals("yes")) {
            this.kitchen = new Kitchen(false, story, longitude, latitude);
        } else {
            this.kitchen = new Kitchen(true, story, longitude, latitude);
        }


    }

    /**
     * initializes the kitchen Filed of the participant with a empty kitchen
     */
    private void initializeKitchenValues() {
        this.kitchen = new Kitchen();
    }

    /**
     * Initializes the values for the kitchen when coordinates are given.
     *
     * @param values String array representing all the values of the participant which have been read in.
     */
    private void handleFullKitchenValues(String[] values) {
        if (values[7].isEmpty()) {
            this.kitchenStory = 0;
            handleKitchenCoordinates(values[8], values[9]);
        } else {
            this.kitchenStory = Byte.parseByte(String.valueOf((int) Double.parseDouble(values[7])));
            handleKitchenCoordinates(values[8], values[9]);
        }
    }

    /**
     * Initializes the values of the kitchen when no coordinates are given.
     *
     * @param values String array representing all the values of the participant which have been read in.
     */
    private void handleSmallKitchenValues(String[] values) {
        if (values.length == 7) {
            this.kitchenStory = 0;
        } else {
            this.kitchenStory = Byte.parseByte(String.valueOf((int) Double.parseDouble(values[7])));
        }

        handleKitchenCoordinates("", "");
    }

    /**
     * Initializes the coordinates of the kitchen, to either default values or the exact coordinates.
     *
     * @param longitude the longitude coordinate of the kitchen as string.
     * @param latitude  the latitude coordinate of the kitchen as string.
     */
    private void handleKitchenCoordinates(String longitude, String latitude) {
        if (longitude.isEmpty()) {
            this.kitchenLongitude = -1.0;
            this.kitchenLatitude = -1.0;
        } else {
            this.kitchenLongitude = Double.parseDouble(longitude);
            this.kitchenLatitude = Double.parseDouble(latitude);
        }
    }

    /**
     * Generates a number according to the foodPreference of the participant.
     *
     * @param foodPreference A String representing the foodPreference of the participant.
     */
    private void calculateFoodPreferenceNumber(FoodPreference foodPreference) {
        switch(foodPreference) {
            case MEAT:
                this.foodPreferenceNumber = 1;
                break;
            case VEGGIE:
                this.foodPreferenceNumber = 2;
                break;
            case VEGAN:
                this.foodPreferenceNumber = 3;
                break;
            default:
                this.foodPreferenceNumber = 0;
                break;
        }
    }

    /**
     * Depending on the age every participant is getting assigned to an age range.
     *
     * @param age the age of the participant.
     */
    private void calculateAgeRangeNumber(byte age) {
        if (age >= 0 && age <= 17) {
            this.ageRange = 0;
        } else if (age >= 18 && age <= 23) {
            this.ageRange = 1;
        } else if (age >= 24 && age <= 27) {
            this.ageRange = 2;
        } else if (age >= 28 && age <= 30) {
            this.ageRange = 3;
        } else if (age >= 31 && age <= 35) {
            this.ageRange = 4;
        } else if (age >= 36 && age <= 41) {
            this.ageRange = 5;
        } else if (age >= 42 && age <= 46) {
            this.ageRange = 6;
        } else if (age >= 47 && age <= 56) {
            this.ageRange = 7;
        } else {
            this.ageRange = 8;
        }
    }

    private void calculateSexNumber(String sex) {
        switch (sex) {
            case "female" -> this.sexNumber = 0;
            case "male", "other" -> this.sexNumber = 1;
        }
    }

    /**
     * Checks if two participants are the same.
     *
     * @param participant the participant to which should be compared.
     * @return a boolean indicating if the participants are equal or not.
     */
    public boolean equals(Participant participant) {
        return this.id.equals(participant.getId());
    }


    /**
     * Increases the countWg from the participant if a member of his wg has registered as well.
     */
    public void increaseCountWG() {
        this.countWg++;
    }

    //Getter
    @JsonIgnore
    public String getHasKitchen() {
        return hasKitchen;
    }

    @JsonIgnore
    public int getCountWg() {
        return countWg;
    }
    @JsonIgnore
    public int getFoodPreferenceNumber() {
        return foodPreferenceNumber;
    }
    @JsonIgnore
    public double getKitchenLongitude() {
        return kitchenLongitude;
    }
    @JsonIgnore
    public double getKitchenLatitude() {
        return kitchenLatitude;
    }
    @JsonIgnore
    public byte getKitchenStory() {
        return kitchenStory;
    }

    @JsonIgnore
    public int getAgeRange() {
        return ageRange;
    }

    @JsonIgnore
    public Participant getPartner() {
        return partner;
    }

    @JsonIgnore
    public boolean getHasPartner() {
        return hasPartner;
    }

    @JsonIgnore
    public int getSexNumber() {
        return sexNumber;
    }
    @JsonIgnore
    public boolean getIsSuccessor() {
        return isSuccessor;
    }


    //jsonGetter
    @JsonGetter("id")
    public String getId() {
        return id;
    }
    @JsonGetter("name")
    public String getName() {
        return name;
    }
    @JsonGetter("foodPreference")
    public FoodPreference getFoodPreference() {
        return foodPreference;
    }
    @JsonGetter("age")
    public byte getAge() {
        return age;
    }
    @JsonGetter("gender")
    public String getSex() {
        return sex;
    }
    @JsonGetter("kitchen")
    public Kitchen getKitchen() {
        return kitchen;
    }

    //Setter
    public void setCountWg(int countWg) {
        this.countWg += countWg;
    }

    public void setHasPartner(boolean hasPartner) {
        this.hasPartner = hasPartner;
    }
    public void setPartner(Participant partner) {
        this.partner = partner;
    }

    public void setSuccessor(boolean successor) {
        isSuccessor = successor;
    }
}