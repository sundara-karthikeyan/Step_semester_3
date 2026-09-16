public class F5_CompanyEmployeeRecord {

    static class Employee {
        private int empId;
        private String empName;
        private double salary;

        Employee(int empId, String empName, double salary) {
            this.empId = empId;
            this.empName = empName;
            this.salary = salary;
        }

        double getSalary() {
            return salary;
        }
    }

    static class ManagerEmployee extends Employee {
        private double teamBonus;

        ManagerEmployee(int empId, String empName, double salary, double teamBonus) {
            super(empId, empName, salary);
            this.teamBonus = teamBonus;
        }

        double effectiveSalary() {
            return getSalary() + teamBonus;
        }
    }

    static class InternEmployee extends Employee {
        private double stipendCap;

        InternEmployee(int empId, String empName, double salary, double stipendCap) {
            super(empId, empName, salary);
            this.stipendCap = stipendCap;
        }

        double effectiveSalary() {
            return Math.min(getSalary(), stipendCap);
        }
    }

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
        }
    }

    static class CompanyEmployeeRecord {
        String name;
        String empId;
        Employee employee;
        ParkingSlot slot;

        static int totalRecords = 0;

        CompanyEmployeeRecord(String name, String empId, Employee employee, ParkingSlot slot) {
            this.name = name;
            this.empId = empId;
            this.employee = employee;
            this.slot = slot;
            totalRecords++;
        }

        String fullProfile() {
            double pay;
            if (employee instanceof ManagerEmployee) {
                pay = ((ManagerEmployee) employee).effectiveSalary();
            } else if (employee instanceof InternEmployee) {
                pay = ((InternEmployee) employee).effectiveSalary();
            } else {
                pay = employee.getSalary();
            }

            String slotInfo = (slot == null) ? "no parking assigned" : slot.slotNo;
            return name + " | Pay: Rs " + pay + " | Slot: " + slotInfo;
        }
    }

    public static void main(String[] args) {
        ParkingSlot a1 = new ParkingSlot("A1", 4, 0);
        ParkingSlot a2 = new ParkingSlot("A2", 4, 0);
        ParkingSlot[] slots = new ParkingSlot[] { a1, a2 };

        ManagerEmployee divyaEmp = new ManagerEmployee(1, "Divya", 70000, 8000);
        Employee karanEmp = new Employee(2, "Karan", 40000);
        InternEmployee meeraEmp = new InternEmployee(3, "Meera", 12000, 10000);

        // Allot parking to only two of the three employees, leaving Meera unallotted on purpose.
        safeAllot(slots, "Divya-Car");
        CompanyEmployeeRecord divya = new CompanyEmployeeRecord("Divya", "E1", divyaEmp, a1);

        safeAllot(slots, "Karan-Car");
        CompanyEmployeeRecord karan = new CompanyEmployeeRecord("Karan", "E2", karanEmp, a2);

        CompanyEmployeeRecord meera = new CompanyEmployeeRecord("Meera", "E3", meeraEmp, null);

        System.out.println(divya.fullProfile());
        System.out.println(karan.fullProfile());
        System.out.println(meera.fullProfile());
        System.out.println("Total records: " + CompanyEmployeeRecord.totalRecords);
    }
}
