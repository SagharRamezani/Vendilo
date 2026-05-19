package ir.ac.kntu.model;

public class Mobile extends DigitalProduct {
    private Resolution frontCamera;
    private Resolution backCamera;
    private NetworkGeneration network;

    public Mobile(String name, double price, String brand, int internalMemory, int ram, Resolution frontCamera, Resolution backCamera, NetworkGeneration network) throws Exception {
        super(name, price, brand, internalMemory, ram);
        this.frontCamera = frontCamera;
        this.backCamera = backCamera;
        this.network = network;
    }

    @Override
    public String getType() {
        return ProductType.MOBILE.toString();
    }

    @Override
    public String toString() {
        return "Mobile{" + super.toString() +
                ", frontCamera: " + frontCamera +
                ", backCamera: " + backCamera +
                ", network: " + network +
                '}';
    }
}
