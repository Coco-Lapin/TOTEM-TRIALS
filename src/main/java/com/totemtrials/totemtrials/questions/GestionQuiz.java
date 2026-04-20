package com.totemtrials.totemtrials.questions;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;

public class GestionQuiz {
    private List<Question> listeComplete; // Toutes les questions du JSON
    private Question questionActuelle;    // La question que l'on pose
    private String themeChoisi;           // Le thème pioché au hasard
    private int niveauChoisi = 1;         // Le niveau cliqué par l'utilisateur
    private Consumer<GestionQuiz> onFinishAction;
    private StackPane vue; // Conteneur racine — ImageView fond + VBox contenu
    private VBox contenu;  // Contenu dynamique (labels, boutons)
    private boolean correcte = false;
    private StackPane zoneCentrale; // Référence pour les bindings adaptatifs

    // Largeur réelle du parchemin = 40% de zoneCentrale (popup) * 0.60 (zone sans bordures pierre)
    private static final double POPUP_W = 0.40;
    private static final double POPUP_H = 0.85;
    private static final double PARCHEMIN_W = POPUP_W * 0.60; // zone utile sans bordures pierre
    private static final double PARCHEMIN_H = POPUP_H * 0.70;

    public  StackPane getZoneCentrale() {
        return zoneCentrale;
    }
    public GestionQuiz(String themeChoix, StackPane zoneCentrale) {
        this.zoneCentrale = zoneCentrale;

        // ImageView comme vrai fond — taille bindée sur zoneCentrale, 100% adaptatif
        ImageView bgView = new ImageView(
                new Image(getClass().getResourceAsStream("/images/questions/backgroundQuestions.png"))
        );
        bgView.fitWidthProperty().bind(zoneCentrale.widthProperty().multiply(POPUP_W));
        bgView.fitHeightProperty().bind(zoneCentrale.heightProperty().multiply(POPUP_H));
        bgView.setPreserveRatio(false);

        // VBox pour le contenu dynamique par dessus l'image
        // maxWidth contraint au parchemin réel — exclut les bordures pierre gauche/droite
        contenu = new VBox(12);
        contenu.setAlignment(Pos.CENTER);
        contenu.prefWidthProperty().bind(zoneCentrale.widthProperty().multiply(PARCHEMIN_W));
        contenu.maxWidthProperty().bind(zoneCentrale.widthProperty().multiply(PARCHEMIN_W));
        contenu.prefHeightProperty().bind(zoneCentrale.heightProperty().multiply(POPUP_H));
        contenu.styleProperty().bind(
                Bindings.concat("-fx-padding: ")
                        .concat(zoneCentrale.heightProperty().multiply(0.15))
                        .concat(" 0 ")
                        .concat(zoneCentrale.heightProperty().multiply(0.05))
                        .concat(" 0;")
        );

        // StackPane empile ImageView (fond) + VBox (contenu)
        vue = new StackPane();
        vue.prefWidthProperty().bind(zoneCentrale.widthProperty().multiply(POPUP_W));
        vue.prefHeightProperty().bind(zoneCentrale.heightProperty().multiply(POPUP_H));
        vue.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 20, 0, 0, 0);");
        vue.getChildren().addAll(bgView, contenu);

