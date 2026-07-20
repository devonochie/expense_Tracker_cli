package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;

public class ExpenseStorage {
    private static  final String FILE_NAME = "expense.json";

    private static Gson buildJson() {
        return  new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    public static ArrayList<Expense> loadExpense() {
        Gson gson = buildJson();
        try (FileReader reader = new FileReader(FILE_NAME)) {
            Type type = new TypeToken<ArrayList<Expense>>(){}.getType();
            ArrayList<Expense> expenses = gson.fromJson(reader, type);
            return  expenses != null ? expenses : new ArrayList<>();
        } catch (IOException e) {
            return  new ArrayList<>();
        }
    }

    public static void saveExpense(ArrayList<Expense> expenses) {
        Gson gson = buildJson();
        try(FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(expenses, writer);
        }  catch (IOException e) {
            System.out.println("Unable to save expenses: " + e);
        }
    }
}
