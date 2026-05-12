package driver;

import db.AdminAuth;
import db.DBConnection;
import mapper.*;
import model.*;
import model.Package;

import java.sql.SQLException;
import java.util.*;

public class Main {

    private static final Scanner          scanner          = new Scanner(System.in);
    private static final CustomerMapper   customerMapper   = new CustomerMapper();
    private static final PackageMapper    packageMapper    = new PackageMapper();
    private static final PackageLogMapper packageLogMapper = new PackageLogMapper();

    static final String RESET  = "\u001B[0m";
    static final String BOLD   = "\u001B[1m";
    static final String CYAN   = "\u001B[96m";
    static final String GREEN  = "\u001B[92m";
    static final String YELLOW = "\u001B[93m";
    static final String RED    = "\u001B[91m";
    static final String BLUE   = "\u001B[94m";
    static final String PURPLE = "\u001B[95m";
    static final String GRAY   = "\u001B[90m";

    public static void main(String[] args) {
        printBanner();

        try {
            AdminAuth.createTableIfNotExists();
            customerMapper.createTableIfNotExists();
            packageMapper.createTableIfNotExists();
            packageLogMapper.createTableIfNotExists();
        } catch (SQLException e) {
            System.err.println(RED + "Gagal inisialisasi DB: " + e.getMessage() + RESET);
            return;
        }

        System.out.println("  Database siap.");
    }

    private static void printHeader(String title) {
        System.out.println("\n" + BOLD + CYAN + "  ── " + title + " ──" + RESET);
    }

    private static void printGaris() {
        System.out.println(GRAY + "  " + "─".repeat(52) + RESET);
    }

    private static void printBox(String title, String[] items, String color) {
        int width = 34;
        System.out.println("\n" + color + BOLD + "  +" + "-".repeat(width) + "+");
        System.out.println("  |  " + title + " ".repeat(width - title.length() - 2) + "|");
        System.out.println("  +" + "-".repeat(width) + "+" + RESET);
        for (String item : items)
            System.out.println(color + "  |  " + item
                    + " ".repeat(Math.max(0, width - item.length() - 2)) + "|" + RESET);
        System.out.println(color + "  +" + "-".repeat(width) + "+" + RESET);
    }

    private static void print(String color, String msg) {
        System.out.println(color + "  " + msg + RESET);
    }

    private static String readString(String prompt) {
        System.out.print("  " + prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        System.out.print("  " + prompt);
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private static double readDouble(String prompt) {
        System.out.print("  " + prompt);
        try { return Double.parseDouble(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return 0.0; }
    }

    private static String colorStatus(PackageStatus status) {
        return switch (status) {
            case PENDING    -> YELLOW + "PENDING"    + RESET;
            case SHIPPED    -> BLUE   + "SHIPPED"    + RESET;
            case IN_TRANSIT -> CYAN   + "IN_TRANSIT" + RESET;
            case DELIVERED  -> GREEN  + "DELIVERED"  + RESET;
        };
    }

    private static void printBanner() {
        System.out.println(BOLD + CYAN);
        System.out.println("  ┌──────────────────────────────────┐");
        System.out.println("  │             JExpress             │");
        System.out.println("  └──────────────────────────────────┘");
        System.out.println(RESET);
    }
}