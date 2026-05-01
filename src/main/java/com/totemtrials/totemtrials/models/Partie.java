package com.totemtrials.totemtrials.models;

public class Partie {

    private Joueur[] joueurs;

    private final Jeton[] jetonsDisponibles = {
            new Jeton("tiger",    "Move forward one extra space in case of a win in a versus",
                    "images/tokens/jetonTigre.png",    "images/tokens/jetonTigre_anim.gif"),
            new Jeton("eagle",    "Move forward one extra space in case of a correct answer",
                    "images/tokens/jetonAigle.png",    "images/tokens/jetonAigle_anim.gif"),
            new Jeton("snake",  "Move back one space less in case of a defeat in a versus",
                    "images/tokens/jetonSerpent.png",  "images/tokens/jetonSerpent_anim.gif"),
            new Jeton("elephant", "Move back one space less for a wrong answer",
                    "images/tokens/jetonElephant.png", "images/tokens/jetonElephant_anim.gif"),
    };

    public void initJoueurs(int count) {
        if (count < 2 || count > 4) {
            throw new IllegalArgumentException("THE NUMBER OF PLAYER HAVE TO BE BETWEEN 2 AND 4");
        }
        joueurs = new Joueur[count];
        for (int i = 0; i < count; i++)
            joueurs[i] = new Joueur("Player " + (i + 1));
    }

    public Joueur[] getJoueurs()           { return joueurs; }
    public Jeton[]  getJetonsDisponibles() { return jetonsDisponibles; }
}