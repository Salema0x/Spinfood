package Entity.Enum;

public enum FoodPreference {
    none, meat, vegan, veggie;

    /**
     * Returns the food preference as a number
     * @return
     */
    public int asNumber() {
        switch(this) {
            case none, meat: return 0;
            case veggie: return 1;
            case vegan: return 2;
            default: return -1;
        }
    }
}
