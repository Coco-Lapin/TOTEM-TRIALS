package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.models.GameConfig;
import com.totemtrials.totemtrials.plateau.BoardGameController;
import com.totemtrials.totemtrials.plateau.Case;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.List;

/**
 * Gère tout ce qui est lié au déplacement physique des pions sur le plateau.
 *
 * Responsabilités :
 *  - Créer et placer les sprites (Circle) de chaque joueur sur le Pane du plateau
 *  - Animer le déplacement case par case (avance ou recul)
 *  - Jouer un son synchronisé à chaque saut de case
 *
 * Instancié par BoardGameController dans initialize().
 * Appelé par GameManager pour déplacer les pions après chaque action.
 */
public class movementController {

    // ── Données joueurs ──────────────────────────────────────────────────────

    /** Chemins vers les images des jetons choisis par les joueurs (ex: /images/tokens/jetonTigre.png) */
    private String[] cheminImages;

    /** Noms des joueurs récupérés depuis GameConfig */
    private String[] nomsJoueurs;

    /** Référence au controller du plateau — permet d'accéder à la liste des cases */
    private BoardGameController boardGame;

    /** Pane JavaFX sur lequel les sprites sont dessinés (le grand Pane 6144x3584) */
    @FXML private Pane plateauJeu;

    /**
     * Position actuelle de chaque joueur dans la liste des cases.
     * indicesCasesActuelles[0] = index de la case du joueur 0, etc.
     * Initialisé à 0 (case de départ) pour tous les joueurs.
     */
    private int[] indicesCasesActuelles;

    /**
     * Sprites visuels de chaque joueur.
     * Un Circle rempli avec l'image du jeton choisi.
     * sprites[0] = pion du joueur 0, etc.
     */
    private Circle[] sprites;

    // ── Audio ────────────────────────────────────────────────────────────────

    /**
     * Son joué à chaque déplacement d'une case.
     * AudioClip est préféré à MediaPlayer pour les SFX courts :
     * il charge le son en RAM et peut se jouer plusieurs fois sans latence.
     * Fichier attendu : src/main/resources/sounds/piece_move.mp3
     */
    private AudioClip sonDeplacement;


    // ── Initialisation ───────────────────────────────────────────────────────

    /**
     * Prépare les tableaux internes à la taille du nombre de joueurs.
     * Appelé automatiquement dans initialiserPions() — ne pas appeler manuellement.
     *
     * @param param nombre de joueurs (2, 3 ou 4)
     */
    public void setupPlayers(int param) {
        this.indicesCasesActuelles = new int[param];
        this.sprites               = new Circle[param];
        for (int i = 0; i < param; i++) {
            indicesCasesActuelles[i] = 0; // tous les joueurs commencent à la case 0
        }
    }

    /** Injecte le BoardGameController (nécessaire pour accéder à la liste des cases) */
    public void setBoardGame(BoardGameController bg) { this.boardGame = bg; }

    /** Injecte le Pane du plateau sur lequel les sprites seront ajoutés */
    public void setPlateauJeu(Pane p) { this.plateauJeu = p; }

    /**
     * Crée et place les sprites de tous les joueurs sur la case de départ.
     * Charge également le son de déplacement en mémoire.
     *
     * Appelé par GameManager.demarrerPartie() après le délai d'initialisation.
     * Doit être appelé APRÈS que la liste des cases soit remplie (Platform.runLater).
     */
    public void initialiserPions() {
        int nbJoueursReels     = GameConfig.getInstance().getNbJoueurs();
        this.cheminImages      = GameConfig.getInstance().getJetonsChoisis();
        this.nomsJoueurs       = GameConfig.getInstance().getNomsJoueurs();
        setupPlayers(nbJoueursReels);

        // Chargement du son une seule fois au démarrage de la partie
        var urlSon = getClass().getResource("/sounds/piece_move.mp3");
        if (urlSon != null) {
            sonDeplacement = new AudioClip(urlSon.toExternalForm());
            // Volume par défaut du SFX — sera ensuite contrôlé par SceneManager.sfxVolume
            sonDeplacement.setVolume(0.8);
            // Préchauffage : joue le son à volume 0 pour éliminer le délai
            // d'initialisation du premier vrai play() pendant la partie
            sonDeplacement.play(0.0);
        } else {
            System.err.println("[movementController] Son introuvable : /sounds/piece_move.mp3");
        }

        // Récupère la case de départ (index 0) pour y placer tous les pions
        Case depart = boardGame.getListeCases().get(0);

        for (int i = 0; i < nbJoueursReels; i++) {

            // Crée un Circle de rayon 70 comme sprite visuel du joueur
            sprites[i] = new Circle(70);
            Image imgJeton = new Image(getClass().getResource(cheminImages[i]).toExternalForm());
            sprites[i].setFill(new ImagePattern(imgJeton));
            sprites[i].setStroke(Color.BLACK);
            sprites[i].setStrokeWidth(3);

            // Décalage pour éviter la superposition des 4 pions sur la même case :
            // Joueur 0 → haut-gauche | Joueur 1 → haut-droit
            // Joueur 2 → bas-gauche  | Joueur 3 → bas-droit
            double posX = (i % 2 == 0) ? -20 : 20;
            double posY = (i < 2)      ? -20 : 20;

            sprites[i].setTranslateX(depart.getCenterX() + posX);
            sprites[i].setTranslateY(depart.getCenterY() + posY);

            // Ajout au Pane seulement si pas déjà présent (sécurité anti-doublon)
            if (!plateauJeu.getChildren().contains(sprites[i])) {
                plateauJeu.getChildren().add(sprites[i]);
            }
        }
    }