        this.themeChoisi = themeChoix;
        chargerQuestionsDuFichier();
        afficherMenuSelection(); // On commence par le menu
    }
    //Constructor for Versus
    public GestionQuiz(String themeChoix,int niveauChoisi, StackPane zoneCentrale) {
        this.zoneCentrale = zoneCentrale;

        // ImageView comme vrai fond — taille bindée sur zoneCentrale, 100% adaptatif
        ImageView bgView = new ImageView(
                new Image(getClass().getResourceAsStream("/images/questions/backgroundQuestions.png"))
        );
        bgView.fitWidthProperty().bind(zoneCentrale.widthProperty().multiply(POPUP_W));
        bgView.fitHeightProperty().bind(zoneCentrale.heightProperty().multiply(POPUP_H));
        bgView.setPreserveRatio(false);

        // VBox pour le contenu dynamique par dessus l'image
        // maxWidth contraint au parchemin réel — exclut les bordures pierre gauche/droite
        contenu = new VBox(12);
        contenu.setAlignment(Pos.CENTER);
        contenu.prefWidthProperty().bind(zoneCentrale.widthProperty().multiply(PARCHEMIN_W));
        contenu.maxWidthProperty().bind(zoneCentrale.widthProperty().multiply(PARCHEMIN_W));
        contenu.prefHeightProperty().bind(zoneCentrale.heightProperty().multiply(POPUP_H));
        contenu.styleProperty().bind(
                Bindings.concat("-fx-padding: ")
                        .concat(zoneCentrale.heightProperty().multiply(0.15))
                        .concat(" 0 ")
                        .concat(zoneCentrale.heightProperty().multiply(0.05))
                        .concat(" 0;")
        );
        // StackPane empile ImageView (fond) + VBox (contenu)
        vue = new StackPane();
        vue.prefWidthProperty().bind(zoneCentrale.widthProperty().multiply(POPUP_W));
        vue.prefHeightProperty().bind(zoneCentrale.heightProperty().multiply(POPUP_H));
        vue.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 20, 0, 0, 0);");
        vue.getChildren().addAll(bgView, contenu);

        this.themeChoisi = themeChoix;
        chargerQuestionsDuFichier();
    }

    public void setOnFinish(Consumer<GestionQuiz> action) {
        this.onFinishAction = action;
    }

    public StackPane getVue() { return vue; }

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

    // Crée un Label bold dont la taille de police est bindée sur la largeur du parchemin
    private Label creerLabel(String texte, double facteur) {
        Label label = new Label(texte);
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.maxWidthProperty().bind(zoneCentrale.widthProperty().multiply(PARCHEMIN_W));
        label.fontProperty().bind(
                Bindings.createObjectBinding(
                        () -> Font.font("System", FontWeight.BOLD,
                                zoneCentrale.getWidth() * PARCHEMIN_W * facteur),
                        zoneCentrale.widthProperty()
                )
        );
        return label;
    }




    // --- ÉCRAN 1 : CHOIX DU NIVEAU ---
    private void afficherMenuSelection() {
        contenu.getChildren().clear(); // On efface l'écran précédent

        Label themeLabel = creerLabel("Theme : " + themeChoisi, 0.08);
        Label instruction = creerLabel("Choose the difficulty :", 0.06);

        // Grille 2x2 — Ligne 1 : Level 1 | Level 2 / Ligne 2 : Level 3 | Level 4
        GridPane grilleBoutons = new GridPane();
        grilleBoutons.setAlignment(Pos.CENTER);
        grilleBoutons.hgapProperty().bind(zoneCentrale.widthProperty().multiply(0.015));
        grilleBoutons.vgapProperty().bind(zoneCentrale.heightProperty().multiply(0.002));

        for (int i = 1; i <= 4; i++) {
            int n = i;

            Image imgNiveau = new Image(
                    getClass().getResourceAsStream("/images/questions/level" + i + ".png")
            );

            // Boutons niveau
            ImageView iv = new ImageView(imgNiveau);
            iv.fitWidthProperty().bind(zoneCentrale.widthProperty().multiply(PARCHEMIN_W * 0.52));
            iv.fitHeightProperty().bind(zoneCentrale.heightProperty().multiply(POPUP_H * 0.20));
            iv.setPreserveRatio(true);

            // Bouton sans texte, fond transparent — l'image fait office de label
            Button b = new Button();
            b.setGraphic(iv);
            b.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-cursor: hand;");
            b.setOnAction(e -> {
                niveauChoisi = n;
                preparerEtAfficherQuestion(); // On passe à l'écran suivant
            });

            // i=1 → col0,row0 | i=2 → col1,row0 | i=3 → col0,row1 | i=4 → col1,row1
            grilleBoutons.add(b, (i - 1) % 2, (i - 1) / 2);
        }

        contenu.getChildren().addAll(themeLabel, instruction, grilleBoutons);
    }

    // --- ÉCRAN 2 : LA QUESTION ---
    public void preparerEtAfficherQuestion() {
        contenu.getChildren().clear(); // On efface le menu

        // 1. Filtrer les questions qui ont le bon thème et le bon niveau
        List<Question> possibles = new ArrayList<>();
        for (Question q : listeComplete) {
            if (q.getTheme().equalsIgnoreCase(themeChoisi) && q.getDifficulty() == niveauChoisi) {
                possibles.add(q);
            }
        }

        // 2. Si on ne trouve rien, on prévient l'utilisateur
        if (possibles.isEmpty()) {
            contenu.getChildren().add(creerLabel("No question available for this level", 0.06));
            Button retour = creerBoutonTexte("Back to the menu");
            retour.setOnAction(e -> afficherMenuSelection());
            contenu.getChildren().add(retour);
            return;
        }

        // 3. On en prend une au hasard parmi les résultats filtrés
        questionActuelle = possibles.get(new Random().nextInt(possibles.size()));

        // 4. Affichage du texte de la question
        Label qLabel = creerLabel(questionActuelle.getTexte(), 0.065);
        contenu.getChildren().add(qLabel);

        // 5. Affichage des boutons de réponses (mélangés)
        List<String> choix = new ArrayList<>(questionActuelle.getChoix());
        Collections.shuffle(choix);

        for (String texte : choix) {
            Button btnR = creerBoutonTexte(texte);
            btnR.setOnAction(e -> verifierLaReponse(texte));
            contenu.getChildren().add(btnR);
        }
    }

    // Crée un bouton texte adaptatif et stylé — largeur contrainte au parchemin
    private Button creerBoutonTexte(String texte) {
        Button b = new Button(texte);
        b.setStyle(
                "-fx-background-color: #5C3A1E; " +
                        "-fx-text-fill: #F5DEB3; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
        b.fontProperty().bind(
                Bindings.createObjectBinding(
                        () -> Font.font("System", FontWeight.BOLD,
                                zoneCentrale.getWidth() * PARCHEMIN_W * 0.06),
                        zoneCentrale.widthProperty()
                )
        );
        // Largeur fixée au parchemin
        b.minWidthProperty().bind(zoneCentrale.widthProperty().multiply(PARCHEMIN_W * 0.80));
        b.maxWidthProperty().bind(zoneCentrale.widthProperty().multiply(PARCHEMIN_W * 0.80));
        return b;
    }

    // --- ÉCRAN 3 : LE RÉSULTAT ---
    private void verifierLaReponse(String reponseCliquee) {
        contenu.getChildren().clear(); // On efface la question
        setCorrecte(false);

        Label resultat;
        if (reponseCliquee.equals(questionActuelle.getReponse())) {
            resultat = creerLabel("Correct Answer !", 0.07);
            resultat.setStyle("-fx-text-fill: #2E8B57;");
            setCorrecte(true);
        } else {
            resultat = creerLabel("Wrong answer...\nCorrect : " + questionActuelle.getReponse(), 0.06);
            resultat.setStyle("-fx-text-fill: #8B0000;");
            setCorrecte(false);
        }

        Button boutonSuivant = creerBoutonTexte("Back to game");
        boutonSuivant.setOnAction(e -> {
            if (this.onFinishAction != null) {
                // On s'envoie soi-même (this) à l'action
                this.onFinishAction.accept(this);
            }
        });

        contenu.getChildren().addAll(resultat, boutonSuivant);
    }

    public boolean isCorrecte() { return correcte; }
    public void setCorrecte(boolean correcte) { this.correcte = correcte; }
    public int getNiveauChoisi() { return niveauChoisi; }

    public void setNiveauChoisi(int niveauChoisi) {
        if(niveauChoisi > 4 && niveauChoisi <= 0) {
            this.niveauChoisi = 4;
        }else{
            this.niveauChoisi = niveauChoisi;
        }
    }
}