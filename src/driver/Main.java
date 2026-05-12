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

    private static void printTableHeader(String[] cols, int[] widths) {
        StringBuilder sep = new StringBuilder("  +");
        StringBuilder row = new StringBuilder("  |");
        for (int i = 0; i < cols.length; i++) {
            sep.append("-".repeat(widths[i] + 2)).append("+");
            row.append(" ").append(BOLD).append(pad(cols[i], widths[i])).append(RESET).append(" |");
        }
        System.out.println(GRAY + sep + RESET);
        System.out.println(row);
        System.out.println(GRAY + sep + RESET);
    }

    private static void printTableRow(String[] vals, int[] widths) {
        StringBuilder row = new StringBuilder("  |");
        for (int i = 0; i < vals.length; i++)
            row.append(" ").append(pad(vals[i], widths[i])).append(" |");
        System.out.println(row);
    }

    private static void printTableFooter(int[] widths) {
        StringBuilder sep = new StringBuilder("  +");
        for (int w : widths) sep.append("-".repeat(w + 2)).append("+");
        System.out.println(GRAY + sep + RESET);
    }

    private static String pad(String s, int width) {
        if (s == null) s = "";
        if (s.length() > width) s = s.substring(0, width - 1) + ".";
        return s + " ".repeat(Math.max(0, width - s.length()));
    }

    private static boolean loginAdmin() {
        printHeader("LOGIN ADMIN");
        String username = readString("Username : ");
        String password = readString("Password : ");
        try {
            if (AdminAuth.login(username, password)) {
                print(GREEN, "Selamat datang, " + BOLD + username + RESET + GREEN + "!");
                return true;
            }
            print(RED, "Username atau password salah.");
        } catch (SQLException e) {
            System.err.println(RED + "Error: " + e.getMessage() + RESET);
        }
        return false;
    }

    private static void daftarAdmin() {
        printHeader("DAFTAR AKUN ADMIN");
        try {
            String username = readString("Username : ");
            if (AdminAuth.isUsernameExists(username)) {
                print(RED, "Username sudah dipakai."); return;
            }
            String password = readString("Password : ");
            AdminAuth.register(username, password);
            print(GREEN, "Akun admin berhasil dibuat! Silakan login.");
        } catch (SQLException e) {
            System.err.println(RED + "Gagal: " + e.getMessage() + RESET);
        }
    }

    private static void tambahCustomer() throws SQLException {
        printHeader("TAMBAH CUSTOMER");
        String name  = readString("Nama  : ");
        String phone = readString("No HP : ");
        Customer c = new Customer(name, phone);
        customerMapper.insert(c);
        print(GREEN, "Customer ditambahkan! ID: " + c.getId());
    }

    private static void lihatCustomer() throws SQLException {
        List<Customer> list = customerMapper.findAll();
        printHeader("DAFTAR CUSTOMER (" + list.size() + ")");
        if (list.isEmpty()) { print(GRAY, "Belum ada customer."); return; }
        printTableHeader(new String[]{"ID", "Nama", "No HP"}, new int[]{4, 22, 16});
        for (Customer c : list)
            printTableRow(new String[]{String.valueOf(c.getId()), c.getName(), c.getPhone()},
                    new int[]{4, 22, 16});
        printTableFooter(new int[]{4, 22, 16});
    }

    private static void tambahPaket() throws SQLException {
        List<Customer> customers = customerMapper.findAll();
        if (customers.isEmpty()) { print(RED, "Tambah customer dulu."); return; }

        printHeader("TAMBAH PAKET");
        printTableHeader(new String[]{"ID", "Nama", "No HP"}, new int[]{4, 22, 16});
        for (Customer c : customers)
            printTableRow(new String[]{String.valueOf(c.getId()), c.getName(), c.getPhone()},
                    new int[]{4, 22, 16});
        printTableFooter(new int[]{4, 22, 16});

        int    custId = readInt("Pilih ID Customer  : ");
        String id     = readString("ID Paket (PKT-xxx) : ");
        String sender = readString("Pengirim           : ");
        String recv   = readString("Penerima           : ");
        String dest   = readString("Kota Tujuan        : ");
        double weight = readDouble("Berat (kg)         : ");
        String origin = readString("Kota Asal          : ");

        System.out.println("  " + CYAN + "1" + RESET + "=Regular  "
                + BLUE + "2" + RESET + "=Express  "
                + PURPLE + "3" + RESET + "=Fragile");

        Package pkg = switch (readInt("Pilih jenis        : ")) {
            case 2 -> new ExpressPackage(id, sender, recv, dest, weight);
            case 3 -> new FragilePackage(id, sender, recv, dest, weight);
            default -> new RegularPackage(id, sender, recv, dest, weight);
        };

        packageMapper.insert(pkg, custId);
        packageLogMapper.insert(new PackageLog(id, PackageStatus.PENDING, origin));
        print(GREEN, "Paket berhasil ditambahkan!");
        System.out.println(YELLOW + "  Ongkir    : Rp" + String.format("%.0f", pkg.calculateShippingCost()) + RESET);
        System.out.println(CYAN   + "  Est. tiba : " + pkg.getEstimasiTiba() + RESET);
    }

    private static void lihatSemuaPaket() throws SQLException {
        List<Package> list = packageMapper.findAll();
        printHeader("SEMUA PAKET (" + list.size() + ")");
        if (list.isEmpty()) { print(GRAY, "Belum ada paket."); return; }
        printTableHeader(new String[]{"ID Paket", "Tipe", "Pengirim", "Penerima", "Tujuan", "Status"},
                new int[]{10, 8, 16, 16, 14, 11});
        for (Package p : list)
            printTableRow(new String[]{p.getId(), p.getPackageType(), p.getSenderName(),
                    p.getReceiverName(), p.getDestination(), p.getStatus().name()},
                    new int[]{10, 8, 16, 16, 14, 11});
        printTableFooter(new int[]{10, 8, 16, 16, 14, 11});
    }

    private static void rekapPerCustomer() throws SQLException {
        Map<String, List<Package>> grouped = packageMapper.findAllGroupedByCustomer();
        printHeader("REKAP PAKET PER CUSTOMER");
        if (grouped.isEmpty()) { print(GRAY, "Belum ada data."); return; }

        grouped.forEach((name, packages) -> {
            double total = packages.stream().mapToDouble(Package::calculateShippingCost).sum();
            System.out.println("\n" + BOLD + CYAN + "  " + name + RESET
                    + GRAY + " (" + packages.size() + " paket)" + RESET);
            printTableHeader(new String[]{"ID", "Tipe", "Tujuan", "Status", "Ongkir"},
                    new int[]{10, 8, 14, 11, 12});
            for (Package p : packages)
                printTableRow(new String[]{p.getId(), p.getPackageType(), p.getDestination(),
                        p.getStatus().name(), "Rp" + String.format("%.0f", p.calculateShippingCost())},
                        new int[]{10, 8, 14, 11, 12});
            printTableFooter(new int[]{10, 8, 14, 11, 12});
            System.out.println(YELLOW + "  Total ongkir: Rp" + String.format("%.0f", total) + RESET);
        });
    }

    private static void updateStatus() throws SQLException {
        printHeader("UPDATE STATUS PAKET");
        String id = readString("ID Paket : ");

        Package pkg = packageMapper.findById(id);
        if (pkg == null) { print(RED, "Paket tidak ditemukan."); return; }

        System.out.println(GRAY + "  Status saat ini: " + RESET + colorStatus(pkg.getStatus()));
        System.out.println("  " + BLUE  + "1" + RESET + ". SHIPPED     (sudah dikirim)");
        System.out.println("  " + CYAN  + "2" + RESET + ". IN_TRANSIT  (dalam perjalanan)");
        System.out.println("  " + GREEN + "3" + RESET + ". DELIVERED   (sudah sampai)");

        PackageStatus newStatus = switch (readInt("Status baru : ")) {
            case 1 -> PackageStatus.SHIPPED;
            case 2 -> PackageStatus.IN_TRANSIT;
            case 3 -> PackageStatus.DELIVERED;
            default -> null;
        };
        if (newStatus == null) { print(RED, "Pilihan tidak valid."); return; }

        String location = readString("Lokasi saat ini : ");
        packageMapper.updateStatus(id, newStatus);
        packageLogMapper.insert(new PackageLog(id, newStatus, location));
        print(GREEN, "Status diperbarui ke " + newStatus + " | " + location);
    }
}