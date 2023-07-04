package Misc;

import Entity.Participant;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Class to implement the Comparator interface, which is used when sorting the list of Participants in the PairListFactory.
 * @author David Krell
 */
public class ParticipantComparator implements Comparator<Participant> {

    private final Function<Participant, Integer> firstMethod;
    private final Function<Participant, Integer> secondMethod;
    private final Function<Participant, Integer> thirdMethod;
    private final boolean sexUp;
    private final int sexFunctionIndex;

    @SafeVarargs
    public ParticipantComparator(boolean sexUp, int sexFunctionIndex, Function<Participant, Integer>... methods) {
        this.firstMethod = methods[0];
        this.secondMethod = methods[1];
        this.thirdMethod = methods[2];

        this.sexUp = sexUp;
        this.sexFunctionIndex = sexFunctionIndex;
    }

    @Override
    public int compare(Participant participant1, Participant participant2) {
        if (sexUp) {
            return compareHelper(participant1, participant2, thirdMethod.apply(participant1), thirdMethod.apply(participant2));
        } else if (sexFunctionIndex == 0) {
            return compareHelper2(participant2, participant1, thirdMethod.apply(participant1), secondMethod.apply(participant2));
        } else if (sexFunctionIndex == 1) {
            return compareHelper2(participant1, participant2, thirdMethod.apply(participant1), thirdMethod.apply(participant2));
        } else {
            return compareHelper(participant1, participant2, thirdMethod.apply(participant2), thirdMethod.apply(participant1));
        }
    }

    private int compareHelper2(Participant participant1, Participant participant2, Integer apply, Integer apply2) {
        return compareHelperBig(participant1, participant2, apply, apply2, secondMethod.apply(participant2), secondMethod.apply(participant1));
    }

    private int compareHelperBig(Participant participant1, Participant participant2, Integer apply, Integer apply2, Integer apply3, Integer apply4) {
        int result;
        result = firstMethod.apply(participant1).compareTo(firstMethod.apply(participant2));

        if (result == 0) {
            result = apply3.compareTo(apply4);
        }

        if (result == 0) {
            result = apply.compareTo(apply2);
        }

        return result;
    }

    private int compareHelper(Participant participant1, Participant participant2, Integer apply, Integer apply2) {
        return compareHelperBig(participant1, participant2, apply, apply2, secondMethod.apply(participant1), secondMethod.apply(participant2));
    }
}
