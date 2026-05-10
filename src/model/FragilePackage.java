package model;

public class FragilePackage extends Package {

    private static final double RATE_PER_KG  = 6_000;
    private static final double HANDLING_FEE = 15_000;

    public FragilePackage() {}

    public FragilePackage(String id, String senderName, String receiverName,
                          String destination, double weightKg) {
        super(id, senderName, receiverName, destination, weightKg);
    }

    @Override
    public double calculateShippingCost() { return (getWeightKg() * RATE_PER_KG) + HANDLING_FEE; }

    @Override
    public String getEstimasiTiba() { return "2-4 hari kerja"; }

    @Override
    public String getPackageType() { return "FRAGILE"; }
}