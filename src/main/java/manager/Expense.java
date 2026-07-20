package manager;

import java.time.LocalDate;


public class Expense {
    private int id;
    private String description;
    private double amount;
    private LocalDate date;

    public Expense(int id , String description, double amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = LocalDate.now();
    }
    // Getters
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    @Override
    public String toString() {
        return "[" + id + "] " + date + " - " + description + " - $" + amount;
    }
}
