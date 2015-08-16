package com.tmathmeyer.ci;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tmathmeyer.interp.runtime.SlipRuntime;
import com.tmathmeyer.interp.values.Number;

public class DefineTest
{

	@Test
	public void testInlineFunction()
	{
		String input = "(#def double (x) (+ x x))"+
					   " (double 2) ";
		assertEquals(new SlipRuntime(input).evaluate().first(), new Number(4));
	}

	@Test
	public void testDefineAsNumber()
	{
		String input = "(#def shitty-pi 3) (+ 1 shitty-pi)";
		assertEquals(new SlipRuntime(input).evaluate().first(), new Number(4));
	}
	
	@Test
	public void testDefineAsFunction()
	{
		String input = "(#def square (lambda (x) (* x x))) (square 3)";
		assertEquals(new SlipRuntime(input).evaluate().first(), new Number(9));
	}
}
