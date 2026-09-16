import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PROBLEM 4 - LibraryMember JavaBean, Chained Constructors & Security Answer
 */
public class Problem4_LibraryMemberJavaBean {

    public static void main(String[] args) {
        System.out.println(new LibraryMemberBean("Priya Nair").getMembershipId()); // null

        LibraryMemberBean withId = new LibraryMemberBean("LIB-8841", "Priya Nair");
        System.out.println(withId.getMembershipId()); // LIB-8841

        LibraryMemberBean m = new LibraryMemberBean();
        m.setMembershipId("LIB-8841");
        m.setMembershipId("FAKE-0000"); // ignored: write-once
        System.out.println(m.getMembershipId()); // LIB-8841

        m.setPremiumMember(true);
        System.out.println(m.isPremiumMember()); // true

        m.setSecurityAnswer("My first pet was Max");
        // no getter exists anywhere to read it back
    }
}

class LibraryMemberBean {

    private String membershipId;
    private String name;
    private boolean premiumMember;
    private String securityAnswerHash; // stores only a one-way, deterministic transform

    /** No-arg constructor required by the JavaBean-scanning framework. */
    public LibraryMemberBean() {
        this(null, null);
    }

    /** Name-only convenience constructor. */
    public LibraryMemberBean(String name) {
        this(null, name);
    }

    /** Full constructor; all three chain into this single init path via this(...). */
    public LibraryMemberBean(String membershipId, String name) {
        this.membershipId = membershipId;
        this.name = name;
        this.premiumMember = false;
    }

    // --- membershipId: write-once setter ---
    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String id) {
        if (this.membershipId == null) {
            this.membershipId = id;
        }
        // Any subsequent call is silently ignored once a value has been set.
    }

    // --- name: ordinary JavaBean property ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // --- premiumMember: boolean uses isX(), not getX() ---
    public boolean isPremiumMember() {
        return premiumMember;
    }

    public void setPremiumMember(boolean premium) {
        this.premiumMember = premium;
    }

    // --- securityAnswer: write-only, forever ---
    public void setSecurityAnswer(String answer) {
        this.securityAnswerHash = oneWayHash(answer);
    }
    // Deliberately no getSecurityAnswer() / isSecurityAnswer() anywhere in this class.

    private static String oneWayHash(String input) {
        if (input == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
