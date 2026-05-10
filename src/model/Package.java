package model;

public abstract class Package {

    private String id;
    private String senderName;
    private String receiverName;
    private String destination;
    private double weightKg;
    private PackageStatus status;

    public Package() {}

    public Package(String id, String senderName, String receiverName,
                   String destination, double weightKg) {
        this.id           = id;
        this.senderName   = senderName;
        this.receiverName = receiverName;
        this.destination  = destination;
        this.weightKg     = weightKg;
        this.status       = PackageStatus.PENDING;
    }

    public abstract double calculateShippingCost();
    public abstract String getEstimasiTiba();
    public abstract String getPackageType();

    public String getId()                          { return id; }
    public void setId(String id)                   { this.id = id; }
    public String getSenderName()                  { return senderName; }
    public void setSenderName(String senderName)   { this.senderName = senderName; }
    public String getReceiverName()                { return receiverName; }
    public void setReceiverName(String r)          { this.receiverName = r; }
    public String getDestination()                 { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public double getWeightKg()                    { return weightKg; }
    public void setWeightKg(double w)              { this.weightKg = w; }
    public PackageStatus getStatus()               { return status; }
    public void setStatus(PackageStatus status)    { this.status = status; }
}