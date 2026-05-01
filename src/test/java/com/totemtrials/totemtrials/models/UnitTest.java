package com.totemtrials.totemtrials.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


class UnitTest {

    //Create game test
    //Test when you try to create with 1 player
    @Test
    void LowPlayerTest(){
        Partie p = new Partie();

        assertThrows(IllegalArgumentException.class, () -> {
            p.initJoueurs(1);
        }, "The game can't accept 1 player");
    }

    //Test when you try to create with 2 players
    @Test
    void TwoPlayerTest(){
        Partie p = new Partie();
        p.initJoueurs(2);

        List<Joueur> joueurs = List.of(p.getJoueurs());
        assertEquals(2,joueurs.size(),"The game should have exactly 2 players");
    }

    //Test when you try to create with 5 players
    @Test
    void HighPlayerTest(){
        Partie p = new Partie();

        assertThrows(IllegalArgumentException.class, () -> {
            p.initJoueurs(5);
        }, "The game can't accept more 4 players");
    }

    //Assign token test
    @Test
    void SameTokenNameTest() {
        Partie p = new Partie();
        p.initJoueurs(2);
        List<Joueur> joueurs = List.of(p.getJoueurs());
        Joueur p1 = joueurs.get(0);
        Joueur p2 = joueurs.get(1);

        Jeton tiger1 = new Jeton("Tiger", "He can advance 1 more if he won a versus.", null, null);
        Jeton tiger2 = new Jeton("Tiger", "He can dance", null, null);

        p1.setJeton(tiger1);
        p2.setJeton(tiger2);

        assertEquals(p1.getJeton().getNom(), p2.getJeton().getNom(), "Sames names.");
    }

    //Assign token test
    @Test
    void SameTokenPassiveTest() {
        Partie p = new Partie();
        p.initJoueurs(2);
        List<Joueur> joueurs = List.of(p.getJoueurs());
        Joueur p1 = joueurs.get(0);
        Joueur p2 = joueurs.get(1);

        Jeton tiger1 = new Jeton("Tiger", "He can advance 1 more if he won a versus.", null, null);
        Jeton tiger2 = new Jeton("Snake", "He can advance 1 more if he won a versus.", null, null);

        p1.setJeton(tiger1);
        p2.setJeton(tiger2);

        assertEquals(p1.getJeton().getPassif(), p2.getJeton().getPassif(), "Sames passives.");
    }



}