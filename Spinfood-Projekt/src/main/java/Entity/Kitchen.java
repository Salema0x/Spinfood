package Entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"emergencyKitchen", "story", "longitude", "latitude"})
public class Kitchen {
    private boolean isEmergencyKitchen;
    private int story;
    private double longitude;
    private double latitude;
    private boolean noKitchen;

    public Kitchen(boolean isEmergencyKitchen, int story, double longitude, double latitude) {
        this.isEmergencyKitchen = isEmergencyKitchen;
        this.story = story;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Kitchen() {
        this.noKitchen = true;
    }





    //Getter
    @JsonIgnore
    public boolean getIsNoKitchen() {
        return noKitchen;
    }

    //JsonGetter
    @JsonGetter("emergencyKitchen")
    public boolean isEmergencyKitchen() {
        return isEmergencyKitchen;
    }
    @JsonGetter("story")
    public int getStory() {
        return story;
    }
    @JsonGetter("longitude")
    public double getLongitude() {
        return longitude;
    }
    @JsonGetter("latitude")
    public double getLatitude() {
        return latitude;
    }

    //Setter


}
