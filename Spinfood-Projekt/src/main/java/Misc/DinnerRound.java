package Misc;

import Entity.Group;

import java.util.ArrayList;
import java.util.List;

public class DinnerRound {
    private final List<Group> groups;

    public DinnerRound() {
        this.groups = new ArrayList<>();
    }

    public List<Group> getGroups() {
        return groups;
    }
}
