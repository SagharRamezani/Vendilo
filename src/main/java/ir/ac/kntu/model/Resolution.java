package ir.ac.kntu.model;

public enum Resolution {
    VGA_480P, HD_720P, FULL_HD_1080P, QHD_1440P, UHD_4K, UHD_5K, UHD_8K;

    @Override
    public String toString() {
        return switch (this) {
            case VGA_480P -> "VGA 480P";
            case HD_720P -> "HD 720P";
            case FULL_HD_1080P -> "FULL_HD_1080P";
            case QHD_1440P -> "QHD_1440P";
            case UHD_4K -> "UHD_4K";
            case UHD_5K -> "UHD_5K";
            case UHD_8K -> "UHD_8K";
        };
    }
}
