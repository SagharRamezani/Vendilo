package ir.ac.kntu.model;

public class Book extends Product {
    private String author;
    private int pages;
    private BookGenre genre;
    private AgeCategory ageCategory;
    private String isbn;

    public Book(String name, double price, String author, int pages, BookGenre genre, AgeCategory ageCategory, String isbn) throws Exception {
        super(name, price);
        this.author = author;
        this.genre = genre;
        this.ageCategory = ageCategory;
        this.isbn = isbn;
        this.pages = pages;
    }

    @Override
    public String getType() {
        return ProductType.BOOK.toString();
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "Book{" + super.toString() +
                ", author: " + author +
                ", pages: " + pages +
                ", genre: " + genre.toString() +
                ", ageCategory: " + ageCategory.toString() +
                ", isbn: " + isbn +
                '}';
    }
}
