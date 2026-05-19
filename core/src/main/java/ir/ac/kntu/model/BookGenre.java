package ir.ac.kntu.model;

public enum BookGenre {
    // Fiction
    FANTASY,
    SCIENCE_FICTION,
    ADVENTURE,
    ROMANCE,
    MYSTERY,
    THRILLER,
    HORROR,

    // Non-fiction
    BIOGRAPHY,
    HEALTH,
    HISTORY,
    SCIENCE,
    PHILOSOPHY,
    TRAVEL,
    TRUE_CRIME,
    POLITICS,
    BUSINESS,
    ECONOMICS,
    RELIGION,
    ART,
    PSYCHOLOGY,
    COOKING,

    // Educational/Reference
    DICTIONARY,
    ENCYCLOPEDIA,
    GUIDE,
    MANUAL,

    // Other
    POETRY,
    COMIC,
    GRAPHIC_NOVEL,
    CHILDREN,
    YOUNG_ADULT,
    DRAMA,
    CLASSIC;


    @Override
    public String toString() {
        return switch (this) {
            case FANTASY -> "Fantasy";
            case SCIENCE_FICTION -> "Science Fiction";
            case ADVENTURE -> "Adventure";
            case ROMANCE -> "Romance";
            case MYSTERY -> "Mystery";
            case THRILLER -> "Thriller";
            case HORROR -> "Horror";
            case BIOGRAPHY -> "Biography";
            case HEALTH -> "Health";
            case HISTORY -> "History";
            case SCIENCE -> "Science";
            case PHILOSOPHY -> "Philosophy";
            case TRAVEL -> "Travel";
            case POLITICS -> "Politics";
            case COOKING -> "Cooking";
            case DICTIONARY -> "Dictionary";
            case ENCYCLOPEDIA -> "Encyclopedia";
            case GUIDE -> "Guide";
            case MANUAL -> "Manual";
            case POETRY -> "Poetry";
            case COMIC -> "Comic";
            case GRAPHIC_NOVEL -> "Graphic Novel";
            case CHILDREN -> "Children";
            case CLASSIC -> "Classic";
            case TRUE_CRIME -> "True Crime";
            case PSYCHOLOGY -> "Psychology";
            case ECONOMICS -> "Economics";
            case RELIGION -> "Religion";
            case ART -> "Art";
            case YOUNG_ADULT -> "Young Adult";
            case DRAMA -> "Drama";
            case BUSINESS -> "Business";
        };
    }
}