package com.example.database;
import com.example.database.DataStructure.MyHashMap;
import com.example.database.DataStructure.MyLinkedList;
import java.io.*;


public class DatabaseEngine {

    private static final String DATABASE_FILE_PATH = "database.dat";
    private MyHashMap<String, DatabaseItem<?>> databaseSpine = new MyHashMap<>();

    public DatabaseEngine() {
        this.databaseSpine = loadDatabaseFromDisk();
    }

    public void insertData(String dataType,String index, DatabaseItem<?> data) {


        switch (dataType) {
            case "str":
                databaseSpine.put(index, new StringItem(data.getValue().toString()));
                break;
            case "c":
                databaseSpine.put(index, new CharItem(data.getValue().toString().charAt(0)));
                break;
            case "num":
                try {
                    double numValue = Double.parseDouble(data.getValue().toString());
                    databaseSpine.put(index, new NumericItem(numValue));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
            default:

        }


    }



    public DatabaseItem<?> getData(String index) {
        return databaseSpine.get(index);
    }

    public DatabaseItem<?> deleteData(String index) {
        return databaseSpine.remove(index);
    }

    public void clearAllData() {
        databaseSpine.clear();
    }

    public String displayData(String key){
        DatabaseItem<?> item = databaseSpine.get(key);
        return key + " " + item.toString();
    }
    public String displayAllData() {
        MyLinkedList<String> keys = databaseSpine.keySet();

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            DatabaseItem<?> item = databaseSpine.get(key);

            result.append(key).append(" ").append(item.toString()).append("\n");
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

