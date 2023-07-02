package Entity;

public class PairSwap {
    private Pair pair;
    private Participant swappedParticipant;
    private Participant newParticipant;

    public PairSwap(Pair pair, Participant swappedParticipant, Participant newParticipant) {
        this.pair = pair;
        this.swappedParticipant = swappedParticipant;
        this.newParticipant = newParticipant;
    }

    public Pair getPair() {
        return pair;
    }

    public void setPair(Pair pair) {
        this.pair = pair;
    }

    public Participant getSwappedParticipant() {
        return swappedParticipant;
    }

    public void setSwappedParticipant(Participant swappedParticipant) {
        this.swappedParticipant = swappedParticipant;
    }

    public Participant getNewParticipant() {
        return newParticipant;
    }

    public void setNewParticipant(Participant newParticipant) {
        this.newParticipant = newParticipant;
    }

    @Override
    public String toString() {
        return "Swap{" +
                "pair=" + pair +
                ", swappedParticipant=" + swappedParticipant +
                ", newParticipant=" + newParticipant +
                '}';
    }
}
