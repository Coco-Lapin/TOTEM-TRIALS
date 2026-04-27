package com.totemtrials.totemtrials.models;   // ← "models" avec s

public class Partie {

    private Joueur[] joueurs;

    private final Jeton[] jetonsDisponibles = {
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
        joueurs = new Joueur[count];
        for (int i = 0; i < count; i++)
            joueurs[i] = new Joueur("Joueur " + (i + 1));
    }

    public Joueur[] getJoueurs()           { return joueurs; }
    public Jeton[]  getJetonsDisponibles() { return jetonsDisponibles; }
}
