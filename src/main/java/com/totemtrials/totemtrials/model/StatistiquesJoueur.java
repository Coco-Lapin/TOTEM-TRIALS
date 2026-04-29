package com.totemtrials.totemtrials.model;

/**

 * Accumulated statistics for a player throughout the entire game.

 * Incremented during the game, read at EndGameView.

 */
public class StatistiquesJoueur {

    private final Joueur joueur;
    private int bonnesReponses;
    private int mauvaisesReponses;
    private int nombreTours;
    private int position; // classement final (1 = vainqueur)

    public StatistiquesJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    // ---- Mutators called during the game ----
    public void ajouterBonneReponse()      { bonnesReponses++; }
    public void ajouterMauvaiseReponse()   { mauvaisesReponses++; }
    public void incrementerTour()          { nombreTours++; }
    public void setPosition(int position)  { this.position = position; }

    // ---- Accessor ----
    public Joueur getJoueur()              { return joueur; }
    public int    getBonnesReponses()      { return bonnesReponses; }
    public int    getMauvaisesReponses()   { return mauvaisesReponses; }
    public int    getNombreTours()         { return nombreTours; }
    public int    getPosition()            { return position; }

    /** Percentage of correct answers, 0 if no question was asked. */
    public int getPourcentageReussite() {
        int total = bonnesReponses + mauvaisesReponses;
        return total == 0 ? 0 : (int) Math.round(100.0 * bonnesReponses / total);
    }
}
