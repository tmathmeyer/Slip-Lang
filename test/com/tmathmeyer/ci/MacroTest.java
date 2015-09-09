package com.tmathmeyer.ci;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tmathmeyer.interp.runtime.SlipRuntime;
import com.tmathmeyer.interp.values.Number;

public class MacroTest
{

    @Test
    public void evals()
    {
        String input = "(+ 1 2)";

        assertEquals(new SlipRuntime(input).evaluate().first(), new Number(3));
    }

    @Test
    public void macrodef()
    {
        String input = "(# (conc a b) (cons a (cons b empty)))" + "(conc 2 3)";
        assertEquals(new SlipRuntime(input, true).evaluate().toString(), "((2 3))");
    }

    @Test
    public void testLet()
    {
        String input = "(let ((x 2) (y 3)) (+ x y))";

        assertEquals(new SlipRuntime(input).evaluate().toString(), "(5)");
    }

    @Test
    public void testList()
    {
        String input = "(list 1 2 3 4 5)";

        assertEquals(new SlipRuntime(input).evaluate().toString(), "((1 2 3 4 5))");
    }

    @Test
    public void testNestedMacros()
    {
        String input = "(list (list 1 2 3) (list 4 5 6))";

        assertEquals(new SlipRuntime(input).evaluate().toString(), "(((1 2 3) (4 5 6)))");
    }

}
