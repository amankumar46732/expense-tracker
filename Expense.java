import java.time.LocalDate;

/**
 * Represents one expense: an amount, a category, a date and a short note.
 */
public class Expense {
    private double amount;
    private String category;
    private LocalDate date;
    private String note;

    public Expense(double amount, String category, LocalDate date, String note) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    // Converts the expense to one line of text so it can be saved in a file.
    // The "|" symbol separates the fields.
    public String toFileLine() {
        return amount + "|" + category + "|" + date + "|" + note;
    }

    // Builds an Expense back from a line that was saved in the file.
    // Returns null if the line is not in the right format.
    public static Expense fromFileLine(String line) {
        String[] parts = line.split("\\|", 4);
        if (parts.length < 4) {
            return null;
        }
        try {
            double amount = Double.parseDouble(parts[0]);
            LocalDate date = LocalDate.parse(parts[2]);
            return new Expense(amount, parts[1], date, parts[3]);
        } catch (Exception e) {
            return null; // skip a damaged line instead of crashing
        }
    }

    @Override
    public String toString() {
        return String.format("%-12s %-12s %10.2f  %s", date, category, amount, note);
    }
}
