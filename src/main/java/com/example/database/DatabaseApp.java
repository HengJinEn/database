package com.example.database;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        GridPane.setConstraints(outputArea, 0, 0, 4, 4);

        TextField commandInput = new TextField();
        commandInput.setPromptText("Enter command");
        GridPane.setConstraints(commandInput, 0, 4);

        Button submitButton = new Button("Submit");
        GridPane.setConstraints(submitButton, 1, 4);

        grid.getChildren().addAll(outputArea, commandInput, submitButton);

        submitButton.setOnAction(e -> handleCommand(commandInput.getText(), outputArea));

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleCommand(String command, TextArea outputArea) {
        // Implement logic to handle different commands
        switch (command.toLowerCase()) {
            case "insert":
                // Implement logic to handle data insertion
                break;
            case "display":
                // Implement logic to display all data
                outputArea.setText(databaseEngine.displayAllData());
                break;
            case "get":
                // Implement logic to get specific data
                break;
            case "delete":
                // Implement logic to delete specific data
                break;
            case "clear":
                // Implement logic to clear all data
                break;
            default:
                outputArea.appendText("[Database] Unknown command. Please try again.\n");
        }
    }
}
