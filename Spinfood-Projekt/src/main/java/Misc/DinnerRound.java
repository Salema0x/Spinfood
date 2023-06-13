package Misc;

import Entity.Group;
import Enum.Course;

import java.util.ArrayList;
import java.util.List;

public class DinnerRound {

    private String course;
    private final List<Group> groups;

    public DinnerRound() {
        this.groups = new ArrayList<>();
    }


    public List<Group> getGroups() {
        return groups;
    }

    public void setCourse(Course course) {
    }
}
