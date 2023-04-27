package Entity;

public class Participant {
    private String id;
    private String name;
    private String foodPreference;
    private byte age;
    private String sex;
    private String hasKitchen;
    private byte kitchenStory;
    private double kitchenLongitude;
    private double kitchenLatitude;

    public Participant(String[] values) {
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
            } else {
                this.kitchenStory = Byte.parseByte(String.valueOf((int) Double.parseDouble(values[7])));
            }
            this.kitchenLongitude = Double.parseDouble(values[8]);
            this.kitchenLatitude = Double.parseDouble(values[9]);
        }
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
}

