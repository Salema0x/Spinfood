package Factory.Group;

import Entity.Pair;
import Misc.PairDistanceComparator;
import java.util.ArrayList;

public class RingFactory {
    private final ArrayList<Pair> pairList;
    private final ArrayList<Pair> successorPairs;
    private final ArrayList<Pair> outerRing;
    private final ArrayList<Pair> middleRing;
    private final ArrayList<Pair> innerRing;
    private final Double[] PARTY_LOCATION;

    public RingFactory(ArrayList<Pair> pairList, Double[] partyLocation) {
        this.pairList = pairList;
        this.successorPairs = new ArrayList<>();
        this.PARTY_LOCATION = partyLocation;

        ArrayList<ArrayList<Pair>> rings = makeRings();

        this.outerRing = rings.get(0);
        this.middleRing = rings.get(1);
        this.innerRing = rings.get(2);
    }

    /**
     * Splits the original list of pairs into three rings.
     * <ul>
     *     <li>Outer Ring: the pairs which are the farthest away from the party location</li>
     *     <li>Middle Ring: the pairs which are between the outer ring and inner ring</li>
     *     <li>Inner Ring: the pairs which are the nearest to the party location</li>
     * </ul>
     */
    private ArrayList<ArrayList<Pair>> makeRings() {
        setDistancesOfAllPairs();

        ArrayList<Pair> sortedPairList = sortPairListAccordingToDistances();
        ArrayList<ArrayList<Pair>> rings = extractRings(sortedPairList);

        trimRings(rings);

        return rings;
    }

    /**
     * Calculates the distance from all pairs to the party location using the harvesine formula.
     */
    private void setDistancesOfAllPairs() {
        for (Pair pair : pairList) {

            double startLatitude = pair.getPlaceOfCooking()[0];
            double startLongitude = pair.getPlaceOfCooking()[1];

            double endLatitude = PARTY_LOCATION[0];
            double endLongitude = PARTY_LOCATION[1];

            double distance = calculateDistance(startLatitude, startLongitude, endLatitude, endLongitude);
            pair.setDistanceToPartyLocation(distance);
        }
    }

    /**
     * Sorts the pair List according to the distances to the party location.
     * @return a copy of the pair list sorted in descending order, according to the distances to the party location.
     */
    private ArrayList<Pair> sortPairListAccordingToDistances() {
        ArrayList<Pair> pairListCopy = new ArrayList<>(pairList);
        pairListCopy.sort(new PairDistanceComparator());
        return pairListCopy;
    }

    /**
     * Takes a list of pairs and extracts the outer, middle and inner ring from it.
     * @param sortedPairList the list of pairs sorted in descending order, according to the distances to the party location.
     * @return a list containing three lists: outer ring(index = 0), middle ring (index = 1) & inner ring (index = 2)
     */
    private ArrayList<ArrayList<Pair>> extractRings(ArrayList<Pair> sortedPairList) {
        ArrayList<ArrayList<Pair>> rings = new ArrayList<>();

        int listSize = sortedPairList.size();
        int mod = listSize % 3;
        int div = listSize / 3;

        int leftSeparator = div + (mod > 0 ? 1 : 0);
        int rightSeparator = leftSeparator + div;

        ArrayList<Pair> outerRing = new ArrayList<>(sortedPairList.subList(0, leftSeparator));
        ArrayList<Pair> middleRing = new ArrayList<>(sortedPairList.subList(leftSeparator, rightSeparator));
        ArrayList<Pair> innerRing = new ArrayList<>(sortedPairList.subList(rightSeparator, listSize));

        rings.add(outerRing);
        rings.add(middleRing);
        rings.add(innerRing);

        return rings;
    }

    /**
     * Makes the size of the rings equal by removing pairs from the rings and adding them to the successor list.
     * @param rings an ArrayList containing the rings.
     */
    private void trimRings(ArrayList<ArrayList<Pair>> rings) {
        ArrayList<Pair> outerRing = rings.get(0);
        ArrayList<Pair> middleRing = rings.get(1);
        ArrayList<Pair> innerRing = rings.get(2);

        int sizeOuterRing = outerRing.size();
        int sizeMiddleRing = middleRing.size();
        int sizeInnerRing = innerRing.size();

        if (sizeOuterRing != sizeMiddleRing || sizeOuterRing != sizeInnerRing) {
            int maxSize = Math.max(Math.max(sizeOuterRing, sizeMiddleRing), sizeInnerRing);

            if (sizeOuterRing == maxSize) {
                successorPairs.add(outerRing.remove(0));
            }

            if (sizeMiddleRing == maxSize) {
                successorPairs.add(middleRing.remove(0));
            }

            if (sizeInnerRing == maxSize) {
                successorPairs.add(innerRing.remove(sizeInnerRing - 1));
            }
        }
    }

    /**
     * Calculates the distance in meter from one point to another using the harvesine formula.
     * @param startLatitude The latitude coordinates from the starting point.
     * @param startLongitude The longitude coordinates from the starting point.
     * @param endLatitude The latitude coordinates from the end point.
     * @param endLongitude The longitude coordinates from the end point.
     * @return a double representing the distance between the two points.
     */
    public static double calculateDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        final int RADIUS_EARTH = 6731;

        double latitudeDistance = Math.toRadians(endLatitude - startLatitude);
        double longitudeDistance = Math.toRadians(endLongitude - startLongitude);

        double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
                + Math.cos(Math.toRadians(startLatitude)) * Math.cos(Math.toRadians(endLatitude))
                * Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIUS_EARTH * c;
    }

    public ArrayList<Pair> getSuccessorPairs() {
        return successorPairs;
    }

    public ArrayList<Pair> getOuterRing() {
        return outerRing;
    }

    public ArrayList<Pair> getMiddleRing() {
        return middleRing;
    }

    public ArrayList<Pair> getInnerRing() {
        return innerRing;
    }
}
