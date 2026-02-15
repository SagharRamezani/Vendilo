package ir.ac.kntu.model;

public enum NetworkGeneration {
    G2, G3, G4, G4Plus, G5;

    @Override
    public String toString() {
        return switch (this) {
            case G2 -> "2G";
            case G3 -> "3G";
            case G4 -> "4G";
            case G4Plus -> "4G+";
            case G5 -> "5G";
        };
    }
}