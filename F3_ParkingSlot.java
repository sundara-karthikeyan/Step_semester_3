public class F3_ParkingSlot {

    static class ParkingSlot {
        String slotNo;
        int capacity;
        int occupiedCount;

        ParkingSlot(String slotNo, int capacity, int occupiedCount) {
            this.slotNo = slotNo;
            this.capacity = capacity;
            this.occupiedCount = occupiedCount;
        }

        void allot(String vehicleNo) {
            occupiedCount++;
        }
    }

    static ParkingSlot findAvailableSlot(ParkingSlot[] slots) {
        for (ParkingSlot slot : slots) {
            if (slot.occupiedCount < slot.capacity) {
                return slot;
            }
        }
        return null;
    }

    static void safeAllot(ParkingSlot[] slots, String vehicleNo) {
        ParkingSlot slot = findAvailableSlot(slots);
        if (slot == null) {
            System.out.println("No slots available for " + vehicleNo);
        } else {
            slot.allot(vehicleNo);
            System.out.println(vehicleNo + " allotted to slot " + slot.slotNo);
        }
    }

    // Passing the ParkingSlot[] array only copies the array's reference (and, element by
    // element, the object references inside it) into the method's parameter — it does NOT
    // create new ParkingSlot objects. So any change made to a slot's fields inside the method
    // (like occupiedCount++ in allot()) is visible to the caller too, since both point to the
    // exact same objects in memory.

    public static void main(String[] args) {
        ParkingSlot[] slots1 = new ParkingSlot[] {
            new ParkingSlot("A1", 4, 3),
            new ParkingSlot("A2", 5, 5)
        };
        safeAllot(slots1, "TN09AB1234");

        ParkingSlot[] slots2 = new ParkingSlot[] {
            new ParkingSlot("A1", 4, 4),
            new ParkingSlot("A2", 5, 5)
        };
        safeAllot(slots2, "TN09AB1234");
    }
}
