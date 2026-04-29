package com.totemtrials.totemtrials.model;

import java.util.Arrays;
import java.util.Comparator;

/**

 * Global container for end-of-game stats.

 * Calculates the ranking and displays the formatted playing time.

 */
public class StatistiquesPartie {

    private final StatistiquesJoueur[] statsJoueurs;
    private final long dureeSecondes; // durée totale de la partie

    /**

     * @param statsJoueurs one PlayerStatistics per player, position already filled in

     * @param dureeSecondes duration in seconds (e.g., System.currentTimeMillis() / 1000)

     */
    public StatistiquesPartie(StatistiquesJoueur[] statsJoueurs, long dureeSecondes) {
        this.statsJoueurs  = statsJoueurs;
        this.dureeSecondes = dureeSecondes;
    }

    /** Returns stats sorted by position (1st in first). */
    public StatistiquesJoueur[] getClassement() {
        StatistiquesJoueur[] sorted = Arrays.copyOf(statsJoueurs, statsJoueurs.length);
        Arrays.sort(sorted, Comparator.comparingInt(StatistiquesJoueur::getPosition));
        return sorted;
    }

    /** Duration formatted "mm:ss". */
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
