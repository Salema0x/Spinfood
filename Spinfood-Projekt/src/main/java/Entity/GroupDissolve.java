package Entity;

public class GroupDissolve {
    private Group group;
    private Pair dissolvedPair1;
    private Pair dissolvedPair2;
    private Pair dissolvedPair3;

    private String course;

    public GroupDissolve(Group group, Pair dissolvedPair1, Pair dissolvedPair2, Pair dissolvedPair3, String course) {
        this.group = group;
        this.dissolvedPair1 = dissolvedPair1;
        this.dissolvedPair2 = dissolvedPair2;
        this.dissolvedPair3 = dissolvedPair3;
        this.course = course;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Pair getDissolvedPair1() {
        return dissolvedPair1;
    }

    public void setDissolvedPair1(Pair dissolvedPair1) {
        this.dissolvedPair1 = dissolvedPair1;
    }

    public Pair getDissolvedPair2() {
        return dissolvedPair2;
    }

    public void setDissolvedPair2(Pair dissolvedPair2) {
        this.dissolvedPair2 = dissolvedPair2;
    }

    public Pair getDissolvedPair3() {
        return dissolvedPair3;
    }

    public void setDissolvedPair3(Pair dissolvedPair3) {
        this.dissolvedPair3 = dissolvedPair3;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "GroupDissolve{" +
                "group=" + group +
                ", dissolvedPair1=" + dissolvedPair1 +
                ", dissolvedPair2=" + dissolvedPair2 +
                ", dissolvedPair3=" + dissolvedPair3 +
                ", course=" + course +
                '}';
    }
}