    // ── Déplacement ──────────────────────────────────────────────────────────

    /**
     * Point d'entrée public pour déplacer un pion d'un certain nombre de cases.
     * Supporte l'avance (positif) et le recul (négatif).
     *
     * Le déplacement est animé case par case, avec un son à chaque saut.
     * La callback auFini est appelée une fois que toutes les cases ont été parcourues.
     *
     * @param idJoueur       index du joueur à déplacer (0 à nbJoueurs-1)
     * @param nbCasesAAvancer nombre de cases (positif = avance, négatif = recul)
     * @param auFini         callback exécutée en fin de déplacement (peut être null)
     */
    public void deplacerPion(int idJoueur, int nbCasesAAvancer, Runnable auFini) {
        List<Case> cases = boardGame.getListeCases();

        // Calcul du décalage visuel propre à ce joueur (même logique qu'à l'initialisation)
        double posX = (idJoueur % 2 == 0) ? -20 : 20;
        double posY = (idJoueur < 2)      ? -20 : 20;

        // direction =  1 → avance  |  direction = -1 → recul
        int direction = nbCasesAAvancer >= 0 ? 1 : -1;
        int total     = Math.abs(nbCasesAAvancer);

        deplacerPionRecursif(idJoueur, cases, total, direction, posX, posY, auFini);
    }

    /**
     * Anime le déplacement case par case via récursion.
     *
     * POURQUOI PAS SequentialTransition ?
     * Dans un SequentialTransition, le setOnFinished des transitions enfants
     * ne se déclenche JAMAIS — seul celui du parent fire. On chaîne donc
     * manuellement : chaque TranslateTransition lance la suivante via son
     * propre setOnFinished, ce qui garantit l'exécution du son à chaque case.
     *
     * @param idJoueur  index du joueur
     * @param cases     liste complète des cases du plateau
     * @param restants  nombre de cases encore à parcourir
     * @param direction +1 (avance) ou -1 (recul)
     * @param posX      décalage X du sprite sur la case (anti-superposition)
     * @param posY      décalage Y du sprite sur la case (anti-superposition)
     * @param auFini    callback finale, appelée quand restants == 0
     */
    private void deplacerPionRecursif(int idJoueur, List<Case> cases, int restants,
                                      int direction, double posX, double posY, Runnable auFini) {
        // Cas de base : plus de cases à parcourir → on exécute la callback de fin
        if (restants == 0) {
            if (auFini != null) auFini.run();
            return;
        }

        // Sécurité : empêche de sortir des bornes du tableau de cases
        int prochainIndex = indicesCasesActuelles[idJoueur] + direction;
        if (prochainIndex < 0 || prochainIndex >= cases.size()) {
            if (auFini != null) auFini.run();
            return;
        }

        // Mise à jour de la position logique du joueur
        indicesCasesActuelles[idJoueur] = prochainIndex;
        Case destination = cases.get(prochainIndex);

        // Animation de déplacement vers la case suivante (1 seconde par case)
        TranslateTransition saut = new TranslateTransition(
                Duration.millis(1000), sprites[idJoueur]);
        saut.setToX(destination.getCenterX() + posX);
        saut.setToY(destination.getCenterY() + posY);

        // Une fois ce saut terminé, on lance le saut suivant (récursion)
        saut.setOnFinished(e ->
                deplacerPionRecursif(idJoueur, cases, restants - 1, direction, posX, posY, auFini)
        );

        // Le son est joué AU DÉBUT du saut pour être synchronisé au mouvement visuel
        // (jouer dans setOnFinished créerait un décalage audible)
        jouerSon();
        saut.play();
    }


    // ── Audio ────────────────────────────────────────────────────────────────

    /**
     * Joue le son de déplacement avec le volume défini par le slider SFX.
     * Le volume est lu depuis SceneManager.getSfxVolume() à chaque appel,
     * ce qui permet de changer le volume en temps réel sans recharger le son.
     */
    private void jouerSon() {
        if (sonDeplacement != null) {
            sonDeplacement.play(SceneManager.getSfxVolume());
        }
    }
}