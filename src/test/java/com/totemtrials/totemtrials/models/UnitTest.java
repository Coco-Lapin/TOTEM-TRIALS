package com.totemtrials.totemtrials.models;

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
        }, "La partie ne devrait pas accepter 1 seul joueur.");
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
        }, "La partie ne devrait pas accepter plus de 4 joueurs.");
    }

    //
}