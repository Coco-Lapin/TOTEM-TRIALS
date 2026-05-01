package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.models.*;

import com.totemtrials.totemtrials.view.ChoixJetonsView;
import com.totemtrials.totemtrials.view.ChoixJoueursView;
import com.totemtrials.totemtrials.view.HomePageView;
import com.totemtrials.totemtrials.view.InfoPassifView;
import javafx.scene.image.ImageView;

import java.util.Map;

public class ChoixJetonsController {

    private int currentPlayerIndex = 0;

    public ChoixJetonsController(ChoixJetonsView view, Partie model,
                                 ChoixJoueursView joueursView, HomePageView homeView) {

        view.getBackButton().setOnMouseClicked(_ ->
                SceneManager.show(joueursView.getScene(), "Player choice")
        );

        view.getInfoPassiveButton().setOnMouseClicked(_ ->
                goInfoView(homeView, model, view)
        );

        view.getLabelInstruction().setText("It's " + model.getJoueurs()[0].getNom() + "'s turn to choose" );

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
        System.out.printf("[TOKEN] %s -> %s%n",
                joueurs[currentPlayerIndex].getNom(), jeton.getNom());

        view.cacherJeton(jeton);

        currentPlayerIndex++;

        if (currentPlayerIndex < joueurs.length) {
            view.getLabelInstruction().setText("It's " + joueurs[currentPlayerIndex].getNom() +"'s turn to choose");
        } else {
            view.getLabelInstruction().setText("All the players have made their choice!");
        }

        if (currentPlayerIndex >= joueurs.length) {
            String[] chemins = new String[joueurs.length];
            String[] noms = new String[joueurs.length]; // Ton tableau pour les noms

            for (int i = 0; i < joueurs.length; i++) {

                String nomDuJetonChoisi = joueurs[i].getJeton().getNom();

                // 2. On met CE nom dans le tableau des joueurs
                noms[i] = nomDuJetonChoisi;

                // 3. On choisit la bonne image en fonction de ce même nom
                chemins[i] = switch (nomDuJetonChoisi.toLowerCase()) {
                    case "tiger"    -> "/images/tokens/TigerToken.png";
                    case "snake"  -> "/images/tokens/SnakeToken.png";
                    case "eagle"    -> "/images/tokens/EagleToken.png";
                    default         -> "/images/tokens/ElephantToken.png";
                };
            }

            // On sauvegarde dans le Singleton !
            GameConfig.getInstance().setNbJoueurs(joueurs.length);
            GameConfig.getInstance().setJetonsChoisis(chemins);
            GameConfig.getInstance().setNomsJoueurs(noms); // Le singleton reçoit ["Tigre", "Aigle", ...]
            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                        getClass().getResource("/FXML/Plateau.fxml")
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

    public void goInfoView(HomePageView homeView, Partie model, ChoixJetonsView cView){

        InfoPassifView iView = new InfoPassifView(
                SceneManager.getStage(),
                homeView.getBackground(),
                model.getJetonsDisponibles()
        );

        new InfoPassifController(iView, cView);
        SceneManager.show(iView.getScene(), "Passives information" +
                "s");

    }
}