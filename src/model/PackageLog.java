package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PackageLog {

    private int id;
    private String packageId;
    private PackageStatus status;
    private String location;
    private LocalDateTime timestamp;

    public PackageLog() {}

    public PackageLog(String packageId, PackageStatus status, String location) {
        this.packageId = packageId;
        this.status    = status;
        this.location  = location;
        this.timestamp = LocalDateTime.now();
    }

    public PackageLog(int id, String packageId, PackageStatus status,
                      String location, LocalDateTime timestamp) {
        this.id        = id;
        this.packageId = packageId;
        this.status    = status;
        this.location  = location;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM HH:mm");
        return String.format("  %s  %-11s  %s", timestamp.format(fmt), status, location);
    }

    public int getId()                         { return id; }
    public String getPackageId()               { return packageId; }
    public PackageStatus getStatus()           { return status; }
    public String getLocation()                { return location; }
    public LocalDateTime getTimestamp()        { return timestamp; }
    public void setTimestamp(LocalDateTime ts) { this.timestamp = ts; }
}