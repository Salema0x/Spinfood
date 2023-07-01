package Entity;

import java.util.Objects;
import Enum.*;

import Entity.Enum.FoodPreference;
import Entity.Enum.Gender;

/**
 * This class is used to define pack the attributes food preference and gender of a pair together.
 * This is done for the group algorithm.
 * @author David Krell
 */
public class PairAttributes {
    private final FoodPreference foodPreference;
    private final Gender gender;

    public PairAttributes(Pair pair) {
        this.foodPreference = pair.getFoodPreference();
        this.gender = pair.getGender();
    }

    public PairAttributes(FoodPreference foodPreference, Gender gender) {
        this.foodPreference = foodPreference;
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PairAttributes that = (PairAttributes) o;
        return this.foodPreference == that.foodPreference && this.gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodPreference, gender);
    }

}
