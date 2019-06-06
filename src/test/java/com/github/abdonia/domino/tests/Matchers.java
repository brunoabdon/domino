package com.github.abdonia.domino.tests;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.hasToString;

import org.hamcrest.Matcher;

public class Matchers {

    public static <X> Matcher<X> hasNonBlankToString(){
        return hasToString(not(blankOrNullString())); 
    }
    

}
