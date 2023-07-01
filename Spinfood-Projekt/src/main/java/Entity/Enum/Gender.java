package Entity.Enum;

public enum Gender {
    MALE, FEMALE, OTHER, MIXED;
    public int asNumber() {
        switch (this) {
            case FEMALE : return 0;
            case MALE, OTHER : return 1;
            default: return -1;
        }
    }
}
