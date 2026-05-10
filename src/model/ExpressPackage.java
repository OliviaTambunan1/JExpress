package model;

public class ExpressPackage extends Package {

    private static final double RATE_PER_KG = 8_000;

    public ExpressPackage() {}

    public ExpressPackage(String id, String senderName, String receiverName,
                          String destination, double weightKg) {
        super(id, senderName, receiverName, destination, weightKg);
    }

    @Override
    public double calculateShippingCost() { return getWeightKg() * RATE_PER_KG; }

    @Override
    public String getEstimasiTiba() { return "1-2 hari kerja"; }

    @Override
    public String getPackageType() { return "EXPRESS"; }
}