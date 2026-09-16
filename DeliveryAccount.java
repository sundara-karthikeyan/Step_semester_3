// Note: calculateSurgeFee reuses a simplified flat-rate model here (2% of orderValue
// per delay minute) rather than Problem 4's full tiered SurgeFeeCalculator, to keep
// this class self-contained.

class DeliveryAccount {

    // One-time, class-level state set up exactly once when the class is first loaded.
    protected static int accountsCreated;

    static {
        accountsCreated = 0;
        System.out.println("DeliveryAccount class initialized.");
    }

    protected final String studentId;
    protected final double orderValue;

    public DeliveryAccount(String studentId, double orderValue) {
        this.studentId = studentId;
        this.orderValue = orderValue;
        accountsCreated++;
    }

    // Provisional constructor — chains to the full constructor with orderValue = 0.
    public DeliveryAccount(String studentId) {
        this(studentId, 0.0);
    }

    public String getStudentId() {
        return studentId;
    }

    public double getOrderValue() {
        return orderValue;
    }

    // Locked against being changed/overridden by subclasses.
    final double calculateSurgeFee(int delayMinutes) {
        if (delayMinutes < 0) {
            throw new IllegalArgumentException("delayMinutes cannot be negative");
        }
        return orderValue * 0.02 * delayMinutes; // flat 2% per delay minute
    }
}

class PremiumDeliveryAccount extends DeliveryAccount {

    public PremiumDeliveryAccount(String studentId, double orderValue) {
        super(studentId, orderValue);
    }

    public PremiumDeliveryAccount(String studentId) {
        super(studentId);
    }

    // Premium accounts get a discounted flat rate (1% per delay minute) settled differently.
    double calculatePremiumSurgeFee(int delayMinutes) {
        if (delayMinutes < 0) {
            throw new IllegalArgumentException("delayMinutes cannot be negative");
        }
        return orderValue * 0.01 * delayMinutes;
    }
}

public class ReconciliationEngine {

    private int processedCount = 0;
    private int nullSkippedCount = 0;
    private int premiumCount = 0;
    private int regularCount = 0;
    private double grandTotalSurgeFees = 0.0;

    void processAccount(DeliveryAccount account, double amount, int delayMinutes) {
        try {
            if (account == null) {
                nullSkippedCount++;
                return;
            }

            double fee;
            if (account instanceof PremiumDeliveryAccount) {
                fee = ((PremiumDeliveryAccount) account).calculatePremiumSurgeFee(delayMinutes);
                premiumCount++;
            } else {
                fee = account.calculateSurgeFee(delayMinutes);
                regularCount++;
            }

            grandTotalSurgeFees += fee;
            processedCount++;
        } catch (Exception e) {
            // A single bad entry must never crash the whole run.
            System.out.println("Skipping account due to error: " + e.getMessage());
        }
    }

    static void processBatch(DeliveryAccount[] accounts, double[] amounts, int[] delayMinutesArray) {
        // Arrays may not match in length (buggy sync job) — process only up to the
        // shortest length rather than crashing, so a partial/misaligned batch is
        // never silently applied against the wrong student.
        int n = Math.min(accounts.length, Math.min(amounts.length, delayMinutesArray.length));

        ReconciliationEngine engine = new ReconciliationEngine();

        for (int i = 0; i < n; i++) {
            engine.processAccount(accounts[i], amounts[i], delayMinutesArray[i]);
        }

        System.out.println(engine.processedCount + " processed | "
                + engine.nullSkippedCount + " null skipped | "
                + engine.premiumCount + " premium | "
                + engine.regularCount + " regular | "
                + "grand total surge fees = " + engine.grandTotalSurgeFees);
    }

    public static void main(String[] args) {
        DeliveryAccount[] accounts = {
                new PremiumDeliveryAccount("STU001", 500),
                null,
                new DeliveryAccount("STU002", 300)
        };
        double[] amounts = {500, 400, 300};
        int[] delayMinutesArray = {10, 5, 0};

        processBatch(accounts, amounts, delayMinutesArray);
    }
}
