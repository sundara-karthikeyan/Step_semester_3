/**
 * PROBLEM 3: The Late-Withdrawal Penalty Override & Audit Trail
 */
public class RaceEntry {

    protected String bibNumber;
    protected double entryFee;
    protected double amountPaid;

    // Private audit trail — never exposed directly, only via a defensive copy.
    private double[] lateFeeHistory = new double[10];
    private int lateFeeCount = 0;

    public RaceEntry(String bibNumber, double entryFee) {
        if (bibNumber == null || bibNumber.trim().isEmpty() || bibNumber.trim().length() < 4) {
            throw new IllegalArgumentException("Invalid bib number: " + bibNumber);
        }
        this.bibNumber = bibNumber;
        this.entryFee = entryFee;
        this.amountPaid = 0.0;
    }

    public void pay(double amount) {
        this.amountPaid += amount;
    }

    public double getBalanceDue() {
        return entryFee - amountPaid;
    }

    /**
     * Base penalty logic: deducts the amount from the balance (by adding it
     * as an outstanding charge) and records it in the audit trail.
     * Subclasses override this but funnel back through super(...) so the
     * recording logic only lives in one place.
     */
    protected void applyLateFee(double amount) {
        this.entryFee += amount;
        recordLateFee(amount);
    }

    private void recordLateFee(double amount) {
        lateFeeHistory[lateFeeCount] = amount;
        lateFeeCount++;
    }

    public double[] getLateFeeHistory() {
        double[] copy = new double[lateFeeCount];
        System.arraycopy(lateFeeHistory, 0, copy, 0, lateFeeCount);
        return copy;
    }

    public static void main(String[] args) {
        RunnerEntry r = new RunnerEntry("BIB2001", 80, "Open 10K");
        r.pay(30);
        r.applyLateFee(20);
        System.out.println(r.getBalanceDue()); // 90.0

        double[] history = r.getLateFeeHistory();
        System.out.println(java.util.Arrays.toString(history)); // [40.0]
        history[0] = 999; // tampering with returned array
        System.out.println(java.util.Arrays.toString(r.getLateFeeHistory())); // still [40.0]
    }
}
