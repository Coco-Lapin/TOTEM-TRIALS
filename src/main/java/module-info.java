module com.totemtrials.totemtrials {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens com.totemtrials.totemtrials to javafx.fxml;
    exports com.totemtrials.totemtrials;
}