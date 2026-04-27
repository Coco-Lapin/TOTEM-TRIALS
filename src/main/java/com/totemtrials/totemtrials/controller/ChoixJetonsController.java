package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.models.GameConfig;
import com.totemtrials.totemtrials.models.Jeton;
import com.totemtrials.totemtrials.models.Joueur;
import com.totemtrials.totemtrials.models.Partie;
import com.totemtrials.totemtrials.view.ChoixJetonsView;
import com.totemtrials.totemtrials.view.ChoixJoueursView;
import com.totemtrials.totemtrials.view.HomePageView;
import javafx.scene.image.ImageView;
import java.util.Map;

public class ChoixJetonsController {

    private int currentPlayerIndex = 0;
    private GameConfig gameConfig;

    public ChoixJetonsController(ChoixJetonsView view, Partie model,
                                 ChoixJoueursView joueursView, HomePageView homeView,GameConfig gm) {
            this.gameConfig = gm;
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
            String[] noms = new String[joueurs.length]; // Ton tableau pour les noms

            for (int i = 0; i < joueurs.length; i++) {

                String nomDuJetonChoisi = joueurs[i].getJeton().getNom();

                // 2. On met CE nom dans le tableau des joueurs
                noms[i] = nomDuJetonChoisi;

                // 3. On choisit la bonne image en fonction de ce même nom
                chemins[i] = switch (nomDuJetonChoisi.toLowerCase()) {
                    case "tigre"    -> "/images/tokens/jetonTigre.png";
                    case "serpent"  -> "/images/tokens/jetonSerpent.png";
                    case "aigle"    -> "/images/tokens/jetonAigle.png";
                    default         -> "/images/tokens/jetonElephant.png";
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
                javafx.scene.layout.AnchorPane root = loader.load();
                SceneManager.show(new javafx.scene.Scene(root), "Totem Trials");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}