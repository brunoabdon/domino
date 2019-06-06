package com.github.abdonia.domino.tests;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.github.abdonia.domino.Vitoria;

public class Vale extends TypeSafeMatcher<Vitoria>{

    private int expectedPontos;

    public static Vale vale(final int expectedPontos) {
        return new Vale(expectedPontos);
    }
    
    public Vale(final int expectedPontos) {
        this.expectedPontos = expectedPontos;
    }
    
    @Override
    protected void describeMismatchSafely(
            final Vitoria vitoria,
            final Description mismatchDescription) {
        
        mismatchDescription
            .appendValue(vitoria)
            .appendText(" vale ")
            .appendValue(vitoria.getPontos())
            .appendText(" pontos ");
        
    }
    
    @Override
    public void describeTo(final Description description) {
        
        description
            .appendText("uma Vitória valendo ")
            .appendValue(expectedPontos)
            .appendText(" pontos");
        
    }

    @Override
    protected boolean matchesSafely(final Vitoria vitoria) {
        return vitoria.getPontos() == expectedPontos;
    }
    
}