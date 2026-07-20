package manager;

import java.time.LocalDate;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        ArrayList<Expense> expenses = ExpenseStorage.loadExpense();
        if(args.length == 0) {
            System.out.println("No argument provided: " + args.length);
            return;
        }

        String commands = args[0];



        switch (commands) {
            case "add":
                if(args.length < 2) {
                    System.out.println("Insufficient arguments");
                    break;
                }

                String description = getArgValue(args, "--description");
                String amountStr = getArgValue(args, "--amount");
                if(description == null || amountStr == null ) {
                    System.out.println("Description and amount must be provided.");
                    break;
                }
                try{
                    double amount = Double.parseDouble(amountStr);
                    if (amount <= 0) {
                        System.out.println("Amount cannot be a negative value");
                        break;
                    }
                    int id = getNextId(expenses);
                    Expense expense = new Expense(id, description, amount);
                    expenses.add(expense);
                    ExpenseStorage.saveExpense(expenses);
                    System.out.println("Expense added successfully (ID: " + expense.getId() + ")");
                } catch (NumberFormatException e) {
                    System.out.println("Error: '" + amountStr + "' is not a valid number.");
                    break;
                }
                break;
            case "list":
                for (Expense e: expenses) {
                    System.out.println(e);
                }
                break;

            case "delete":
                if(args.length < 2) {
                    System.out.println("Insufficient arguments");
                    break;
                }

                String deleteIdStr = getArgValue(args, "--id");

                int deleteId;
                try {
                    deleteId = Integer.parseInt(deleteIdStr);
                } catch (NumberFormatException e) {
                    System.out.println("Error: '" + deleteIdStr + "' is not a valid id.");
                    break;
                }

                Expense expenseToDelete = findExpenseById(expenses, deleteId);
                if (expenseToDelete == null) {
                    System.out.println("No expense found with this ID: " + deleteId);
                    break; // <-- this was missing
                }
                expenses.remove(expenseToDelete);
                ExpenseStorage.saveExpense(expenses);
                System.out.println("Expense with Id: " + deleteId + " Deleted successfully");
                break;
            case "update":
                if (args.length < 2) {
                    System.out.println("Insufficient arguments");
                    break;
                }

                String updateIdStr = getArgValue(args, "--id");
                if (updateIdStr == null) {
                    System.out.println("Error: --id is required.");
                    break;
                }

                int updateId;
                try {
                    updateId = Integer.parseInt(updateIdStr);
                } catch (NumberFormatException e) {
                    System.out.println("Error: '" + updateIdStr + "' is not a valid id.");
                    break;
                }

                Expense expenseToUpdate = findExpenseById(expenses, updateId);
                if (expenseToUpdate == null) {
                    System.out.println("No expense found with this ID: " + updateId);
                    break;
                }

                String newDescription = getArgValue(args, "--description");
                String newAmountStr = getArgValue(args, "--amount");

                if (newDescription == null && newAmountStr == null) {
                    System.out.println("Error: provide --description and/or --amount to update.");
                    break;
                }

                if (newDescription != null) {
                    expenseToUpdate.setDescription(newDescription);
                }

                if (newAmountStr != null) {
                    try {
                        double newAmount = Double.parseDouble(newAmountStr);
                        if (newAmount <= 0) {
                            System.out.println("Amount cannot be zero or negative.");
                            break;
                        }
                        expenseToUpdate.setAmount(newAmount);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: '" + newAmountStr + "' is not a valid number.");
                        break;
                    }
                }

                ExpenseStorage.saveExpense(expenses);
                System.out.println("Expense with Id: " + updateId + " Updated successfully");
                break;
            case "summary":
                if (args.length < 2) {
                    double totalExpense;
                    totalExpense = expenses.stream()
                            .mapToDouble(Expense:: getAmount)
                                    .sum();
                    System.out.println("Total expenses: $" + totalExpense);


                } else {
                    String filterStr = getArgValue(args, "--month");
                    if (filterStr == null) {
                        System.out.println("Error: --month requires a value.");
                        break;
                    }
                    int filter;
                    try {
                        filter = Integer.parseInt(filterStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: '" + filterStr + "' is not a valid number.");
                        break;
                    }
                    if (filter < 1 || filter > 12) {
                        System.out.println("Error: month must be between 1 and 12.");
                        break;
                    }

                    double monthTotal = 0;
                    int currentYear = LocalDate.now().getYear();

                    for (Expense e : expenses) {
                        if (e.getDate().getMonthValue() == filter && e.getDate().getYear() == currentYear) {
                            monthTotal += e.getAmount();
                        }
                    }

                    System.out.println("Total expenses for month " + filter + ": $" + monthTotal);

                }
                break;

            default:
                System.out.println("Unrecognized command");
        }

    }

    public static String getArgValue(String[] args, String flag) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(flag)) {
                return  args[i + 1];
            }

        }
        return  null;
    }

    private static int getNextId(ArrayList<Expense> expenses) {
        int maxId = 0;
        for (Expense expense: expenses) {
            if (expense.getId() > maxId) {
                maxId = expense.getId();
            }
        }
        return  maxId + 1;
    }

    private static Expense findExpenseById(ArrayList<Expense> expenses, int id) {
        for (Expense expense: expenses) {
            if (expense.getId() == id) {
                return  expense;
            }
        }
        return  null;
    }
}
