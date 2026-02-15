package ir.ac.kntu.model;

public class DigitalProduct extends Product {
    private String brand;
    private int internalMemory;
    private int ram;

    public DigitalProduct(String name, double price, String brand, int internalMemory, int ram) throws Exception {
        super(name, price);
        this.brand = brand;
        this.internalMemory = internalMemory;
        this.ram = ram;
    }

    @Override
    public String getType() {
        return ProductType.DIGITAL.toString();
    }

    @Override
    public String toString() {
        return super.toString() +
                ", brand: " + brand +
                ", internalMemory: " + internalMemory +
                ", ram: " + ram;
    }
}
