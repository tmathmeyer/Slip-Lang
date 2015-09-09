package com.tmathmeyer.ci;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tmathmeyer.interp.runtime.SlipRuntime;
import com.tmathmeyer.interp.values.Number;

public class DefineTest
{
    public static final boolean FILE = true;

    @Test
    public void testInlineFunction()
    {
        String input = "(define double (x) (+ x x))" + " (double 2) ";
        assertEquals(new SlipRuntime(input, FILE).evaluate().first(), new Number(4));
    }

    @Test
    public void testDefineAsNumber()
    {
        String input = "(define shitty-pi 3) (+ 1 shitty-pi)";
        assertEquals(new SlipRuntime(input, FILE).evaluate().first(), new Number(4));
    }

    @Test
    public void testDefineAsFunction()
    {
        String input = "(define square (lambda (x) (* x x))) (square 3)";
        assertEquals(new SlipRuntime(input, FILE).evaluate().first(), new Number(9));
    }
}
