package model;

public class RegularPackage extends Package {

    private static final double RATE_PER_KG = 5_000;

    public RegularPackage() {}

    public RegularPackage(String id, String senderName, String receiverName,
                          String destination, double weightKg) {
        super(id, senderName, receiverName, destination, weightKg);
    }

    @Override
    public double calculateShippingCost() { return getWeightKg() * RATE_PER_KG; }

    @Override
    public String getEstimasiTiba() { return "3-5 hari kerja"; }

    @Override
    public String getPackageType() { return "REGULAR"; }
}