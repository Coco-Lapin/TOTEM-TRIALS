package com.totemtrials.totemtrials.models;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Conteneur global des stats de fin de partie.
 * Calcule le classement et expose le temps de jeu formaté.
 */
public class StatistiquesPartie {

    private final StatistiquesJoueur[] statsJoueurs;
    private final long dureeSecondes; // durée totale de la partie

    /**
     * @param statsJoueurs  un StatistiquesJoueur par joueur, position déjà renseignée
     * @param dureeSecondes durée en secondes (ex: System.currentTimeMillis() / 1000)
     */
    public StatistiquesPartie(StatistiquesJoueur[] statsJoueurs, long dureeSecondes) {
        this.statsJoueurs  = statsJoueurs;
        this.dureeSecondes = dureeSecondes;
    }

    /** Retourne les stats triées par position (1er en tête). */
    public StatistiquesJoueur[] getClassement() {
        StatistiquesJoueur[] sorted = Arrays.copyOf(statsJoueurs, statsJoueurs.length);
        Arrays.sort(sorted, Comparator.comparingInt(StatistiquesJoueur::getPosition));
        return sorted;
    }

    /** Durée formatée "mm:ss". */
    public String getDureeFormatee() {
        long minutes = dureeSecondes / 60;
        long secondes = dureeSecondes % 60;
        return String.format("%02d:%02d", minutes, secondes);
    }

    public long getDureeSecondes() { return dureeSecondes; }

    public int getTotalTours() {
        return Arrays.stream(statsJoueurs)
                     .mapToInt(StatistiquesJoueur::getNombreTours)
                     .max()
                     .orElse(0);
    }

    public int getTotalQuestions() {
        return Arrays.stream(statsJoueurs)
                     .mapToInt(s -> s.getBonnesReponses() + s.getMauvaisesReponses())
                     .sum();
    }
}
