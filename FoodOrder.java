public class FoodOrder {

    private final String studentName;
    private final String dishName;
    private boolean delivered;

    // Only parameterized constructor exists — no usable no-arg constructor.
    public FoodOrder(String studentName, String dishName) {
        if (studentName == null || studentName.trim().isEmpty()) {
            throw new IllegalArgumentException("studentName cannot be null, empty, or blank");
        }
        if (dishName == null || dishName.trim().isEmpty()) {
            throw new IllegalArgumentException("dishName cannot be null, empty, or blank");
        }
        this.studentName = studentName;
        this.dishName = dishName;
        this.delivered = false;
    }

    public void markDelivered() {
        if (!delivered) {
            delivered = true;
            System.out.println("Order for " + studentName + " (" + dishName + ") marked as delivered.");
        } else {
            System.out.println("WARNING: Order for " + studentName + " (" + dishName
                    + ") was already delivered! Possible double-serve detected.");
        }
    }

    public String getStudentName() {
        return studentName;
    }

    public String getDishName() {
        return dishName;
    }

    static void processBatch(String[][] rawOrders) {
        int valid = 0;
        int rejected = 0;

        for (String[] rawOrder : rawOrders) {
            try {
                String studentName = rawOrder[0];
                String dishName = rawOrder[1];
                new FoodOrder(studentName, dishName);
                valid++;
            } catch (IllegalArgumentException e) {
                rejected++;
            }
        }

        System.out.println("Valid: " + valid + " | Rejected: " + rejected);
    }

    public static void main(String[] args) {
        String[][] rawOrders = {
                {"Ravi", "Paneer Butter Masala"},
                {"", "Chole Bhature"},
                {"Meera", " "},
                {"Divya", "Veg Biryani"}
        };

        processBatch(rawOrders);

        FoodOrder order = new FoodOrder("Ravi", "Paneer Butter Masala");
        order.markDelivered();
        order.markDelivered();
    }
}
