module com.totemtrials.totemtrials {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.media;


    opens com.totemtrials.totemtrials to javafx.fxml;
    opens com.totemtrials.totemtrials.questions to com.google.gson;
    exports com.totemtrials.totemtrials;
}