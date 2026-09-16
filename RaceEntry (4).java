/**
 * PROBLEM 5: Race-Wide Bib Issuance, Discount Codes & Nightly Settlement Engine
 */
public class RaceEntry {

    protected String bibNumber;
    protected double entryFee;
    protected double amountPaid;

    // Never settable from outside, never reassignable after construction.
    protected final String entryCode;

    // Shared static counter — incremented exactly once per successful
    // construction, in the base class, so every subclass benefits for free.
    private static int bibCounter = 0;

    public RaceEntry(String bibNumber, double entryFee) {
        if (bibNumber == null || bibNumber.trim().isEmpty() || bibNumber.trim().length() < 4) {
            throw new IllegalArgumentException("Invalid bib number: " + bibNumber);
        }
        this.bibNumber = bibNumber;
        this.entryFee = entryFee;
        this.amountPaid = 0.0;

        bibCounter++;
        this.entryCode = "EC-" + bibCounter;
    }

    // Flat-amount payment.
    public void pay(double amount) {
        this.amountPaid += amount;
    }

    // Mode-aware overload — reuses the flat version instead of duplicating
    // the balance-update logic.
    public void pay(double amount, String mode) {
        System.out.println("Paying via " + mode);
        pay(amount);
    }

    public double getBalanceDue() {
        return entryFee - amountPaid;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public String announce() {
        return "Race Entry | Bib: " + bibNumber + " | Balance: " + getBalanceDue();
    }

    static int getBibCounter() {
        return bibCounter;
    }

    /**
     * Validates the exact format "M" + three digits + one uppercase letter
     * (e.g. "M123A") using charAt() / Character.isDigit() / isUpperCase()
     * only — no regular expressions. Length is checked first so charAt()
     * never runs off the end of the string.
     */
    static boolean isValidDiscountCode(String code) {
        if (code == null || code.length() != 5) {
            return false;
        }
        if (code.charAt(0) != 'M') {
            return false;
        }
        if (!Character.isDigit(code.charAt(1))
                || !Character.isDigit(code.charAt(2))
                || !Character.isDigit(code.charAt(3))) {
            return false;
        }
        return Character.isUpperCase(code.charAt(4));
    }

    /**
     * Reconciles a night's worth of entries. Uses instanceof to separate
     * relay entries from regular individual entries, and never throws on a
     * null batch entry.
     */
    static String settleNight(RaceEntry[] entries) {
        int processed = 0;
        int nullSkipped = 0;
        int relayCount = 0;
        int individualCount = 0;

        for (RaceEntry entry : entries) {
            if (entry == null) {
                nullSkipped++;
                continue;
            }
            processed++;
            if (entry instanceof RelayTeamEntry) {
                relayCount++;
            } else {
                individualCount++;
            }
        }

        return processed + " processed | " + nullSkipped + " null skipped | "
                + relayCount + " relay | " + individualCount + " individual";
    }

    public static void main(String[] args) {
        System.out.println(isValidDiscountCode("M123A")); // true
        System.out.println(isValidDiscountCode("M12A"));  // false
        System.out.println(isValidDiscountCode("X123A")); // false

        RunnerEntry r = new RunnerEntry("BIB2001", 80, "Open 10K");
        r.pay(10, "UPI"); // "Paying via UPI"

        EliteRunnerEntry eliteEntry = new EliteRunnerEntry("BIB3001", 150, "Elite Full Marathon", 500);
        RelayTeamEntry relayEntry = new RelayTeamEntry("BIB4001", 300, 4);

        System.out.println(settleNight(new RaceEntry[]{eliteEntry, null, relayEntry}));
        // "2 processed | 1 null skipped | 1 relay | 1 individual"

        // Reflects however many RaceEntry objects were successfully built above
        // (r, eliteEntry, relayEntry) -> 3 in this demo run.
        System.out.println(getBibCounter());
    }
}
