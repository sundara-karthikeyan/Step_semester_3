/**
 * PROBLEM 1: Race Entry Foundation & Batch Bib Validator
 *
 * Base class for every race entry. Validates the bib number in the
 * constructor so that ALL subclasses automatically inherit the same
 * validation rule (single source of truth).
 */
public class RaceEntry {

    protected String bibNumber;
    protected double entryFee;
    protected double amountPaid;

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
     * Attempts to construct one RaceEntry per bib number. Relies entirely on
     * the constructor's own validation (via try/catch) instead of
     * duplicating any validation logic here.
     */
    static String registerBatch(String[] bibNumbers, double entryFee) {
        int registered = 0;
        int rejected = 0;

        for (String bib : bibNumbers) {
            try {
                new RaceEntry(bib, entryFee);
                registered++;
            } catch (IllegalArgumentException e) {
                rejected++;
            }
        }

        return "Registered: " + registered + " | Rejected: " + rejected;
    }

    public static void main(String[] args) {
        // new RaceEntry("B1", 50) -> construction rejected
        try {
            new RaceEntry("B1", 50);
            System.out.println("Unexpected: construction succeeded");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }

        RunnerEntry r = new RunnerEntry("BIB2001", 80, "Open 10K");
        r.pay(30);
        System.out.println(r.getBalanceDue()); // 50.0

        System.out.println(registerBatch(new String[]{"BIB1", "B1", "BIB2"}, 80));
        // "Registered: 2 | Rejected: 1"
    }
}
