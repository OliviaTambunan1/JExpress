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

    private static void printBanner() {
        System.out.println(BOLD + CYAN);
        System.out.println("  ┌──────────────────────────────────┐");
        System.out.println("  │             JExpress             │");
        System.out.println("  └──────────────────────────────────┘");
        System.out.println(RESET);
    }
}