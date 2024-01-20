package com.example.database;
import com.example.database.DataStructure.MyHashMap;
import com.example.database.DataStructure.MyLinkedList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;



public class DatabaseEngine implements Serializable {
    private static DatabaseEngine instance;

    MyHashMap<String, DatabaseItem<?>> databaseSpine = new MyHashMap<>();

    public DatabaseEngine() {

    }

    public static void setInstance(DatabaseEngine engineInstance) {
        instance = engineInstance;
    }

    public static DatabaseEngine getInstance() {
        if (instance == null) {
            instance = new DatabaseEngine();
        }
        return instance;
    }

    public void insertData(String dataType, String index, DatabaseItem<?> data) throws ParseException {

        switch (dataType) {
            case "String" -> {
                if (data.getValue() instanceof String) {
                    databaseSpine.put(index, new StringItem(data.getValue().toString()));
                } else {
                    showErrorDialog("Invalid value for String type. Please try again!");
                }
            }
            case "Character" -> {
                if (data.getValue().toString().length() == 1) {
                    databaseSpine.put(index, new CharItem(data.getValue().toString().charAt(0)));
                } else {
                    showErrorDialog("Invalid character input. Please try again!");
                }
            }
            case "Integer" -> {
                if (data.getValue().toString().matches("\\d+")) {
                    int intValue = Integer.parseInt(data.getValue().toString());
                    databaseSpine.put(index, new NumericItem(intValue));
                } else {
                    showErrorDialog("Invalid integer input. Please enter a valid integer!");
                }
            }
            case "Double" -> {
                if (data.getValue() instanceof Number) {
                    double numValue = ((Number) data.getValue()).doubleValue();
                    databaseSpine.put(index, new NumericItem(numValue));
                } else {
                    showErrorDialog("Invalid value for Double type. Please enter a valid number!");
                }
            }
            case "Boolean" -> {
                if (data.getValue() instanceof Boolean) {
                    boolean boolValue = (Boolean) data.getValue();
                    databaseSpine.put(index, new BooleanItem(boolValue));
                } else {
                    showErrorDialog("Invalid value for Boolean type. Please enter a valid boolean!");
                }
            }
            case "Collections" -> {
                if (data.getValue() instanceof Object[]) {
                    databaseSpine.put(index, new CollectionItem<>((Object[]) data.getValue()));
                } else {
                    showErrorDialog("Invalid collection input. Please try again!");
                }
            }
            case "Date" -> {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date dateValue = dateFormat.parse(data.getValue().toString());
                    databaseSpine.put(index, new DateItem(dateValue));
                } catch (ParseException e) {
                    showErrorDialog("Invalid date format. Please enter a valid date!");
                }
            }
            default -> showErrorDialog("Unknown data type. Please try again!");
        }
    }


    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error");
        alert.showAndWait();
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

    // Save the data to a file
    public void saveData(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(this);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving data.");
        }
    }

    // Load the data from a file
    public static DatabaseEngine loadData(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            DatabaseEngine databaseEngine = (DatabaseEngine) inputStream.readObject();
            System.out.println("Data loaded successfully.");
            return databaseEngine;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading data. Returning a new DatabaseEngine instance.");
            return new DatabaseEngine();
        }
    }

    // Debug function
    public String displayAllData() {
        MyLinkedList<String> keys = databaseSpine.keySet();
        System.out.println("Display the keys: " + keys);
        StringBuilder result = new StringBuilder();

        if(databaseSpine.isEmpty()){
            result.append("\n[Database] Database is empty!\n");
        }else{
            result.append("\nDisplay all data:\n");
            result.append(String.format("%-15s  %-15s  %-15s\n", "index", "type", "value"));
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                DatabaseItem<?> item = databaseSpine.get(key);
                result.append(String.format("%-15s  %-15s  %-15s\n", key, item.getType(), item.toString()));

            }
        }

        return result.toString();
    }


}

