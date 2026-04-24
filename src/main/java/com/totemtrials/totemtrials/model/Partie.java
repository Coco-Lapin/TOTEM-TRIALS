package com.totemtrials.totemtrials.model;

public class Partie {

    private Joueur[] joueurs;

    private final Jeton[] jetonsDisponibles = {
        new Jeton("tigre", "Avance d'une case en plus en cas de victoire d'un versus"   ,"com/totemtrials/totemtrials/Images/tokkens/jetonTigre.png",    "com/totemtrials/totemtrials/Images/tokkens/jetonTigre_anim.gif"),
        new Jeton("aigle", "Avance d'une case en plus en cas de bonne réponse"   ,"com/totemtrials/totemtrials/Images/tokkens/jetonAigle.png",    "com/totemtrials/totemtrials/Images/tokkens/jetonAigle_anim.gif"),
        new Jeton("serpent", "Recule d'une case en moins en cas de défaites d'un versus" ,"com/totemtrials/totemtrials/Images/tokkens/jetonSerpent.png",  "com/totemtrials/totemtrials/Images/tokkens/jetonSerpent_anim.gif"),
        new Jeton("elephant", "Recule d'une case en moins en cas de mauvaise réponse","com/totemtrials/totemtrials/Images/tokkens/jetonElephant.png", "com/totemtrials/totemtrials/Images/tokkens/jetonElephant_anim.gif"),
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
