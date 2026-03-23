package com.totemtrials.totemtrials.plateau;

public class Case {


        private String id;
        private double x ;
        private double y;
        private String type;

        public Case(String id , double x , double y , String type){

        }
        public double getX(){
            return this.x;
        }
        public double getY(){
            return this.y;
        }

        public String getId(){
            return this.id;
        }
        public  String getType(){
            return this.type;
        }

}
