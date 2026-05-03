package com.totemtrials.totemtrials.models;

import com.totemtrials.totemtrials.controller.FinPartieController;
import com.totemtrials.totemtrials.controller.movementController;
import com.totemtrials.totemtrials.plateau.BoardGameController;
import com.totemtrials.totemtrials.plateau.Case;
import com.totemtrials.totemtrials.questions.GestionQuiz;
import com.totemtrials.totemtrials.view.FinPartieView;
import com.totemtrials.totemtrials.view.HomePageView;
import javafx.animation.PauseTransition;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.List;

public class GameManager {
    private BoardGameController boardGameController;
    private movementController MC;
    private GameConfig gameConfig;
    private FinPartieController FPC ;
    private FinPartieView finPartieView;
    private HomePageView homePageView;
    private StatistiquesPartie statistiquesPartie;
    private StatistiquesJoueur[] statsJoueurs;
    private Partie partie;
    private long startTime; // Pour stocker le moment du départ
    private List<Case> listeCases;
    private Shortcut shortcut;
    private StackPane zoneCentrale;
    private boolean gameFinished = false;
    private int ActualRound=0 ;
    // 1. Nouvelles variables pour le multijoueur
    private int[] positionsJoueurs = new int[GameConfig.getInstance().getNbJoueurs()]; // La position exacte de chaque joueur
    private int joueurActuel = 0; // 0 = Bleu, 1 = Rouge, 2 = Vert, 3 = Jaune
    String[] playerNames = GameConfig.getInstance().getNomsJoueurs();

    public GameManager(BoardGameController bg, movementController mc, List<Case> cases, StatistiquesJoueur[] sj) {
        this.boardGameController = bg;
        this.MC = mc;
        this.listeCases = cases;
        this.statsJoueurs= sj;
        this.shortcut = new Shortcut(this.boardGameController, this);
    }

