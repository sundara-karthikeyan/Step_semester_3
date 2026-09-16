// Class is locked against modification (final) since the surge-fee model is a fixed
// business rule; the configured rate field is also final since it must not drift
// after construction.
public final class SurgeFeeCalculator {

    private final double minimumSurgePercent;

    public SurgeFeeCalculator(double minimumSurgePercent) {
        this.minimumSurgePercent = minimumSurgePercent;
    }

    // Locked (final) so the tiered-rate rule can never be overridden/changed by a subclass.
    final double calculateSurgeFee(double orderValue, int delayMinutes) {
        if (orderValue < 0) {
            throw new IllegalArgumentException("orderValue cannot be negative");
        }
        if (delayMinutes < 0) {
            throw new IllegalArgumentException("delayMinutes cannot be negative");
        }

        if (delayMinutes == 0) {
            return 0.0; // on-time order: floor never applies
        }

        // Minutes falling in each bracket.
        int band1 = Math.min(delayMinutes, 5);                       // minutes 1-5
        int band2 = Math.min(Math.max(delayMinutes - 5, 0), 10);     // minutes 6-15
        int band3 = Math.max(delayMinutes - 15, 0);                  // minute 16+

        double tieredFee = orderValue * (band1 * 0.005 + band2 * 0.01 + band3 * 0.02);

        double floorFee = orderValue * (minimumSurgePercent / 100.0);

        return Math.max(tieredFee, floorFee);
    }

    public static void main(String[] args) {
        SurgeFeeCalculator calc = new SurgeFeeCalculator(1.0); // 1% minimum surge floor

        System.out.println("Rs " + calc.calculateSurgeFee(500, 0));   // Rs 0.0
        System.out.println("Rs " + calc.calculateSurgeFee(500, 1));   // Rs 5.0
        System.out.println("Rs " + calc.calculateSurgeFee(500, 16));  // Rs 72.5
    }
}
