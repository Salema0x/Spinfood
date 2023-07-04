package Entity.Enum;

public enum Gender {
    male, female, other, mixed;
    public int asNumber() {
        switch (this) {
            case female: return 0;
            case male, other: return 1;
            default: return -1;
        }
    }
}
