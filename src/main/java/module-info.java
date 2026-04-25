module com.totemtrials.totemtrials {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;
    requires java.compiler;
    requires com.google.gson;

    opens com.totemtrials.totemtrials to javafx.fxml;
    opens com.totemtrials.totemtrials.questions to com.google.gson;
    opens com.totemtrials.totemtrials.plateau to javafx.graphics, javafx.fxml;

    exports com.totemtrials.totemtrials;
    exports com.totemtrials.totemtrials.plateau;
}