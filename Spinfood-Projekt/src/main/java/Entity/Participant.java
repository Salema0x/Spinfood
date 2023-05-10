package Entity;

public class Participant {
    private final String id;
    private final String name;
    private final String foodPreference;
    private final byte age;
    private final String sex;
    private final String hasKitchen;
    private byte kitchenStory;
    private double kitchenLongitude;
    private double kitchenLatitude;
    private boolean isSuccessor;
    private int count_wg;
    private String agerange;

    public Participant(String[] values, boolean isSuccessor) {
        this.id = values[1];
        this.name = values[2];
        this.foodPreference = values[3];
        this.age = Byte.parseByte(values[4]);
        this.sex = values[5];
        this.hasKitchen = values[6];

        boolean sizeIsSeven = values.length == 7;
        boolean sizeIsEight = values.length == 8;

        if (sizeIsSeven || sizeIsEight) {
            this.kitchenLatitude = -1;
            this.kitchenLongitude = -1;

            if (sizeIsSeven) {
                this.kitchenStory = 0;
            } else {
                if (values[7].isEmpty()) {
                    this.kitchenStory = 0;
                } else {
                    this.kitchenStory = Byte.parseByte(String.valueOf((int) Double.parseDouble(values[7])));
                }
            }

        } else if (values.length >= 10) {
            if (values[7].isEmpty()) {
                this.kitchenStory = 0;
                this.kitchenLongitude = -1;
                this.kitchenLatitude = -1;
            } else if (values[8].isEmpty() && values[9].isEmpty()) {
                this.kitchenLatitude = -1;
                this.kitchenLongitude = -1;
            } else {
                this.kitchenStory = Byte.parseByte(String.valueOf((int) Double.parseDouble(values[7])));
                this.kitchenLongitude = Double.parseDouble(values[8]);
                this.kitchenLatitude = Double.parseDouble(values[9]);
            }
        }

        this.isSuccessor = isSuccessor;
    }

    /**
     * Checks if two participants are the same.
     * @param participant the participant to which should be compared.
     * @return a boolean indicating if the participants are equal or not.
     */
    public boolean equals(Participant participant) {
        return this.id.equals(participant.getId());
    }

    public String getHasKitchen() {
        return hasKitchen;
    }

    public String getFoodPreference() {
        return foodPreference;
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

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public byte getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public boolean isSuccessor() {
        return isSuccessor;
    }

    public void setSuccessor(boolean successor) {
        isSuccessor = successor;
    }
}

