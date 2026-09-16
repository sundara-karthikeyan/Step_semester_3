import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * PROBLEM 5 - Immutable Loan Receipt & Nightly Circulation Ledger
 */
public class Problem5_ImmutableLoanReceipt {

    public static void main(String[] args) {
        try {
            new LoanReceipt("LIB-8841", new String[]{"BK-100", "bad"});
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected: " + e.getMessage());
        }

        LoanReceipt r = new LoanReceipt("LIB-8841", new String[]{"BK-100", "BK-101"});
        String[] ids = r.getBookIds();
        ids[0] = "HACKED"; // mutating the returned copy must not affect the receipt
        System.out.println(r.getBookIds()[0]); // BK-100

        LoanReceipt[] batch = {
            new ReferenceOnlyLoanReceipt("LIB-001", new String[]{"BK-200"}, "Reading Room 3"),
            null,
            new LoanReceipt("LIB-002", new String[]{"BK-201"})
        };
        System.out.println(CirculationProcessor.processNightlyCirculation(batch));
        // "2 processed | 1 null skipped | 1 reference-only | 1 regular"
    }
}

/**
 * A genuinely immutable loan receipt: final class, final fields, defensive
 * copies on the way in and out, corrections produced via a "wither" method.
 */
class LoanReceipt {

    private static final Pattern BOOK_ID_PATTERN = Pattern.compile("^BK-\\d{3}$");

    static {
        // One-time shared setup for the whole LoanReceipt family.
        System.out.println("[LoanReceipt] static initialization complete.");
    }

    private final String memberId;
    private final String[] bookIds;

    public LoanReceipt(String memberId, String[] bookIds) {
        if (memberId == null || bookIds == null) {
            throw new IllegalArgumentException("memberId/bookIds must not be null");
        }
        for (String id : bookIds) {
            if (id == null || !BOOK_ID_PATTERN.matcher(id).matches()) {
                throw new IllegalArgumentException("Invalid book id: " + id);
            }
        }
        this.memberId = memberId;
        this.bookIds = Arrays.copyOf(bookIds, bookIds.length); // defensive copy in
    }

    public String getMemberId() {
        return memberId;
    }

    public String[] getBookIds() {
        return Arrays.copyOf(bookIds, bookIds.length); // defensive copy out
    }

    /** Returns a brand-new, corrected LoanReceipt; this object is left untouched. */
    public LoanReceipt withCorrectedBookId(int index, String newId) {
        String[] updated = getBookIds(); // already a fresh copy
        updated[index] = newId;
        return new LoanReceipt(this.memberId, updated);
    }

    @Override
    public String toString() {
        return "LoanReceipt{memberId=" + memberId + ", bookIds=" + Arrays.toString(bookIds) + "}";
    }
}

/**
 * A reference-only variant of a loan receipt (e.g. in-library reading room use).
 */
class ReferenceOnlyLoanReceipt extends LoanReceipt {

    private final String roomNumber;

    public ReferenceOnlyLoanReceipt(String memberId, String[] bookIds, String roomNumber) {
        super(memberId, bookIds);
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}

/**
 * Reconciles a night's worth of receipts, tolerating null batch entries and
 * distinguishing reference-only receipts from regular ones via instanceof.
 */
class CirculationProcessor {

    static String processNightlyCirculation(LoanReceipt[] receipts) {
        int processed = 0;
        int nullSkipped = 0;
        int referenceOnly = 0;
        int regular = 0;

        for (LoanReceipt receipt : receipts) {
            if (receipt == null) {
                nullSkipped++;
                continue;
            }
            processed++;
            if (receipt instanceof ReferenceOnlyLoanReceipt) {
                referenceOnly++;
            } else {
                regular++;
            }
        }

        return processed + " processed | " + nullSkipped + " null skipped | "
                + referenceOnly + " reference-only | " + regular + " regular";
    }
}
