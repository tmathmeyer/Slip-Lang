package com.tmathmeyer.ci;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tmathmeyer.interp.Real;
import com.tmathmeyer.interp.ds.mipl.EmptyMappingImmutablePartialList;
import com.tmathmeyer.interp.maths.Plus;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Number;

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
