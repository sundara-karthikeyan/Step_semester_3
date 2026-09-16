/**
 * PROBLEM 3 - Book Copy Circulation Guard
 */
public class Problem3_BookCopyCirculationGuard {

    public static void main(String[] args) {
        try {
            new BookInventory(0);
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected: " + e.getMessage());
        }

        BookInventory b = new BookInventory(3);
        b.checkOut();
        b.checkOut();
        b.checkOut();
        b.checkOut(); // rejected silently, nothing left
        System.out.println(b.getCopiesAvailable()); // 0

        b.checkIn();
        b.checkIn();
        b.checkIn();
        b.checkIn(); // rejected silently, already full
        System.out.println(b.getCopiesAvailable()); // 3
    }
}

class BookInventory {

    private final int copiesTotal;
    private int copiesAvailable;

    BookInventory(int copiesTotal) {
        if (copiesTotal <= 0) {
            throw new IllegalArgumentException("copiesTotal must be positive");
        }
        this.copiesTotal = copiesTotal;
        this.copiesAvailable = copiesTotal;
    }

    /** Silently does nothing if there is nothing available to check out. */
    void checkOut() {
        if (copiesAvailable > 0) {
            copiesAvailable--;
        }
    }

    /** Silently does nothing if the inventory is already at full capacity. */
    void checkIn() {
        if (copiesAvailable < copiesTotal) {
            copiesAvailable++;
        }
    }

    int getCopiesAvailable() {
        return copiesAvailable;
    }
}
