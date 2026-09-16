public class F4_LibraryMember {

    // ---- Broken version ----
    static class BrokenLibraryMember {
        static String name;
        static String memberId;
        static int booksIssued;

        BrokenLibraryMember(String name, String memberId, int booksIssued) {
            BrokenLibraryMember.name = name;
            BrokenLibraryMember.memberId = memberId;
            BrokenLibraryMember.booksIssued = booksIssued;
        }
    }

    // Why static is wrong for each field here:
    // - name: static means there is only ONE copy of "name" shared by every member ever
    //   created. Creating a second member overwrites the first member's name entirely.
    // - memberId: same problem — each member needs their OWN unique id, but a static field
    //   forces all members to share a single id value, so the id keeps getting overwritten.
    // - booksIssued: each member issues books independently; a static field means one
    //   member's book count would overwrite/interfere with every other member's count.
    // In short: static fields belong to the CLASS, not to any one object, so they cannot hold
    // per-object data — they get silently shared and overwritten.

    // ---- Fixed version ----
    static class LibraryMember {
        String name;
        String memberId;
        int booksIssued;

        static String libraryName = "City Public Library";
        static int memberCount = 0;

        LibraryMember(String name, int booksIssued) {
            this.name = name;
            memberCount++;
            this.memberId = "LM-" + (1000 + memberCount);
            this.booksIssued = booksIssued;
        }

        void printMemberCard() {
            System.out.println(name + " | " + memberId);
        }

        static void printTotalMembers() {
            System.out.println("Total members: " + memberCount);
        }
    }

    public static void main(String[] args) {
        System.out.println("Broken version:");
        BrokenLibraryMember m1 = new BrokenLibraryMember("Aditi", "LM-1001", 2);
        BrokenLibraryMember m2 = new BrokenLibraryMember("Rohan", "LM-1002", 1);
        System.out.println(BrokenLibraryMember.name);
        System.out.println(BrokenLibraryMember.name);
        System.out.println("(Aditi's data was overwritten - both members now show \"Rohan\")");

        System.out.println();
        System.out.println("Fixed version:");
        LibraryMember member1 = new LibraryMember("Aditi", 2);
        LibraryMember member2 = new LibraryMember("Rohan", 1);
        member1.printMemberCard();
        member2.printMemberCard();
        LibraryMember.printTotalMembers();
    }
}
