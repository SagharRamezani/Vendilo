package ir.ac.kntu.model;

public enum AgeCategory {
    Board, Picture, EarlyReader, Chapter, MiddleGrade, YoungAdult; // 0-3, 3-8, 5-9, 6-10, 8-12, 12+

    @Override
    public String toString() {
        return switch (this) {
            case Board -> "Board";
            case Picture -> "Picture";
            case EarlyReader -> "EarlyReader";
            case Chapter -> "Chapter";
            case MiddleGrade -> "MiddleGrade";
            case YoungAdult -> "YoungAdult";
        };
    }
}