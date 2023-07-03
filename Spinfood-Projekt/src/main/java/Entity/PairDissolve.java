package Entity;

public class PairDissolve {
    private Pair pair;
    private Participant dissolvedParticipant1;
    private Participant dissolvedParticipant2;

    public PairDissolve(Pair pair, Participant dissolvedParticipant1, Participant dissolvedParticipant2) {
        this.pair = pair;
        this.dissolvedParticipant1 = dissolvedParticipant1;
        this.dissolvedParticipant2 = dissolvedParticipant2;
    }

    public Pair getPair() {
        return pair;
    }

    public Participant getDissolvedParticipant2() {
        return dissolvedParticipant2;
    }

    public Participant getDissolvedParticipant1() {
        return dissolvedParticipant1;
    }

    public void setPair(Pair pair) {
        this.pair = pair;
    }



    @Override
    public String toString() {
        return "Dissolve{" +
                "pair=" + pair +
                ", dissolvedParticipant1= " + dissolvedParticipant1 +
                ", dissolvedParticipant2= " + dissolvedParticipant2 +
                '}';
    }
}
