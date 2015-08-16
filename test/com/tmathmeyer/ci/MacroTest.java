package com.tmathmeyer.ci;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import com.tmathmeyer.interp.runtime.SlipRuntime;
import com.tmathmeyer.interp.values.Number;

public class MacroTest
{

	@Test
	public void evals() throws FileNotFoundException
	{
		String input = "(+ 1 2)";

		assertEquals(new SlipRuntime(input).evaluate().first(), new Number(3));
	}
	
	@Test
	public void macrodef() throws FileNotFoundException
	{
		String input =
				"(# (conc a b) (cons a (cons b empty)))" + 
				"(print (conc 2 3))";
		new SlipRuntime(input).evaluate();
	}
	
	@Test
	public void testBmatch() throws FileNotFoundException
	{
		String input =
				"((lambda (x) (bmatch ((= x 1) 2) ((= x 3) 4) ((= x 5) 6))) 1)";
		System.out.println(new SlipRuntime(input).evaluate());
	}

}
