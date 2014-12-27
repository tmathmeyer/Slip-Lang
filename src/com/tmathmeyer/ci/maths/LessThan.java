package com.tmathmeyer.ci.maths;

import com.tmathmeyer.ci.types.Expression;

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