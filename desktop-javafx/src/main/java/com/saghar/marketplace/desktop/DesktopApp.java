package com.saghar.marketplace.desktop;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DesktopApp extends Application {

    private static final String DEFAULT_BASE = "http://localhost:8080";

    @Override
    public void start(Stage stage) {
        TextField baseUrl = new TextField(DEFAULT_BASE);
        baseUrl.setPrefColumnCount(25);
        Button load = new Button("Load Products");
        TextArea output = new TextArea();
        output.setWrapText(true);

        load.setOnAction(e -> {
            try {
                String url = baseUrl.getText().trim() + "/api/products?page=0&size=10";
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                output.setText("HTTP " + res.statusCode() + "\n\n" + res.body());
            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        HBox top = new HBox(8, new Label("API Base:"), baseUrl, load);
        top.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setCenter(output);
        BorderPane.setMargin(output, new Insets(10));

        stage.setTitle("Marketplace Desktop Demo (JavaFX)");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
