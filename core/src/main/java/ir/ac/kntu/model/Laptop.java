package ir.ac.kntu.model;

public class Laptop extends DigitalProduct {
    private boolean bluetooth;
    private boolean webcam;
    private int gpu; // in GB

    public Laptop(String name, double price, String brand, int internalMemory, int ram, boolean bluetooth, boolean webcam, int gpu) throws Exception {
        super(name, price, brand, internalMemory, ram);
        this.bluetooth = bluetooth;
        this.webcam = webcam;
        this.gpu = gpu;
    }

    @Override
    public String getType() {
        return ProductType.LAPTOP.toString();
    }

    @Override
    public String toString() {
        return "Laptop{" + super.toString() +
                ", bluetooth: " + bluetooth +
                ", webcam: " + webcam +
                ", gpu: " + gpu +
                '}';
    }
}