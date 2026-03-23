package com.totemtrials.totemtrials;

import com.totemtrials.totemtrials.questions.GestionQuiz;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        // On crée notre gestionnaire qui s'occupe de tout
        GestionQuiz quiz = new GestionQuiz();

        Scene scene = new Scene(quiz.getVue(), 450, 450);
        stage.setTitle("Totem Trials - Quiz");
        stage.setScene(scene);
        stage.show();
    }
}