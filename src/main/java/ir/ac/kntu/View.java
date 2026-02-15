package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class View {
    // Reset
    public static final String RESET = "\u001B[0m";

    // Regular Colors
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Bright Colors
    public static final String BRIGHT_BLACK = "\u001B[90m";
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_MAGENTA = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";

    public static <T> void display(List<T> list, int index) {
        if (list == null || list.isEmpty()) {
            System.out.println(BRIGHT_YELLOW + "Nothing to show." + RESET);
            return;
        }
        int start = index * 10;
        int end = Math.min(start + 10, list.size());
        displayN(list.subList(start, end));
    }

    public static <T> void displayN(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(BRIGHT_CYAN + "&&&" + (i + 1) + "&&&" + RESET);
            System.out.println(list.get(i).toString());
            System.out.println("--------------------------------------------------------------------------------");
        }
    }

    public static void printLine(String line) {
        System.out.println(line);
    }

    // Single shared scanner to avoid nextInt/nextLine pitfalls and resource leaks.
    private static final Scanner SCANNER = new Scanner(System.in);

    private static String readLine() {
        return SCANNER.nextLine();
    }

    public static String getStringInput(String msg) {
        printLine(msg);
        return readLine();
    }

    public static int getIntInput(String msg) {
        while (true) {
            String s = getStringInput(msg).trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println(BRIGHT_YELLOW + "Invalid number. Try again." + RESET);
            }
        }
    }

    public static double getDoubleInput(String msg) {
        while (true) {
            String s = getStringInput(msg).trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println(BRIGHT_YELLOW + "Invalid number. Try again." + RESET);
            }
        }
    }

    /**
     * Interactive pagination. Commands:
     * - n: next page
     * - p: previous page
     * - # : go to page number (1-based)
     * - q: quit
     * - item number: select an item (1-based in the whole list)
     *
     * @return selected item index (0-based) or -1 if quit.
     */
    public static <T> int paginateAndSelect(List<T> list, int pageSize) {
        if (list == null || list.isEmpty()) {
            System.out.println(BRIGHT_YELLOW + "Nothing to show." + RESET);
            return -1;
        }
        int page = 0;
        int totalPages = (list.size() + pageSize - 1) / pageSize;
        while (true) {
            display(list, page, pageSize);
            System.out.println(BRIGHT_BLACK + "Commands: n/p/#/q or item number" + RESET);
            String cmd = getStringInput("> ").trim();
            if (cmd.equalsIgnoreCase("q")) {
                return -1;
            }
            if (cmd.equalsIgnoreCase("n")) {
                if (page + 1 < totalPages) {
                    page++;
                } else {
                    System.out.println(BRIGHT_YELLOW + "Already at last page." + RESET);
                }
                continue;
            }
            if (cmd.equalsIgnoreCase("p")) {
                if (page > 0) {
                    page--;
                } else {
                    System.out.println(BRIGHT_YELLOW + "Already at first page." + RESET);
                }
                continue;
            }
            // go to page
            if (cmd.matches("\\d+")) {
                int num = Integer.parseInt(cmd);
                // If user types a page number prefixed with #
                // we also accept plain numbers as selection; we disambiguate by range.
                // If within [1, totalPages] and also within [1, listSize], prefer selection.
                if (num >= 1 && num <= list.size()) {
                    return num - 1;
                }
                System.out.println(BRIGHT_YELLOW + "Out of range." + RESET);
                continue;
            }
            if (cmd.startsWith("#")) {
                String ps = cmd.substring(1).trim();
                if (ps.matches("\\d+")) {
                    int p = Integer.parseInt(ps) - 1;
                    if (p >= 0 && p < totalPages) {
                        page = p;
                    } else {
                        System.out.println(BRIGHT_YELLOW + "Page out of range." + RESET);
                    }
                } else {
                    System.out.println(BRIGHT_YELLOW + "Invalid page." + RESET);
                }
                continue;
            }
            System.out.println(BRIGHT_YELLOW + "Invalid command." + RESET);
        }
    }

    public static <T> void extracted(List<T> list) {
        // Backward-compatible pagination for older pages.
        paginateOnly(list, 10);
    }

    public static <T> void paginateOnly(List<T> list, int pageSize) {
        if (list == null || list.isEmpty()) {
            System.out.println(View.YELLOW + "List is empty" + View.RESET);
            return;
        }
        int page = 0;
        int totalPages = (list.size() + pageSize - 1) / pageSize;
        while (true) {
            display(list, page, pageSize);
            String cmd = getStringInput(View.BRIGHT_BLACK + "n=next, p=prev, #=page, q=quit: " + View.RESET).trim();
            if (cmd.equalsIgnoreCase("q")) {
                return;
            }
            if (cmd.equalsIgnoreCase("n")) {
                if (page + 1 < totalPages) {
                    page++;
                }
                continue;
            }
            if (cmd.equalsIgnoreCase("p")) {
                if (page > 0) {
                    page--;
                }
                continue;
            }
            if (cmd.startsWith("#")) {
                String ps = cmd.substring(1).trim();
                if (ps.matches("\\d+")) {
                    int p = Integer.parseInt(ps) - 1;
                    if (p >= 0 && p < totalPages) {
                        page = p;
                    }
                }
                continue;
            }
            System.out.println(BRIGHT_YELLOW + "Invalid command." + RESET);
        }
    }

    public static int getInt(String msg) {
        return getIntInput(msg);
    }

    public static double getDouble(String msg) {
        return getDoubleInput(msg);
    }

    public static <T> void display(List<T> list, int page, int pageSize) {
        if (list == null || list.isEmpty()) {
            System.out.println(BRIGHT_YELLOW + "Nothing to show." + RESET);
            return;
        }
        int start = page * pageSize;
        int end = Math.min(start + pageSize, list.size());
        System.out.println(BRIGHT_BLACK + "Page " + (page + 1) + "/" + ((list.size() + pageSize - 1) / pageSize) + RESET);
        displayN(withGlobalIndex(list, start, end));
    }

    private static <T> List<String> withGlobalIndex(List<T> list, int start, int end) {
        List<String> out = new ArrayList<>();
        for (int i = start; i < end; i++) {
            out.add("#" + (i + 1) + "\n" + list.get(i));
        }
        return out;
    }
}
