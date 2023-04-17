package com.example.charliebot;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URLEncoder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatBot extends Application {
    public String str;
    private TextArea chatArea;
    private TextField inputField;
    private Button sendButton;

    static  String lurl;

    @Override
    public void start(Stage primaryStage)throws Exception {
//        browser myBrowser = new browser();
//        myBrowser.start(new Stage());
        BorderPane root = new BorderPane();
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setFont(new Font("Arial", 14));

        ScrollPane scrollPane = new ScrollPane(chatArea);
        scrollPane.setFitToWidth(true);

        inputField = new TextField();
        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                processInput();
            }
        });

        sendButton = new Button("Send");
        sendButton.setOnAction(event -> processInput());

        HBox inputPanel = new HBox(inputField, sendButton);
        inputPanel.setSpacing(5);
        inputPanel.setPadding(new Insets(5, 5, 5, 5));

        root.setCenter(scrollPane);
        root.setBottom(inputPanel);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Charlie");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void processInput() {
        String input = inputField.getText();
        chatArea.appendText("You: " + input + "\n");

        if (input.trim().isEmpty()) {
            return;
        }

        String response = getResponse(input);

        if (response == null) {
            response = "I'm sorry, I don't understand.";
        }

        chatArea.appendText("Title: " + response + "\n");
        inputField.setText("");

    }

    private String getResponse(String input) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = "https://www.google.com/search?q=" + URLEncoder.encode(input, "UTF-8");

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36")
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            Document document = Jsoup.parse(responseBody);
            Elements results = document.select("div.g");

            double bestScore = 0.0;
            String bestResult = "";

            for (Element result : results) {
                String title = result.select("h3").text();
                String snippet = result.select("div.s").text();
                String link = result.select("a").attr("href");
                double score = computeScore(input, title, snippet);
                if (score > bestScore) {
                    bestScore = score;
                    System.out.println(bestScore);
                    bestResult = title + "\n" + snippet + "\n" + link + "\n";
                    lurl = new String(link);
//                    callBrowser();
                }
                System.out.println(" "+title + "\n" + snippet + "\n" + link + "\n");
            }

            if (bestScore > 0.0) {
                return bestResult;
            } else {
                return "I'm sorry, I don't understand.";
            }
        } catch (IOException e) {
            System.err.println("Failed to perform Google search: " + e.getMessage());
            return "Sorry, I was unable to search for \"" + input + "\".";
        } catch (Exception e) {
            System.err.println("An error occurred while searching for \"" + input + "\": " + e.getMessage());
            return "Sorry, an error occurred while searching for \"" + input + "\".";
        }
    }

    private void callBrowser() {
            browser myBrowser = new browser();
            myBrowser.start(new Stage());
    }


    private double computeScore(String input, String title, String snippet) {
        String text = title;
        String snip=snippet.toLowerCase();
        text = text.toLowerCase();
        input = input.toLowerCase();

        String[] inputWords = input.split("\\s+");
        double score = 0.0;

        if(snip.contains(input)){
            score+=1.0;
        }
        for (String word : inputWords) {
            if (text.contains(word)){
                score += 1.0;
            }
        }

        return score / inputWords.length;
    }

    public static String getUrl(){
        return lurl;
    }

    public static void main(String[] args) {
//        Application.launch(args);
        launch(args);

    }
}