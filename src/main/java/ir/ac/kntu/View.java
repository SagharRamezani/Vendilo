package ir.ac.kntu;

import java.util.List;

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

    public static String getStringInput(String msg) {
        printLine(msg);
        return new java.util.Scanner(System.in).nextLine();
    }

    public static int getIntInput(String msg) {
        printLine(msg);
        return new java.util.Scanner(System.in).nextInt();
    }

    public static double getDoubleInput(String msg) {
        printLine(msg);
        return new java.util.Scanner(System.in).nextDouble();
    }

    public static <T> void extracted(List<T> list) throws Exception {
        if (list.isEmpty()) {
            System.out.println(View.YELLOW + "List is empty" + View.RESET);
        }
        int index = 0;
        boolean next = true;
        while (index < list.size() && next) {
            View.display(list, index);
            String choice = View.getStringInput(View.BRIGHT_BLACK + "Next Page? (y/n) " + View.RESET);
            switch (choice) {
                case "n" -> next = false;
                case "y" -> index += 10;
                default -> throw new Exception(View.BRIGHT_YELLOW + "Invalid choice. Try again." + View.RESET);
            }
        }
    }

    public static int getInt(String msg) {
        printLine(msg);
        return new java.util.Scanner(System.in).nextInt();
    }

    public static double getDouble(String msg) {
        printLine(msg);
        return new java.util.Scanner(System.in).nextDouble();
    }
}
