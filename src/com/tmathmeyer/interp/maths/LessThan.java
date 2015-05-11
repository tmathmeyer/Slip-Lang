package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.types.Expression;

public class LessThan extends GreaterThan
{
	public LessThan(Expression left, Expression right)
	{
		super(right, left);
	}

	public String toString()
	{
		return "(" + L + " < " + R + ")";
	}
}