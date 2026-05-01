<<<<<<< HEAD
package com.totemtrials.totemtrials.models;   // ← "models" avec s
=======
package com.totemtrials.totemtrials.models;
>>>>>>> feat/menus

public class Partie {

    private Joueur[] joueurs;

    private final Jeton[] jetonsDisponibles = {
<<<<<<< HEAD
            new Jeton("tigre",    "Avance d'une case en plus en cas de victoire d'un versus",
                    "images/tokens/jetonTigre.png",    "images/tokens/jetonTigre_anim.gif"),
            new Jeton("aigle",    "Avance d'une case en plus en cas de bonne réponse",
                    "images/tokens/jetonAigle.png",    "images/tokens/jetonAigle_anim.gif"),
            new Jeton("serpent",  "Recule d'une case en moins en cas de défaite d'un versus",
                    "images/tokens/jetonSerpent.png",  "images/tokens/jetonSerpent_anim.gif"),
            new Jeton("elephant", "Recule d'une case en moins en cas de mauvaise réponse",
                    "images/tokens/jetonElephant.png", "images/tokens/jetonElephant_anim.gif"),
    };

    public void initJoueurs(int count) {
        if (count < 2 || count > 4) {
            throw new IllegalArgumentException("Le nombre de joueurs doit être compris entre 2 et 4.");
        }

        joueurs = new Joueur[count];
        for (int i = 0; i < count; i++)
            joueurs[i] = new Joueur("Joueur " + (i + 1));
=======
            new Jeton("tiger", "Advances 1 extra space upon winning a versus", "com/totemtrials/totemtrials/Images/tokkens/jetonTigre.png", "com/totemtrials/totemtrials/Images/tokkens/jetonTigre_anim.gif"),
            new Jeton("eagle", "Advances 1 extra space for a correct answer", "com/totemtrials/totemtrials/Images/tokkens/jetonAigle.png", "com/totemtrials/totemtrials/Images/tokkens/jetonAigle_anim.gif"),
            new Jeton("snake", "Moves back 1 less space upon losing a versus", "com/totemtrials/totemtrials/Images/tokkens/jetonSerpent.png", "com/totemtrials/totemtrials/Images/tokkens/jetonSerpent_anim.gif"),
            new Jeton("elephant", "Moves back 1 less space for a wrong answer", "com/totemtrials/totemtrials/Images/tokkens/jetonElephant.png", "com/totemtrials/totemtrials/Images/tokkens/jetonElephant_anim.gif"),
    };

    /** Initializes N players named "Player 1" … "Player N". */
    public void initJoueurs(int count) {
        joueurs = new Joueur[count];
        for (int i = 0; i < count; i++)
            joueurs[i] = new Joueur("Player " + (i + 1));
>>>>>>> feat/menus
    }

    public Joueur[] getJoueurs()           { return joueurs; }
    public Jeton[]  getJetonsDisponibles() { return jetonsDisponibles; }
<<<<<<< HEAD


=======
>>>>>>> feat/menus
}
