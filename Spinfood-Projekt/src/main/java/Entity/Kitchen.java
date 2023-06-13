package Entity;

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

    public boolean isEmergencyKitchen() {
        return isEmergencyKitchen;
    }

    public int getStory() {
        return story;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}
