package com.tmathmeyer.ci;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tmathmeyer.ci.ds.mipl.EmptyMappingImmutablePartialList;
import com.tmathmeyer.ci.maths.Plus;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.Number;

public class LispTest
{

	@Test
	public void additiontest()
	{
		Real five = new Real(5);
		Real four = new Real(4);

		Expression basicAddition = new Plus(new Number(five), new Number(four));

		Value v = basicAddition.desugar().interp(new EmptyMappingImmutablePartialList<>());

		assertEquals(v.getClass(), Number.class);

		Real r = ((Number) v).value;

		assertEquals(r, new Real(9));
	}

	@Test
	public void testLambda()
	{

	}
}
