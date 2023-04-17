package com.example.charliebot;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class browser extends Application {

    private BorderPane root;
    private WebView webView;
    private WebEngine webEngine;
    private TextField addressBar;
    private Button backButton;
    private Button forwardButton;
    private Button reloadButton;
    private Button homeButton;
    private ProgressIndicator loadingIndicator;
    private Stage primaryStage;

    static String l = ChatBot.getUrl();


    public void initUI() {
        root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);
        webView = new WebView();
        webEngine = webView.getEngine();
        addressBar = new TextField();
        backButton = new Button("<");
        forwardButton = new Button(">");
        reloadButton = new Button("Reload");
        homeButton = new Button("Home");
        loadingIndicator = new ProgressIndicator();

        loadingIndicator.progressProperty().bind(webEngine.getLoadWorker().progressProperty());

       webEngine.load(l);
        addressBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String url = addressBar.getText().trim();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                webEngine.load(url);
            }
        });

        backButton.setOnAction(event -> {
            if (webEngine.getHistory().getCurrentIndex() > 0) {
                webEngine.getHistory().go(-1);
            }
        });

        forwardButton.setOnAction(event -> {
            if (webEngine.getHistory().getCurrentIndex() < webEngine.getHistory().getEntries().size() - 1) {
                webEngine.getHistory().go(1);
            }
        });

        reloadButton.setOnAction(event -> {
            webEngine.reload();
        });

        homeButton.setOnAction(event -> {
//            webEngine.load("https://www.google.com");
            webEngine.load(l);
        });

        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            addressBar.setText(newValue);
        });

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(backButton, forwardButton, reloadButton, homeButton, addressBar, loadingIndicator);
        toolBar.setPadding(new Insets(5, 5, 5, 5));

        root.setTop(toolBar);
        root.setCenter(webView);

        primaryStage.setTitle("Charlie Browser");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initUI();
    }



    public static void main(String[] args) {
        launch(args);
    }
}

