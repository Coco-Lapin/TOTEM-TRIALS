package com.totemtrials.totemtrials.questions;

import java.util.List;

public class Question
{
    private String theme;
    private String question;
    private String answer;
    private List<String> choices;
    private int difficulty;

    public String getTexte() { return question; }
    public String getReponse() { return answer; }
    public List<String> getChoix() { return choices; }

    public String getTheme() { return theme; }
    public int getDifficulty() { return difficulty; }
}