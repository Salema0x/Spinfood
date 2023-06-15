package Data;

import Entity.Group;
import Entity.Pair;
import Entity.Participant;

import java.util.ArrayList;
import java.util.function.Function;

public class GroupList {
    private ArrayList<Group> groupList = new ArrayList<>();
    public final int groupCount;
    public final int successorCount;
    public double genderDiversity;
    public double ageDifference;
    public double preferenceDeviation;
    public double pathLength;
    public Double[] partyLocation;

    public GroupList(ArrayList<Group> groupList, ArrayList<Participant> successors, Double[] partylocation) {
        this.groupList = groupList;
        this.groupCount = groupList.size();
        this.successorCount = successors.size();
        this.genderDiversity = calculateGenderDiversityScore();
        this.ageDifference = calculateAverageScores(Group::getAgeDifference);
        this.preferenceDeviation = calculateAverageScores(Group::getPreferenceDeviation);
        this.partyLocation = partylocation;
        this.pathLength = gogje();
    }

    private double gogje() {
        double sum = 0;
        for (Group group : groupList) {
            Pair pair = group.getCookingPair();
            sum += calculateGeographicalDistance(partyLocation, pair.getPlaceOfCooking());
        }
        return sum;
    }


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

    /**
     * Calculates the average scores of the GroupList.
     * @param method the method to gather the data from the groups.
     * @return the average key identification of the list.
     */
    private double calculateAverageScores(Function<Group, Double> method) {
        double sumDiversityScores = 0.0d;

        for (Group group : groupList) {
            sumDiversityScores += method.apply(group);
        }

        if (groupCount != 0) {
            return sumDiversityScores / groupCount;
        }

        return 0;
    }

    /**
     * Calculates the gender diversity score of the group list..
     * @return a double representing the gender diversity score.
     */
    private double calculateGenderDiversityScore() {
        double sumDeviationFromIdeal = 0.0d;

        for (Group pair : groupList) {
            sumDeviationFromIdeal += Math.abs(0.5 - pair.getGenderDiversityScore());
        }

        if (groupCount!= 0) {
            return sumDeviationFromIdeal / groupCount;
        }

        return 0;
    }

    public double getAgeDifference() {
        return ageDifference;
    }

    public double getPreferenceDeviation() {
        return preferenceDeviation;
    }

    public double getGenderDiversity() {
        return genderDiversity;
    }
}
