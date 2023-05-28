package Factory;

import Entity.Pair;
import Entity.Group;

import java.util.*;

public class GroupFactory {
    private final List<Pair> registeredPairs;
    private final List<Pair> successorList;
    private final int maxGroupSize;
    private Double[] partyLocation;
    private final List<Group> groups;

    /**
     * Constructor for GroupFactory. It initializes the registeredPairs and partyLocation.
     *
     * @param pairListFactory the list of pairs registered for the event from the PairListFactoryClass
     * @param partyLocation the location of the party
     */
    public GroupFactory(PairListFactory pairListFactory, int maxGroupSize, Double[] partyLocation) {
        this.registeredPairs = pairListFactory.getRegisteredPairs();
        this.successorList = new ArrayList<>();
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
            if (group.getPairs().size() < 3) {
                successorList.addAll(group.getPairs());
            } else {
                groups.add(group);
            }
        }
        return groups;
    }

    public void showGroups(List<Group> groups) {
        String leftAlignFormat = "%-9s| %-36s | %-36s | %-15s | %-15s | %-15s | %-15s |%n";
        int groupNr = 0;

        System.out.format("+---------|--------------------------------------+--------------------------------------+-----------------+-----------------+-----------------+-----------------+%n");
        System.out.format("|Group Nr | ID1                                  | ID2                                  | Name1           | Name2           | Pref1           | Pref2           |%n");
        System.out.format("+---------|--------------------------------------+--------------------------------------+-----------------+-----------------+-----------------+-----------------+%n");

        for (Group group: groups) {
            groupNr++;
            int pairNr = 0;
            for (Pair pair : group.getPairs()) {
                String id1 = pair.getParticipant1().getId();
                String id2 = pair.getParticipant2().getId();
                String name1 = pair.getParticipant1().getName();
                String name2 = pair.getParticipant2().getName();
                String pref1 = pair.getParticipant1().getFoodPreference();
                String pref2 = pair.getParticipant2().getFoodPreference();
                pairNr++;

                System.out.format(leftAlignFormat, "Group "+groupNr+" Pair "+pairNr, id1, id2, name1, name2, pref1, pref2);
            }
        }

        System.out.format("+---------|--------------------------------------+--------------------------------------+-----------------+-----------------+-----------------+-----------------+%n");
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

        // Check if the group already contains a pair with foodPreference "0" (Fleischi), "meat" or "none"
        boolean groupContainsNonVegPreference = group.getPairs().stream().anyMatch(pair -> pair.getParticipant1().getFoodPreference().equals("0")
                || pair.getParticipant1().getFoodPreference().toLowerCase().equals("meat")
                || pair.getParticipant1().getFoodPreference().toLowerCase().equals("none")
                || pair.getParticipant2().getFoodPreference().equals("0")
                || pair.getParticipant2().getFoodPreference().toLowerCase().equals("meat")
                || pair.getParticipant2().getFoodPreference().toLowerCase().equals("none"));

        for (Pair pair : registeredPairs) {
            // If the group already contains a pair with foodPreference "0", "meat" or "none" and the current pair also has foodPreference "0", "meat" or "none", skip this pair
            if (groupContainsNonVegPreference && (pair.getParticipant1().getFoodPreference().equals("0")
                    || pair.getParticipant1().getFoodPreference().toLowerCase().equals("meat")
                    || pair.getParticipant1().getFoodPreference().toLowerCase().equals("none")
                    || pair.getParticipant2().getFoodPreference().equals("0")
                    || pair.getParticipant2().getFoodPreference().toLowerCase().equals("meat")
                    || pair.getParticipant2().getFoodPreference().toLowerCase().equals("none"))) {
                continue;
            }

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
    private double calculateGeographicalDistance(Double[] place1, Double[] place2) {
        double latitudeDifference = Math.toRadians(place2[0] - place1[0]);
        double longitudeDifference = Math.toRadians(place2[1] - place1[1]);

        double a = Math.pow(Math.sin(latitudeDifference / 2), 2)
                + Math.cos(Math.toRadians(place1[0])) * Math.cos(Math.toRadians(place2[0]))
                * Math.pow(Math.sin(longitudeDifference / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Radius of the earth in kilometers
        final int EARTH_RADIUS = 6371;

        return EARTH_RADIUS * c;
    }
}