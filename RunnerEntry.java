/**
 * PROBLEM 1: RunnerEntry extends RaceEntry directly (single inheritance).
 * Forwards shared fields via super(...) instead of duplicating them.
 */
public class RunnerEntry extends RaceEntry {

    protected String category;

    public RunnerEntry(String bibNumber, double entryFee, String category) {
        super(bibNumber, entryFee);
        this.category = category;
    }
}
