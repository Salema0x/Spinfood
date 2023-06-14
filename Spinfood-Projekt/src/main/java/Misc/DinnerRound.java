package Misc;

import Entity.Group;
import Enum.Course;

import java.util.ArrayList;
import java.util.List;

public class DinnerRound {

    private Course course;
    private final List<Group> groups;

    public DinnerRound() {
        this.groups = new ArrayList<>();
    }

    public DinnerRound(Course course) {
        this.course = course;
        this.groups = new ArrayList<>();
    }


    public List<Group> getGroups() {
        return groups;
    }

    public Course getCourse() {
        return course;
    }





    public void setCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        this.course = course;
    }

    public void addGroup(Group group) {
        if (group == null) {
            throw new IllegalArgumentException("Group cannot be null");
        }
        groups.add(group);
    }

    public void addGroupList(List<Group> groupList) {
        if (groupList == null) {
            throw new IllegalArgumentException("GroupList cannot be null");
        }
        groups.addAll(groupList);
    }
}
