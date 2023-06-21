package Enum;

public enum FoodPreference {
    NONE, MEAT, VEGAN, VEGGIE;

    /**
     * Returns the food preference as a number
     * @return
     */
    public int asNumber() {
        switch(this) {
            case NONE: return 0;
            case MEAT: return 1;
            case VEGGIE: return 2;
            case VEGAN: return 3;
            default: return -1;
        }
    }
}
