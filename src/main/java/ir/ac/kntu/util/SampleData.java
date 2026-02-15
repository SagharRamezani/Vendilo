package ir.ac.kntu.util;

import ir.ac.kntu.model.*;

import java.util.ArrayList;
import java.util.List;

public final class SampleData {
    private SampleData() {
    }

    // strong pass samples
    private static final String P1 = "Abcd@1234";
    private static final String P2 = "Qwer!5678";
    private static final String P3 = "Zxcv#9012";
    private static final String P4 = "Admin@2025";
    private static final String P5 = "User$7788";
    private static final String P6 = "Shop%8899";
    private static final String P7 = "Supp*3344";
    private static final String P8 = "Mng^0000";

    // users
    public static List<User> users() throws Exception {
        List<User> list = new ArrayList<>();
        // customers
        list.add(new Customer("Ali", "Rezaei", P1, "ali@mail.com", "09120000001", 1000000));
        list.add(new Customer("Sara", "Nasiri", P2, "sara@mail.com", "09120000002"));
        list.add(new Customer("Mina", "Taheri", P3, "mina@mail.com", "09120000003", 600000));
        list.add(new Customer("Hamed", "Ghasemi", P5, "hamed@mail.com", "09120000004"));
        list.add(new Customer("Nima", "Karimi", P6, "nima@mail.com", "09120000005", 96243763));
        list.add(new Customer("Parsa", "Zahedi", P7, "parsa@mail.com", "09120000006"));
        list.add(new Customer("Elham", "Kiani", P1, "elham@mail.com", "09120000007", 4577));
        list.add(new Customer("Reyhane", "Akbari", P2, "rey@mail.com", "09120000008"));

        // sellers
        list.add(new Seller("Sara", "Mohammadi", P3, "ST1001", "09123450001", "Sara Store", "Tehran", "1122334455"));
        list.add(new Seller("Navid", "Ebrahimi", P4, "ST1002", "09123450002", "Navid Shop", "Shiraz", "2233445566"));
        list.add(new Seller("Leila", "Farahani", P5, "ST1003", "09123450003", "Leila Books", "Tabriz", "3344556677"));
        list.add(new Seller("Armin", "Soltani", P6, "ST1004", "09123450004", "Armin Digital", "Isfahan", "4455667788"));
        list.add(new Seller("Sahar", "Yazdani", P7, "ST1005", "09123450005", "Sahar Mobile", "Mashhad", "5566778899"));

        // supporters (username present)
        list.add(new Supporter("Reza", "Karimi", "rezakar", P7, SupportSection.ORDER));
        list.add(new Supporter("Mahsa", "Shirazi", "mahsa_sup", P5, SupportSection.SETTINGS));
        list.add(new Supporter("Pouya", "Rostami", "pouya_sup", P6, SupportSection.ORDER));
        list.add(new Supporter("Zahra", "Moradi", "zahra_sup", P3, SupportSection.QUALITY));

        // managers (rank: lower = stronger, 0 = root)
        list.add(new Manager("Mina", "Ahmadi", "admin", P4, 0));
        list.add(new Manager("Kaveh", "Jafari", "mgr_kaveh", P3, 1));
        list.add(new Manager("Dorsa", "Hosseini", "mgr_dorsa", P2, 2));

        return list;
    }

    // === Products ===
    public static List<Product> products() throws Exception {
        List<Product> ps = new ArrayList<>();

        // Books (8)
        ps.add(new Book("Clean Code", 350000, "Robert C. Martin", 464,
                BookGenre.SCIENCE, AgeCategory.YoungAdult, "9780132350884"));
        ps.add(new Book("Effective Java", 420000, "Joshua Bloch", 416,
                BookGenre.SCIENCE, AgeCategory.YoungAdult, "9780134685991"));
        ps.add(new Book("Harry Potter and the Sorcerer's Stone", 220000, "J.K. Rowling", 320,
                BookGenre.FANTASY, AgeCategory.MiddleGrade, "9780747532743"));
        ps.add(new Book("The Hobbit", 260000, "J.R.R. Tolkien", 310,
                BookGenre.FANTASY, AgeCategory.YoungAdult, "9780547928227"));
        ps.add(new Book("Deep Learning", 680000, "Ian Goodfellow", 800,
                BookGenre.SCIENCE, AgeCategory.YoungAdult, "9780262035613"));
        ps.add(new Book("Pride and Prejudice", 180000, "Jane Austen", 279,
                BookGenre.CLASSIC, AgeCategory.YoungAdult, "9780141439518"));
        ps.add(new Book("The Little Prince", 150000, "Antoine de Saint-Exupéry", 96,
                BookGenre.CHILDREN, AgeCategory.MiddleGrade, "9780156012195"));
        ps.add(new Book("The Great Gatsby", 200000, "F. Scott Fitzgerald", 180,
                BookGenre.CLASSIC, AgeCategory.YoungAdult, "9780743273565"));

        // Laptops (6)
        ps.add(new Laptop("Dell XPS 13", 25000000, "Dell", 512, 16, true, true, 4090));
        ps.add(new Laptop("Lenovo ThinkPad X1", 27000000, "Lenovo", 1024, 32, true, true, 3080));
        ps.add(new Laptop("Asus TUF A15", 22000000, "Asus", 1024, 16, true, false, 3060));
        ps.add(new Laptop("HP Envy 14", 23000000, "HP", 512, 16, true, true, 3050));
        ps.add(new Laptop("MacBook Pro 14", 42000000, "Apple", 1024, 32, true, true, 4090));
        ps.add(new Laptop("Acer Swift 3", 18000000, "Acer", 512, 8, true, true, 1650));

        // Mobiles (6)
        ps.add(new Mobile("Samsung Galaxy S23", 35000000, "Samsung", 256, 8,
                Resolution.HD_720P, Resolution.UHD_4K, NetworkGeneration.G5));
        ps.add(new Mobile("iPhone 15", 52000000, "Apple", 256, 6,
                Resolution.FULL_HD_1080P, Resolution.UHD_4K, NetworkGeneration.G5));
        ps.add(new Mobile("Xiaomi 13T", 21000000, "Xiaomi", 256, 8,
                Resolution.HD_720P, Resolution.QHD_1440P, NetworkGeneration.G5));
        ps.add(new Mobile("Google Pixel 8", 39000000, "Google", 128, 8,
                Resolution.FULL_HD_1080P, Resolution.UHD_5K, NetworkGeneration.G5));
        ps.add(new Mobile("OnePlus 11", 28000000, "OnePlus", 256, 12,
                Resolution.FULL_HD_1080P, Resolution.UHD_4K, NetworkGeneration.G5));
        ps.add(new Mobile("Nokia 3310 Classic", 500000, "Nokia", 32, 1,
                Resolution.VGA_480P, Resolution.VGA_480P, NetworkGeneration.G2));

        return ps;
    }
}
