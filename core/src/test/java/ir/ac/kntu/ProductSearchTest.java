package ir.ac.kntu;

import ir.ac.kntu.model.Book;
import ir.ac.kntu.model.BookGenre;
import ir.ac.kntu.model.AgeCategory;
import ir.ac.kntu.model.Product;
import ir.ac.kntu.search.ProductQuery;
import ir.ac.kntu.search.ProductSearchService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductSearchTest {

    @Test
    void fuzzySearchFindsTypo() throws Exception {
        ProductManager pm = new ProductManager();
        pm.addProduct(new Book("Clean Code", 350000, "Robert Martin", 464,
                BookGenre.SCIENCE, AgeCategory.YoungAdult, "9780132350884"));
        pm.addProduct(new Book("Effective Java", 420000, "Joshua Bloch", 416,
                BookGenre.SCIENCE, AgeCategory.YoungAdult, "9780134685991"));

        List<Product> res = pm.fuzzyByName("clen cod");
        assertFalse(res.isEmpty());
        assertTrue(res.stream().anyMatch(p -> p.getName().equalsIgnoreCase("Clean Code")));
    }

    @Test
    void searchPipelineFiltersAndSorts() throws Exception {
        ProductManager pm = new ProductManager();
        pm.addProduct(new Book("B1", 100, "A", 10, BookGenre.CLASSIC, AgeCategory.YoungAdult, "1"));
        pm.addProduct(new Book("B2", 200, "A", 10, BookGenre.CLASSIC, AgeCategory.YoungAdult, "2"));
        pm.addProduct(new Book("B3", 150, "A", 10, BookGenre.CLASSIC, AgeCategory.YoungAdult, "3"));

        ProductSearchService svc = new ProductSearchService(pm);
        ProductQuery q = new ProductQuery("B", "Book", 120, 200, 0,
                ProductQuery.SortBy.PRICE, ProductQuery.SortOrder.ASC, false);
        List<Product> res = svc.search(q);
        assertEquals(2, res.size());
        assertEquals("B3", res.get(0).getName());
        assertEquals("B2", res.get(1).getName());
    }
}
