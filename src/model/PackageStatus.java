package model;

public enum PackageStatus {
    PENDING, SHIPPED, IN_TRANSIT, DELIVERED;

    public static PackageStatus fromString(String value) {
        return PackageStatus.valueOf(value.toUpperCase());
    }
}