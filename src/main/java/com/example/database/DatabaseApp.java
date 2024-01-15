package com.example.database;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.concurrent.atomic.AtomicBoolean;

public class DatabaseApp extends Application {
    private DatabaseEngine databaseEngine;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        databaseEngine = new DatabaseEngine();

        primaryStage.setTitle("My Simple Database Engine");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        // Create a label for displaying the prompt

        Label promptLabel = new Label(
                "Welcome to My Simple Database Engine!\n" +
                        "Please enter command:\n" +
                        "- insert   ---> Insert new data\n" +
                        "- display  ---> Display all data\n" +
                        "- get      ---> Get a specific data\n" +
                        "- delete   ---> Delete a specific data\n" +
                        "- clear    ---> Clear all data stored"
        );

        // Set font and text alignment
        //promptLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        promptLabel.setAlignment(javafx.geometry.Pos.CENTER);

        GridPane.setConstraints(promptLabel, 0, 0, 2, 1);

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setFont(Font.font("Courier New"));
        GridPane.setConstraints(outputArea, 0, 1, 4, 3);
        // GridPane.setConstraints(outputArea, 0, 1, 4, 3);

        TextField commandInput = new TextField();

        commandInput.setPromptText("Enter command");
        GridPane.setConstraints(commandInput, 0, 4);

        Button submitButton = new Button("Submit");
        GridPane.setConstraints(submitButton, 1, 4);

        grid.getChildren().addAll(promptLabel, outputArea, commandInput, submitButton);

        submitButton.setOnAction(e -> handleCommand(commandInput.getText(), outputArea));

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleCommand(String command, TextArea outputArea) {
        // Implement logic to handle different commands
        switch (command.toLowerCase()) {
            case "insert":
                AtomicBoolean exitLoop = new AtomicBoolean(false);
                // Prompt the user for data type, data, and index
                do {
                    TextInputDialog dataTypeDialog = new TextInputDialog();
                    dataTypeDialog.setHeaderText("Enter data type: \n--> 'str' for string\n--> 'c' for character\n--> 'num' for number\n--> 'coll' for collection\n--> 'dt' for date\n--> 'bool' for boolean\n--> 'X' for exit");
                    dataTypeDialog.showAndWait().ifPresent(dataType -> {
                        if (dataType.equalsIgnoreCase("X")) {
                            outputArea.appendText("[Database] Exiting...\n");
                            exitLoop.set(true);
                        } else if (dataType.equalsIgnoreCase("str") || dataType.equalsIgnoreCase("c") || dataType.equalsIgnoreCase("num") || dataType.equalsIgnoreCase("dt") || dataType.equalsIgnoreCase("coll")|| dataType.equalsIgnoreCase("bool")) {
                            TextInputDialog dataDialog = getTextInputDialog(dataType);

                            dataDialog.showAndWait().ifPresent(data -> {

                                TextInputDialog indexDialog = new TextInputDialog();
                                indexDialog.setHeaderText("Enter index:");
                                indexDialog.showAndWait().ifPresent(index -> {
                                    String errorMessage = null;

                                    if (dataType.equalsIgnoreCase("coll")) {
                                        // Handle collection data separately
                                        Object[] parsedData = parseCollectionData(data);
                                        errorMessage = databaseEngine.insertData(dataType, index, new DatabaseItem<>(parsedData));
                                    } else {
                                        // Handle other data types
                                        errorMessage = databaseEngine.insertData(dataType, index, new DatabaseItem<>(data));
                                    }

                                    if (errorMessage != null) {
                                        outputArea.appendText(errorMessage + "\n");
                                    } else {
                                        outputArea.appendText("[Database] Inserting...\n");
                                        outputArea.appendText("[Database] Done!\n");
                                    }
                                });

                            });
                        } else {
                            outputArea.appendText("[Database] Error, unknown data type! Please try again\n");
                        }
                    });
                } while (!exitLoop.get());
                break;
            case "display":
                // Implement logic to display all data
                outputArea.setText(databaseEngine.displayAllData());
                break;
            case "get":
                // Implement logic to get specific data
                // Prompt the user for the index to get data
                TextInputDialog getDialog = new TextInputDialog();
                getDialog.setHeaderText("Enter index to get data:");
                getDialog.showAndWait().ifPresent(index -> {
                    DatabaseItem<?> getItem = databaseEngine.getData(index);
                    if (getItem != null) {
                        outputArea.appendText("[Database] Data at index " + index + ": " + getItem.toString() + "\n");
                    } else {
                        outputArea.appendText("[Database] No data found at index " + index + "\n");
                    }
                });
                break;
            case "delete":
                // Implement logic to delete specific data
                TextInputDialog deleteDialog = new TextInputDialog();
                deleteDialog.setHeaderText("Enter index to delete data:");
                deleteDialog.showAndWait().ifPresent(index -> {
                    DatabaseItem<?> deletedItem = databaseEngine.deleteData(index);
                    if (deletedItem != null) {
                        outputArea.appendText("[Database] Deleted data at index " + index + ": " + deletedItem.toString() + "\n");
                    } else {
                        outputArea.appendText("[Database] No data found at index " + index + ". Deletion failed.\n");
                    }
                });
                break;
            case "clear":
                // Implement logic to clear all data
                if (databaseEngine.clearAllData()) {
                    outputArea.appendText("[Database] Cleared all data.\n");
                } else {
                    outputArea.appendText("[Database] Database is empty, no data to clear.\n");
                }
                break;
            default:
                outputArea.appendText("[Database] Unknown command. Please try again.\n");
        }
    }

    private static TextInputDialog getTextInputDialog(String dataType) {
        TextInputDialog dataDialog = new TextInputDialog();
        if (dataType.equalsIgnoreCase("coll")) {
            dataDialog.setHeaderText("Enter collection data (format: [item1, item2, ...]):");
        } else if(dataType.equalsIgnoreCase("dt")) {
            dataDialog.setHeaderText("Enter date (format: yyyy-MM-dd):");
        } else {
            dataDialog.setHeaderText("Enter data:");
        }
        return dataDialog;
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
            throw new IllegalArgumentException("Invalid collection data format");
        }

        return collectionData;
    }
}