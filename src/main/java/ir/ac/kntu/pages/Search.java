package ir.ac.kntu.pages;

import ir.ac.kntu.PageManager;
import ir.ac.kntu.ProductManager;
import ir.ac.kntu.View;
import ir.ac.kntu.model.Product;
import ir.ac.kntu.search.ProductQuery;
import ir.ac.kntu.search.ProductSearchService;

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
        System.out.println(View.BRIGHT_CYAN + "&&& Search Bar &&&" + View.RESET);

        // read filters
        String kw = View.getStringInput("Keyword (blank=all): ").trim();
        String cat = View.getStringInput("Category {Book, Laptop, Mobile} (blank=all): ").trim();
        String min = View.getStringInput("Min price (blank=0): ").trim();
        String max = View.getStringInput("Max price (blank=inf): ").trim();
        String minScore = View.getStringInput("Min score (0..5, blank=0): ").trim();
        String sort = View.getStringInput("Sort by {price, score, name} (blank=price): ").trim();
        String order = View.getStringInput("Order {asc, desc} (blank=asc): ").trim();
        String fuzzy = View.getStringInput("Fuzzy keyword search? (y/n, blank=n): ").trim();

        double minP = min.isEmpty() ? 0 : Double.parseDouble(min);
        double maxP = max.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(max);
        double minS = minScore.isEmpty() ? 0 : Double.parseDouble(minScore);

        ProductQuery.SortBy sortBy = switch (sort.toLowerCase()) {
            case "score" -> ProductQuery.SortBy.SCORE;
            case "name" -> ProductQuery.SortBy.NAME;
            default -> ProductQuery.SortBy.PRICE;
        };
        ProductQuery.SortOrder sortOrder = order.equalsIgnoreCase("desc")
                ? ProductQuery.SortOrder.DESC
                : ProductQuery.SortOrder.ASC;
        boolean useFuzzy = fuzzy.equalsIgnoreCase("y");

        ProductQuery query = new ProductQuery(kw, cat, minP, maxP, minS, sortBy, sortOrder, useFuzzy);
        ProductSearchService service = new ProductSearchService(productManager);
        List<Product> res = service.search(query);

        if (res.isEmpty()) {
            View.printLine(View.BRIGHT_YELLOW + "No result" + View.RESET);
            pageManager.back();
            return;
        }

        int chosen = View.paginateAndSelect(res, 10);
        if (chosen >= 0) {
            Product p = res.get(chosen);
            pageManager.navigateToPage(PageTitle.PRODUCT, p);
            return;
        }
        pageManager.back();
    }
}