package cis3334.java_firebase_parklist.data.model;

import com.google.firebase.firestore.Exclude; // Import for excluding fields if needed later

/**
 * Represents a park.
 */
public class Park {
    private String id; // Made non-final, Firestore will not set this directly unless annotated
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    // No-argument constructor required for Firestore deserialization
    public Park() {
    }

    // Constructor to initialize all fields (can still be used for creating new parks in code)
    public Park(String id, String name, String address, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getter methods
    public String getId() { // This will get the ID if you set it manually or it was part of the map.
        return id;
    }

    // Setter for ID - useful if you fetch the document ID separately and want to store it in the object
    // Or use @DocumentId annotation on the field if you want Firestore to populate it automatically.
    // For now, we will set it manually after fetching or let it be null/client-generated for local list.
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // equals(), hashCode(), and toString() methods are omitted for simplicity.
}
