package com.github.abdonia.domino.tests;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.hasToString;

import org.hamcrest.Matcher;

import com.github.abdonia.domino.Vitoria;

public class Matchers {

    public static <X> Matcher<X> hasNonBlankToString(){
        return hasToString(not(blankOrNullString())); 
    }
    
    public static Matcher<Vitoria> vale(final int expectedPontos){
        return Vale.vale(expectedPontos); 
    }
}
