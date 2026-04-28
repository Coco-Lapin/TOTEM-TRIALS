package com.totemtrials.totemtrials.model;

public class Partie {

    private Joueur[] joueurs;

    private final Jeton[] jetonsDisponibles = {
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
    }

    public Joueur[] getJoueurs()           { return joueurs; }
    public Jeton[]  getJetonsDisponibles() { return jetonsDisponibles; }
}
