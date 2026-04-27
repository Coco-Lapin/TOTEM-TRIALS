package com.totemtrials.totemtrials.models;

/**
 * Statistiques accumulées pour un joueur sur toute la partie.
 * Incrémentées au fil du jeu, lues à la FinPartieView.
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

    // ---- Mutateurs appelés pendant la partie ----
    public void ajouterBonneReponse()      { bonnesReponses++; }
    public void ajouterMauvaiseReponse()   { mauvaisesReponses++; }
    public void incrementerTour()          { nombreTours++; }
    public void setPosition(int position)  { this.position = position; }

    // ---- Accesseurs ----
    public Joueur getJoueur()              { return joueur; }
    public int    getBonnesReponses()      { return bonnesReponses; }
    public int    getMauvaisesReponses()   { return mauvaisesReponses; }
    public int    getNombreTours()         { return nombreTours; }
    public int    getPosition()            { return position; }

    /** Pourcentage de bonnes réponses, 0 si aucune question posée. */
    public int getPourcentageReussite() {
        int total = bonnesReponses + mauvaisesReponses;
        return total == 0 ? 0 : (int) Math.round(100.0 * bonnesReponses / total);
    }
}
