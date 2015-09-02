package com.tmathmeyer.ci.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.expr.Real;
import com.tmathmeyer.interp.runtime.SlipRuntime;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Void;
import com.tmathmeyer.interp.values.Number;

public class ASTest
{
    public static Value asValue(int i)
    {
        return new Number(new Real(i));
    }

    @Test
    public void reallisp() throws InterpException
    {
        String input = "(+ 99 (+ 1 (+ 2 (+ 3 (+ 4 (+ 5 6))))))";

        assertEquals(new SlipRuntime(input).evaluate().first(), new Number(120));
    }

    @Test
    public void letTest() throws InterpException
    {
        String input = "(let ((x 5) (y 6)) (+ x y))";
        assertEquals(new SlipRuntime(input).evaluate().first(), new Number(11));
    }

    @Test
    public void spaces() throws InterpException
    {
        String input = "    1      ";
        assertEquals(new SlipRuntime(input).evaluate().first(), new Number(1));
    }

    @Test
    public void morespaces() throws InterpException
    {
        String input = "    (  +      1  2   )   ";
        assertEquals(new SlipRuntime(input).evaluate().first(), new Number(3));
    }

    @Test
    public void listTests() throws InterpException
    {
        String input = "(let ((X (lambda (y) empty))) (print (X 5)))";
        assertEquals(new SlipRuntime(input).evaluate().first(), Void.NOTHING);
    }

    @Test
    public void morelistTests() throws InterpException
    {
        String input = "(cons 5 empty)";
        assertEquals(new SlipRuntime(input).evaluate().first().toString(), "(5)");
    }

    @Test
    public void evenMorelistTests() throws InterpException
    {
        String input = "(first (rest (cons 1 (cons 4 (cons 99 (cons 5 empty))))))";
        assertEquals(new SlipRuntime(input).evaluate().first(), new Number(4));
    }

    @Test
    public void evenMorelistTestsLast() throws InterpException
    {
        String input = "(rest (cons 1 (cons 4 (cons 99 (cons 5 empty)))))";
        assertEquals(new SlipRuntime(input).evaluate().first().toString(), "(4 99 5)");
    }
    
    @Test
    public void testNegativeNumbers()
    {
    	String input = "(+ -2 2)";
    	assertEquals(new SlipRuntime(input).evaluate().first().toString(), "0");
    }

}
