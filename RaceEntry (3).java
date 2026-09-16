/**
 * PROBLEM 4: The Race-Day Announcer Board
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

    public String announce() {
        return "Race Entry | Bib: " + bibNumber + " | Balance: " + getBalanceDue();
    }

    protected void applyLateFee(double amount) {
        this.entryFee += amount;
    }

    /**
     * Loops polymorphically over any mix of RaceEntry subclasses, calling
     * announce() without any instanceof-based if-else chain. Builds the
     * report with a single shared StringBuilder. Only when an entry is
     * genuinely a RelayTeamEntry does it guard a downcast to reach the
     * relay-specific team size.
     */
    static String announceAll(RaceEntry[] entries) {
        StringBuilder sb = new StringBuilder();

        for (RaceEntry entry : entries) {
            sb.append(entry.announce()).append(" | ");
            if (entry instanceof RelayTeamEntry) {
                RelayTeamEntry relay = (RelayTeamEntry) entry;
                sb.append("[Team size via downcast: ").append(relay.getTeamSize()).append("] | ");
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        RunnerEntry runnerEntry = new RunnerEntry("BIB2001", 80, "Open 10K");
        runnerEntry.pay(30);
        runnerEntry.applyLateFee(20); // doubled internally by RunnerEntry -> balance becomes 90.0

        RelayTeamEntry relayEntry = new RelayTeamEntry("BIB4001", 300, 4);

        RaceEntry[] fleet = { runnerEntry, relayEntry };
        System.out.println(announceAll(fleet));

        RaceEntry plain = new RaceEntry("BIB5001", 50);
        try {
            RelayTeamEntry bad = (RelayTeamEntry) plain; // ClassCastException at runtime
            System.out.println(bad);
        } catch (ClassCastException e) {
            System.out.println("ClassCastException at runtime");
        }
    }
}
