public class DeliverySlot {

    private static final String DEFAULT_SLOT = "ASAP";

    private final String orderId;
    private final String timeSlot;

    public DeliverySlot(String orderId, String timeSlot) {
        this.orderId = orderId;
        this.timeSlot = timeSlot;
    }

    // Chains to the main constructor — "ASAP" is typed only once, here.
    public DeliverySlot(String orderId) {
        this(orderId, DEFAULT_SLOT);
    }

    boolean isPeakHour() {
        return timeSlot.equals("12:00-13:00")
                || timeSlot.equals("13:00-14:00")
                || timeSlot.equals("19:00-20:00")
                || timeSlot.equals("20:00-21:00");
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public static void main(String[] args) {
        DeliverySlot slot1 = new DeliverySlot("ORD101", "13:00-14:00");
        System.out.println(slot1.isPeakHour()); // true

        DeliverySlot slot2 = new DeliverySlot("ORD102");
        System.out.println(slot2.getTimeSlot()); // ASAP
        System.out.println(slot2.isPeakHour()); // false
    }
}
