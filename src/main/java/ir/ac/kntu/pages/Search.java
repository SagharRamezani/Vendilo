package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.ProductManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Product;

import java.util.List;

public class Search extends Page {
    private final PageManager pageManager;
    private final ProductManager productManager;

    public Search(PageManager pageManager, ProductManager productManager) {
        super(PageTitle.SEARCH);
        this.pageManager = pageManager;
        this.productManager = productManager;
    }

    @Override
    public void show() throws Exception {
        try {
            System.out.println(View.BRIGHT_CYAN + "&&& Search Bar &&&" + View.RESET);
            // read filters
            String q = View.getStringInput("Keyword (blank=all): ").trim();
            String cat = View.getStringInput("Category {Book, Laptop, Mobile} (blank=all): ").trim();
            String min = View.getStringInput("Min price (blank=0): ").trim();
            String max = View.getStringInput("Max price (blank=inf): ").trim();
            double minP = min.isEmpty() ? 0 : Double.parseDouble(min);
            double maxP = max.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(max);
            List<Product> res = productManager.search(q, cat, minP, maxP);
            if (res.isEmpty()) { // print list
                View.printLine(View.BRIGHT_YELLOW + "No result" + View.RESET);
            } else {
                View.extracted(res);
                String choose = View.getStringInput("Open product by index (blank=back): ").trim();
                if (!choose.isEmpty()) {
                    int idx = Integer.parseInt(choose);
                    if (idx < 1 || idx > res.size()) {
                        throw new Exception("Out of range");
                    }
                    Product p = res.get(idx - 1);
                    pageManager.navigateToPage(PageTitle.PRODUCT, p);
                } else {
                    pageManager.back();
                }
            }
            pageManager.back();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            pageManager.back();
        }
    }
}