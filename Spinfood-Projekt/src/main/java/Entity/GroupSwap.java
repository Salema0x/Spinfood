package Entity;

public class GroupSwap {
    private Group group;
    private Pair swappedPair;
    private Pair newPair;

    public GroupSwap(Group group, Pair swappedPair, Pair newPair) {
        this.group = group;
        this.swappedPair = swappedPair;
        this.newPair = newPair;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Pair getSwappedPair() {
        return swappedPair;
    }

    public void setSwappedPair(Pair swappedPair) {
        this.swappedPair = swappedPair;
    }

    public Pair getNewPair() {
        return newPair;
    }

    public void setNewPair(Pair newPair) {
        this.newPair = newPair;
    }

    @Override
    public String toString() {
        return "GroupSwap{" +
                "group=" + group +
                ", swappedPair=" + swappedPair +
                ", newPair=" + newPair +
                '}';
    }
}
