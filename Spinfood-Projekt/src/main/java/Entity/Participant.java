package Entity;

public class Participant {
    private final String id;
    private final String name;
    private final String foodPreference;
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

    public Participant(String[] values, boolean isSuccessor) {
        this.id = values[1];
        this.name = values[2];
        this.foodPreference = values[3];
        this.age = Byte.parseByte(values[4]);
        this.sex = values[5];
        this.hasKitchen = values[6];

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
     * Initializes the values for the kitchen when coordinates are given.
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
     * @param longitude the longitude coordinate of the kitchen as string.
     * @param latitude the latitude coordinate of the kitchen as string.
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
     * @param foodPreference A String representing the foodPreference of the participant.
     */
    private void calculateFoodPreferenceNumber(String foodPreference) {
        if (foodPreference.equals("none") || foodPreference.equals("meat")) {
            this.foodPreferenceNumber = 0;
        } else if (foodPreference.equals("veggie")) {
            this.foodPreferenceNumber = 1;
        } else {
            this.foodPreferenceNumber = 2;
        }
    }

    /**
     * Depending on the age every participant is getting assigned to an age range.
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

    public String getHasKitchen() {
        return hasKitchen;
    }

    public String getFoodPreference() {
        return foodPreference;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getSex() {
        return sex;
    }

    public int getCountWg() {
        return countWg;
    }

    public int getFoodPreferenceNumber() {
        return foodPreferenceNumber;
    }

    public double getKitchenLongitude() {
        return kitchenLongitude;
    }

    public double getKitchenLatitude() {
        return kitchenLatitude;
    }

    public byte getKitchenStory() {
        return kitchenStory;
    }

    public byte getAge() {
        return age;
    }

    public int getAgeRange() {
        return ageRange;
    }

    public boolean isSuccessor() {
        return isSuccessor;
    }

    public void setSuccessor(boolean successor) {
        isSuccessor = successor;
    }

    public Participant getPartner() {
        return partner;
    }

    public void setPartner(Participant partner) {
        this.partner = partner;
    }

    public void setHasPartner(boolean hasPartner) {
        this.hasPartner = hasPartner;
    }

    public boolean hasPartner() {
        return hasPartner;
    }

    public void setCountWg(int countWg) {
        this.countWg += countWg;
    }

    public int getSexNumber() {
        return sexNumber;
    }
}