    public void demarrerPartie() {
        this.startTime = System.currentTimeMillis();
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            MC.initialiserPions(); // Fait apparaître les 4 pions
            jouerUnTour(); // Lance le tour du Joueur 0
        });
        pause.play();
    }

    public void jouerUnTour() {
        System.out.println("--- IT'S THE TURN OF THE PLAYER " + joueurActuel + " ---");
        String[] jetons = GameConfig.getInstance().getJetonsChoisis();
        String[] noms = GameConfig.getInstance().getNomsJoueurs();
        if (joueurActuel < jetons.length) {
            boardGameController.setJoueurActuelImage(jetons[joueurActuel], noms[joueurActuel]);
        }

        // Vérifie si le joueur est sur la case de départ (index 0)
        int distance;
        if (positionsJoueurs[joueurActuel] == 0) {
            distance = 1; // Il avance de 1 s'il est au départ
        } else {
            distance = 0; // Sinon, il ne bouge pas
        }

        // 2. On met à jour la position mathématique du joueur actuel
        positionsJoueurs[joueurActuel] += distance;

        // Sécurité : on empêche le pion d'aller plus loin que la case FINISH
        if (positionsJoueurs[joueurActuel] >= listeCases.size()) {
            positionsJoueurs[joueurActuel] = listeCases.size() - 1;
        }

        // 3. On déplace le bon pion, et une fois arrêté, on analyse la case
        MC.deplacerPion(joueurActuel, distance, () -> {
            System.out.println("THE TOKEN " + joueurActuel + " STOPPED ON THE CASE N°" + positionsJoueurs[joueurActuel]);
            this.analyserCaseArrivee(positionsJoueurs[joueurActuel]);
        });
    }

    public void analyserCaseArrivee(int positionActuelle) {
        // Petite optimisation : tu as déjà "listeCases" dans la classe, pas besoin de repasser par le controller
        Case c = this.listeCases.get(positionActuelle);
        String tileTheme = c.getType();
        //-------------------------------------------------------------------------------------
        // --------------------------------VERSUS GAMEPLAY ------------------------------------
        //-------------------------------------------------------------------------------------
        if(tileTheme.equals("VERSUS") ) {
            Versus v = new Versus(boardGameController.getZoneCentrale(),this.gameConfig);
            v.setGameManager(this); // On lie le GameManager
            v.setBoardGameController(this.boardGameController);
            // ... set les controllers ...
            v.choosePlayer(); // Prépare les boutons
            this.boardGameController.afficherPopUpQuiz(v.getContenu()); // Affiche la liste des adversaires
            return;
        }
        //-------------------------------------------------------------------------------------
        // --------------------------------SHORTCUT GAMEPLAY ----------------------------------
        //-------------------------------------------------------------------------------------
        if(tileTheme.equals("HOP")){
            // CORRECTION 2 : Afficher la pop-up de raccourci à l'écran
            this.boardGameController.afficherPopUpQuiz(shortcut.displayBox());

            return;
        }
        if(tileTheme.equals("BONUS")) {
            tileTheme="Mystery";
        }
        //-----------------------
        // --- START ENDGAME-----
        //-----------------------
        if(tileTheme.equals("FINISH") || verifierFinDePartie()) {
            return; // Le jeu s'arrête ici si on est sur Finish
        }

        GestionQuiz nouveauQuiz = setupQuiz(tileTheme);
        this.boardGameController.afficherPopUpQuiz(nouveauQuiz.getVue());
    }

    private GestionQuiz setupQuiz(String tileTheme) {
        // On passe zoneCentrale pour le binding adaptatif
        GestionQuiz quiz = new GestionQuiz(tileTheme, boardGameController.getZoneCentrale());

        quiz.setOnFinish(q -> {
            // On ferme la fenêtre d'abord
            this.boardGameController.fermerPopUpQuiz(q.getVue());

            if (q.isCorrecte()) {
                //-----UPDATE STATS------
                statsJoueurs[joueurActuel].ajouterBonneReponse();

                int steps = q.getNiveauChoisi();
                if(playerNames[joueurActuel].equalsIgnoreCase("eagle")){
                    steps +=1 ;
                }

                System.out.println("GOOD ANSWER ! THE PLAYER " + joueurActuel + " STEPPED FORWARD OF " + steps + " cases bonus.");

                // On met à jour la position après le bonus
                positionsJoueurs[joueurActuel] += steps;
                if (positionsJoueurs[joueurActuel] >= listeCases.size()) {
                    positionsJoueurs[joueurActuel] = listeCases.size() - 1;
                }

                // On déplace le pion pour le bonus, puis on passe au joueur suivant
                MC.deplacerPion(joueurActuel, steps, () -> {
                    if (!verifierFinDePartie()) {
                        passerAuJoueurSuivant();
                    }
                });

            } else {

                System.out.println("WRONG ANSWER ! END OF ROUND FOR PLAYER " + joueurActuel);
                //-----UPDATE STATS------
                statsJoueurs[joueurActuel].ajouterMauvaiseReponse();

                int steps = (q.getNiveauChoisi() <= 2) ? -1 : -2;
                if (playerNames[joueurActuel].equalsIgnoreCase("elephant")) {
                    steps++;
                }

                positionsJoueurs[joueurActuel] += steps;
                if (positionsJoueurs[joueurActuel] < 0) {
                    positionsJoueurs[joueurActuel] = 0;
                }

                System.out.println("WRONG ANSWER ! THE PLAYER " + joueurActuel + " STEPS BACK OF " + Math.abs(steps) + " cases.");

                MC.deplacerPion(joueurActuel, steps, () -> {
                    if (!verifierFinDePartie()) {
                        passerAuJoueurSuivant();
                    }
                });
            }
        });

        return quiz;
    }

    // 4. La méthode qui gère le roulement des tours
    private void passerAuJoueurSuivant() {
        if (gameFinished) return;
        //---- UPDATE STATS -----
        statsJoueurs[joueurActuel].incrementerTour();
        joueurActuel++;

        // Si on dépasse le joueur 3 (le 4ème joueur), on revient au joueur 0
        if (joueurActuel >= GameConfig.getInstance().getNbJoueurs()) {
            joueurActuel = 0;
            setLabelRound();
        }

        // On met une petite pause d'une seconde pour que les joueurs respirent avant le prochain tour
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> jouerUnTour());
        pause.play();
    }

    public void EndingHop(boolean aJoue, boolean estVictorieux) {
        int distance;
        // 1. Si le joueur a refusé d'utiliser le raccourci (il a cliqué sur "No")
        if (!aJoue) {

            distance = 1 ;
            positionsJoueurs[joueurActuel] += distance;

            // Sécurité pour ne pas sortir du plateau
            if (positionsJoueurs[joueurActuel] >= listeCases.size()) {
                positionsJoueurs[joueurActuel] = listeCases.size() - 1;
            }
            MC.deplacerPion(joueurActuel, distance, () -> {
                if (!verifierFinDePartie()) {
                    passerAuJoueurSuivant();
                }
            });
            return; // On arrête la méthode ici
        }
        // 2. Si le joueur a accepté de jouer (il a cliqué sur "Yes")

        if (estVictorieux) {
            statsJoueurs[joueurActuel].ajouterBonneReponse();
            distance = 6; // Victoire : il avance de 6

            System.out.println("Raccourci réussi ! Le joueur " + joueurActuel + " avance de " + distance + " cases.");
        } else {

            distance = -3; // Défaite : il recule de 3
            // en cas d'erreur l'elephant recule d'une case en moins
            if(playerNames[joueurActuel].equalsIgnoreCase("elephant")){
                distance++;
            }
            System.out.println("Raccourci échoué... Le joueur " + joueurActuel + " recule de 3 cases.");
        }
        // 3. Mise à jour de la position dans le tableau
        positionsJoueurs[joueurActuel] += distance;
        // 4. Sécurité pour ne pas sortir du plateau
        if (positionsJoueurs[joueurActuel] >= listeCases.size()) {
            positionsJoueurs[joueurActuel] = listeCases.size() - 1; // Bloque à la fin
        } else if (positionsJoueurs[joueurActuel] < 0) {
            positionsJoueurs[joueurActuel] = 0; // Bloque au début pour ne pas passer dans le négatif
        }
        // 5. On lance l'animation de déplacement
        // Une fois l'animation terminée, on appelle passerAuJoueurSuivant()
        MC.deplacerPion(joueurActuel, distance, () -> {
            if (!verifierFinDePartie()) {
                passerAuJoueurSuivant();
            }
        });
    }

    public int getJoueurActuel() {
        return joueurActuel;
    }

    public void EndingVersus(int idChallenger, int idOpponent, boolean challengerOk, boolean adversaireOk) {
        System.out.println("Versus result");
        int distChallenger = challengerOk ? 4 : -4;
        int distOpponent = adversaireOk ? 4 : -4;

        if(challengerOk) {
            statsJoueurs[idChallenger].ajouterBonneReponse();
        }else if(!challengerOk) {
            statsJoueurs[idChallenger].ajouterMauvaiseReponse();
        }
        if(adversaireOk) {
            statsJoueurs[idOpponent].ajouterBonneReponse();
        }else if(!adversaireOk) {
            statsJoueurs[idOpponent].ajouterMauvaiseReponse();
        }
        if(playerNames[idChallenger].equalsIgnoreCase("tiger") && challengerOk){
            distChallenger++;
        }

        if(playerNames[idOpponent].equalsIgnoreCase("tiger") && adversaireOk){
            distOpponent++;
        }

        if(playerNames[idChallenger].equalsIgnoreCase("snake") && !challengerOk){
            distChallenger++;
        }

        if(playerNames[idOpponent].equalsIgnoreCase("snake") && !adversaireOk){
            distOpponent++;
        }

        // 2. Update of the different positions
        positionsJoueurs[idChallenger] += distChallenger;
        positionsJoueurs[idOpponent] += distOpponent;

        // 3. SECURITY : the player cannot go further back than the starting tile
        if (positionsJoueurs[idChallenger] < 0) positionsJoueurs[idChallenger] = 0;
        if (positionsJoueurs[idOpponent] < 0) positionsJoueurs[idOpponent] = 0;
        // SECURITY : the player cannot go further than the ending tile
        int Maxtile = listeCases.size() - 1;
        if (positionsJoueurs[idChallenger] > Maxtile) positionsJoueurs[idChallenger] = Maxtile;
        if (positionsJoueurs[idOpponent] > Maxtile) positionsJoueurs[idOpponent] = Maxtile;
       // ONE BY ONE
        //The challenger moves first
        int finalDistOpponent = distOpponent;
        MC.deplacerPion(idChallenger, distChallenger, () -> {
            MC.deplacerPion(idOpponent, finalDistOpponent, () -> {
                // On vérifie si l'un des deux a atteint la fin
                boolean finChallenger = listeCases.get(positionsJoueurs[idChallenger]).getType().equals("FINISH");
                boolean finOpponent = listeCases.get(positionsJoueurs[idOpponent]).getType().equals("FINISH");

                if (finChallenger || finOpponent) {
                    // On force le joueur actuel sur celui qui a gagné pour les stats
                    if (finOpponent && !finChallenger) joueurActuel = idOpponent;
                    else joueurActuel = idChallenger;

                    verifierFinDePartie();
                } else {
                    passerAuJoueurSuivant();
                }
            });
        });



    }
    private boolean verifierFinDePartie() {
        if (gameFinished) return true;

        int posActuelle = positionsJoueurs[joueurActuel];
        Case c = this.listeCases.get(posActuelle);

        if (c.getType().equals("FINISH")) {
            gameFinished = true;
            System.out.println("VICTORY OF " + playerNames[joueurActuel]);

            long endTime = System.currentTimeMillis();
            long dureeEnSecondes = (endTime - startTime) / 1000;

            if (this.statistiquesPartie != null) {
                this.statistiquesPartie.setDureeSecondes(dureeEnSecondes);
            }

            // On affiche l'écran de fin
            FinPartieController.lancerFinPartie(this.statistiquesPartie, this.partie, this.homePageView);
            return true;
        }
        return false;
    }
    public void setLabelRound(){
        this.ActualRound++;
        boardGameController.labelRound.setText(""+ActualRound);
    }

    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    public void setHomeView(HomePageView homeView) {
        this.homePageView=homeView;
    }
   public void setStatistiquesPartie(StatistiquesPartie statistiquesPartie) {
        this.statistiquesPartie = statistiquesPartie;
   }
}