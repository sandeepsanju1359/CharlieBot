module com.example.charliebot {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jsoup;
    requires javafx.web;
    requires java.desktop;
    requires okhttp;
    requires stanford.corenlp;

    opens com.example.charliebot to javafx.fxml;
    exports com.example.charliebot;
}