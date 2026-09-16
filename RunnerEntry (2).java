public class RunnerEntry extends RaceEntry {

    protected String category;

    public RunnerEntry(String bibNumber, double entryFee, String category) {
        super(bibNumber, entryFee);
        this.category = category;
    }

    @Override
    protected void applyLateFee(double amount) {
        // Reuses the parent's deduction + recording logic; doubles the amount
        // for runners instead of reimplementing the penalty from scratch.
        super.applyLateFee(amount * 2);
    }
}
