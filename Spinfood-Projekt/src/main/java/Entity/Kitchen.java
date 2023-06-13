package Entity;

public class Kitchen {
    private boolean isEmergencyKitchen;
    private int story;
    private double longitude;
    private double latitude;
    private boolean noKitchen;

    public Kitchen(boolean isEmergencyKitchen, int story, Double[] coordinates) {
        this.isEmergencyKitchen = isEmergencyKitchen;
        this.story = story;
        this.longitude = coordinates[0];
        this.latitude = coordinates[1];
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
