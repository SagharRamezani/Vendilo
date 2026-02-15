package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Product;

public class ProductInfo extends Page {
    private final PageManager pageManager;
    private final Product product;

    public ProductInfo(PageManager pageManager, Product product) {
        super(PageTitle.PRODUCT);
        this.pageManager = pageManager;
        this.product = product;
    }

    @Override
    public void show() throws Exception {
        try {
            if (product == null) {
                throw new Exception("Product is null");
            }
            View.printLine(View.BRIGHT_CYAN + "=== Product ===" + View.RESET);
            View.printLine("Name: " + product.getName());
            View.printLine("Type: " + product.getType());
            View.printLine("Price: " + product.getPrice());
            View.printLine("Sold: " + product.getSold());
            View.printLine("Sellers: ");
            View.extracted(product.getSellers());
            pageManager.back();
        } catch (Exception e) {
            View.printLine("Error: " + e.getMessage());
            pageManager.back();
        }
    }
}
