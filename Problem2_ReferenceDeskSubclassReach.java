import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PROBLEM 2 - Reference Desk Subclass Reach
 */
public class Problem2_ReferenceDeskSubclassReach {

    public static void main(String[] args) {
        System.out.println(AccessChecker.classifyAccess("protected", "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"));    // ALLOWED
        System.out.println(AccessChecker.classifyAccess("protected", "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE")); // DENIED
        System.out.println(AccessChecker.describeContext("SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"));                // "Subclass Different Package Own Type"
    }
}

/**
 * Extends the field-reach linter with the two subclass-in-a-different-package
 * contexts, and adds a human-readable formatter for context codes.
 */
class AccessChecker {

    static String classifyAccess(String fieldModifier, String accessorContext) {
        if (fieldModifier == null || accessorContext == null) {
            throw new IllegalArgumentException("fieldModifier/accessorContext must not be null");
        }

        switch (fieldModifier) {
            case "private":
                return accessorContext.equals("SAME_CLASS") ? "ALLOWED" : "DENIED";

            case "default":
                return (accessorContext.equals("SAME_CLASS") || accessorContext.equals("SAME_PACKAGE"))
                        ? "ALLOWED" : "DENIED";

            case "protected":
                switch (accessorContext) {
                    case "SAME_CLASS":
                    case "SAME_PACKAGE":
                        return "ALLOWED";
                    case "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE":
                        // Accessed through a reference whose *declared* (compile-time) type
                        // is the subclass itself -> Java allows this.
                        return "ALLOWED";
                    case "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE":
                        // Accessed through a reference whose declared type is the parent
                        // class, from a different package -> Java denies this, even though
                        // the runtime object may actually be a subclass instance.
                        return "DENIED";
                    default:
                        return "DENIED";
                }

            case "public":
                return "ALLOWED";

            default:
                throw new IllegalArgumentException("Unknown fieldModifier: " + fieldModifier);
        }
    }

    static String summarizeByModifier(String[][] attempts) {
        String[] order = {"private", "default", "protected", "public"};
        Map<String, int[]> counts = new LinkedHashMap<>();
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
            if (bucket[0] == 0 && bucket[1] == 0) continue;
            if (!first) sb.append(" | ");
            sb.append(entry.getKey()).append(": ")
              .append(bucket[0]).append(" allowed / ")
              .append(bucket[1]).append(" denied");
            first = false;
        }
        return sb.toString();
    }

    /**
     * Turns an UNDERSCORE_SEPARATED_CODE into "Title Cased Words".
     */
    static String describeContext(String accessorContext) {
        if (accessorContext == null || accessorContext.isEmpty()) {
            throw new IllegalArgumentException("accessorContext must not be null/empty");
        }
        String[] parts = accessorContext.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].toLowerCase();
            if (!part.isEmpty()) {
                sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
            }
            if (i < parts.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
