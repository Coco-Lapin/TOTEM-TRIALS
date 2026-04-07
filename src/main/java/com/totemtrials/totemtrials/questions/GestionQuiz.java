package com.totemtrials.totemtrials.questions;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class GestionQuiz {
    private List<Question> listeComplete; // Toutes les questions du JSON
    private Question questionActuelle;    // La question que l'on pose
    private String themeChoisi;           // Le thème pioché au hasard
    private int niveauChoisi;             // Le niveau cliqué par l'utilisateur
    private Consumer<VBox> end;
    private VBox vue; // Le conteneur principal

    public GestionQuiz(String themeChoix, Consumer<VBox> end) {
        vue = new VBox(20);
        vue.setAlignment(Pos.CENTER);
        this.themeChoisi = themeChoix;
        this.end=end;
        chargerQuestionsDuFichier();
        afficherMenuSelection(); // On commence par le menu
    }

    public VBox getVue() { return vue; }

    // Charge le JSON une seule fois au début
    private void chargerQuestionsDuFichier() {
        try {
            var flux = getClass().getResourceAsStream("/Json/question.json");
            listeComplete = new Gson().fromJson(
                    new InputStreamReader(flux),
                    new TypeToken<List<Question>>(){}.getType()
            );
        } catch (Exception e) {
            System.out.println("Erreur de lecture du fichier JSON");
        }
    }

    // --- ÉCRAN 1 : CHOIX DU NIVEAU ---
    private void afficherMenuSelection() {
        vue.getChildren().clear(); // On efface l'écran précédent

        /*On choisit un thème au hasard dans la liste (TEMPORAIRE)
        if (listeComplete != null && !listeComplete.isEmpty()) {
            int hasard = new Random().nextInt(listeComplete.size());
            themeChoisi = listeComplete.get(hasard).getTheme();
        }*/
        Label themeLabel = new Label("Thème pioché : " + themeChoisi);
        Label instruction = new Label("Choisissez une difficulté :");

        // Ligne pour mettre les boutons 1, 2, 3, 4 côte à côte
        HBox ligneBoutons = new HBox(10);
        ligneBoutons.setAlignment(Pos.CENTER);

        for (int i = 1; i <= 4; i++) {
            int n = i;
            Button b = new Button("Niveau " + i);
            b.setOnAction(e -> {
                niveauChoisi = n;
                preparerEtAfficherQuestion(); // On passe à l'écran suivant
            });
            ligneBoutons.getChildren().add(b);
        }

        vue.getChildren().addAll(themeLabel, instruction, ligneBoutons);
    }

    // --- ÉCRAN 2 : LA QUESTION ---
    private void preparerEtAfficherQuestion() {
        vue.getChildren().clear(); // On efface le menu

        // 1. Filtrer les questions qui ont le bon thème et le bon niveau
        List<Question> possibles = new ArrayList<>();
        for (Question q : listeComplete) {
            if (q.getTheme().equalsIgnoreCase(themeChoisi) && q.getDifficulty() == niveauChoisi) {
                possibles.add(q);
            }
        }

        // 2. Si on ne trouve rien, on prévient l'utilisateur
        if (possibles.isEmpty()) {
            vue.getChildren().add(new Label("Aucune question pour ce niveau."));
            Button retour = new Button("Retour au menu");
            retour.setOnAction(e -> afficherMenuSelection());
            vue.getChildren().add(retour);
            return;
        }

        // 3. On en prend une au hasard parmi les résultats filtrés
        questionActuelle = possibles.get(new Random().nextInt(possibles.size()));

        // 4. Affichage du texte de la question
        Label qLabel = new Label(questionActuelle.getTexte());
        qLabel.setWrapText(true);
        vue.getChildren().add(qLabel);

        // 5. Affichage des boutons de réponses (mélangés)
        List<String> choix = new ArrayList<>(questionActuelle.getChoix());
        Collections.shuffle(choix);

        for (String texte : choix) {
            Button btnR = new Button(texte);
            btnR.setMinWidth(200);
            btnR.setOnAction(e -> verifierLaReponse(texte));
            vue.getChildren().add(btnR);
        }
    }

    // --- ÉCRAN 3 : LE RÉSULTAT ---
    private void verifierLaReponse(String reponseCliquee) {
        vue.getChildren().clear(); // On efface la question

        Label resultat = new Label();
        if (reponseCliquee.equals(questionActuelle.getReponse())) {
            resultat.setText("BRAVO ! C'est juste.");
            resultat.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            resultat.setText("FAUX... La réponse était : " + questionActuelle.getReponse());
            resultat.setStyle("-fx-text-fill: red;");
        }

        Button boutonSuivant = new Button("Retour au jeu");
        boutonSuivant.setOnAction(e -> {
            if (this.end != null) {
                // On "donne" la vue au consumer pour qu'il la nettoie
                this.end.accept(this.vue);
            }
        });
        vue.getChildren().addAll(resultat, boutonSuivant);

    }
}