package com.example.database;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

public class DatabaseApp extends Application {

    private static final String DATABASE_FILE_NAME = "databaseHistory.dat";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        loadDatabase();

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("database-app.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(event -> saveDatabase());
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDatabase() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(DATABASE_FILE_NAME))) {
            DatabaseEngine.setInstance((DatabaseEngine) inputStream.readObject());
            System.out.println("Database loaded successfully.");
            System.out.println(DatabaseEngine.getInstance().displayAllData());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading database. Starting with a new DatabaseEngine instance.");
            DatabaseEngine.setInstance(new DatabaseEngine());
        }
    }

    private void saveDatabase() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(DATABASE_FILE_NAME))) {
            outputStream.writeObject(DatabaseEngine.getInstance());
            System.out.println("Database saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving database.");
        }
    }
}
