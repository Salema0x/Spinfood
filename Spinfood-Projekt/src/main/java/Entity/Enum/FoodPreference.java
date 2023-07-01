package Entity.Enum;

public enum FoodPreference {
    NONE, MEAT, VEGAN, VEGGIE;

    /**
     * Returns the food preference as a number
     * @return
     */
    public int asNumber() {
        switch(this) {
            case NONE, MEAT: return 0;
            case VEGGIE: return 1;
            case VEGAN: return 2;
            default: return -1;
        }
    }
}
