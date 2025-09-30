package cis3334.java_firebase_parklist.data.model;

/**
 * Represents a park.
 */
public class Park {
    private final String id;
    private final String name;
    private final String address;
    private final double latitude;
    private final double longitude;

    // Constructor to initialize all fields
    public Park(String id, String name, String address, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // equals(), hashCode(), and toString() methods are omitted for simplicity.
}
