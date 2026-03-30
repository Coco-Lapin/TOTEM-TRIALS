package com.totemtrials.totemtrials.model;

public class Partie {

    private Joueur[] joueurs;

    private final Jeton[] jetonsDisponibles = {
        new Jeton("tigre",    "com/totemtrials/totemtrials/Images/tokkens/jetonTigre.png",    "com/totemtrials/totemtrials/Images/tokkens/jetonTigre_anim.gif"),
        new Jeton("aigle",    "com/totemtrials/totemtrials/Images/tokkens/jetonAigle.png",    "com/totemtrials/totemtrials/Images/tokkens/jetonAigle_anim.gif"),
        new Jeton("serpent",  "com/totemtrials/totemtrials/Images/tokkens/jetonSerpent.png",  "com/totemtrials/totemtrials/Images/tokkens/jetonSerpent_anim.gif"),
        new Jeton("elephant", "com/totemtrials/totemtrials/Images/tokkens/jetonElephant.png", "com/totemtrials/totemtrials/Images/tokkens/jetonElephant_anim.gif"),
    };

    /** Initialise N joueurs nommés "Joueur 1" … "Joueur N". */
    public void initJoueurs(int count) {
        joueurs = new Joueur[count];
        for (int i = 0; i < count; i++)
            joueurs[i] = new Joueur("Joueur " + (i + 1));
    }

    public Joueur[] getJoueurs()           { return joueurs; }
    public Jeton[]  getJetonsDisponibles() { return jetonsDisponibles; }
}
