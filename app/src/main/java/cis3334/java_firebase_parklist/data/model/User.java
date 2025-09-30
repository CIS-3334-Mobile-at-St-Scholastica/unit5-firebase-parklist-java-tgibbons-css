package cis3334.java_firebase_parklist.data.model;

/**
 * Represents a user.
 * For now, it's a simplified version. It will be aligned with FirebaseUser later.
 */
public class User {
    private final String uid;
    private final String email; // Email might be null depending on the auth provider
    private final String displayName;
    private final String favoriteParkType; // e.g., "National", "State", "Local", "Dog Park"

    public User(String uid, String email, String displayName, String favoriteParkType) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.favoriteParkType = favoriteParkType;
    }

    // Getter for uid (non-nullable in Kotlin, so assumed to be always provided)
    public String getUid() {
        return uid;
    }

    // Getter for email (nullable in Kotlin)
    public String getEmail() {
        return email;
    }

    // Getter for displayName (nullable in Kotlin)
    public String getDisplayName() {
        return displayName;
    }

    // Getter for favoriteParkType (nullable in Kotlin)
    public String getFavoriteParkType() {
        return favoriteParkType;
    }

    // equals(), hashCode(), and toString() methods are removed for simplicity.
}
