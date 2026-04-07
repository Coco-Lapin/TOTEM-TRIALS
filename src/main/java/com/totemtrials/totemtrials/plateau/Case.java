package com.totemtrials.totemtrials.plateau;

import javafx.scene.shape.Rectangle;





public class Case {
        private String id;
        private double centerX; // Position X du milieu
        private double centerY; // Position Y du milieu
        private String type;
        private Rectangle rectangle; // Référence vers l'objet visuel
        public Case() {}
        public Case(String id, String type, Rectangle rectangle) {
            this.id = id;
            this.type = type;
            this.rectangle = rectangle;

            // On calcule le centre immédiatement
            this.centerX = calculerMilieuX(rectangle);
            this.centerY = calculerMilieuY(rectangle);
        }

        public double calculerMilieuX(Rectangle r) {
            // Formule : Position X + (Largeur / 2)
            return r.getLayoutX() + (r.getWidth() / 2);
        }

        public double calculerMilieuY(Rectangle r) {
            // Formule : Position Y + (Hauteur / 2)
            return r.getLayoutY() + (r.getHeight() / 2);
        }

        // Getters
        public double getCenterX() { return centerX; }
        public double getCenterY() { return centerY; }
        public String getId() { return id; }
        public String getType() { return type; }
        public Rectangle getRectangle() { return rectangle; }

        public Case getCase() { return this; }
    }

