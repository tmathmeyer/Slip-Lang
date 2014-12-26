package com.tmathmeyer.ci;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tmathmeyer.ci.ds.mipl.EmptyMappingImmutablePartialList;

public class LispTest
{

	@Test
	public void additiontest()
	{
		Real five = new Real(5);
		Real four = new Real(4);
		
		
		Expression basicAddition = new Expression.Plus(new Expression.Number(five),
													   new Expression.Number(four));
		
		Value v = basicAddition.desugar().interp(new EmptyMappingImmutablePartialList<>());
		
		assertEquals(v.getClass(), Value.Number.class);
		
		Real r = ((Value.Number)v).V;
		
		assertEquals(r, new Real(9));
	}

	
	@Test
	public void testLambda()
	{
		
	}
}
