package com.example.database;

import com.example.database.DataStructure.MyLinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable, Serializable {


    public AnchorPane pane2;
    public MenuButton typeMenu;
    public Text valueExample;
    public TextField index;
    public TextField value;
    public Button goButton;
    private static DatabaseEngine databaseEngine;
    public Text retrieveData;
    public TextField indexFromGet;
    public TextField deleteField;
    public Text displayDeletedData;
    public Text messageToClear;

    private String selectedType;

    @FXML
    private TableView<OneRow> tableView;
    @FXML
    private TableColumn<OneRow, String> indexUnit;
    @FXML
    private TableColumn<OneRow, String> typeUnit;
    @FXML
    private TableColumn<OneRow, String> valueUnit;

    @FXML
    private static ObservableList<OneRow> tableData = FXCollections.observableArrayList();

    @FXML
    public void switchToInsert(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("insert.fxml"));
        Parent insertView = loader.load();
        pane2.getChildren().setAll(insertView);
    }

    @FXML
    public void switchToGet(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("get.fxml"));
        Parent getView = loader.load();
        pane2.getChildren().setAll(getView);


    }

    @FXML
    public void switchToDelete(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("delete.fxml"));
        Parent deleteView = loader.load();
        pane2.getChildren().setAll(deleteView);

    }

    @FXML
    public void switchToClear(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("clear.fxml"));
        Parent clearView = loader.load();
        pane2.getChildren().setAll(clearView);

        // Initialize messageToClear if not injected properly
        if (messageToClear == null) {
            messageToClear = (Text) clearView.lookup("#messageToClear");
        }

        if (messageToClear != null) {
            databaseEngine.clearAllData();
            messageToClear.setText("Your database has been cleared. Click display to refresh the table.");
        } else {
            System.out.println("messageToClear not found!");
        }
    }


    @FXML
    private String handleMenuSelection(ActionEvent event) throws IOException {
        if (event.getSource() instanceof MenuItem selectedItem) {
            selectedType = selectedItem.getText();
            return selectedType;
        } else {
            System.out.println("Unexpected source: " + event.getSource());
            return "";
        }
    }


    @FXML
    private void setValueExample(ActionEvent event) throws IOException {
        String selectedType = handleMenuSelection(event);

        switch (selectedType) {
            case "String" -> {
                valueExample.setText("Example: My Simple Database");
                typeMenu.setText(selectedType); // Update MenuButton text
            }
            case "Double" -> {
                valueExample.setText("Example: 1.2");
                typeMenu.setText(selectedType); // Update MenuButton text
            }
            case "Integer" -> {
                valueExample.setText("Example: 3");
                typeMenu.setText(selectedType); // Update MenuButton text
            }
            case "Character" -> {
                valueExample.setText("Example: s (single-valued character)");
                typeMenu.setText(selectedType); // Update MenuButton text
            }
            case "Boolean" -> {
                valueExample.setText("Example: true");
                typeMenu.setText(selectedType); // Update MenuButton text
            }
            case "Date" -> {
                valueExample.setText("Example: 28-02-2024");
                typeMenu.setText(selectedType); // Update MenuButton text
            }
            case "Collections" -> {
                valueExample.setText("Example: [1,2,3] ");
                typeMenu.setText(selectedType); // Update MenuButton text
            }
            default -> valueExample.setText("Example: Default Message");
        }
    }

    private String getIndex(ActionEvent event){
        return index.getText();
    }

    private String getValue(ActionEvent event){
        return value.getText();
    }

    @FXML
    private void pressGoToInsertData(ActionEvent event) throws ParseException {

        if (selectedType == null) {
            System.out.println("Please select a type from the menu first.");
            return;
        }
        String selectedIndex = getIndex(event);
        String selectedValue = getValue(event);
        DatabaseItem<?> dataItem = createDataItem(selectedValue);

        databaseEngine.insertData(selectedType,selectedIndex,dataItem);
        index.clear();
        value.clear();
    }


    private DatabaseItem<?> createDataItem(String selectedValue) {
        if (Objects.equals(selectedType, "Collections")) {
            Object[] parsedData = parseCollectionData(selectedValue);
            return new CollectionItem<>(parsedData);
        }

       return new DatabaseItem<>(selectedValue);
    }

    private Object[] parseCollectionData(String data) {
        // Remove leading and trailing brackets
        if(data.startsWith("[")&&data.endsWith("]")) {
            data = data.substring(1, data.length() - 1);
        }

        // Split the data into items
        String[] items = data.split(", ");

        // Convert items to Object[]
        Object[] collectionData = new Object[items.length];
        for (int i = 0; i < items.length; i++) {
            collectionData[i] = items[i];
        }

        if (collectionData.length == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Collection format. Please enter a valid Collections", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        }

        return collectionData;
    }


    @FXML
    private void createTable(){

        tableData.clear();

        MyLinkedList<String> keys = databaseEngine.databaseSpine.keySet();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            DatabaseItem<?> item = databaseEngine.getData(key);
            // Check if 'item' is null before adding it to 'tableData'
            if (item != null) {
                tableData.add(new OneRow(key, item.getType(), item.toString()));
            }
        }

        indexUnit.setCellValueFactory(new PropertyValueFactory<>("index"));
        typeUnit.setCellValueFactory(new PropertyValueFactory<>("type"));
        valueUnit.setCellValueFactory(new PropertyValueFactory<>("value"));
        tableView.setItems(tableData);

    }

    @FXML
    private void pressGoToGetData(ActionEvent event){
        String index = indexFromGet.getText();
        DatabaseItem<?> databaseItem = databaseEngine.getData(index);
        String displayItem = databaseItem.toString();
        retrieveData.setText("Retrieve Data: " + displayItem);
    }

    @FXML
    private void pressGoToDeleteData(ActionEvent event) {
        String index = deleteField.getText();
        DatabaseItem<?> databaseItem = databaseEngine.deleteData(index);
        String displayItem = databaseItem.toString();
        displayDeletedData.setText("Deleted data: " + displayItem);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (databaseEngine == null) {
            databaseEngine = new DatabaseEngine();
        }
        DatabaseEngine.setInstance(databaseEngine);
        tableData = FXCollections.observableArrayList();
    }
}

