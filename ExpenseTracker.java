import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A simple console Expense Tracker.
 * Features: add, view, delete, total, total by category, highest expense.
 * Data is saved in expenses.txt so it stays after the program closes.
 */
public class ExpenseTracker {

    private static final String FILE_NAME = "expenses.txt";
    private static ArrayList<Expense> expenses = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    viewAllExpenses();
                    break;
                case 3:
                    deleteExpense();
                    break;
                case 4:
                    showTotal();
                    break;
                case 5:
                    showTotalByCategory();
                    break;
                case 6:
                    showHighestExpense();
                    break;
                case 7:
                    saveToFile();
                    System.out.println("Saved. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Please choose a number from 1 to 7.");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("===== Expense Tracker =====");
        System.out.println("1. Add expense");
        System.out.println("2. View all expenses");
        System.out.println("3. Delete an expense");
        System.out.println("4. Show total spent");
        System.out.println("5. Show total by category");
        System.out.println("6. Show highest expense");
        System.out.println("7. Save and exit");
    }

    // ---------- Features ----------

    private static void addExpense() {
        double amount = readPositiveDouble("Amount: ");

        System.out.print("Category (for example Food, Travel, Books): ");
        String category = scanner.nextLine().trim();
        if (category.isEmpty()) {
            category = "Other";
        }

        LocalDate date = readDate("Date (YYYY-MM-DD, press Enter for today): ");

        System.out.print("Note (optional): ");
        String note = scanner.nextLine().trim().replace("|", "/");

        expenses.add(new Expense(amount, category, date, note));
        saveToFile(); // save right away so data is not lost
        System.out.println("Expense added.");
    }

    private static void viewAllExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }
        System.out.println();
        System.out.printf("%-4s %-12s %-12s %10s  %s%n", "No.", "Date", "Category", "Amount", "Note");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.printf("%-4d %s%n", i + 1, expenses.get(i));
        }
    }

    private static void deleteExpense() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses to delete.");
            return;
        }
        viewAllExpenses();
        int number = readInt("Enter the number to delete: ");
        if (number < 1 || number > expenses.size()) {
            System.out.println("That number does not exist.");
            return;
        }
        Expense removed = expenses.remove(number - 1);
        saveToFile();
        System.out.println("Deleted: " + removed);
    }

    private static void showTotal() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        System.out.printf("Total spent: %.2f%n", total);
    }

    // A HashMap stores one total for each category name.
    private static void showTotalByCategory() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }
        HashMap<String, Double> totals = new HashMap<>();
        for (Expense e : expenses) {
            String key = e.getCategory().toLowerCase();
            totals.put(key, totals.getOrDefault(key, 0.0) + e.getAmount());
        }
        System.out.println("Total by category:");
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            System.out.printf("  %-12s %10.2f%n", entry.getKey(), entry.getValue());
        }
    }

    private static void showHighestExpense() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }
        Expense highest = expenses.get(0);
        for (Expense e : expenses) {
            if (e.getAmount() > highest.getAmount()) {
                highest = e;
            }
        }
        System.out.println("Highest expense:");
        System.out.println(highest);
    }

    // ---------- File handling ----------

    private static void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                writer.println(e.toFileLine());
            }
        } catch (IOException e) {
            System.out.println("Could not save the file: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Expense e = Expense.fromFileLine(line);
                if (e != null) {
                    expenses.add(e);
                }
            }
        } catch (IOException e) {
            // First run: the file does not exist yet. That is fine.
        }
    }

    // ---------- Safe input helpers ----------

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readPositiveDouble(String message) {
        while (true) {
            System.out.print(message);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Amount must be more than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static LocalDate readDate(String message) {
        while (true) {
            System.out.print(message);
            String text = scanner.nextLine().trim();
            if (text.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(text);
            } catch (Exception e) {
                System.out.println("Use the format YYYY-MM-DD, for example 2026-11-05.");
            }
        }
    }
}
