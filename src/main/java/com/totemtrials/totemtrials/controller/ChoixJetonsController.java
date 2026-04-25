package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.model.*;
import com.totemtrials.totemtrials.view.ChoixJetonsView;
import com.totemtrials.totemtrials.view.ChoixJoueursView;
import com.totemtrials.totemtrials.view.HomePageView;
import javafx.scene.image.ImageView;
import java.util.Map;

public class ChoixJetonsController {

    private int currentPlayerIndex = 0;

    public ChoixJetonsController(ChoixJetonsView view, Partie model,
                                 ChoixJoueursView joueursView, HomePageView homeView) {

        view.getBackButton().setOnAction(_ ->
                SceneManager.show(joueursView.getScene(), "Player choice")
        );

        view.getLabelInstruction().setText("C'est au tour de : " + model.getJoueurs()[0].getNom());

        Map<Jeton, ImageView> jetonViews = view.getJetonViews();

        for (Map.Entry<Jeton, ImageView> entry : jetonViews.entrySet()) {
            Jeton jeton = entry.getKey();
            ImageView iv = entry.getValue();

            iv.setOnMouseClicked(_ -> assignJeton(jeton, model, view, joueursView, homeView));
        }
    }

    private void assignJeton(Jeton jeton, Partie model, ChoixJetonsView view,
                             ChoixJoueursView joueursView, HomePageView homeView) {
        Joueur[] joueurs = model.getJoueurs();
        if (currentPlayerIndex >= joueurs.length) return;

        joueurs[currentPlayerIndex].setJeton(jeton);
        System.out.printf("[ChoixJetons] %s -> %s%n",
                joueurs[currentPlayerIndex].getNom(), jeton.getNom());

        view.cacherJeton(jeton);

        currentPlayerIndex++;

        if (currentPlayerIndex < joueurs.length) {
            view.getLabelInstruction().setText("C'est au tour de : " + joueurs[currentPlayerIndex].getNom());
        } else {
            view.getLabelInstruction().setText("Tous les joueurs ont choisi !");
        }

        if (currentPlayerIndex >= joueurs.length) {
            String[] chemins = new String[joueurs.length];
            for (int i = 0; i < joueurs.length; i++) {
                String nom = joueurs[i].getJeton().getNom();
                chemins[i] = switch (nom.toLowerCase()) {
                    case "tigre"    -> "com/totemtrials/totemtrials/Images/tokkens/jetonTigre.png";
                    case "serpent"  -> "com/totemtrials/totemtrials/Images/tokkens/jetonSerpent.png";
                    case "aigle"    -> "com/totemtrials/totemtrials/Images/tokkens/jetonAigle.png";
                    default         -> "com/totemtrials/totemtrials/Images/tokkens/jetonElephant.png";
                };
            }

            System.setProperty("game.nbJoueurs", String.valueOf(joueurs.length));
            for (int i = 0; i < chemins.length; i++) {
                System.setProperty("game.jeton." + i, chemins[i]);
            }

            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                        getClass().getResource("/com/totemtrials/totemtrials/FXML/Plateau.fxml")
                );

                //debug
                java.net.URL url = getClass().getResource("/com/totemtrials/totemtrials/FXML/Plateau.fxml");
                System.out.println("[DEBUG] Plateau.fxml URL = " + url);

                javafx.scene.layout.AnchorPane root = loader.load();
                SceneManager.show(new javafx.scene.Scene(root), "Totem Trials");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}