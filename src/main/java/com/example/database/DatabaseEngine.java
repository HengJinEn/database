package com.example.database;
import com.example.database.DataStructure.MyHashMap;
import com.example.database.DataStructure.MyLinkedList;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DatabaseEngine {

    private static final String DATABASE_FILE_PATH = "database.dat";
    private MyHashMap<String, DatabaseItem<?>> databaseSpine = new MyHashMap<>();

    public DatabaseEngine() {
        this.databaseSpine = loadDatabaseFromDisk();
    }

    public String insertData(String dataType,String index, DatabaseItem<?> data) {

        switch (dataType) {
            case "str":
                databaseSpine.put(index, new StringItem(data.getValue().toString()));
                break;
            case "c":
                if (data.getValue().toString().length() == 1) {
                    databaseSpine.put(index, new CharItem(data.getValue().toString().charAt(0)));
                } else {
                    return "[Database] Error! Character data must be a single character. Please try again!";
                }
                break;
            case "num":
                try {
                    // If it's a whole number, insert as Integer
                    if (data.getValue().toString().matches("\\d+")) {
                        int intValue = Integer.parseInt(data.getValue().toString());
                        databaseSpine.put(index, new NumericItem(intValue));
                    } else {
                        // Otherwise, insert as Double
                        double numValue = Double.parseDouble(data.getValue().toString());
                        databaseSpine.put(index, new NumericItem(numValue));
                    }

                } catch (NumberFormatException e) {
                    return "[Database] Error! Not a number. Please try again!\n";
                }
                break;
            case "coll":
                // Check if the data is an array
                if (data.getValue() instanceof Object[]) {
                    databaseSpine.put(index, new CollectionItem<>((Object[]) data.getValue()));
                } else {
                    return "[Database] Error! Collection data must be an array. Please try again!\n";
                }
                break;
            case "dt":
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Change the date format as needed
                    Date dateValue = dateFormat.parse(data.getValue().toString());
                    databaseSpine.put(index, new DateItem(dateValue));
                } catch (ParseException e) {
                    return "[Database] Error! Invalid date format. Please try again!\n";
                }
                break;
            case "bool":
                boolean boolValue = Boolean.parseBoolean(data.getValue().toString());
                databaseSpine.put(index, new BooleanItem(boolValue));
                break;
            default:
                return "[Database] Unknown data type. Please try again!\n";

        }

        return null;
    }


    public DatabaseItem<?> getData(String index) {
        return databaseSpine.get(index);
    }

    public DatabaseItem<?> deleteData(String index) {
        return databaseSpine.remove(index);
    }

    public boolean clearAllData() {
        if(databaseSpine.isEmpty()){
            return false;
        }
        databaseSpine.clear();
        return true;
    }

    public String displayData(String key){
        DatabaseItem<?> item = databaseSpine.get(key);
        return key + " " + item.toString();
    }
    public String displayAllData() {
        MyLinkedList<String> keys = databaseSpine.keySet();

        StringBuilder result = new StringBuilder();

        if(databaseSpine.isEmpty()){
            result.append("\n[Database] Database is empty!\n");
        }else{
            result.append("\nDisplay all data:\n");
            result.append(String.format("%-15s  %-15s  %-15s\n", "index", "type", "value"));
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                DatabaseItem<?> item = databaseSpine.get(key);

                // Append key, type, and value to the result
                result.append(String.format("%-15s  %-15s  %-15s\n", key, item.getType(), item.toString()));


                //result.append(key).append(" ").append(item.toString()).append("\n");
            }
        }

        return result.toString();
    }

    public void createTable(String tableName) {
        // You can implement table creation logic here
        // For simplicity, let's assume each table is just a separate HashMap
        MyHashMap<String, DatabaseItem<?>> table = new MyHashMap<>();
        // Add additional logic as needed

        // Save the updated data to disk
        saveDatabaseToDisk();
    }

    private void saveDatabaseToDisk() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATABASE_FILE_PATH))) {
            oos.writeObject(databaseSpine);
        } catch (IOException e) {
            // Handle exceptions (e.g., unable to write to file)
        }
    }

    private MyHashMap<String, DatabaseItem<?>> loadDatabaseFromDisk() {
        MyHashMap<String, DatabaseItem<?>> loadedData = new MyHashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATABASE_FILE_PATH))) {
            loadedData = (MyHashMap<String, DatabaseItem<?>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions (e.g., file not found, corrupted data)
        }

        return loadedData;
    }


}

