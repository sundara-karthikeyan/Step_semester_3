import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PROBLEM 1 - Membership Field Reach Checker
 */
public class Problem1_MembershipFieldReachChecker {

    public static void main(String[] args) {
        // Quick smoke tests
        System.out.println(AccessChecker.classifyAccess("private", "SAME_CLASS"));       // ALLOWED
        System.out.println(AccessChecker.classifyAccess("protected", "DIFFERENT_PACKAGE")); // DENIED

        String[][] attempts = {
            {"private", "SAME_CLASS"},
            {"private", "SAME_PACKAGE"},
            {"default", "SAME_PACKAGE"},
            {"default", "DIFFERENT_PACKAGE"},
            {"protected", "SAME_PACKAGE"},
            {"protected", "SAME_CLASS"},
            {"public", "DIFFERENT_PACKAGE"}
        };
        System.out.println(AccessChecker.summarizeByModifier(attempts));

        try {
            new LibraryMember("LB9", "BR1", 0, "Priya Nair");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected: " + e.getMessage());
        }

        LibraryMember m = new LibraryMember("LB94", "BR1", 0, "Priya Nair");
        System.out.println("Constructed OK: " + m);
    }
}

/**
 * Pure, stateless linter that mimics Java's real access-modifier rules.
 */
class AccessChecker {

    static String classifyAccess(String fieldModifier, String accessorContext) {
        if (fieldModifier == null || accessorContext == null) {
            throw new IllegalArgumentException("fieldModifier/accessorContext must not be null");
        }

        switch (fieldModifier) {
            case "private":
                // Only reachable from inside the very same class.
                return accessorContext.equals("SAME_CLASS") ? "ALLOWED" : "DENIED";

            case "default":
                // Package-private: same class or same package only.
                return (accessorContext.equals("SAME_CLASS") || accessorContext.equals("SAME_PACKAGE"))
                        ? "ALLOWED" : "DENIED";

            case "protected":
                // Same class, same package always allowed.
                // Different package only allowed via a subclass reference of the subclass's own type.
                switch (accessorContext) {
                    case "SAME_CLASS":
                    case "SAME_PACKAGE":
                    case "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE":
                        return "ALLOWED";
                    default:
                        return "DENIED";
                }

            case "public":
                // Always reachable.
                return "ALLOWED";

            default:
                throw new IllegalArgumentException("Unknown fieldModifier: " + fieldModifier);
        }
    }

    static String summarizeByModifier(String[][] attempts) {
        // Preserve first-seen modifier order, but report in the canonical order below if present.
        String[] order = {"private", "default", "protected", "public"};
        Map<String, int[]> counts = new LinkedHashMap<>(); // [allowed, denied]

        for (String modifier : order) {
            counts.put(modifier, new int[]{0, 0});
        }

        for (String[] attempt : attempts) {
            String modifier = attempt[0];
            String context = attempt[1];
            String result = classifyAccess(modifier, context);

            counts.putIfAbsent(modifier, new int[]{0, 0});
            int[] bucket = counts.get(modifier);
            if (result.equals("ALLOWED")) {
                bucket[0]++;
            } else {
                bucket[1]++;
            }
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, int[]> entry : counts.entrySet()) {
            int[] bucket = entry.getValue();
            if (bucket[0] == 0 && bucket[1] == 0) {
                continue; // modifier never attempted
            }
            if (!first) {
                sb.append(" | ");
            }
            sb.append(entry.getKey())
              .append(": ")
              .append(bucket[0]).append(" allowed / ")
              .append(bucket[1]).append(" denied");
            first = false;
        }
        return sb.toString();
    }
}

/**
 * LibraryMember: refuses construction on a blank / whitespace / too-short membershipId.
 * No usable no-arg constructor is provided.
 */
class LibraryMember {

    private final String membershipId;     // private: reachable only within this class
    private String branchCode;             // default (package-private)
    protected double finesOwed;            // protected: this class, package, and subclasses
    public String displayName;             // public: reachable from anywhere

    public LibraryMember(String membershipId, String branchCode, double finesOwed, String displayName) {
        if (membershipId == null) {
            throw new IllegalArgumentException("membershipId must not be null");
        }
        String trimmed = membershipId.trim();
        if (trimmed.isEmpty() || trimmed.length() < 4) {
            throw new IllegalArgumentException("membershipId must be at least 4 characters and not blank");
        }
        this.membershipId = trimmed;
        this.branchCode = branchCode;
        this.finesOwed = finesOwed;
        this.displayName = displayName;
    }

    public String getMembershipId() {
        return membershipId;
    }

    @Override
    public String toString() {
        return "LibraryMember{id=" + membershipId + ", branch=" + branchCode
                + ", fines=" + finesOwed + ", name=" + displayName + "}";
    }
}
