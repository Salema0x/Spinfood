package Factory;

import Entity.Pair;
import Entity.Group;

import java.util.*;

public class GroupFactory {
    private final List<Pair> registeredPairs;
    private final int maxGroupSize;
    private double[][] partyLocation;
    private final List<Group> groups;

    /**
     * Constructor for GroupFactory. It initializes the registeredPairs and partyLocation.
     *
     * @param pairListFactory the list of pairs registered for the event from the PairListFactoryClass
     * @param partyLocation the location of the party
     */
    public GroupFactory(PairListFactory pairListFactory, int maxGroupSize, double[][] partyLocation) {
        this.registeredPairs = pairListFactory.getRegisteredPairs();
        this.maxGroupSize = maxGroupSize;
        this.partyLocation = partyLocation;
        this.groups = new ArrayList<>();
    }

    /**
     * This method generates the groups based on the provided list of pairs and the location of the party.
     * It tries to ensure that the pairs in the same group live close to each other and that the total distance traveled by all pairs in a group is as small as possible.
     *
     * @return a list of groups
     */
    public List<Group> createGroups() {
        while (!registeredPairs.isEmpty()) {
            Pair initialPair = registeredPairs.remove(0);
            Group group = new Group(initialPair);
            while (group.getPairs().size() < maxGroupSize) {
                Pair closestPair = findClosestPair(group);
                if (closestPair != null) {
                    group.addPair(closestPair);
                    registeredPairs.remove(closestPair);
                } else {
                    break;
                }
            }
            groups.add(group);
        }
        return groups;
    }

    /**
     * This method finds the closest Pair to a given Group based on a set of criteria like geographical distance,
     * age difference, preference deviation, and gender diversity score.
     *
     * @param group The group to which we want to find the closest Pair
     * @return The closest Pair to the Group
     */
    private Pair findClosestPair(Group group) {
        Pair closestPair = null;
        double smallestDistance = Double.MAX_VALUE;
        for (Pair pair : registeredPairs) {
            double distance = calculateGroupPairDeviation(group, pair);
            if (distance < smallestDistance) {
                smallestDistance = distance;
                closestPair = pair;
            }
        }
        return closestPair;
    }

    /**
     * This method calculates the total deviation between a Pair and a Group.
     * The deviation is based on a set of criteria like geographical distance, age difference,
     * preference deviation, and gender diversity score.
     *
     * @param group The group for which we want to calculate the distance
     * @param pair The Pair for which we want to calculate the distance
     * @return The total deviation between the Pair and the Group
     */
    private double calculateGroupPairDeviation(Group group, Pair pair) {
        double deviation = 0;
        for (Pair groupPair : group.getPairs()) {
            deviation += calculatePairDeviation(groupPair, pair);
        }
        return deviation / group.getPairs().size();
    }

    /**
     * This method calculates the deviation between two Pairs based on a set of criteria like geographical distance,
     * age difference, preference deviation, and gender diversity score.
     *
     * @param pair1 The first Pair
     * @param pair2 The second Pair
     * @return The deviation between the two Pairs
     */
    private double calculatePairDeviation(Pair pair1, Pair pair2) {
        double geographicalDistance = calculateGeographicalDistance(pair1.getPlaceOfCooking(), pair2.getPlaceOfCooking());
        double ageDifference = Math.abs(pair1.getAgeDifference() - pair2.getAgeDifference());
        double preferenceDeviation = Math.abs(pair1.getPreferenceDeviation() - pair2.getPreferenceDeviation());
        double genderDiversityScore = Math.abs(pair1.getGenderDiversityScore() - pair2.getGenderDiversityScore());
        return geographicalDistance + ageDifference + preferenceDeviation + genderDiversityScore;
    }

    /**
     * This method calculates the geographical distance between two places.
     *
     * @param place1 The coordinates of the first place
     * @param place2 The coordinates of the second place
     * @return The geographical distance between the two places
     */
    private double calculateGeographicalDistance(Double[][] place1, Double[][] place2) {
        double latitudeDifference = Math.toRadians(place2[0][0] - place1[0][0]);
        double longitudeDifference = Math.toRadians(place2[0][1] - place1[0][1]);

        double a = Math.pow(Math.sin(latitudeDifference / 2), 2)
                + Math.cos(Math.toRadians(place1[0][0])) * Math.cos(Math.toRadians(place2[0][0]))
                * Math.pow(Math.sin(longitudeDifference / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Radius of the earth in kilometers
        final int EARTH_RADIUS = 6371;

        return EARTH_RADIUS * c;
    }
}