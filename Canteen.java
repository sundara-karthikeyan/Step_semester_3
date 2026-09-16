public class Canteen implements Comparable<Canteen> {

    private static final int DEFAULT_TRUST_SCORE = 3;

    private final String canteenCode;
    private final String canteenName;
    private final int trustScore;

    public Canteen(String canteenCode, String canteenName, int trustScore) {
        // Field/parameter naming clash resolved with 'this'.
        this.canteenCode = canteenCode;
        this.canteenName = canteenName;
        this.trustScore = trustScore;
    }

    // Chains to the main constructor with a sensible default trust score.
    public Canteen(String canteenCode, String canteenName) {
        this(canteenCode, canteenName, DEFAULT_TRUST_SCORE);
    }

    public String getCanteenCode() {
        return canteenCode; // stored/displayed exactly as given, case untouched
    }

    public String getCanteenName() {
        return canteenName;
    }

    public int getTrustScore() {
        return trustScore;
    }

    // Java convention: negative if this < other, 0 if equal, positive if this > other,
    // where "less than" here means "ranks higher" (higher score first).
    @Override
    public int compareTo(Canteen other) {
        // Higher trust score ranks first -> descending order.
        int scoreCompare = Integer.compare(other.trustScore, this.trustScore);
        if (scoreCompare != 0) {
            return scoreCompare;
        }

        // Tie-break 1: canteen code, compared case-insensitively (storage/display unaffected).
        int codeCompare = this.canteenCode.compareToIgnoreCase(other.canteenCode);
        if (codeCompare != 0) {
            return codeCompare;
        }

        // Tie-break 2: shorter canteen name ranks first.
        return Integer.compare(this.canteenName.length(), other.canteenName.length());
    }

    // Simple, self-implemented sort (bubble sort) — no built-in sort utility used.
    static Canteen[] rankCanteens(Canteen[] canteens) {
        Canteen[] result = canteens.clone();
        int n = result.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (result[j].compareTo(result[j + 1]) > 0) {
                    Canteen temp = result[j];
                    result[j] = result[j + 1];
                    result[j + 1] = temp;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Canteen[] canteens = {
                new Canteen("HB3-C", "Spice Junction", 3),
                new Canteen("hb1-c", "Grand Mess", 5),
                new Canteen("HB2-C", "Southern Treats") // defaults to trustScore 3
        };

        Canteen[] ranked = rankCanteens(canteens);
        for (Canteen c : ranked) {
            System.out.print(c.getCanteenCode() + " ");
        }
        System.out.println(); // hb1-c HB2-C HB3-C
    }
}
