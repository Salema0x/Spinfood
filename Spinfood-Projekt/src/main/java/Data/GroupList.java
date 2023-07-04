package Data;

import Entity.Group;
import Entity.Participant;

import java.util.ArrayList;
import java.util.function.Function;

public class GroupList {
    private ArrayList<Group> groupList = new ArrayList<>();
    private final int groupCount;
    private final int successorCount;

    public int getGroupCount() {
        return groupCount;
    }

    public int getSuccessorCount() {
        return successorCount;
    }

    private double genderDiversity;
    private double ageDifference;
    private double preferenceDeviation;
    private double pathLength;

    public GroupList(ArrayList<Group> groupList, ArrayList<Participant> successors) {
        this.groupList = groupList;
        this.groupCount = groupList.size();
        this.successorCount = successors.size();
        this.genderDiversity = calculateGenderDiversityScore();
        this.ageDifference = calculateAverageScores(Group::getAgeDifference);
        this.preferenceDeviation = calculateAverageScores(Group::getPreferenceDeviation);
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
